package main.java.business.excel;

import main.java.common.constant.Constant;
import main.java.common.model.MySheet;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tool.compet.core.log.DkLogger;

import java.io.File;

public class ExcelReader {
    public MySheet[] read(File inDirPath) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(inDirPath);
        int sheetCount = workbook.getNumberOfSheets();
        if (sheetCount < 0) {
            throw new RuntimeException("No sheet found");
        }

        MySheet[] mySheets = new MySheet[sheetCount];
        DataFormatter dataFormatter = new DataFormatter();

        for (int sheetIndex = 0; sheetIndex < sheetCount; ++sheetIndex) {
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();
            final int rowCount = lastRowIndex - firstRowIndex + 1;
            int nullRowCount = 0;

            MySheet mySheet = mySheets[sheetIndex] = new MySheet(sheet.getSheetName(), rowCount);

            for (int index = 0, rowIndex = firstRowIndex; index < rowCount && rowIndex <= lastRowIndex; ++index, ++rowIndex) {
                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    ++nullRowCount;
                }
                else {
                    short cellCount = row.getLastCellNum();

                    for (int cellIndex = 0; cellIndex < cellCount; ++cellIndex) {
                        String value = dataFormatter.formatCellValue(row.getCell(cellIndex));
                        mySheet.addCellAtRow(index, value == null ? null : value.trim());
                    }
                }
            }

            if (Constant.DEBUG) {
                DkLogger.getIns().debug(this, "First row index: %d, Last row index: %d, nullRowCount: %d",
                   firstRowIndex, lastRowIndex, nullRowCount);
            }
        }

        return mySheets;
    }
}
