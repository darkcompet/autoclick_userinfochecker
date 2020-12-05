package main.java.presentation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {
   @Expose
   @SerializedName("viettel")
   public Viettel viettel;

   @Expose
   @SerializedName("mobi")
   public Mobi mobi;

   @Expose
   @SerializedName("bhxh")
   public Bhxh bhxh;

   private static class CompanyType {
      @Expose
      @SerializedName("browserType")
      public String browserType;

      @Expose
      @SerializedName("driverPath")
      public String driverPath;
   }

   public static class Viettel extends CompanyType {
      @Expose
      @SerializedName("url")
      public String url;

      @Expose
      @SerializedName("username")
      public String username;

      @Expose
      @SerializedName("password")
      public String password;
   }

   public static class Mobi extends CompanyType {
      @Expose
      @SerializedName("url")
      public String url;

      @Expose
      @SerializedName("username")
      public String username;

      @Expose
      @SerializedName("password")
      public String password;
   }

   public static class Bhxh extends CompanyType {
      @Expose
      @SerializedName("url")
      public String url;

      @Expose
      @SerializedName("number")
      public String number;

      @Expose
      @SerializedName("username")
      public String username;

      @Expose
      @SerializedName("password")
      public String password;

      @Expose
      @SerializedName("urlFindWithBhxh")
      public String urlFindWithBhxh;

      @Expose
      @SerializedName("attempSearchCount")
      public int attempSearchCount;

      @Expose
      @SerializedName("searchTimeoutInSeconds")
      public int searchTimeoutInSeconds;
   }
}
