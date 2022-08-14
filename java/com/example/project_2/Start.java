package com.example.project_2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.media.Media;

public class Start extends Application {

    private Stage okno;
    @Override
    public void start(Stage primaryStage) throws IOException, RuntimeException {
        String path = "C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\kahoot_music.mp3";
//        Media media = new Media(new File(path).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);

        Button chooseFile = new Button("Choose a file");
        FileChooser fileChooser = new FileChooser();

        Image zadnyiFon = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\background.jpg");
        ImageView pokazat = new ImageView(zadnyiFon);

        chooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file1 = fileChooser.showOpenDialog(primaryStage);
                QuizMaker quizStart = new QuizMaker(primaryStage);
                boolean isOrderedShuffle = false;
//                try {
//                    quizStart.start(file1.getPath(), isOrderedShuffle);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                primaryStage.setScene(quizStart.getScene(0));
                primaryStage.show();
            }
        });

        StackPane startPage = new StackPane(pokazat, chooseFile);
        Scene scene = new Scene(startPage, 800, 650);
        primaryStage.setScene(scene);
        //mediaPlayer.play();
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}