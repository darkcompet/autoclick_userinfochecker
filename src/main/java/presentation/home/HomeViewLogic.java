package main.java.presentation.home;

import javafx.application.Platform;
import main.java.presentation.helper.Alerter;
import tool.compet.javacore.util.DkStrings;

public class HomeViewLogic {
   private final HomeView view;
   private final HomeModelLogic modelLogic;

   public HomeViewLogic(HomeView view) {
      this.view = view;
      this.modelLogic = new HomeModelLogic(this);
   }

   public void onStartClick(int siteType, String inFilePath, String outDirPath) {
      if (DkStrings.isWhite(inFilePath) || DkStrings.isWhite(outDirPath)) {
         Alerter.error("Validation", "Input and Output are required");
      }
      else if (siteType <= 0) {
         Alerter.error("Validation", "Site type is required");
      }
      else {
         modelLogic.start(siteType, inFilePath.trim(), outDirPath.trim());
      }
   }

   void logln(LogItemModel logItemModel) {
      Platform.runLater(() -> view.appendLog(logItemModel));
   }

   void alert(String format, Object... args) {
      Platform.runLater(() -> {
         Alerter.error("Alert", DkStrings.format(format, args));
      });
   }

   void clearLogMsg() {
      view.logView.getItems().clear();
   }

   public void onStateChanged(int runState) {
      if (runState == HomeConst.RUN_STATE_RUNNING) {
         view.btnStart.setText("Stop");
      }
      else if (runState == HomeConst.RUN_STATE_STOPPED) {
         view.btnStart.setText("Start");
      }
   }

   public void onExitProgram() {
      modelLogic.onExitProgram();
   }
}
