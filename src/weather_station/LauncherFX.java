package weather_station;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This is the launcher for weather station. Weather Station is a wind speed, temperature and light
 * insolation display/logger. It makes use of serial communication in order to display live weather
 * details It can then save and load them as weather records in .csv files. This project has a MVC
 * architecture : Model - DataRecord (find DataRecord.java in source code View - FXMLDocument.fxml
 * (responsible for handling GUI aspect of the software Controller - FXMLDocumentController.java
 * (Contains core logic of the software)
 * 
 * @author Anant Tuli - ananttuli@hotmail.com
 */

public class LauncherFX extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

    Application.setUserAgentStylesheet(STYLESHEET_MODENA);

    Scene scene = new Scene(root);

    stage.setTitle("Weather Station");
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Weather Station");
    alert.setHeaderText("Welcome");

    alert.setContentText("A wind speed, temperature and light insolation display/logging software");
    alert.showAndWait();

  }

  /**
   * @param args - Command line arguments This is the main function and program execution starts
   *        here
   */
  public static void main(String[] args) {
    launch(args);

  }

}
