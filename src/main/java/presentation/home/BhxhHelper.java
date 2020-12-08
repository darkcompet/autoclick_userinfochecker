package main.java.presentation.home;

import main.java.common.model.MySheet;
import org.openqa.selenium.WebElement;
import tool.compet.core.util.DkStrings;

import java.util.ArrayList;
import java.util.List;

// Package-private helper for bhxh
class BhxhHelper {
    static void addTitleRows(int titleRowIndex, MySheet inSheet, MySheet outSheet) {
        for (int rowIndex = 0; rowIndex <= titleRowIndex; ++rowIndex) {
            List<String> patchRow = inSheet.getRowAt(rowIndex);
            List<String> newRow = new ArrayList<>();

            newRow.add(rowIndex == titleRowIndex ? "STT" : "");
            newRow.addAll(patchRow);

            outSheet.addRow(newRow);
        }
    }

    static void addNoResultRow(int rowIndex, int START_READ_ROW_INDEX, MySheet inSheet, MySheet outSheet) {
        List<String> patchRow = inSheet.getRowAt(rowIndex);
        List<String> newRow = new ArrayList<>();

        newRow.add(String.valueOf(rowIndex - START_READ_ROW_INDEX + 1));
        newRow.addAll(patchRow);

        outSheet.addRow(newRow);
    }

    static void addRows(int rowIndex, int START_READ_ROW_INDEX, MySheet inSheet, List<BhxhResultRow> addRows, MySheet outSheet) {
        int sttIndex = rowIndex - START_READ_ROW_INDEX;
        List<String> inRow = inSheet.getRowAt(rowIndex);

        for (int index = 0, addRowCount = addRows.size(); index < addRowCount; ++index) {
            BhxhResultRow addRow = addRows.get(index);
            List<String> newRow = new ArrayList<>();

            if (index == 0) {
                newRow.add(DkStrings.format("%d", sttIndex + 1));
            }
            else {
                newRow.add(DkStrings.format("%d.%d", sttIndex + 1, index + 1));
            }

            for (int tmp = 0; tmp < 5; ++tmp) {
                newRow.add(index == 0 ? inRow.get(tmp) : "");
            }

            newRow.add(addRow.name);
            newRow.add(addRow.cmnd);
            newRow.add(addRow.sex);
            newRow.add(addRow.birthday);

            newRow.add(addRow.workTo);
            newRow.add(addRow.position);
            newRow.add(addRow.salary);
            newRow.add(addRow.hsl);
            newRow.add(addRow.company_name);
            newRow.add(addRow.company_address);

            outSheet.addRow(newRow);
        }
    }

    static void collectInfoFromResultRow(BhxhResultRow resultRow, List<WebElement> cols) {
        try {
            resultRow.workTo = cols.get(2).getText(); // den thang
            resultRow.position = cols.get(6).getText(); // chuc danh, cong viec
            resultRow.salary = cols.get(7).getText(); // muc luong
            resultRow.hsl = cols.get(8).getText(); // HSL
            resultRow.company_name = cols.get(19).getText(); // ten don vi
            resultRow.company_address = cols.get(20).getText(); // noi lam viec
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
