package com.example.project_2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Admin extends Application {

    private TextArea ta = new TextArea();
    private int clientNum = 0;
    private static String pinCode = "";
    private static int indexPage = 0;
    public static ArrayList<Question> questions = new ArrayList<Question>();
    private static ArrayList<String> vybory = new ArrayList<>();
    private static int sec = 0;
    private Button nextButton;
    private static ArrayList<String> rightAnswers = new ArrayList<String>();
    private List<Socket> sockets = new ArrayList<>();
    private static Map<String, Integer> results = new TreeMap<>();
    static Stage okno;

    @Override
    public void start(Stage primarystage) throws Exception {
        okno = primarystage;
        String path = "C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\kahoot_music.mp3";

        Button chooseFile = new Button("Choose a file");
        FileChooser fileChooser = new FileChooser();

        Image zadnyiFon = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\background.jpg");
        ImageView pokazat = new ImageView(zadnyiFon);

        chooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file1 = fileChooser.showOpenDialog(primarystage);
                QuizMaker quizStart = new QuizMaker(primarystage);
                try {
                    quizStart.loadFromFile(file1.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < quizStart.getSize(); i++) {
                    if(quizStart.getQuestion(i).getDescription().contains("{blank}") || quizStart.getQuestion(i).getDescription().contains("{boolean}")) {
                        rightAnswers.add(quizStart.getQuestion(i).getAnswer());
                    }else {
                        for (int k = 0; k < 4; k++) {
                            if(quizStart.getQuestion(i).getOptions(k).equals(quizStart.getQuestion(i).getAnswer())) {
                                rightAnswers.add(String.valueOf((char)('A' + k)));
                                System.out.println(rightAnswers.get(rightAnswers.size() - 1));
                            }
                        }
                    }
                }
                BorderPane pane = new BorderPane();
                pane.setStyle("-fx-background-color: #3e147f");
                String pin = "";

                for(int i = 0; i < 6;i++) {
                    pin += String.valueOf((int)(Math.random() * 9));
                }

                pinCode = pin;

                Text pinLbl = new Text("Game pin: " + pin);
                pinLbl.setFill(Color.WHITE);
                pinLbl.setX(285);
                pinLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 24));

                nextButton = new Button("Start");
                nextButton.setMaxHeight(75);
                nextButton.setMaxWidth(150);

                VBox startName = new VBox();
                startName.setSpacing(10);
                startName.setAlignment(Pos.CENTER);

                HBox names = new HBox();
                names.setAlignment(Pos.CENTER);
                startName.getChildren().addAll(nextButton, names);

                pane.setTop(new StackPane(pinLbl));
                pane.setCenter(new StackPane(startName));
                primarystage.setScene(new Scene(pane, 800, 600));

                HBox wrong = new HBox();
                wrong.setAlignment(Pos.CENTER);
                pane.setBottom(new StackPane(wrong));


                new Thread(() -> {
                    clientNum = 1;
                    try {
                        ServerSocket serverSocket = new ServerSocket(2004);
                        while (true) {
                            System.out.println("Waiting for incomes");
                                Socket socket = serverSocket.accept();
                            System.out.println(clientNum + " Client is Connected!");
                                new Thread(() -> {
                                    int total = 0;
                                    try {
                                        DataInputStream clientten = new DataInputStream(socket.getInputStream());
                                        DataOutputStream clientke = new DataOutputStream(socket.getOutputStream());
                                        sockets.add(socket);
                                        boolean t = true;
                                        Label wrong1 = new Label("");
                                        wrong1.setTextFill(Color.WHITE);
                                        while (t) {
                                            int p = clientten.readInt();
                                            if (p == Integer.parseInt(pinCode)) {
                                                clientke.writeUTF("Success");
                                                t = false;
                                            } else {
                                                clientke.writeUTF("WrongPin");
                                                String w = clientten.readUTF();
                                                System.out.println(w);
                                                wrong1.setText(w);
                                                Platform.runLater(() -> {
                                                    wrong.getChildren().addAll(wrong1);
                                                });
                                            }
                                        }
                                        String name = clientten.readUTF();
                                        System.out.println(name);
                                        Text at = new Text(name + ", ");
                                        at.setFont(Font.font("Times New Roman", FontWeight.THIN, FontPosture.ITALIC, 20));
                                        at.setFill(Color.WHITE);
                                        results.put(name, total);

                                        Platform.runLater(() -> {
                                            names.getChildren().add(at);
                                        });
                                        while (true) {
                                            nextButton.setOnAction(e -> {
                                                if(indexPage!= quizStart.getSize()) {
                                                    if (quizStart.getQuestion(indexPage).getDescription().contains("{blank}")) {
                                                        try {
                                                            for (Socket socket1 : sockets) {
                                                                DataOutputStream client = new DataOutputStream(socket1.getOutputStream());
                                                                client.writeUTF("blank");
                                                            }
                                                        } catch (IOException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                        primarystage.setScene(new Scene(forFillin(quizStart.getQuestion(indexPage), indexPage, quizStart.getSize(), results), 800, 600));
                                                        indexPage++;
                                                    } else if (quizStart.getQuestion(indexPage).getDescription().contains("{boolean}")) {
                                                        try {
                                                            for (Socket socket1 : sockets) {
                                                                DataOutputStream client = new DataOutputStream(socket1.getOutputStream());
                                                                client.writeUTF("boolean");
                                                            }

                                                        } catch (IOException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                        primarystage.setScene(new Scene(trueFalse(quizStart.getQuestion(indexPage), indexPage, quizStart.getSize(), results), 800, 600));
                                                        indexPage++;
                                                    } else {
                                                        try {
                                                            for (Socket socket1 : sockets) {
                                                                DataOutputStream client = new DataOutputStream(socket1.getOutputStream());
                                                                client.writeUTF("test");
                                                            }
                                                        } catch (IOException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                        primarystage.setScene(new Scene(testQuestion(quizStart.getQuestion(indexPage), indexPage, quizStart.getSize(), results), 800,600));
                                                        indexPage++;
                                                    }
                                                }else {
                                                    try {
                                                        for (Socket socket1 : sockets) {
                                                            DataOutputStream client = new DataOutputStream(socket1.getOutputStream());
                                                            client.writeUTF("last");
                                                        }
                                                    } catch (IOException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                    indexPage++;
                                                    primarystage.setScene(new Scene(songyBet(results, indexPage, quizStart.getSize()), 800, 600));
                                                }
                                                primarystage.show();
                                            });
                                            String otvet = clientten.readUTF();
                                            if(otvet.equals(rightAnswers.get(indexPage - 1))) {
                                                total++;
                                                results.put(name, total);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            clientNum++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        StackPane startPage = new StackPane(pokazat, chooseFile);
        Scene scene = new Scene(startPage, 800, 650);
        primarystage.setScene(scene);
        primarystage.show();
    }

    public Pane testQuestion(Question question, int ind, int size, Map<String, Integer> results)  {
        BorderPane test = new BorderPane();

        Text description = new Text(ind + 1 + ") " +  question.getDescription());
        description.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 14));
        test.setTop(new StackPane(description));

        Timeline secundomer = new Timeline();
        int[] secundtar = {10};
        int[] n = {1};
        Label uaqytL = new Label(uaqyt(secundtar[0]));
        secundomer.setCycleCount(-1);
        secundomer.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000),
                        event -> {
                            uaqytL.setText(uaqyt(--secundtar[0]));
                            if(indexPage - ind == 2) {
                                secundomer.stop();
                            }
                            if(secundtar[0] == 0) {
                                secundomer.stop();
                                okno.setScene(new Scene(songyBet(results, ind, size),800,600));
                            }
                        }));
        secundomer.play();

        Image znachok = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\TekseruQate\\logo.png");
        ImageView logo = new ImageView(znachok);
        logo.setFitWidth(400);
        logo.setFitHeight(300);

        Rectangle[] variant = new Rectangle[4];
        Text[] texts = new Text[4];

        for(int i = 0; i < 4; i++) {
            variant[i] = new Rectangle();
            variant[i].setWidth(400);
            variant[i].setHeight(100);
            texts[i] = new Text(question.getOptions(i));
            texts[i].setFill(Color.WHITE);
            if (texts[i].getText().equals(question.getAnswer())) {
                rightAnswers.add(String.valueOf('A' + i));
            }
        }
        variant[0].setFill(Color.RED);
        variant[1].setFill(Color.BLUE);
        variant[2].setFill(Color.ORANGE);
        variant[3].setFill(Color.GREEN);

        HBox vverh= new HBox();
        vverh.getChildren().addAll(new StackPane(variant[0], texts[0]),new StackPane(variant[1], texts[1]));
        vverh.setSpacing(10);
        HBox niz = new HBox();
        niz.getChildren().addAll(new StackPane(variant[2], texts[2]),new StackPane(variant[3], texts[3]));
        niz.setSpacing(10);

        VBox knopky = new VBox();
        knopky.getChildren().addAll(vverh, niz);
        knopky.setAlignment(Pos.CENTER);
        knopky.setSpacing(10);

        Button nextPage = nextButton;
        nextPage.setText("Next");

        StackPane uaqytPane = new StackPane();
        Circle domalaq = new Circle();
        domalaq.setFill(Color.VIOLET);
        domalaq.setRadius(50);
        uaqytPane.getChildren().addAll(domalaq, uaqytL);

        test.setLeft(new StackPane(uaqytPane));
        test.setRight(new StackPane(nextPage));


        VBox center = new VBox(new StackPane(logo));
        center.setPadding(new Insets(30,0,0,0));
        test.setCenter(center);

        test.setBottom(new StackPane(knopky));

        return test;
    }

    public Pane forFillin(Question question, int ind, int size, Map<String, Integer> results) {
        BorderPane toltyru = new BorderPane();

        Text surak = new Text(ind + 1 + ") " + question.getDescription().replace("{blank}", "_____"));
        surak.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 14));

        Image fillinSuret = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\fillin.png");
        ImageView suretFillin = new ImageView(fillinSuret);
        suretFillin.setFitHeight(300);
        suretFillin.setFitWidth(450);
        Timeline secundomer = new Timeline();
        int[] secundtar = {10};
        Label uaqytL = new Label(uaqyt(secundtar[0]));

        secundomer.setCycleCount(-1);
        secundomer.getKeyFrames().add(
                new KeyFrame(Duration.millis(1007 - 7),
                        event -> {
                            uaqytL.setText(uaqyt(--secundtar[0]));
                            if (indexPage - ind == 2) {
                                secundomer.stop();
                            }
                            if (secundtar[0] == 0) {
                                secundomer.stop();
                                okno.setScene(new Scene(songyBet(results, ind, size), 800, 600));
                            }
                        }
                ));
        secundomer.play();

        Image kDegenBelgi = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\k.png");
        ImageView key = new ImageView(kDegenBelgi);
        key.setFitWidth(40);
        key.setFitHeight(20);

        HBox top = new HBox();
        top.getChildren().addAll(key, surak);
        top.setAlignment(Pos.CENTER);
        toltyru.setTop(new StackPane(top));

        TextField fillin = new TextField();
        fillin.setMaxWidth(500);

        Text yourAns = new Text("TYPE YOUR ANSWER!");
        yourAns.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));
        yourAns.setFill(Color.BLUE);
        VBox verticalPane = new VBox(suretFillin,yourAns);
        verticalPane.setAlignment(Pos.CENTER);
        toltyru.setCenter(new VBox(new StackPane(verticalPane)));

        toltyru.setRight(new StackPane(nextButton));

        StackPane uaqytPane = new StackPane();
        Circle domalaq = new Circle();
        domalaq.setFill(Color.VIOLET);
        domalaq.setRadius(50);
        uaqytPane.getChildren().addAll(domalaq, uaqytL);

        toltyru.setLeft(new StackPane(uaqytPane));

        return toltyru;

    }

    public Pane trueFalse(Question question, int ind, int size, Map<String, Integer> results) {
        BorderPane trueFalse = new BorderPane();

        Text suraq = new Text(ind + 1 + ") " + question.getDescription().replace("{boolean}", "_____"));
        suraq.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 14));

        Rectangle tr = new Rectangle();
        tr.setWidth(395);
        tr.setHeight(200);
        tr.setFill(Color.RED);
        Rectangle fa = new Rectangle();
        fa.setWidth(395);
        fa.setHeight(200);
        fa.setFill(Color.BLUE);

        Text t = new Text("TRUE");
        Text f = new Text("FALSE");
        t.setFill(Color.WHITE);
        f.setFill(Color.WHITE);
        t.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));
        f.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));

        HBox falseTrue = new HBox();
        falseTrue.getChildren().addAll(new StackPane(tr, t),new StackPane(fa, f));
        falseTrue.setSpacing(10);
        falseTrue.setAlignment(Pos.CENTER);

        Image znachok = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\TekseruQate\\logo.png");
        ImageView logo = new ImageView(znachok);
        logo.setFitWidth(400);
        logo.setFitHeight(300);
        trueFalse.setCenter(new StackPane(logo));

        Timeline secundomer = new Timeline();
        int[] secundtar = {10};
        Label uaqytL = new Label(uaqyt(secundtar[0]));

        secundomer.setCycleCount(-1);
        secundomer.getKeyFrames().add(
                new KeyFrame(Duration.millis(1007 - 7),
                        event -> {
                            uaqytL.setText(uaqyt(--secundtar[0]));
                            if(indexPage - ind == 2) {
                                secundomer.stop();
                            }
                            if(secundtar[0] == 0) {
                                secundomer.stop();
                                okno.setScene(new Scene(songyBet(results, ind, size),800,600));
                            }
                        }));
        secundomer.play();


        trueFalse.setBottom(new StackPane(falseTrue));


        trueFalse.setTop(new StackPane(suraq));
        trueFalse.setRight(new StackPane(nextButton));

        StackPane uaqytPane = new StackPane();
        Circle domalaq = new Circle();
        domalaq.setFill(Color.VIOLET);
        domalaq.setRadius(50);
        uaqytPane.getChildren().addAll(domalaq, uaqytL);

        trueFalse.setLeft(new StackPane(uaqytPane));
        return trueFalse;
    }

    public Pane songyBet(Map<String, Integer> results, int ind, int size){
        HBox result = new HBox();
        for (Map.Entry<String, Integer> e: results.entrySet()) {
            VBox a = new VBox();
            Rectangle r = new Rectangle();
            r.setWidth(120);
            r.setHeight(e.getValue() * 35);
            r.setFill(Color.PURPLE);
            Text total = new Text(String.valueOf(e.getValue()) + "/" + String.valueOf(size));
            Text name = new Text(e.getKey());
            total.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20));
            total.setFill(Color.WHITE);
            name.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20));
            name.setFill(Color.WHITE);
            a.getChildren().addAll(total,r,name);
            a.setAlignment(Pos.CENTER);
            result.getChildren().add(a);
        }
        result.setAlignment(Pos.CENTER);
        result.setSpacing(20);

        if(ind == size + 1) {
            nextButton.setVisible(false);
        }
        Rectangle endTest = new Rectangle();
        endTest.setHeight(80);
        endTest.setWidth(800);
        endTest.setFill(Color.ORANGE);
        Text end = new Text("SCOREBOARD");
        end.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 40));

        BorderPane songyPage = new BorderPane();
        songyPage.setStyle("-fx-background-color: #3e147f");
        songyPage.setTop(new StackPane(endTest, end));
        songyPage.setRight(new StackPane(nextButton));
        songyPage.setCenter(result);

        return songyPage;
    }


    public String uaqyt(int s) {
        String uaqytL = "";

        int sekundy = s % (70 - 10);
        String sekundyS = sekundy + "";

        if(sekundy < 10) sekundyS = 0 + "" + sekundyS;

        uaqytL = sekundyS;
        return uaqytL;
    }

}

