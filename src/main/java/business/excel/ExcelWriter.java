package main.java.business.excel;

import main.java.common.model.MySheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelWriter {
   public void write(MySheet[] mySheets, File outFile) throws Exception {
      XSSFWorkbook workbook = new XSSFWorkbook();

      for (MySheet mySheet : mySheets) {
         XSSFSheet sheet = workbook.createSheet(mySheet.getName());
         int rowIndex = 0;

         for (List<String> values : mySheet.getContent()) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;

            for (String value : values) {
               Cell cell = row.createCell(cellIndex++);
               cell.setCellValue(value);
            }
         }
      }

      FileOutputStream fos = new FileOutputStream(outFile);
      workbook.write(fos);
      fos.close();
   }
}
