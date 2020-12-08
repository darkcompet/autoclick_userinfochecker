package main.java.presentation.home;

public class LogItemModel {
   public String created;
   public String msg;

   public LogItemModel(String created, String msg) {
      this.created = created;
      this.msg = msg;
   }

   @Override
   public String toString() {
      return msg;
   }

   public String toFileLogString() {
      return created + ": " + msg;
   }
}
