package weather_station;

import java.sql.Timestamp;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class defines a Data Record i.e the basic data model for this software. It is an object with
 * properties such as TimeStamp, Wind Speed, Temperature, light Insolation
 * 
 * @author Anant Tuli - ananttuli@hotmail.com
 */
public class DataRecord {
  SimpleStringProperty timeStamp;
  SimpleStringProperty windSpeed;
  SimpleStringProperty temperature;
  SimpleStringProperty lightInsolation;

  public DataRecord(SimpleStringProperty fields, SimpleStringProperty fields2,
      SimpleStringProperty fields3, SimpleStringProperty fields4) {
    super();
    this.timeStamp = fields;
    this.windSpeed = fields2;
    this.temperature = fields3;
    this.lightInsolation = fields4;
  }

  // Parameterized constructor creates new DataRecord object when string
  // values are passed to it
  public DataRecord(String string, String string2, String string3, String string4) {
    // TODO Auto-generated constructor stub
    this.timeStamp = new SimpleStringProperty(string);
    this.windSpeed = new SimpleStringProperty(string2);
    this.temperature = new SimpleStringProperty(string3);
    this.lightInsolation = new SimpleStringProperty(string4);
  }

  public DataRecord() {
    // TODO Auto-generated constructor stub
  }

  public String getTimeStamp() {
    return timeStamp.get();
  }

  public void setTimeStamp(SimpleStringProperty timeStamp) {
    this.timeStamp = timeStamp;
  }
  
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = new SimpleStringProperty(timeStamp);
    
  }

  public String getWindSpeed() {
    return windSpeed.get();
  }

  public void setWindSpeed(SimpleStringProperty windSpeed) {
    this.windSpeed = windSpeed;
  }

  public String getTemperature() {
    return temperature.get();
  }

  public void setTemperature(SimpleStringProperty temperature) {
    this.temperature = temperature;
  }

  public String getLightInsolation() {
    return lightInsolation.get();
  }

  public void setLightInsolation(SimpleStringProperty lightInsolation) {
    this.lightInsolation = lightInsolation;
  }

}
