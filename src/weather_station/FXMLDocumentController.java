package weather_station;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Platform;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.sun.glass.ui.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Ellipse;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

/**
 * This is the controller class of the project WeatherStation
 * 
 * @author Anant Tuli - ananttuli@hotmail.com
 */
public class FXMLDocumentController implements Initializable {


  /*
   * Object of Class SerialPort (imported from external library), used to implement serial
   * communication into software
   */
  SerialPort allCommPorts[];
  
  SerialPort serialCommPort;


  RecordLoader recordLoader = new RecordLoader();
  /**
   * DataRecord - The Model component of this software
   */
  DataRecord dr;

  ObservableList<DataRecord> recordList = FXCollections.observableArrayList();
  /*
   * 
   * Stores live data records
   */
  ObservableList<DataRecord> liveDataRecordList = FXCollections.observableArrayList();

  /*
   * A manual listener for stop button
   */
  boolean stopClicked = false;
  /**
   * isLive has value 1 if user chooses live data, 0 otherwise
   */
  private int isLive = 0;
  /**
   * isAnalog = 0 for Digital, 1 for Analog and 2 for stripchart
   */
  private int isAnalog = 0;
  /**
   * Set according to multiplier
   */
  long sleepTime;
  /*
   * A variable to keep track of current DataRecord Count
   */
  private int globalResumeCounter = 0;
  int randomLiveVariable = 0;
  private int i = 0;

  // Graphical elements mapped to FXML file

  @FXML
  private MenuItem digitalMenuItem;

  @FXML
  private MenuItem analogMenuItem;

  @FXML
  private MenuItem stripChartMenuItem;

  @FXML
  private MenuItem mapMenuItem;
  @FXML
  private MenuItem saveMenuItem;
  @FXML
  private MenuItem liveMenuItem;

  @FXML
  private BarChart<Timestamp, Double> windBarChart;
  @FXML
  private BarChart<Timestamp, Double> temperatureBarChart;
  @FXML
  private BarChart<Timestamp, Double> lightBarChart;
  @FXML
  private LineChart<String, Number> lineChart;
  @FXML
  CategoryAxis xAxis = new CategoryAxis();
  @FXML
  NumberAxis yAxis = new NumberAxis();
  @FXML
  NumberAxis lightYAxis = new NumberAxis();
  @FXML
  NumberAxis tempYAxis = new NumberAxis();
  @FXML
  CategoryAxis lightXAxis = new CategoryAxis();
  @FXML
  CategoryAxis tempXAxis = new CategoryAxis();
  @FXML
  private TableView<DataRecord> digitalTableView;

  @FXML
  private TableColumn<DataRecord, Timestamp> timeStamp;
  @FXML
  private TableColumn<DataRecord, Double> windSpeed;
  @FXML
  private TableColumn<DataRecord, Double> temperature;
  @FXML
  private TableColumn<DataRecord, Double> lightInsolation;


  @FXML
  private Label timestampValueLabel;
  @FXML
  private Label windSpeedValueLabel;
  @FXML
  private Label temp;
  @FXML
  private Label lightValueLabel;
  @FXML
  private Button load_5x;
  @FXML
  private Button load_10x;
  @FXML
  private Button load_realtime;
  @FXML
  private Button stop;
  @FXML
  private Label temperatureWordLabel;
  @FXML
  private Label windSpeedWordLabel;
  @FXML
  private Label lightWordLabel;

  @FXML
  private Label weatherheading;


  // END OF DATA MEMBERS

