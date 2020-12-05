package main.java.presentation.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tool.compet.javacore.constant.DkConstant;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class HomeView {
    @FXML public TextField edtInDirPath;
    @FXML public TextField edtOutDirPath;
    @FXML public Button btnSelectInput;
    @FXML public Button btnSelectOutput;
    @FXML public Button btnStart;
    @FXML public RadioButton rdViettel;
    @FXML public RadioButton rdMobi;
    @FXML public RadioButton rdBhxh;
    @FXML public ListView<LogItemModel> logView;

    private static HomeViewLogic currentHomeViewLogic;
    private final HomeViewLogic homeViewLogic = new HomeViewLogic(this);
    private final ObservableList<LogItemModel> logItems = FXCollections.observableList(new ArrayList<>());

    @FXML
    public void initialize() {
        currentHomeViewLogic = homeViewLogic;

        ToggleGroup toggleGroup = new ToggleGroup();
        rdViettel.setToggleGroup(toggleGroup);
        rdMobi.setToggleGroup(toggleGroup);
        rdBhxh.setToggleGroup(toggleGroup);

        btnSelectInput.setOnAction(event -> {
            SwingUtilities.invokeLater(() -> {
                String curDirPath = edtInDirPath.getText().trim();
                if (curDirPath.length() == 0) {
                    curDirPath = DkConstant.ABS_PATH;
                }
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jFileChooser.setCurrentDirectory(new File(curDirPath));

                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    edtInDirPath.setText(jFileChooser.getSelectedFile().getPath());
                }
            });
        });
        btnSelectOutput.setOnAction(event -> {
            SwingUtilities.invokeLater(() -> {
                String curDirPath = edtInDirPath.getText().trim();
                if (curDirPath.length() == 0) {
                    curDirPath = DkConstant.ABS_PATH;
                }
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jFileChooser.setCurrentDirectory(new File(curDirPath));

                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    edtOutDirPath.setText(jFileChooser.getSelectedFile().getPath());
                }
            });
        });
        logView.setItems(logItems);
        btnStart.setOnAction(event -> {
            Toggle selectedToggle = toggleGroup.getSelectedToggle();
            int siteType = selectedToggle == null ? -1 :
               (selectedToggle == rdViettel) ? HomeConst.ACTION_VIETTEL :
                  (selectedToggle == rdMobi) ? HomeConst.ACTION_MOBI :
                     (selectedToggle == rdBhxh) ? HomeConst.ACTION_BHXH : -1;

            String inDirPath = edtInDirPath.getText();
            String outDirPath = edtOutDirPath.getText();

            homeViewLogic.onStartClick(siteType, inDirPath, outDirPath);
        });
    }

    public void appendLog(LogItemModel logItemModel) {
        ObservableList<LogItemModel> items = logView.getItems();
        items.add(logItemModel);
        logView.scrollTo(items.size() - 1);
    }

    public static void onExitProgram() {
        if (currentHomeViewLogic != null) {
            currentHomeViewLogic.onExitProgram();
        }
    }
}
