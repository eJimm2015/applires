package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientGUI extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Label label = new Label("Salut");
        //creating a text field
        TextField textField = new TextField();
        textField.setMaxWidth(200);

        //Creating the play button
        Button playButton = new Button("Commencer");


        //Instantiating the VBox class
        VBox vBox = new VBox();

        //Setting the space between the nodes of a VBox pane
        vBox.setSpacing(5);

        vBox.setAlignment(Pos.BASELINE_CENTER);

        //Setting the margin to the nodes
        vBox.setMargin(textField, new Insets(20, 20, 20, 20));
        vBox.setMargin(playButton, new Insets(20, 20, 20, 20));

        //retrieving the observable list of the VBox
        ObservableList list = vBox.getChildren();

        //Adding all the nodes to the observable list
        list.addAll(label, textField, playButton);

        //Creating a scene object
        Scene scene = new Scene(vBox);

        //Setting title to the Stage
        stage.setTitle("Vbox Example");

        //Adding scene to the stage
        stage.setScene(scene);

        stage.setMaximized(true);
        //Displaying the contents of the stage
        stage.show();
    }
}
