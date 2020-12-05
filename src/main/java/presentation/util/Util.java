package main.java.presentation.util;

public class Util {
   public static void sleep(long millis) {
      try {
         Thread.sleep(millis);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}
