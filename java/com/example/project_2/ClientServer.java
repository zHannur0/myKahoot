package com.example.project_2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;



public class ClientServer extends Application {

        private Stage clientServer;
        private Socket kahootClient;
        private DataOutputStream serverge;
        private DataInputStream serverden;
        private static int indVodros = 1;
    @Override
    public void start(Stage stage) throws Exception {
        clientServer = stage;
        kahootClient = new Socket("localhost", 2004);
        serverge = new DataOutputStream(kahootClient.getOutputStream());
        serverden = new DataInputStream(kahootClient.getInputStream());

        BorderPane pinPane = new BorderPane();
        TextField pinField = new TextField();
        pinField.setPromptText("Game Pin");
        pinField.setMaxWidth(200);
        pinField.setAlignment(Pos.CENTER);

        Button enter = new Button("Enter");
        enter.setMaxWidth(200);
        enter.setMaxHeight(40);
        enter.setStyle("-fx-background-color: black; -fx-text-fill: white" );
        enter.setAlignment(Pos.CENTER);

        VBox pinEnter = new VBox();
        pinEnter.getChildren().addAll(pinField, enter);
        pinEnter.setSpacing(5);
        pinEnter.setAlignment(Pos.CENTER);

        pinPane.setCenter(new StackPane(pinEnter));
        pinPane.setStyle("-fx-background-color: purple");
        pinPane.requestFocus();

        enter.setOnAction(e -> {
            try {
                serverge.writeInt(Integer.parseInt(pinField.getText()));
                if(serverden.readUTF().equals("Success")) {
                    clientServer.setScene(new Scene(namePane(), 400, 400));
                    clientServer.setTitle("Name Part");
                }else {
                    serverge.writeUTF("Wrong");
                    Label lbl = new Label("Wrong");
                    lbl.setAlignment(Pos.CENTER);
                    Platform.runLater(() -> {
                        pinPane.setBottom(new StackPane(lbl));
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(pinPane, 400,400);
        stage.setScene(scene);
        stage.show();

    }

    public Pane namePane() {
        BorderPane namePane = new BorderPane();

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Name");
        nameField.setMaxWidth(200);
        nameField.setAlignment(Pos.CENTER);

        Button enter = new Button("Enter");
        enter.setMaxWidth(200);
        enter.setMaxHeight(40);
        enter.setStyle("-fx-background-color: black; -fx-text-fill: white" );
        enter.setAlignment(Pos.CENTER);

        VBox pinEnter = new VBox();
        pinEnter.getChildren().addAll(nameField, enter);
        pinEnter.setSpacing(5);
        pinEnter.setAlignment(Pos.CENTER);

        enter.setOnAction(e -> {
            try {
                serverge.writeUTF(nameField.getText());
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        namePane.setCenter(new StackPane(pinEnter));
        namePane.setStyle("-fx-background-color: #3e147f");
        namePane.requestFocus();
        return namePane;
    }

    public Pane gamePane() throws IOException {
        HBox firstSecond = new HBox();
        HBox thirdFour = new HBox();

        StackPane gamePane = new StackPane();
        Image gifka = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\kahoot.gif");
        ImageView gifkaView = new ImageView(gifka);

        Button first = new Button();
        first.setStyle("-fx-background-color: red; -fx-min-width: 195; -fx-min-height: 195");
        first.setOnAction(e -> {
            try {
                serverge.writeUTF("A");
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "last" -> {
                                clientServer.setScene(new Scene(lastPane(), 400,400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button second = new Button();
        second.setStyle("-fx-background-color: blue; -fx-min-width: 195; -fx-min-height: 195");
        second.setOnAction(e -> {
            try {
                serverge.writeUTF("B");
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "last" -> {
                                clientServer.setScene(new Scene(lastPane(), 400,400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button third = new Button();
        third.setStyle("-fx-background-color: orange; -fx-min-width: 195; -fx-min-height: 195");
        third.setOnAction(e -> {
            try {
                serverge.writeUTF("C");
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "last" -> {
                                clientServer.setScene(new Scene(lastPane(), 400,400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button four = new Button();
        four.setStyle("-fx-background-color: green; -fx-min-width: 195; -fx-min-height: 195");
        four.setOnAction(e -> {
            try {
                serverge.writeUTF("D");
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "last" -> {
                                clientServer.setScene(new Scene(lastPane(), 400,400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        firstSecond.getChildren().addAll(first, second);
        firstSecond.setSpacing(5);
        firstSecond.setAlignment(Pos.CENTER);

        thirdFour.getChildren().addAll(third, four);
        thirdFour.setSpacing(5);
        thirdFour.setAlignment(Pos.CENTER);

        VBox game = new VBox(new StackPane(firstSecond), new StackPane(thirdFour));
        game.setSpacing(5);

        gamePane.getChildren().add(game);

        return gamePane;
    }

    public Pane gameFillPane() {
        BorderPane fillPane = new BorderPane();

        TextField fillin = new TextField();
        fillin.setMaxWidth(200);
        fillin.setMaxHeight(40);

        Button enter = new Button("Enter");
        enter.setTextFill(Color.WHITE);
        enter.setStyle("-fx-background-color: black");
        enter.setOnAction(e-> {
            try {
                serverge.writeUTF(fillin.getText());
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                            case "last" -> {
                                clientServer.setScene(new Scene(lastPane(), 400,400));
                                indVodros++;
                                clientServer.setTitle(String.valueOf(indVodros));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        VBox center = new VBox();
        center.getChildren().addAll(fillin, enter);
        center.setAlignment(Pos.CENTER);

        fillPane.setCenter(center);
        return fillPane;
    }

    public Pane trueFalsePane() {
        BorderPane booleanPane = new BorderPane();

        Button trueButton = new Button("TRUE");
        trueButton.setOnAction(e-> {
            try {
                serverge.writeUTF("True");
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                            }
                            case "last" -> {
                                clientServer.setScene(new Scene(lastPane(), 400,400));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button falseButton = new Button("FALSE");
        falseButton.setOnAction(e-> {
            try {
                serverge.writeUTF("false");
                String message = serverden.readUTF();
                boolean t = true;
                while (t) {
                    System.out.println(message);
                    if(message.equals("blank") || message.equals("boolean") || message.equals("test") || message.equals("last")) {
                        switch (message) {
                            case "boolean" -> {
                                clientServer.setScene(new Scene(trueFalsePane(), 400, 400));
                            }
                            case "blank" -> {
                                clientServer.setScene(new Scene(gameFillPane(), 400, 400));
                            }
                            case "test" -> {
                                clientServer.setScene(new Scene(gamePane(), 400, 400));
                            }
                            case "last" -> {
                                clientServer.setScene(new Scene(lastPane(), 400,400));
                            }
                        }
                        t = false;
                    }else message = serverden.readUTF();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        trueButton.setStyle("-fx-background-color: red; -fx-min-width: 195; -fx-min-height: 390; -fx-text-fill: white");
        falseButton.setStyle("-fx-background-color: blue; -fx-min-width: 195; -fx-min-height: 390; -fx-text-fill: white");

        HBox bottom = new HBox();
        bottom.getChildren().addAll(trueButton, falseButton);
        bottom.setAlignment(Pos.CENTER);

        booleanPane.setBottom(bottom);
        return booleanPane;
    }

    public Pane waitPane() {
        Image gifka = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\kahoot.gif");
        ImageView gifkaView = new ImageView(gifka);
        gifkaView.setFitWidth(400);
        gifkaView.setFitHeight(400);

        return new StackPane(gifkaView);
    }

    public Pane lastPane() {
        Text text = new Text("CONGRATULATIONS!");
        text.setFont(Font.font("Times New Roman", FontWeight.THIN, FontPosture.ITALIC, 30));
        text.setFill(Color.RED);
        Image i = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\background.jpg");
        ImageView imageView = new ImageView(i);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        return new StackPane(imageView, text);
    }
}
