package com.Dinesh.toDoList;

import com.Dinesh.toDoList.DataModel.ToDoData;
import com.Dinesh.toDoList.DataModel.ToDoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
    private ToggleButton filterToggleButton;

    @FXML
    private ContextMenu listContextMenu;

    private FilteredList<ToDoItem> filteredList;
    private Predicate<ToDoItem> wantAllItems;
    private Predicate<ToDoItem> todaysItems;

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

        // Delete a Menu ITEM, with a Context menu.
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItem item = (ToDoItem) todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        listContextMenu.getItems().addAll(deleteMenuItem);


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

        // Filter Predicate for all Items
        wantAllItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return true;
            }
        };

        // Filter Predicate for Todays Items
        todaysItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return (toDoItem.getDeadline().equals(LocalDate.now()));
            }
        };

        // Show All.
        /*filteredList = new FilteredList<ToDoItem>(ToDoData.getInstance().getToDoItems(),
                new Predicate<ToDoItem>() {
                    @Override
                    public boolean test(ToDoItem toDoItem) {
                        return true;
                    }
                }); */
        filteredList = new FilteredList<ToDoItem>(ToDoData.getInstance().getToDoItems(), wantAllItems);

        // Sorted List
        /*SortedList<ToDoItem> sortedList = new SortedList<ToDoItem>(ToDoData.getInstance().getToDoItems(),
                new Comparator<ToDoItem>() {
                    @Override
                    public int compare(ToDoItem o1, ToDoItem o2) {
                        return o1.getDeadline().compareTo(o2.getDeadline());
                    }
                }); */

        // Sorting the Filtered List instead of entire list
        SortedList<ToDoItem> sortedList = new SortedList<ToDoItem>(filteredList,
                new Comparator<ToDoItem>() {
                    @Override
                    public int compare(ToDoItem o1, ToDoItem o2) {
                        return o1.getDeadline().compareTo(o2.getDeadline());
                    }
                });

        // Specify from the Singleton, after loadToDoDate(..) we come to Controller initialize
        // Here after adding the Action Listener, we are getting the Instance of Singleton and using the Method to get Items.
        // The Beauty of Application here is, ToDoDate is loaded at Main.Java, and the same scope is used in Controller.Java
        // That is because, In JVM, only one instance of that Singleton is created and referred to.
        //todoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
        //todoListView.setItems(ToDoData.getInstance().getToDoItems());
        todoListView.setItems(ToDoData.getInstance().getToDoItems());

        // Select only one item at a time
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // It will select the First List item and fire the changed(ObservableValue, Object, Object) { ... } method and details get executed
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                ListCell<ToDoItem> cell = new ListCell<ToDoItem>() {
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if (item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.RED);
                            } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.BROWN);
                            }
                        }
                    }
                };

                // Lambda Expression
                cell.emptyProperty().addListener(
                        (abs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        });
                return cell;
            }
        });
    }

    public void handleFilterButton() {
        ToDoItem selectedItem =(ToDoItem) todoListView.getSelectionModel().getSelectedItem();
        if (filterToggleButton.isSelected()) {
            filteredList.setPredicate(todaysItems);
            if(filteredList.isEmpty()){
                ListDescription.clear();;
                DeadLineLabel.setText("");
            } else if (filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            }else {
                todoListView.getSelectionModel().selectFirst();
            }
        } else {
            filteredList.setPredicate(wantAllItems);
            todoListView.getSelectionModel().select(selectedItem);
        }
    }

    public void deleteItem(ToDoItem item) {
        // AlertType.CONFIRMATION will automatically provide the ok and cancel buttons
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete ToDo Item");
        alert.setHeaderText("Delete Item: " + item.getShortDescription());
        alert.setContentText("Are you Sure ? Press Ok to Continue, or Cancel to Back Out");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            ToDoData.getInstance().deleteToDoItem(item);
        }
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

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        ToDoItem selectedItem = (ToDoItem) todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteItem(selectedItem);
            }
        }
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }
}
