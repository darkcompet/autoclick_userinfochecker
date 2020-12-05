package main.java.presentation.home;

public class BhxhResultRow {
   public String name; // ho va ten
   public String cmnd; // so cmnd
   public String sex; // gioi tinh
   public String birthday; // ngay sinh

   public String workTo; // den thang
   public String position; // chuc danh, cong viec
   public String salary; // muc luong
   public String hsl; // HSL
   public String company_name; // ten don vi
   public String company_address; // noi lam viec

   @Override
   public String toString() {
      return "BhxhResultRow{" +
         "name='" + name + '\'' +
         ", cmnd='" + cmnd + '\'' +
         ", sex='" + sex + '\'' +
         ", birthday='" + birthday + '\'' +
         ", workTo='" + workTo + '\'' +
         ", position='" + position + '\'' +
         ", salary='" + salary + '\'' +
         ", hsl='" + hsl + '\'' +
         ", company_name='" + company_name + '\'' +
         ", company_address='" + company_address + '\'' +
         '}';
   }
}
