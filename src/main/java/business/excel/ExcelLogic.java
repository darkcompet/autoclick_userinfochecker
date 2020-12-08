package main.java.business.excel;

import main.java.common.constant.Constant;
import main.java.common.model.MySheet;
import tool.compet.core.log.DkLogger;
import tool.compet.core.util.DkFiles;

import java.io.File;
import java.util.List;

public class ExcelLogic {
    public MySheet[] readAll(File inFile) throws Exception {
        if (!inFile.exists()) {
            throw new RuntimeException("Not found file " + inFile.getPath());
        }
        ExcelReader reader = new ExcelReader();
        MySheet[] mySheets = reader.read(inFile);

        if (Constant.DEBUG) {
            System.out.println("Read file [" + inFile.getName() + "]");
            for (MySheet mySheet : mySheets) {
                for (List<String> values : mySheet.getContent()) {
                    for (String value : values) {
                        System.out.print(value + ", ");
                    }
                    System.out.println();
                    DkLogger.getIns().debug(this, "Read %d cells in row", values.size());
                }
                System.out.println();
                DkLogger.getIns().debug(this, "Read %d rows in sheet [%s]", mySheets.length, mySheet.getName());
            }
        }

        return mySheets;
    }

    public void write(MySheet[] mySheets, File outFile) throws Exception {
        if (!outFile.exists() && !DkFiles.createNewFileRecursively(outFile)) {
            throw new RuntimeException("Could not create file " + outFile.getPath());
        }

        if (Constant.DEBUG) {
            System.out.println("Write to file [" + outFile.getName() + "]");
            for (MySheet mySheet : mySheets) {
                for (List<String> values : mySheet.getContent()) {
                    for (String value : values) {
                        System.out.print(value + ", ");
                    }
                    System.out.println();
                    DkLogger.getIns().debug(this, "Write %d cells in row", values.size());
                }
                System.out.println();
                DkLogger.getIns().debug(this, "Write %d rows in sheet [%s]", mySheets.length, mySheet.getName());
            }
        }

        ExcelWriter writer = new ExcelWriter();
        writer.write(mySheets, outFile);
    }
}
