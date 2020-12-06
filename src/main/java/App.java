package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.presentation.home.HomeView;
import main.java.presentation.model.Config;
import main.java.presentation.model.Session;
import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.helper.DkJsonHelper;
import tool.compet.javacore.log.DkGuiLogs;
import tool.compet.javacore.util.DkFiles;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main/res/layout/home/home.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        HomeView.onExitProgram();
        super.stop();
    }

    public static void main(String[] args) {
        try {
            parseConfig();
            launch(args);
        }
        catch (Exception e) {
            DkGuiLogs.showMsgBox("Error", "Could not parse config file, errMsg: %s", e.getMessage());
        }
    }

    private static void parseConfig() throws Exception {
        String configJson = DkFiles.loadAsString(DkFiles.makePath(DkConstant.ABS_PATH, "setting", "config.json"));
        Session.config = DkJsonHelper.getIns().json2obj(configJson, Config.class);
    }
}
