package main.java.presentation.home;

import javafx.application.Platform;
import main.java.presentation.model.Config;
import main.java.presentation.model.Session;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import tool.compet.core.constant.DkConstant;
import tool.compet.core.util.DkFiles;
import tool.compet.core.util.DkStrings;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeModelLogic implements Callback {
    private final HomeViewLogic viewLogic;
    private WebDriver webdriver;
    private int runState = HomeConst.RUN_STATE_STOPPED;
    private InfoFinder infoFinder;
    private final List<LogItemModel> logItems = new ArrayList<>();

    public HomeModelLogic(HomeViewLogic viewLogic) {
        this.viewLogic = viewLogic;
    }

    private void _initializeWebDriver(int actionType) throws IllegalAccessException, InstantiationException {
        // Setup web driver
        String driverName;
        Class driverClass;
        Config config = Session.config;
        String browserType;
        String driverPath;

        if (actionType == HomeConst.ACTION_VIETTEL) {
            browserType = config.viettel.browserType;
            driverPath = config.viettel.driverPath;
        }
        else if (actionType == HomeConst.ACTION_MOBI) {
            browserType = config.mobi.browserType;
            driverPath = config.mobi.driverPath;
        }
        else if (actionType == HomeConst.ACTION_BHXH) {
            browserType = config.bhxh.browserType;
            driverPath = config.bhxh.driverPath;
        }
        else {
            throw new RuntimeException("Invalid action type");
        }

        if (HomeConst.FIREFOX.equalsIgnoreCase(browserType)) {
            driverName = HomeConst.FIREFOX_DRIVER_NAME;
            driverClass = FirefoxDriver.class;
        }
        else if (HomeConst.CHROME.equalsIgnoreCase(browserType)) {
            driverName = HomeConst.CHROME_DRIVER_NAME;
            driverClass = ChromeDriver.class;
        }
        else if (HomeConst.IE.equalsIgnoreCase(browserType)) {
            driverName = HomeConst.IE_DRIVER_NAME;
            driverClass = InternetExplorerDriver.class;
        }
        else {
            throw new RuntimeException(DkStrings.format("Invalid browserType: [%s]", browserType));
        }

        System.setProperty(driverName, DkFiles.makePath(DkConstant.ABS_PATH, driverPath));
        webdriver = (WebDriver) driverClass.newInstance();
    }

    public void start(int actionType, String inDirPath, String outDirPath) {
        try {
            // Start work
            if (runState == HomeConst.RUN_STATE_STOPPED) {
                if (this.infoFinder != null) {
                    this.infoFinder.stop();
                }

                _initializeWebDriver(actionType);

                if (actionType == HomeConst.ACTION_VIETTEL) {
                    this.infoFinder = new ViettelFinder(this, webdriver, inDirPath, outDirPath);
                }
                else if (actionType == HomeConst.ACTION_MOBI) {
                    this.infoFinder = new MobiFinder(this, webdriver, inDirPath, outDirPath);
                }
                else if (actionType == HomeConst.ACTION_BHXH) {
                    this.infoFinder = new BhxhFinder(this, webdriver, inDirPath, outDirPath);
                }
                else {
                    throw new RuntimeException("Invalid action type");
                }

                if (this.infoFinder.start()) {
                    this.runState = HomeConst.RUN_STATE_RUNNING;
                }

                viewLogic.onStateChanged(this.runState);
            }
            // Stop program and write output, log...
            else if (runState == HomeConst.RUN_STATE_RUNNING) {
                if (this.infoFinder != null) {
                    this.infoFinder.writeOutputs();
                    this.infoFinder.stop();
                }

                // write log when user stop process
                writeLogToFileAndCleanupOnMainThread();

                runState = HomeConst.RUN_STATE_STOPPED;
                viewLogic.onStateChanged(runState);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            runState = HomeConst.RUN_STATE_STOPPED;
            viewLogic.onStateChanged(runState);

            log("Failed to start/stop, error: %s", e.getMessage());
        }
    }

    @Override
    public void log(String format, Object... args) {
        String msg = DkStrings.format(format, args);

        String created = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").format(new Date());
        LogItemModel logItemModel = new LogItemModel(created, msg);

        logItems.add(logItemModel);
        viewLogic.logln(logItemModel);
    }

    private void writeLogToFileAndCleanupOnMainThread() {
        Platform.runLater(() -> {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String logFileName = "log-" + time + ".txt";

            String logFilePath = DkFiles.makePath(DkConstant.ABS_PATH, "log", logFileName);
            StringBuilder logContent = new StringBuilder(2 << 13);

            for (LogItemModel logItem : logItems) {
                logContent.append(logItem.toFileLogString()).append(DkConstant.LS);
            }

            try {
                File logFile = new File(logFilePath);
                DkFiles.createNewFileRecursively(logFile);

                BufferedWriter bw = DkFiles.newUtf8Writer(logFile);
                bw.write(logContent.toString());
                bw.write(DkConstant.LS);
                bw.close();

                logItems.clear();
                viewLogic.clearLogMsg();
            }
            catch (Exception e) {
                e.printStackTrace();
                viewLogic.alert("Could not write log, error: %s", e.getMessage());
            }
        });
    }

    public void onExitProgram() {
        writeLogToFileAndCleanupOnMainThread();
    }
}
