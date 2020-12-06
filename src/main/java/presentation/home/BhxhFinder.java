package main.java.presentation.home;

import main.java.business.excel.ExcelLogic;
import main.java.common.model.MySheet;
import main.java.presentation.helper.DriverHelper;
import main.java.presentation.model.Config;
import main.java.presentation.model.Session;
import main.java.presentation.util.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tool.compet.javacore.log.DkConsoleLogs;
import tool.compet.javacore.util.DkFiles;
import tool.compet.javacore.util.DkObjects;
import tool.compet.javacore.util.DkStrings;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BhxhFinder extends InfoFinder {
    private final Callback callback;
    private final Config.Bhxh config;
    private final WebDriver webdriver;
    private final String inDirPath;
    private final String outDirPath;
    private final Random random = new Random();
    private final AtomicBoolean isStop = new AtomicBoolean(false);

    private static final long DEFAULT_WAIT_TIMEOUT = 10; // seconds

    private static final String ID_NUMBER = "bhxhMa";
    private static final String ID_USERNAME = "username";
    private static final String ID_PASSWORD = "password";
    private static final String ID_BHXH_NUMBER = "field_soSoBhxh";

    private static final String XPATH_BTN_LOGIN = "//button[text()='Đăng nhập']";
    private static final String XPATH_BTN_SEARCH_BHXH = "//button[@ng-click='func.findAllHoSoCaNhan()']";
    private static final String XPATH_BTN_CHOOSE_DOCUMENT = "//button[@ng-click=\"choose(hoSo)\"]";
    private static final String XPATH_RESULT_DATA_ROW = "//tr[@ng-repeat=\"qtThamGia in data.listDetail\" and contains(@ng-class, 'qtThamGia.bhxh')]";
    private static final String XPATH_POPUP_SEARCH_BHXH_RESULT = "//h4[@translate=\"smsApp.hoSoCaNhan.traCuuToanQuoc.title\"]";
    private static final String XPATH_BHXH_RESULT_ONE_ROW = ".//td[@ng-repeat=\"col in table.qtxh\"]"; // use `dot` for current web element (not root element)
    private static final String XPATH_BTN_CLOSE_SEARCH_BHXH_POPUP = "//button[@ng-click=\"close()\" and text()='×']";

    private static final String XPATH_RESULT_NAME = "//input[@name=\"hoTen\"]";
    private static final String XPATH_RESULT_BIRTHDAY = "//input[@name=\"ngaySinh\"]";
    private static final String XPATH_RESULT_SEX = "//input[@name=\"gioiTinh\"]";
    private static final String XPATH_RESULT_CMND = "//input[@name=\"soCmnd\"]";

    private static final Pattern PATTERN_SEARCH_POPUP_TEXT = Pattern.compile("Danh sách hồ sơ cá nhân");

    private static final int START_READ_ROW_INDEX = 2;
    private static final int INDEX_BHXH = 4;

    private static final int ACTION_GET_INFO = 1;
    private static final int ACTION_SELECT_IN_POPUP = 2;

    private int fileIndex;
    private List<File> inFiles; // input files
    private MySheet[][] outputs; // output documents
    private List<MySheet[]> mysheets;

    public BhxhFinder(Callback callback, WebDriver webdriver, String inDirPath, String outDirPath) {
        this.callback = callback;
        this.config = Session.config.bhxh;
        this.webdriver = webdriver;
        this.inDirPath = inDirPath;
        this.outDirPath = outDirPath;
    }

    @Override
    public boolean start() {
        // reset fields
        isStop.set(false);
        fileIndex = 0;
        inFiles = null;
        mysheets = null;

        try {
            readAllInput();

            if (inFiles.size() == 0 || mysheets.size() == 0) {
                throw new RuntimeException("Input folder must not be empty");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            callback.log("Invalid input, error: %s", e.getMessage());
            return false;
        }

        enterLoginPageAndStart();
        return true;
    }

    @Override
    public boolean stop() {
        try {
            webdriver.close();
            isStop.set(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            callback.log("Could not stop program, error: %s", e.getMessage());
        }
        return isStop.get();
    }

    // Step 1
    private void readAllInput() throws Exception {
        File inFile = new File(inDirPath);
        if (!inFile.exists()) {
            throw new RuntimeException("Input file is not exist");
        }

        File outDir = new File(outDirPath);
        if (!outDir.exists() && !DkFiles.createNewDirRecursively(outDir)) {
            throw new RuntimeException("Could not create output folder");
        }

        ExcelLogic excelLogic = new ExcelLogic();
        inFiles = new ArrayList<>();
        mysheets = new ArrayList<>();

        if (inFile.isDirectory()) {
            File[] fs = inFile.listFiles();

            if (fs != null) {
                for (File f : fs) {
                    inFiles.add(f);
                    mysheets.add(excelLogic.readAll(f));
                }
            }
        } else {
            inFiles.add(inFile);
            mysheets.add(excelLogic.readAll(inFile));
        }
    }

    // Step 2
    private void enterLoginPageAndStart() {
        final String url = config.url;
        final Config.Bhxh config = Session.config.bhxh;

        // Login
        callback.log("Login with username `%s`", config.username);
        webdriver.get(url);

        Timer waitLoginTimer = new Timer();
        waitLoginTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isStop.get()) {
                    waitLoginTimer.cancel();
                }
                else if (!config.url.equalsIgnoreCase(webdriver.getCurrentUrl())) {
                    waitLoginTimer.cancel();
                    fillLoginFormAndStartLogin();
                }
            }
        }, 1000, 2000);
    }

    // Step 3
    private void fillLoginFormAndStartLogin() {
        // Wait until we can set press login button
        new WebDriverWait(webdriver, DEFAULT_WAIT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_BTN_LOGIN)));

        final String currentUrl = webdriver.getCurrentUrl();

        // Set text and press login button
        webdriver.findElement(By.id(ID_NUMBER)).sendKeys(config.number);
        webdriver.findElement(By.id(ID_USERNAME)).sendKeys(config.username);
        webdriver.findElement(By.id(ID_PASSWORD)).sendKeys(config.password);

        sleepBeforePerformClick();
        webdriver.findElement(By.xpath(XPATH_BTN_LOGIN)).click();

        Timer waitFinishLoginTimer = new Timer();
        waitFinishLoginTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isStop.get()) {
                    waitFinishLoginTimer.cancel();
                }
                else if (!currentUrl.equalsIgnoreCase(webdriver.getCurrentUrl())) {
                    waitFinishLoginTimer.cancel();
                    startWorkingFromHomepage();
                }
            }
        }, 1000, 2000);
    }

    // Step 4
    private void startWorkingFromHomepage() {
        // We are in homepage, now we jump to bhxh-finder page
        // Enter test BHXH number: 6822891887
        final String currentUrl = webdriver.getCurrentUrl();
        webdriver.get(config.urlFindWithBhxh);

        Timer waitFinishLoadBhxhFinderPageTimer = new Timer();
        waitFinishLoadBhxhFinderPageTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isStop.get()) {
                    waitFinishLoadBhxhFinderPageTimer.cancel();
                }
                else if (!currentUrl.equalsIgnoreCase(webdriver.getCurrentUrl())) {
                    waitFinishLoadBhxhFinderPageTimer.cancel();
                    startFromBhxhFinderPage();
                }
            }
        }, 1000, 2000);
    }

    // Step 5
    private void startFromBhxhFinderPage() {
        new WebDriverWait(webdriver, DEFAULT_WAIT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(By.id(ID_BHXH_NUMBER)));

        this.outputs = new MySheet[inFiles.size()][];

        for (int N = inFiles.size(); fileIndex < N; ++fileIndex) {
            if (isStop.get()) {
                return;
            }
            final File inFile = inFiles.get(fileIndex);
            final String inFileName = inFile.getName();
            final MySheet[] inSheets = mysheets.get(fileIndex);
            MySheet[] outSheets = this.outputs[fileIndex] = new MySheet[inSheets.length];

            // Only do with sheet 0
            for (int sheetIndex = 0, sheetCount = Math.min(1, inSheets.length); sheetIndex < sheetCount; ++sheetIndex) {
                if (isStop.get()) {
                    return;
                }
                MySheet inSheet = inSheets[sheetIndex];
                MySheet outSheet = outSheets[sheetIndex] = new MySheet(inSheet.getName(), 0);

                BhxhHelper.addTitleRows(START_READ_ROW_INDEX - 1, inSheet, outSheet);

                callback.log("Get info in row indices: %d -> %d", START_READ_ROW_INDEX, inSheet.getRowCount() - 1);
                for (int rowIndex = START_READ_ROW_INDEX, rowCount = inSheet.getRowCount(); rowIndex < rowCount; ++rowIndex) {
                    if (isStop.get()) {
                        return;
                    }
                    final String bhxh = inSheet.isExistCell(rowIndex, INDEX_BHXH) ? inSheet.getValueAt(rowIndex, INDEX_BHXH) : null;

                    if (DkStrings.isEmpty(bhxh)) {
                        BhxhHelper.addNoResultRow(rowIndex, START_READ_ROW_INDEX, inSheet, outSheet);
                        callback.log("Skip find info for empty bhxh, but added empty record at row `%d`", rowIndex + 1);
                        continue;
                    }

                    // THIS IS FINDER PROCESSING
                    callback.log("Find info for bhxh `%s` at row `%d` in file `%s`", bhxh, rowIndex + 1, inFileName);
                    List<BhxhResultRow> bhxhResultRows = findInfo(bhxh);

                    if (DkObjects.isEmpty(bhxhResultRows)) {
                        BhxhHelper.addNoResultRow(rowIndex, START_READ_ROW_INDEX, inSheet, outSheet);
                        callback.log("Added empty record at row `%d`", rowIndex + 1);
                        continue;
                    }

                    BhxhHelper.addRows(rowIndex, START_READ_ROW_INDEX, inSheet, bhxhResultRows, outSheet);
                    callback.log("Added `%d` rows at row `%d`", bhxhResultRows.size(), rowIndex + 1);
                }
            }
        }

        writeOutputs();
    }

    @Override
    public void writeOutputs() {
        if (isStop.get()) {
            return;
        }
        if (this.outputs != null && this.inFiles != null) {
            for (int fileIndex = outputs.length - 1; fileIndex >= 0; --fileIndex) {
                // Write output to excel file
                try {
                    File outFile = new File(DkFiles.makePath(outDirPath, "out-" + inFiles.get(fileIndex).getName()));
                    ExcelLogic excelLogic = new ExcelLogic();
                    excelLogic.write(this.outputs[fileIndex], outFile);
                    callback.log("Done write output to file `%s`", outFile.getPath());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    callback.log("Could not write output, error: %s", e.getMessage());
                }
            }
        }
    }

    // This is main action
    private List<BhxhResultRow> findInfo(String bhxh) {
        if (isStop.get()) {
            return null;
        }
        // Maybe last popup was shown, we try to close it first for next input
        tryCloseBhxhPopup();

        // test bhxh code: 6822891887
        try {
            DriverHelper.setText(webdriver.findElement(By.id(ID_BHXH_NUMBER)), bhxh);

            // Check which action should be performed next
            int nextAction = determineNextAction();

            if (nextAction == ACTION_GET_INFO) {
                callback.log("Collect info from result table");
                return collectInfoFromDataInResultTable();
            }
            else if (nextAction == ACTION_SELECT_IN_POPUP) {
                callback.log("Collect info from bhxh list popup");
                return collectInfoFromSelectInBhxhListPopup();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            callback.log("Failed to find info, error: %s", e.getMessage());
        }

        return new ArrayList<>();
    }

    // Option 1
    private List<BhxhResultRow> collectInfoFromDataInResultTable() {
        if (isStop.get()) {
            return null;
        }
        final List<BhxhResultRow> resultRows = new ArrayList<>();

        // Get info in result table
        Util.sleep(200);
        List<WebElement> rows = webdriver.findElements(By.xpath(XPATH_RESULT_DATA_ROW));
        callback.log("Found %d rows in result table", rows.size());

        // Get basic info first
        BhxhResultRow row = new BhxhResultRow();
        row.name = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_NAME)));
        row.birthday = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_BIRTHDAY)));
        row.sex = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_SEX)));
        row.cmnd = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_CMND)));

        // Just assert rows is not empty
        if (DkObjects.isEmpty(rows)) {
            resultRows.add(row);
            return resultRows;
        }

        WebElement lastRow = rows.get(rows.size() - 1);
        List<WebElement> cols = lastRow.findElements(By.xpath(XPATH_BHXH_RESULT_ONE_ROW));
        callback.log("We choose last row which has %d columns", cols.size());

        BhxhHelper.collectInfoFromResultRow(row, cols);
        callback.log("Got last result row: %s", row.toString());

        resultRows.add(row);

        return resultRows;
    }

    // Option 2
    private List<BhxhResultRow> collectInfoFromSelectInBhxhListPopup() {
        if (isStop.get()) {
            return null;
        }
        final List<BhxhResultRow> resultRows = new ArrayList<>();

        // Loop each selection and click to them in shown-popup
        new WebDriverWait(webdriver, DEFAULT_WAIT_TIMEOUT).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XPATH_BTN_CHOOSE_DOCUMENT)));
        List<WebElement> selectButtons = webdriver.findElements(By.xpath(XPATH_BTN_CHOOSE_DOCUMENT));

        if (DkObjects.isEmpty(selectButtons)) {
            return resultRows;
        }

        for (int rowIndex = selectButtons.size() - 1; rowIndex >= 0; --rowIndex) {
            // Perform click button selection
            new WebDriverWait(webdriver, DEFAULT_WAIT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_BTN_CHOOSE_DOCUMENT)));

            sleepBeforePerformClick();
            webdriver.findElements(By.xpath(XPATH_BTN_CHOOSE_DOCUMENT)).get(rowIndex).click();

            // Get info in result table
            Util.sleep(200);
            List<WebElement> rows = webdriver.findElements(By.xpath(XPATH_RESULT_DATA_ROW));

            callback.log("Found %d results in table", rows.size());

            // click button search bhxh to show popup again
            if (DkObjects.isEmpty(rows)) {
                // Click button search bhxh to show selection popup
                if (!tryShowBhxhPopup()) {
                    callback.log("[Warning] Skip find info for rows `%d → %d`", 1, rowIndex + 1);
                    break;
                }
                // Try close popup
                if (rowIndex == 0) {
                    tryCloseBhxhPopup();
                }
                continue;
            }

            WebElement lastRow = rows.get(rows.size() - 1);
            List<WebElement> cols = lastRow.findElements(By.xpath(XPATH_BHXH_RESULT_ONE_ROW));
            callback.log("We choose last row which has %d columns", cols.size());

            BhxhResultRow row = new BhxhResultRow();
            row.name = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_NAME)));
            row.birthday = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_BIRTHDAY)));
            row.sex = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_SEX)));
            row.cmnd = DriverHelper.getInputText(webdriver.findElement(By.xpath(XPATH_RESULT_CMND)));

            // Collect basic info first
            BhxhHelper.collectInfoFromResultRow(row, cols);
            callback.log("Got last result row: %s", row.toString());

            resultRows.add(row);

            // Click button search bhxh to show selection popup
            if (!tryShowBhxhPopup()) {
                callback.log("[Warning] Skip find info for rows `%d → %d`", 1, rowIndex + 1);
                break;
            }
        }

        return resultRows;
    }

    private void tryCloseBhxhPopup() {
        if (isStop.get()) {
            return;
        }
        try {
            sleepBeforePerformClick();
            webdriver.findElement(By.xpath(XPATH_BTN_CLOSE_SEARCH_BHXH_POPUP)).click();
        }
        catch (Exception e) {
            DkConsoleLogs.info(this, "Try close search bhxh popup but exception: %s", e.getMessage());
        }
    }

    private boolean tryShowBhxhPopup() {
        try {
            // Attemp some seconds to show bhxh popup
            for (int i = 0; i < config.attempSearchCount; ++i) {
                try {
                    // Click button search bhxh
                    sleepBeforePerformClick();
                    webdriver.findElement(By.xpath(XPATH_BTN_SEARCH_BHXH)).click();
                    callback.log("Clicked to button search");

                    Boolean popupShown = new WebDriverWait(webdriver, config.searchTimeoutInSeconds)
                        .until(ExpectedConditions.textMatches(By.xpath(XPATH_POPUP_SEARCH_BHXH_RESULT), PATTERN_SEARCH_POPUP_TEXT));

                    callback.log("Popup was shown? %b", popupShown);

                    if (popupShown) {
                        return true;
                    }
                }
                catch (Exception e) {
                    callback.log("Retry click button search bhxh since error (%s)", e.getMessage());
                }
            }
        }
        catch (Exception e) {
            callback.log("Give up click button search bhxh since error (%s)", e.getMessage());
        }

        return false;
    }

    private void sleepBeforePerformClick() {
        Util.sleep(3000 + random.nextInt(2000));
    }

    private int determineNextAction() {
        try {
            // Attemp some seconds to detemine next action
            for (int i = 0; i < config.attempSearchCount; ++i) {
                if (isStop.get()) {
                    return -1;
                }
                try {
                    // Click button search bhxh
                    sleepBeforePerformClick();
                    new WebDriverWait(webdriver, config.searchTimeoutInSeconds)
                        .until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_BTN_SEARCH_BHXH)))
                        .click();
                    callback.log("Clicked to button search");

                    List<WebElement> dataRows = webdriver.findElements(By.xpath(XPATH_RESULT_DATA_ROW));

                    if (!DkObjects.isEmpty(dataRows)) {
                        callback.log("Got nextAction: %d", ACTION_GET_INFO);
                        return ACTION_GET_INFO;
                    }

                    // Wait until popup was shown
                    Boolean popupShown = new WebDriverWait(webdriver, config.searchTimeoutInSeconds)
                        .until(ExpectedConditions.textMatches(By.xpath(XPATH_POPUP_SEARCH_BHXH_RESULT), PATTERN_SEARCH_POPUP_TEXT));

                    callback.log("Popup was shown? %b", popupShown);

                    if (popupShown) {
                        return ACTION_SELECT_IN_POPUP;
                    }
                }
                catch (Exception e) {
                    callback.log("Retry determine next action since error (%s)", e.getMessage());
                }
            }
        }
        catch (Exception e) {
            callback.log("Gave up determine since error (%s)", e.getMessage());
        }

        return -1;
    }
}
