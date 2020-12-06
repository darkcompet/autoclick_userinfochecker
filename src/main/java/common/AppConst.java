package main.java.common;

import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.util.DkFiles;

public class AppConst {
   public static final boolean DEBUG = false; // todo change to false when release

   // regex of line separator
   public static final String LS_REGEX = "\\r\\n|\\n|\\r";

   public static final String SETTING_FILE_PATH = DkFiles.makePath(DkConstant.ABS_PATH, "setting", "config.json");
}
