package com.Dinesh.toDoList;

import com.Dinesh.toDoList.DataModel.ToDoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("To Do Things");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        // When X button is pressed
        try{
            ToDoData.getInstance().storeToDoItems();
        }catch (IOException exp){
            System.out.println(exp.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        // When Application is Started
        try{
            ToDoData.getInstance().loadToDoItems();
        }catch (IOException exp){
            System.out.println(exp.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
