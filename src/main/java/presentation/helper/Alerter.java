package main.java.presentation.helper;

import javafx.scene.control.Alert;

public class Alerter {
   public static void error(String title, String body) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setHeaderText(title);
      alert.setContentText(body);
//      alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(body)));
      alert.show();
   }
}
