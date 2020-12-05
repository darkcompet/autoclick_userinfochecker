package main.java.presentation.home;

public interface HomeConst {
   int RUN_STATE_RUNNING = 1;
   int RUN_STATE_STOPPED = 2;

   int ACTION_VIETTEL = 1;
   int ACTION_MOBI = 2;
   int ACTION_BHXH = 3;

   String FIREFOX = "firefox";
   String CHROME = "chrome";
   String IE = "ie";

   String FIREFOX_DRIVER_NAME = "webdriver.gecko.driver";
   String CHROME_DRIVER_NAME = "webdriver.chrome.driver";
   String IE_DRIVER_NAME = "webdriver.ie.driver";

   // Viettel extract info
   String VT_EXTRACT_INFO_ID = "pnBoxTabViewCustomer";
   String VT_CUST_NAME = "Tên khách hàng"; // Vũ Phi Cơ
   String VT_CUST_CODE = "Mã khách hàng"; // 1067604377
   String VT_BIRTHDAY = "Ngày sinh"; // 09/11/1991
   String VT_CMT = "CMT"; // 173817280
   String VT_CMT_ISSUE_DATE = "Ngày cấp"; // 10/01/2009
   String VT_CMT_ISSUE_PLACE = "Nơi cấp"; // Thanh Hóa
   String VT_SEX = "Giới tính"; // Nam
   String VT_NATIONALITY = "Quốc tịch"; // Việt Nam
   String VT_CUST_GROUP = "Nhóm khách hàng"; // Cá nhân trong nước
   String VT_CUST_TYPE = "Loại khách hàng"; // Tư nhân trong nước
   String VT_CUST_ADDRESS = "Địa chỉ khách hàng"; // Quảng Văn Quảng Xương Thanh Hóa

   // Mobi extract info
   String MB_EXTRACT_INFO_ID = "unknown";

   // Vina extract info
   String VN_EXTRACT_INFO_ID = "unknown";
}
