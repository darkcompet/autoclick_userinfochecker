package main.java.business;

import main.java.business.excel.ExcelLogic;

public class Business {
    public static ExcelLogic excel() {
        return new ExcelLogic();
    }
}
