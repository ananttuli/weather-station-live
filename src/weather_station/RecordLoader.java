package weather_station;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class loads data file chosen from GUI File chooser and creates DataRecord List from the
 * loaded data
 * 
 * @author Anant Tuli - ananttuli@hotmail.com
 * 
 */
public class RecordLoader {

  ObservableList<DataRecord> recordList = FXCollections.observableArrayList();
  String filename;

  public ObservableList<DataRecord> loadRecords(String filename) throws IOException {
    this.filename = filename;
    BufferedReader br = new BufferedReader(new FileReader(this.filename));

    String line;
    while ((line = br.readLine()) != null) {
      String[] fields = line.split(",", -1);


      String specTimeString;
      // Time stamp formatting for easier comprehension in software
      specTimeString = fields[0].substring(0, 2) + "/" + fields[0].substring(2, 4) + "/"
          + fields[0].substring(4, 6);
      specTimeString += " " + fields[0].substring(7, 9) + ":" + fields[0].substring(9, 11) + ":"
          + fields[0].substring(11);
      DataRecord dr = new DataRecord(specTimeString, fields[1], fields[2], fields[3]);
      recordList.add(dr);

    }
    br.close();
    return recordList;
  }

  /**
   * @return the recordList
   */
  public ObservableList<DataRecord> getRecordList() {
    return recordList;
  }

  /**
   * @param recordList the recordList to set
   */
  public void setRecordList(ObservableList<DataRecord> recordList) {
    this.recordList = recordList;
  }

  /**
   * @return the filename
   */
  public String getFilename() {
    return filename;
  }

  /**
   * @param filename the filename to set
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }
}
