package main.java.presentation.home;

import main.java.presentation.model.Config;
import main.java.presentation.model.Session;
import org.openqa.selenium.WebDriver;

public class MobiFinder extends InfoFinder {
   private final Callback callback;
   private final Config.Mobi config;
   private final WebDriver webdriver;
   private final String inDirPath;
   private final String outDirPath;

   public MobiFinder(Callback callback, WebDriver webdriver, String inDirPath, String outDirPath) {
      this.callback = callback;
      this.config = Session.config.mobi;
      this.webdriver = webdriver;
      this.inDirPath = inDirPath;
      this.outDirPath = outDirPath;
   }

   @Override
   public boolean start() {
      return false;
   }

   @Override
   public boolean stop() {
      return false;
   }

   @Override
   public void writeOutputs() {
   }
}
