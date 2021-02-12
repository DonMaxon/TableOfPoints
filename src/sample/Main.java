package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import FXFiles.MainWindowController;

import java.io.IOException;

public class Main extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXFiles/MainWindow.fxml"));
        Parent root = loader.load();
        MainWindowController controller = (MainWindowController) loader.getController();
        Scene scene = new Scene(root, 840, 600);
        mainStage = new Stage();
        mainStage.setMinWidth(700);
        mainStage.setMinHeight(500);
        mainStage.setResizable(true);
        mainStage.setScene(scene);
        controller.setStage(mainStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
