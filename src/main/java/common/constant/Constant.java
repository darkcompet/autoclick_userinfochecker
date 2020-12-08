package main.java.common.constant;

import tool.compet.core.constant.DkConstant;
import tool.compet.core.util.DkFiles;

public class Constant {
    public static final boolean DEBUG = false; // todo change to false when release

    public static String getSettingFilePath() {
        return DkFiles.makePath(DkConstant.ABS_PATH, "setting", "config.json");
    }
}