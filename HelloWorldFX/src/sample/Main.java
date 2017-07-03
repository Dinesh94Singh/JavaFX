package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Main extends Application {

    // Stage is from "Window" Class
    @Override
    public void start(Stage primaryStage) throws Exception {

       /*
        You can have only one root. */

       // *****************************
       // --------- FROM JAVA ---------
       // *****************************

        GridPane java_root = new GridPane();
        java_root.setAlignment(Pos.CENTER);
        java_root.setVgap(10);
        java_root.setHgap(20);

        primaryStage.setTitle("Hello JavaFX");
        primaryStage.setScene(new Scene(java_root, 500, 500));

        Label greeting = new Label("Welcome to JavaFX");
        greeting.setTextFill(Color.GREEN);
        greeting.setFont(Font.font("Times New Roman", FontWeight.BOLD, 70));
        java_root.getChildren().add(greeting);
        primaryStage.show();



        // *****************************
        // --------- FROM FXML ---------
        // *****************************

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Hello World JavaFX");
        primaryStage.setScene(new Scene(root,500,500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
