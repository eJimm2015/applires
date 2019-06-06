package gui;

import client.AsyncIOClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Enchere;
import model.EnchereDTO;
import utils.VerbeHTTP;

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.*;

public class ClientGUI extends Application {

    private AsyncIOClient asyncIOClient;
    private Gson gson = new Gson();
    private Set<Enchere> encheres = new HashSet<>();

    private FileInputStream input;
    private Image image;
    private ImageView imageView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {


        input = new FileInputStream("src/main/resources/assets/image.png");
        image = new Image(input);
        imageView = new ImageView(image);
        imageView.setFitHeight(240);
        imageView.setPreserveRatio(true);

        asyncIOClient = new AsyncIOClient();
        asyncIOClient.startConnection("localhost", 8080);
        stage.setScene(accueil(stage));
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        stage.setResizable(true);
        stage.show();
        stage.setOnCloseRequest(e -> {
            try {
                asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setVerbe(VerbeHTTP.DISCONNECT)));
                asyncIOClient.stopConnection();
                stop();
                System.exit(0);
            } catch (Exception ex){}
        });
    }

    private VBox principal(Stage stage){

        VBox mainVBox = new VBox();
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        CheckBox mesEncheres = new CheckBox("Les enchères qui me concernent");
        hbox.getChildren().addAll(mesEncheres);

        TableView table = new TableView<>();
        TableColumn<Enchere, String> label = new TableColumn<>("Titre");
        label.prefWidthProperty().bind(table.widthProperty().divide(3));
        TableColumn<Enchere, Integer> auteur = new TableColumn<>("Auteur");
        auteur.prefWidthProperty().bind(table.widthProperty().divide(4.5));
        TableColumn<Enchere, Integer> statut = new TableColumn<>("État");
        statut.prefWidthProperty().bind(table.widthProperty().divide(9));
        TableColumn<Enchere, Integer> prix = new TableColumn<>("Prix");
        prix.prefWidthProperty().bind(table.widthProperty().divide(9));
        TableColumn<Enchere, Integer> gagnant = new TableColumn<>("Gagnant");
        gagnant.prefWidthProperty().bind(table.widthProperty().divide(4.5));
        table.getColumns().addAll(label, auteur, gagnant, statut, prix);
        label.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        statut.setCellValueFactory(new PropertyValueFactory<>("etat"));
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        gagnant.setCellValueFactory(new PropertyValueFactory<>("gagnant"));
        table.setItems(FXCollections.observableArrayList(encheres));
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(!mesEncheres.isSelected()) {
                        table.getItems().setAll(toEncheres(asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setVerbe(VerbeHTTP.GET)))));
                        table.refresh();
                    }
                } catch (Exception e){

                }
            }
        }, 1000, 1000);
     table.setOnMouseClicked(ev -> {
                 if (ev.getClickCount() > 1) {
                     Enchere e = (Enchere) table.getSelectionModel().getSelectedItem();
                     if(e.getAuteur().equals(asyncIOClient.getId())){
                         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                         alert.setTitle("Attention !");
                         alert.setHeaderText("Suppression d'une enchère");
                         alert.setContentText("Confirmez-vous la suppression de cette enchère ?");
                         alert.getDialogPane().getStylesheets().add("assets/style.css");
                         Optional<ButtonType> buttonType = alert.showAndWait();
                         if(buttonType.isPresent()){
                             if(buttonType.get() == ButtonType.OK) {
                                 try {
                                     table.getItems().setAll(toEncheres(asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setEnchere(e).setVerbe(VerbeHTTP.DELETE)))));
                                     table.refresh();
                                     alert.close();
                                 } catch (Exception exception){}

                             }
                         }
                     } else {
                         TextInputDialog alert = new TextInputDialog();
                         alert.setHeaderText("Votre prix ");
                         alert.setContentText("Votre offre : ");
                         alert.getDialogPane().getStylesheets().add("assets/style.css");
                         Optional<String> alertResult = alert.showAndWait();
                         alertResult.ifPresent(value -> {
                             try {
                                 EnchereDTO dto = new EnchereDTO().setVerbe(VerbeHTTP.PUT).setEnchere(e.setGagnant(asyncIOClient.getId()).setPrix(Double.parseDouble(value)));
                                 String s = asyncIOClient.sendMessage(gson.toJson(dto));
                                 encheres = toEncheres(s);
                                 table.getItems().setAll(encheres);
                                 table.refresh();
                                 alert.close();

                             } catch (Exception exc) {
                             }
                         });
                     }
                 }
             });

        mesEncheres.setOnAction(event -> {
            try {
                if(mesEncheres.isSelected()){
                    table.getItems().setAll(toEncheres(asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setEnchere(new Enchere().setAuteur(asyncIOClient.getId())).setFilter(true).setVerbe(VerbeHTTP.GET)))));
                } else table.getItems().setAll(toEncheres(asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setVerbe(VerbeHTTP.GET)))));
                table.refresh();
            } catch (Exception e){}
        });

        HBox form = new HBox();
        TextField titre = new TextField();
        titre.setMinWidth(400);
        titre.setPromptText("Titre de l'enchère");
        TextField prixDepart = new TextField();
        prixDepart.setPromptText("Prix de départ");
        Button submitForm = new Button("Lancer une enchère");
        submitForm.setOnAction(e -> {
           try {
               Enchere enchere = new Enchere().setAuteur(asyncIOClient.getId()).setTitre(titre.getText()).setPrix(Double.parseDouble(prixDepart.getText()));
               String s = asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setVerbe(VerbeHTTP.POST).setEnchere(enchere)));
               encheres = toEncheres(s);
               table.getItems().setAll(encheres);
               table.refresh();
               titre.setText("");
               prixDepart.setText("");

           } catch (Exception e2){}
        });
        form.getChildren().addAll(titre, prixDepart, submitForm);
        stage.setTitle("Principal");
        stage.setHeight(700);
        stage.setWidth(1000);
        stage.centerOnScreen();
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().addAll(hbox, table, form);
        return mainVBox;
    }

    private Scene accueil(Stage stage) {

        TextField errorField = new TextField();
        errorField.setPromptText("Pseudo déjà utilisé, merci de réessayer.");
        errorField.setAlignment(Pos.CENTER);
        errorField.setVisible(false);
        TextField textField = new TextField();
        textField.setMaxWidth(200);
        textField.setPromptText("Veuillez choisir un pseudo");
        textField.setFocusTraversable(false);

        Button playButton = new Button("Commencer");
        playButton.setOnAction(e -> {
            boolean response = true;
            try {
                String s = asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setVerbe(VerbeHTTP.CONNECT).setEnchere(new Enchere().setAuteur(textField.getText()))));
                response = gson.fromJson(s, Boolean.class);
                if(response) {
                    asyncIOClient.setId(textField.getText());
                    s = asyncIOClient.sendMessage(gson.toJson(new EnchereDTO().setVerbe(VerbeHTTP.GET)));
                    encheres = toEncheres(s);
                    stage.getScene().setRoot(principal(stage));
                }
                else errorField.setVisible(true);
            }
            catch (Exception e2){

            }
        });

        VBox vBox = new VBox();

        vBox.setAlignment(Pos.CENTER);

        vBox.setMargin(textField, new Insets(20, 20, 20, 20));
        vBox.setMargin(playButton, new Insets(20, 20, 20, 20));

        vBox.getChildren().addAll(imageView, textField, errorField, playButton);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("assets/style.css");
        return scene;
    }

    private Set<Enchere> toEncheres(String s) throws Exception {
        Type listType = new TypeToken<Set<Enchere>>(){}.getType();
        return gson.fromJson(s, listType);
    }

}
