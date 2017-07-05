package com.Dinesh.toDoList;

import com.Dinesh.toDoList.DataModel.ToDoData;
import com.Dinesh.toDoList.DataModel.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {
    private List<ToDoItem> todoitems;

    @FXML
    private ListView todoListView;

    @FXML
    private TextArea ListDescription;

    @FXML
    private Label DeadLineLabel;

    @FXML
    private BorderPane mainBorderPain;

    @FXML
    public void initialize() {
        // If Hard Coded, use this. If you are using a TEXT file, ToDoDate.loadTodoItems() is there.
        /*ToDoItem item1 = new ToDoItem("Mail BirthDay card",
                "Buy a new Birthday Card",
                LocalDate.of(2017, Month.APRIL, 9));

        ToDoItem item2 = new ToDoItem("Doctors Appointment",
                "See Doctor Mike and Bring a Donut",
                LocalDate.of(2017, Month.MAY, 6));

        ToDoItem item3 = new ToDoItem("Finish Design Proposal",
                "Finish the UI for Client",
                LocalDate.of(2017, Month.JUNE, 9));

        ToDoItem item4 = new ToDoItem("PickUp at the Train Station",
                "Train arriving on March 23 on 5:00 Train",
                LocalDate.of(2017, Month.MARCH, 23));

        todoitems = new ArrayList<>();

        todoitems.add(item1);
        todoitems.add(item2);
        todoitems.add(item3);
        todoitems.add(item4);

        // Hardcoding 101
        todoListView.getItems().setAll(todoitems);

        // Singleton Way of doing things - If first time
        ToDoData.getInstance().setToDoItems(todoitems);*/

        // Load up with the First Item selected and details on left side
        // We can hard code this, but since, when we add feature of adding our own, and list gets appended at first
        // it still needs to display the first one, so we are not hardcoding it.

        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    ToDoItem item = (ToDoItem) todoListView.getSelectionModel().getSelectedItem();
                    ListDescription.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    DeadLineLabel.setText(df.format(item.getDeadline()));
                }
            }
        });

        // Specify from the Singleton, after loadToDoDate(..) we come to Controller initialize
        // Here after adding the Action Listener, we are getting the Instance of Singleton and using the Method to get Items.
        // The Beauty of Application here is, ToDoDate is loaded at Main.Java, and the same scope is used in Controller.Java
        // That is because, In JVM, only one instance of that Singleton is created and referred to.
        //todoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
        todoListView.setItems(ToDoData.getInstance().getToDoItems());

        // Select only one item at a time
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // It will select the First List item and fire the changed(ObservableValue, Object, Object) { ... } method and details get executed
        todoListView.getSelectionModel().selectFirst();
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPain.getScene().getWindow());
        dialog.setTitle("Add New ToDo Item");
        dialog.setHeaderText("Use this Dialog to Create a New ToDo Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ToDoItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException exp) {
            System.out.println("Dialog Not able to Load");
            exp.printStackTrace();
            return;
        }
        // Adding Buttons - Only show when User Interacts is success
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            DialogController controller = fxmlLoader.getController();
            ToDoItem newItem = controller.processResults();
            todoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
            // To Select the Item added, so we need to get the Item as well
            // To get the item we modified the processResults to return a ToDoItem
            todoListView.getSelectionModel().select(newItem);
            System.out.println("Ok Pressed");
        } else {
            System.out.println("Cancel Pressed");
        }
    }

    @FXML
    public void handleClickListView() {
        ToDoItem item = (ToDoItem) todoListView.getSelectionModel().getSelectedItem();
        System.out.println("The Selected item is " + item);
        ListDescription.setText(item.getDetails());
        DeadLineLabel.setText(item.getDeadline().toString());

        /*StringBuilder sb = new StringBuilder(item.getDetails());
        sb.append("\n\n\n");
        sb.append("Due: ");
        sb.append(item.getDeadline().toString());
        ListDescription.setText(sb.toString());*/
    }
}