  // CLASS METHODS
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    hideAllViews();
    digitalTableView.setVisible(true);
    timeStamp.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
    windSpeed.setCellValueFactory(new PropertyValueFactory<>("windSpeed"));
    temperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));
    lightInsolation.setCellValueFactory(new PropertyValueFactory<>("lightInsolation"));
    weatherheading.setVisible(true);


  }

  /**
   * This is the event when data is re-played in real time (button is clicked)
   * 
   * @param event - Real time button is clicked
   * @throws InterruptedException
   */
  @FXML
  private void onClickLoadRealTime(ActionEvent event) throws InterruptedException {
    if (isAnalog == 0)
      replayData(1, 0);
    else if (isAnalog == 1)
      replayData(1, 1);
    else if (isAnalog == 2)
      replayData(1, 2);

    load_10x.setDisable(true);
    load_5x.setDisable(true);
    load_realtime.setDisable(true);
  }

  /**
   * This is the event when data is re-played at 5x speed (button is clicked)
   * 
   * @param event - 5x button is clicked
   * @throws InterruptedException
   */
  @FXML
  private void onClickLoad5x(ActionEvent event) throws InterruptedException {
    if (isAnalog == 0)
      replayData(5, 0);
    else if (isAnalog == 1)
      replayData(5, 1);
    else if (isAnalog == 2)
      replayData(5, 2);

    load_5x.setDisable(true);
    load_10x.setDisable(true);
    load_realtime.setDisable(true);
  }

  /**
   * This is the event when data is re-played at Speed 10x speed (button is clicked)
   * 
   * @param event - 10x button is clicked
   * @throws InterruptedException
   */
  @FXML
  private void onClickLoad10x(ActionEvent event) throws InterruptedException {
    if (isAnalog == 0)
      replayData(10, 0);
    else if (isAnalog == 1)
      replayData(10, 1);
    else if (isAnalog == 2)
      replayData(10, 2);

    load_10x.setDisable(true);
    load_5x.setDisable(true);
    load_realtime.setDisable(true);
  }

  /**
   * Triggered by clicking stop button
   * 
   * @param event
   * @throws InterruptedException
   */
  @FXML
  private void onClickStop(ActionEvent event) throws InterruptedException {
    stopClicked = true;
    load_10x.setDisable(false);
    load_5x.setDisable(false);
    load_realtime.setDisable(false);

    if (isLive == 1) {
      recordList = liveDataRecordList;
      serialCommPort.closePort();



    }
  }

  @FXML
  private void analogMenuItemAction(ActionEvent event) {
    hideAllViews();
    isAnalog = 1;
    showViews();

  }

  @FXML
  private void digitalMenuItemAction(ActionEvent event) throws FileNotFoundException {
    hideAllViews();
    isAnalog = 0;
    showViews();

  }

  @FXML
  private void stripChartMenuItemAction(ActionEvent event) {
    hideAllViews();
    isAnalog = 2;
    showViews();

  }

  @FXML
  private void helpMenuItemAction(ActionEvent event) {


    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information Dialog");
    alert.setHeaderText("Help Guide for WeatherStation");

    alert.setContentText("Software help guide can be found at " + System.getProperty("user.dir")
        + "\\Software Help Guide for users.pdf");

    alert.showAndWait();

  }



  /**
   * This function saves DataRecords from a DataRecord Observable List to comma separated values In
   * future, it will be able to save Live data
   * 
   * @param event - Save option is clicked from File Menu
   * @throws FileNotFoundException
   */
  @FXML
  private void saveMenuItemAction(ActionEvent event) throws FileNotFoundException {
    isLive = 0;


    ObservableList<DataRecord> dataRecordList = FXCollections.observableArrayList();

    // Populate dataRecordList with elements of liveDataRecordList
    // Parse the date for saving in specified format
    for (int p = 0; p < liveDataRecordList.size(); p++) {
      DataRecord d = liveDataRecordList.get(p);
      String specTimeString;
      specTimeString = d.getTimeStamp().substring(0, 2) + d.getTimeStamp().substring(3, 5)
          + d.getTimeStamp().substring(6, 8);
      specTimeString += "." + d.getTimeStamp().substring(9, 11) + d.getTimeStamp().substring(12, 14)
          + d.getTimeStamp().substring(15);
      d.setTimeStamp(specTimeString);
      System.out.println(specTimeString);
      dataRecordList.add(d);
    }
    liveDataRecordList.clear();

    FileChooser fileChooser = new FileChooser();

    // Set extension filter
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    // Show save file dialog
    File saveFile = fileChooser.showSaveDialog(digitalTableView.getScene().getWindow());

    FileWriter writer = null;

    try {
      writer = new FileWriter(saveFile);

      for (int j = 0; j < dataRecordList.size(); j++) {

        writer.append(dataRecordList.get(j).getTimeStamp());
        writer.append(",");
        writer.append(String.valueOf(dataRecordList.get(j).getWindSpeed()));
        writer.append(",");
        writer.append(String.valueOf(dataRecordList.get(j).getTemperature()));
        writer.append(",");
        writer.append(String.valueOf(dataRecordList.get(j).getLightInsolation()));


        writer.append("\n");
      }

      System.out.println("CSV File was generated at " + saveFile.getAbsolutePath());

    } catch (Exception e) {

      e.printStackTrace();
    } finally {

      try {
        writer.flush();
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 
   * @param event - Live Option is chosen from File menu
   */
  @FXML
  private void liveMenuItemAction(ActionEvent event) {
    
    isLive = 1;
    isAnalog = 0;
    new Thread() {
      public void run() {
        allCommPorts = SerialPort.getCommPorts();
        for(int k = 0; k < allCommPorts.length; k++) {
  //        if(allCommPorts[i].getDescriptivePortName().contains("USB Serial Port (COM")) {
            if(allCommPorts[i].LISTENING_EVENT_DATA_AVAILABLE == 1) {
            serialCommPort = allCommPorts[i];
            }
   //       }
        }
        
       
       serialCommPort.setBaudRate(14400);
      
        serialCommPort.openPort();
        System.out.println(serialCommPort.getDescriptivePortName());

        InputStream inputStream = serialCommPort.getInputStream();
        char read;
        String windString = "";
        String tempString = "";
        String lightString = "";
        boolean windRead = false;
        boolean tempRead = false;
        boolean lightRead = false;
        stopClicked = false;
        for (randomLiveVariable = 0; randomLiveVariable < 1000000
            && stopClicked == false; randomLiveVariable++) {
          try {
            windString = "";
            tempString = "";
            lightString = "";

            int month, day, year, hour, min, sec;
            month = LocalDateTime.now().getMonthValue();
            day = LocalDateTime.now().getDayOfMonth();
            year = LocalDateTime.now().getYear();
            hour = LocalDateTime.now().getHour();
            min = LocalDateTime.now().getMinute();
            sec = LocalDateTime.now().getSecond();
            String.valueOf(min);
            String.valueOf(hour);
            String.valueOf(sec);
            String.valueOf(day);
            String.valueOf(month);
            String.valueOf(year);
            String yearString = String.valueOf(year).substring(2);
            // Formats current System time as dd/mm/yy hh:mm:ss for increasing comprehension of
            // date and time within software. It is still saved in the format that the spec
            // sspecifies


            String localDateTime = String.valueOf(day).format("%02d", day) + "/"
                + String.valueOf(month).format("%02d", month) + "/" + yearString;

            localDateTime += " " + String.valueOf(hour).format("%02d", hour) + ":"
                + String.valueOf(min).format("%02d", min) + ":"
                + String.valueOf(min).format("%02d", sec);

            for (int j = 0; j < 10; ++j) {
              read = (char) inputStream.read();
              if (Character.isDigit(read) || read == '.') {

                windString = windString.concat(String.valueOf(read));

              }
              if (read == ',')
                break;
            }


            for (int j = 0; j < 10; ++j) {
              read = (char) inputStream.read();
              if (Character.isDigit(read)  || read == '-') {
                tempString.concat(String.valueOf(read));
                tempString = tempString.concat(String.valueOf(read));
                System.out.println(tempString);
              }
              if (read == ',')
                break;
            }

            for (int j = 0; j < 10; ++j) {
              read = (char) inputStream.read();
              if (Character.isDigit(read)) {
                lightString.concat(String.valueOf(read));
                lightString = lightString.concat(String.valueOf(read));

              }
              if (read == '\n')
                break;
            }

            dr = new DataRecord(localDateTime, windString, tempString, lightString);
            liveDataRecordList.add(dr);

          } catch (Exception e) {
            e.printStackTrace();
          }
          // ----------------------------LIVE DATA DISPLAY----------------
          Platform.runLater(new Runnable() {
            public void run() {
              stop.setVisible(true);
              hideAllViews();
              showViews();

              windSpeedValueLabel.setText(dr.getWindSpeed());
              timestampValueLabel.setText(dr.getTimeStamp());
              temp.setText(dr.getTemperature());

              lightValueLabel.setText(dr.getLightInsolation());


              XYChart.Series w = new XYChart.Series<>();
              w.getData()
                  .add(new XYChart.Data(dr.getTimeStamp(), Double.parseDouble(dr.getWindSpeed())));
              windBarChart.getData().clear();
              windBarChart.getData().add(w);

              XYChart.Series t = new XYChart.Series<>();
              temperatureBarChart.getYAxis().setAutoRanging(false);
              tempYAxis.setUpperBound(60);
              tempYAxis.setLowerBound(-15);
              t.getData().add(
                  new XYChart.Data(dr.getTimeStamp(), Double.parseDouble(dr.getTemperature())));
              temperatureBarChart.getData().clear();
              temperatureBarChart.getData().add(t);

              XYChart.Series l = new XYChart.Series<>();
              l.getData().add(
                  new XYChart.Data(dr.getTimeStamp(), Double.parseDouble(dr.getLightInsolation())));
              lightBarChart.getData().clear();
              lightBarChart.getData().add(l);


              lineChart.setAnimated(false);

              timestampValueLabel.setText(dr.getTimeStamp());
              xAxis = new CategoryAxis();
              yAxis = new NumberAxis();
              lineChart.getData().clear();
              xAxis.setAutoRanging(false);
              yAxis.setLowerBound(-10.0);
              yAxis.setAutoRanging(false);
              yAxis.setUpperBound(80);

              XYChart.Series windSeries = new XYChart.Series<Number, Number>();
              windSeries.setName("Wind Speed");


              XYChart.Series tempSeries = new XYChart.Series<Number, Number>();
              tempSeries.setName("Temperature");

              XYChart.Series lightSeries = new XYChart.Series<Number, Number>();
              lightSeries.setName("Light Insolation (Lux) X 10" + "\u207B" + "\u00B2");

              windSeries.getData().clear();
              tempSeries.getData().clear();
              lightSeries.getData().clear();



              if (randomLiveVariable - 5 >= 0) {
                randomLiveVariable -= 5;
              }

              for (int n = randomLiveVariable; n < randomLiveVariable + 5; n++) {
                windSeries.getData().add(new XYChart.Data(liveDataRecordList.get(n).getTimeStamp(),
                    Double.parseDouble(liveDataRecordList.get(n).getWindSpeed())));
                System.out.println("Wind Add : " + liveDataRecordList.get(n).getWindSpeed());
                tempSeries.getData().add(new XYChart.Data(liveDataRecordList.get(n).getTimeStamp(),
                    Double.parseDouble(liveDataRecordList.get(n).getTemperature())));
                System.out.println("Wind Add : " + liveDataRecordList.get(n).getTemperature());
                lightSeries.getData().add(new XYChart.Data(liveDataRecordList.get(n).getTimeStamp(),
                    Double.parseDouble(liveDataRecordList.get(n).getLightInsolation()) / 100.0));
                System.out.println("Wind Add : " + liveDataRecordList.get(n).getLightInsolation());


              }
              lineChart.getData().addAll(windSeries, tempSeries, lightSeries);
              randomLiveVariable += 5;

            }

          });
          // ---------------------------END OF LIVE DATA VIEW-----------

        }

      }
    }.start();

  }


  /**
   * Sets the desired visibility according to chosen view
   */


  /**
   * 
   * @param event - Load Button from File Menu is clicked
   * @throws FileNotFoundException
   * @throws InterruptedException
   */

  @FXML
  private void loadMenuItemAction(ActionEvent event)
      throws FileNotFoundException, InterruptedException {
    isLive = 0;
    hideAllViews();
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);
    fileChooser.setTitle("Open Weather Records");

    File records;

    records = fileChooser.showOpenDialog(digitalTableView.getScene().getWindow());
    if (records != null) {
      try {
        recordList.clear();
        globalResumeCounter = 0;
        recordList = recordLoader.loadRecords(records.getAbsolutePath());
        digitalTableView.setItems(recordList);
      } catch (FileNotFoundException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      isAnalog = 0;
      showViews();
    }


  }



  /**
   * Controls and sets digital and analog displays according to their load speed chosen
   * 
   * @param multiplier, or the speed at which data is to be replayed
   * @param isAnalog, value is 0 for Digital display, 1 for Analog display and 2 for Stripchart
   * @throws InterruptedException
   */
  public void replayData(int multiplier, int isAnalog) throws InterruptedException {

    if (multiplier == 5)
      sleepTime = 40;
    else if (multiplier == 10)
      sleepTime = 20;
    else if (multiplier == 1)
      sleepTime = 200;

    new Thread() {
      public void run() {
        stopClicked = false;

        for (i = globalResumeCounter; i < (recordList.size() - 5) && stopClicked == false; i++) {
          globalResumeCounter = i;
          try {

            Thread.sleep(sleepTime);

          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          // Run on new thread
          Platform.runLater(new Runnable() {
            public void run() {
              showViews();

              windSpeedValueLabel.setText(recordList.get(i).getWindSpeed());
              timestampValueLabel.setText(recordList.get(i).getTimeStamp());
              temp.setText(recordList.get(i).getTemperature());

              lightValueLabel.setText(recordList.get(i).getLightInsolation());


              timestampValueLabel.setText(recordList.get(i).getTimeStamp());
              windBarChart.getYAxis().setAutoRanging(false);
              XYChart.Series w = new XYChart.Series<>();
              w.getData().add(new XYChart.Data(recordList.get(i).getTimeStamp(),
                  Double.parseDouble(recordList.get(i).getWindSpeed())));
              windBarChart.getData().clear();
              windBarChart.getData().add(w);

              temperatureBarChart.getYAxis().setAutoRanging(false);
              tempYAxis.setUpperBound(60);
              tempYAxis.setLowerBound(-15);
             
              XYChart.Series t = new XYChart.Series<>();
              t.getData().add(new XYChart.Data(recordList.get(i).getTimeStamp(),
                  Double.parseDouble(recordList.get(i).getTemperature())));
              temperatureBarChart.getData().clear();
              temperatureBarChart.getData().add(t);

              
              lightBarChart.getYAxis().setAutoRanging(true);
              XYChart.Series l = new XYChart.Series<>();
              l.getData().add(new XYChart.Data(recordList.get(i).getTimeStamp(),
                  Double.parseDouble(recordList.get(i).getLightInsolation())));
              lightBarChart.getData().clear();
              lightBarChart.getData().add(l);
              // }

              // else if(isAnalog == 2) {
              lineChart.setAnimated(false);

              if ((i + 4) < recordList.size())
                i = i + 4;
              else {
                stop.fire();

              }
              timestampValueLabel.setText(recordList.get(i).getTimeStamp());
              xAxis = new CategoryAxis();
              lineChart.getData().clear();
              xAxis.setAutoRanging(false);
              yAxis.setAutoRanging(false);
              yAxis.setUpperBound(80.0);
              yAxis.setLowerBound(-10.0);

              XYChart.Series windSeries = new XYChart.Series<String, Number>();
              windSeries.setName("Wind Speed");

              XYChart.Series tempSeries = new XYChart.Series<String, Number>();
              tempSeries.setName("Temperature");

              XYChart.Series lightSeries = new XYChart.Series<String, Number>();
              lightSeries.setName("Light Insolation (Lux) X 10" + "\u207B" + "\u00B2");

              windSeries.getData().clear();
              tempSeries.getData().clear();
              lightSeries.getData().clear();

              for (int j = i - 4; j < i + 4 && (i + 3 < recordList.size()); j++) {
                windSeries.getData().add(new XYChart.Data(recordList.get(j).getTimeStamp(),
                    Double.parseDouble(recordList.get(j).getWindSpeed())));
                tempSeries.getData().add(new XYChart.Data(recordList.get(j).getTimeStamp(),
                    Double.parseDouble(recordList.get(j).getTemperature())));
                lightSeries.getData().add(new XYChart.Data(recordList.get(j).getTimeStamp(),
                    Double.parseDouble(recordList.get(j).getLightInsolation()) / 100.0));

              }
              lineChart.getData().addAll(windSeries, tempSeries, lightSeries);
              i -= 4;


            }
          });
        }

      }

    }.start();

  }

  void showViews() {
    hideAllViews();


    stop.setVisible(true);
    if (isLive == 0) {
      load_realtime.setVisible(true);
      load_5x.setVisible(true);
      load_10x.setVisible(true);
    }
    if (isAnalog == 0) {
      timestampValueLabel.setVisible(true);
      windSpeedValueLabel.setVisible(true);
      lightValueLabel.setVisible(true);

      temp.setVisible(true);

      windSpeedWordLabel.setVisible(true);

      lightWordLabel.setVisible(true);

      temperatureWordLabel.setVisible(true);

      if (isLive == 0) {

        digitalTableView.setVisible(true);
        digitalTableView.setItems(recordList);
      }

    } else if (isAnalog == 1) {
      windBarChart.setVisible(true);
      lightBarChart.setVisible(true);
      temperatureBarChart.setVisible(true);
    } else if (isAnalog == 2) {
      lineChart.setVisible(true);
    }
  }

  /**
   * Hides all unwanted elements from the scene
   */
  @FXML
  private void hideAllViews() {

    digitalTableView.setVisible(false);
    timestampValueLabel.setVisible(false);
    windSpeedValueLabel.setVisible(false);
    lightValueLabel.setVisible(false);

    temp.setVisible(false);
    load_realtime.setVisible(false);
    load_5x.setVisible(false);
    load_10x.setVisible(false);
    weatherheading.setVisible(false);
    windSpeedWordLabel.setVisible(false);

    lightWordLabel.setVisible(false);
    temperatureWordLabel.setVisible(false);

    windBarChart.setVisible(false);
    temperatureBarChart.setVisible(false);
    lightBarChart.setVisible(false);
    stop.setVisible(false);
    lineChart.setVisible(false);
  }


  /**
   * Generates random dummy data for all parameters of DataRecord, then creates an ObservableList
   * @deprecated
   * @return ObservableList<DataRecord>
   */
  public ObservableList<DataRecord> randomDataGenerator() {
    ObservableList<DataRecord> dataRecordList = FXCollections.observableArrayList();

    for (int i = 0; i < 5000; i++) {
      double liValue = ThreadLocalRandom.current().nextDouble(0, 600);
      double tempValue = ThreadLocalRandom.current().nextDouble(5, 40);
      double wsValue = ThreadLocalRandom.current().nextDouble(0, 30);

      String localDateTime =
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy.HHmmss"));

      dataRecordList.add(new DataRecord(localDateTime, String.format("%.1f", wsValue),
          String.format("%.0f", tempValue), String.format("%.0f", liValue)));
    }
    return dataRecordList;
  }

}
