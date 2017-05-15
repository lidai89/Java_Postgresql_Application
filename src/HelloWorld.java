import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.*;
import javafx.beans.value.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
 
public class HelloWorld extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Database Application, Dai Li and Yuxiang Sun");
        //add a sql connection
        postgresqltest conn=new postgresqltest();
        //default csv
        TextField text=new TextField();
       
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
        
        //function selector for insertion
        Text insert_text=new Text("Choose a table to insert");
        Text read_information=new Text("Read a CSV before you start!");
        Text helpinformation=new Text();
        helpinformation.setText("Please load CSV file first");
        Text message=new Text();
        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Org",
                "Leg",
                "Stroke",
                "Distance",
                "Meet",
                "Participant",
                "Event",
                "StrokeOf",
                "Heat",
                "Swim");
        comboBox.getSelectionModel().selectFirst();
        Button func_btn=new Button("Confirm and Insert");
        func_btn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
            public void handle(ActionEvent event) {
        		String input=text.getText();
            	System.out.println(input);
            	//parse and input parameters
            	String[] parameter=text.getText().replaceAll("^[,\\s]+", "").split("[,\\s]+");
                conn.insert_params((String)comboBox.getValue(), parameter);
                conn.test_show_leg();
                message.setText("You inserted '"+input+"' into "+(String)comboBox.getValue());
            }
        });
        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                //System.out.println(ov);
                //System.out.println(t);
                  System.out.println(t1);
                  if(t1.equals("Org")){helpinformation.setText("Please type in the data in format: id, name, is_univ");}
                  if(t1.equals("Meet")){helpinformation.setText("Please type in the data in format: name, start_date, num_days,org_id");}
                  if(t1.equals("Participant")){helpinformation.setText("Please type in the data in format: id, gender, org_id,name");}
                  if(t1.equals("Leg")){helpinformation.setText("Please type in the data in format: leg");}
                  if(t1.equals("Distance")){helpinformation.setText("Please type in the data in format: distance");}
                  if(t1.equals("Stroke")){helpinformation.setText("Please type in the data in format: stroke");}
                  if(t1.equals("Event")){helpinformation.setText("Please type in the data in format: id, gender, distance");}
                  if(t1.equals("StrokeOf")){helpinformation.setText("Please type in the data in format: event_id, leg, stroke");}
                  if(t1.equals("Heat")){helpinformation.setText("Please type in the data in format: id, event_id, meet_name");}
                  if(t1.equals("Swim")){helpinformation.setText("Please type in the data in format: heat_id, event_id, meet_name, participant_id, leg, time");}
              }    
          });
        
        
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                System.out.println(text.getText());
                System.out.println(comboBox.getValue());
            }
        });
        
        
        //Read in CSV file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        Label  csvlabel=new Label("Read in CSV file");
        final Button choosefilebutton = new Button("Choose Your CSV file");
        choosefilebutton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            System.out.println(file.getAbsolutePath());
                            conn.readcsv(file.getAbsolutePath());
                        }
                    }
                });
        
        //part 2
        //parameter input
        TextField text_for_display=new TextField();
        ComboBox comboBox2=new ComboBox();
        comboBox2.getItems().addAll(
        		"For a Meet, display a Heat Sheet",
        		"For a Participant and Meet, display a Heat Sheet limited to just that swimmer",
        		"For a School and Meet, display a Heat Sheet limited to just that School's swimmer",
        		"For a School and Meet, display just the names of the competing swimmers",
        		"For an Event and Meet, display all results sorted by time. Include the heat, lane,swimmers, names and rank.",
        		"For a Meet, display the scores of each school, sorted by scores.");
        TextArea sheet=new TextArea();//display result in text area
        
        Text help_information_display=new Text("Input parameters for sheet display here");
        
        comboBox2.getSelectionModel().selectFirst();
        comboBox2.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                //System.out.println(ov);
                //System.out.println(t);
                  System.out.println(t1);
                  if(t1.equals("For a Meet, display a Heat Sheet"))
                  {help_information_display.setText("Please type in the data in format: Meet_Name");}
                  if(t1.equals("For a Participant and Meet, display a Heat Sheet limited to just that swimmer"))
                  {help_information_display.setText("Please type in the data in format: Meet_Name, Participant_ID");}
                  if(t1.equals("For a School and Meet, display a Heat Sheet limited to just that School's swimmer"))
                  {help_information_display.setText("Please type in the data in format: Meet_Name, School_Name");}
                  if(t1.equals("For a School and Meet, display just the names of the competing swimmers"))
                  {help_information_display.setText("Please type in the data in format: Meet_Name, School_Name");}
                  if(t1.equals("For an Event and Meet, display all results sorted by time. Include the heat, lane,swimmers, names and rank."))
                  {help_information_display.setText("Please type in the data in format: Meet_Name, Event_Name");}
                  if(t1.equals("For a Meet, display the scores of each school, sorted by scores."))
                  {help_information_display.setText("Please type in the data in format: Meet_Name");}
                 }    
          });
        
        GridPane root = new GridPane();
        Label label_for_display=new Label("Choose a sheet to display");
        Button btn_display=new Button("Display");
        btn_display.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
            public void handle(final ActionEvent e) {
                String[] params=text_for_display.getText().replaceAll("^[,\\s]+", "").split("[,\\s]+");
                String type="";
                if(comboBox2.getSelectionModel().getSelectedIndex()==0){type="DisplayHeat";}
                if(comboBox2.getSelectionModel().getSelectedIndex()==1){type="DisplayHeatOfMeetPlayer";}
                if(comboBox2.getSelectionModel().getSelectedIndex()==2){type="DisplayHeatOfMeetSchool";}
                if(comboBox2.getSelectionModel().getSelectedIndex()==3){type="DisplayNameBySchool";}
                if(comboBox2.getSelectionModel().getSelectedIndex()==4){type="DisplayResultByEventMeet";}
                if(comboBox2.getSelectionModel().getSelectedIndex()==5){type="DisplayRank";}
                String result=conn.display_sheet(type,params);
                sheet.setText(result);
            }
        }
        );
        
        //save data into csv
        TextField savepath=new TextField();
        Text savehint=new Text("Input saving path here");
        Button savebtn=new Button("Save Into CSV");
        savebtn.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		conn.savecsv(savepath.getText().replaceAll("^[,\\s]+", ""));
        		
        	}
        });
        root.add(read_information,100,100);
        root.add(text,100,200);
        root.add(insert_text, 800, 200);
        root.add(comboBox,1500, 200);
        root.add(func_btn,1500,300);
        root.add(message, 1502, 300);
        root.add(choosefilebutton, 1500, 100);
        root.add(csvlabel,800,100);
        root.add(savebtn, 800, 4000);
        root.add(savepath, 200, 3500);
        root.add(savehint, 200, 4000);
        root.add(helpinformation, 100, 150);
        root.add(text_for_display, 100, 2000);
        root.add(comboBox2, 1500, 2000);
        root.add(label_for_display, 800, 2000);
        root.add(btn_display,100, 2200);
        root.add(sheet, 100, 3000);
        root.add(help_information_display, 100, 300);
        primaryStage.setScene(new Scene(root, 1600, 1000));
        primaryStage.show();
    }
};