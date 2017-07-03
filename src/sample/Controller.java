package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class Controller {
    @FXML
    private TextField name; // same as specified in fxml file
    @FXML
    private Button HelloButton;
    @FXML
    private Button ByeButton;
    @FXML
    private CheckBox MyCheckBox;
    @FXML
    private Label MyLabel;
    @FXML
    public void initialize(){
        HelloButton.setDisable(true);
        ByeButton.setDisable(true);
    }

    @FXML
    public void onButtonClicked(javafx.event.ActionEvent event) {
        if(event.getSource().equals(HelloButton)){
            System.out.println("Hello "+name.getText());
        }else if(event.getSource().equals(ByeButton)){
            System.out.println("Bye "+name.getText());
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            MyLabel.setText("Thread Changed This !!");
                        }
                    });
                }catch(InterruptedException exp){
                    exp.printStackTrace();
                }
            }
        };



        new Thread(task).start();

        if(MyCheckBox.isSelected()){
            name.clear();
            HelloButton.setDisable(true);
            ByeButton.setDisable(true);
        }
    }

    @FXML
    public void handleKeyReleased(){
        String text = name.getText();
        boolean disabledButtons = text.isEmpty() || text.trim().isEmpty();
        HelloButton.setDisable(disabledButtons);
        ByeButton.setDisable(disabledButtons);
    }

    public void handleChanged(){
        System.out.println("The CheckBox is "+ (MyCheckBox.isSelected() ? "Checked": "not Checked"));
    }
}
