package com.example.project_2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class QuizMaker {

    private static ArrayList<Pane> pages = new ArrayList<>();
    private String name;
    private static ArrayList<Scene> scenesPage = new ArrayList<>();
    private static int indexPage = 0;
    private final Timeline secundomer = new Timeline();
    Stage voprosyOkno;
    public static ArrayList<Question> questions = new ArrayList<Question>();
    private static ArrayList<String> vybory = new ArrayList<>();
    private static int sec = 0;

    public QuizMaker(Stage stage) {
        voprosyOkno = stage;
    }

    public Scene getScene(int i) {
        return scenesPage.get(i);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addQuestions(Question questions) {
        this.questions.add(questions);
    }

    public Question getQuestion(int i) {
        return questions.get(i);
    }

    public int getSize() {
        return questions.size();
    }


    public QuizMaker loadFromFile(String put) throws FileNotFoundException {

        QuizMaker quiz = new QuizMaker(voprosyOkno);

        File out = new File(put);
        Scanner in = new Scanner(out);

        while (in.hasNext()) {
            String question = in.nextLine();

            if (question == null) {
                question = in.nextLine();
            }

            if (!question.contains("{blank}") && !question.contains("{boolean}")) {
                Test test = new Test();
                test.setDescription(question);
                for (int i = 0; i < 4; i++) {
                        test.addOptions(in.nextLine());
                }
                test.setAnswer(test.getOptions(0));
                quiz.addQuestions(test);
                quiz.getQuestion(quiz.getSize() - 1).shuffleArray();
            }
                else if(question.contains("{boolean}")) {
                    TrueOrFalse trueOrFalse = new TrueOrFalse();
                    trueOrFalse.setDescription(question);
                    trueOrFalse.setAnswer(in.nextLine());
                    quiz.addQuestions(trueOrFalse);
                }
            else {
                Fillin fill = new Fillin();
                fill.setDescription(question);
                fill.setAnswer(in.nextLine());
                quiz.addQuestions(fill);
            }

            try {
                in.nextLine();
            } catch (NoSuchElementException ignored) {
            }

        }
        in.close();
        return quiz;
    }

    public void start(String path, boolean randomVopros, DataOutputStream clientke, DataInputStream clientten) throws FileNotFoundException {
        QuizMaker quiz = loadFromFile(path);

        if(randomVopros == true) {
            Collections.shuffle(quiz.questions);
        }

        ArrayList<Question> suraqtar = quiz.questions;

        int i = 0;
        for (Question question : suraqtar) {
            if (!suraqtar.get(i).getDescription().contains("blank") && !suraqtar.get(i).getDescription().contains("{boolean}")) {
                pages.add(quiz.testQuestion(suraqtar.get(i), i, suraqtar.size(), clientke, clientten));
                vybory.add("");
            }
                else if(suraqtar.get(i).getDescription().contains("{boolean}")) {
                    pages.add(quiz.trueFalse(suraqtar.get(i), i, suraqtar.size(), clientke, clientten));
                    vybory.add("");
                }
            else {
                pages.add(quiz.forFillin(suraqtar.get(i), i, suraqtar.size(), clientke, clientten));
                vybory.add("");
            }
            i++;
        }


        for(int k = 0; k < pages.size(); k++) {
            Scene scene = new Scene(pages.get(k), 800, 550);
            scenesPage.add(scene);
        }

    }

    public Pane testQuestion(Question question, int ind, int size, DataOutputStream clientke, DataInputStream clientten) {
        question.shuffleArray();
        BorderPane test = new BorderPane();

        Text description = new Text(ind + 1 + ") " +  question.getDescription());
        description.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 14));
        test.setTop(new StackPane(description));


        int[] secundtar = {30};
        int[] n = {1};
        Label uaqytL = new Label(uaqyt(secundtar[0]));
        secundomer.setCycleCount(-1);
        secundomer.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000),
                        event -> {
                            uaqytL.setText(uaqyt(--secundtar[0]));
                            if(secundtar[0] == 0) {
                                secundtar[0] = 30;
                            }
                        }));
        secundomer.play();

        Image znachok = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\TekseruQate\\logo.png");
        ImageView logo = new ImageView(znachok);
        logo.setFitWidth(400);
        logo.setFitHeight(300);
        RadioButton[] variant = new RadioButton[4];

        for(int i = 0; i < 4; i++) {
            variant[i] = new RadioButton(question.getOptions(i));
        }

        ToggleGroup otvety = new ToggleGroup();
        variant[0].setToggleGroup(otvety);
        variant[1].setToggleGroup(otvety);
        variant[2].setToggleGroup(otvety);
        variant[3].setToggleGroup(otvety);

        variant[0].setStyle("-fx-background-color: red; -fx-border-width: 1px; -fx-font-weight: bold; "  +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 400; -fx-min-height: 100");
            variant[0].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    vybory.set(indexPage, variant[0].getText());
                }
            });
        variant[1].setStyle("-fx-background-color: blue; -fx-border-width: 1px; -fx-font-weight: bold; "  +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 400; -fx-min-height: 100");
        variant[1].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vybory.set(indexPage, variant[1].getText());
            }
        });
        variant[2].setStyle("-fx-background-color: orange; -fx-border-width: 1px; -fx-font-weight: bold;"   +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 400; -fx-min-height: 100");
        variant[2].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vybory.set(indexPage, variant[2].getText());
            }
        });
        variant[3].setStyle("-fx-background-color: green; -fx-border-width: 1px; -fx-font-weight: bold;" +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 400; -fx-min-height: 100");
        variant[3].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vybory.set(indexPage, variant[3].getText());
            }
        });

        HBox vverh= new HBox();
        vverh.getChildren().addAll(variant[0],variant[1]);
        vverh.setSpacing(10);
        HBox niz = new HBox();
        niz.getChildren().addAll(variant[2],variant[3]);
        niz.setSpacing(10);

        VBox knopky = new VBox();
        knopky.getChildren().addAll(vverh, niz);
        knopky.setAlignment(Pos.CENTER);
        knopky.setSpacing(10);

        Button nextPage = new Button("Next");
        //Button previousPage = new Button("<<");

//            previousPage.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    indexPage--;
//                    System.out.println(indexPage);
//                    voprosyOkno.setScene(scenesPage.get(indexPage));
//                }
//            });

        if(ind == size - 1) {
            nextPage.setText("Finish Test");
        }

//        if(ind == 0) {
//            previousPage.setVisible(false);
//        }
       nextPage.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               indexPage++;
               sec = secundtar[0];
               if(indexPage == pages.size()) {
                   voprosyOkno.setScene(new Scene(songyBet(), 800,550));
               }else {
                   voprosyOkno.setScene(scenesPage.get(indexPage));
               }

           }
       });

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

    public Pane forFillin(Question question, int ind, int size, DataOutputStream clientke, DataInputStream clientten) {
        BorderPane toltyru = new BorderPane();

        Text surak = new Text(ind + 1 + ") " + question.getDescription().replace("{blank}", "_____"));
        surak.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 14));

        Image fillinSuret = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\fillin.png");
        ImageView suretFillin = new ImageView(fillinSuret);
        suretFillin.setFitHeight(300);
        suretFillin.setFitWidth(450);

        int[] secundtar = {30};
        Label uaqytL = new Label(uaqyt(secundtar[0]));

        secundomer.setCycleCount(-1);
        secundomer.getKeyFrames().add(
                new KeyFrame(Duration.millis(1007 - 7),
                        event -> uaqytL.setText(uaqyt(--secundtar[0]))));
                            if(secundtar[0] == 0) {
                                secundtar[0] = 30;
                            }
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

        Text yourAns = new Text("Type your answer here");
        VBox verticalPane = new VBox(suretFillin,yourAns, fillin);
        verticalPane.setAlignment(Pos.CENTER);
        toltyru.setCenter(new VBox(new StackPane(verticalPane)));

        Button nextPage = new Button("Next");
        //Button previousPage = new Button("<<");

        if(ind == size - 1) {
            nextPage.setText("Finish Test");
        }

//        if(ind == 0)
//        {
//            previousPage.setVisible(false);
//        }

        nextPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                indexPage++;
                sec = secundtar[0];
                if(indexPage == pages.size()) {
                    voprosyOkno.setScene(new Scene(songyBet(), 800,550));
                }else voprosyOkno.setScene(scenesPage.get(indexPage));

            }
        });

//        previousPage.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                indexPage--;
//                System.out.println(indexPage);
//                voprosyOkno.setScene(scenesPage.get(indexPage));
//            }
//        });

        toltyru.setRight(new StackPane(nextPage));

       StackPane uaqytPane = new StackPane();
        Circle domalaq = new Circle();
        domalaq.setFill(Color.VIOLET);
        domalaq.setRadius(50);
        uaqytPane.getChildren().addAll(domalaq, uaqytL);

        toltyru.setLeft(new StackPane(uaqytPane));

        return toltyru;

    }

    public Pane trueFalse(Question question, int ind, int size, DataOutputStream clientke, DataInputStream clientten) {
        BorderPane trueFalse = new BorderPane();

        Text suraq = new Text(ind + 1 + ") " + question.getDescription().replace("{blank}", "_____"));
        suraq.setFont(Font.font("Times to Roman", FontWeight.BOLD, FontPosture.REGULAR, 14));

        RadioButton tr = new RadioButton("True");
        tr.setStyle("-fx-background-color: red; -fx-border-width: 1px; -fx-font-weight: bold; "  +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 390; -fx-min-height: 100");
        RadioButton f = new RadioButton("False");
        f.setStyle("-fx-background-color: blue; -fx-border-width: 1px; -fx-font-weight: bold; "  +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 390; -fx-min-height: 100");

        HBox falseTrue = new HBox();
        falseTrue.getChildren().addAll(tr, f);
        falseTrue.setSpacing(10);
        falseTrue.setAlignment(Pos.CENTER);

        Image znachok = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\TekseruQate\\logo.png");
        ImageView logo = new ImageView(znachok);
        logo.setFitWidth(400);
        logo.setFitHeight(300);
        trueFalse.setCenter(new StackPane(logo));

        ToggleGroup two = new ToggleGroup();
        tr.setToggleGroup(two);
        f.setToggleGroup(two);

        tr.setOnAction(e -> vybory.set(ind, tr.getText()));
        f.setOnAction(e -> vybory.set(ind, f.getText()));

        int[] secundtar = {30};
        Label uaqytL = new Label(uaqyt(secundtar[0]));

        secundomer.setCycleCount(-1);
        secundomer.getKeyFrames().add(
                new KeyFrame(Duration.millis(1007 - 7),
                        event -> uaqytL.setText(uaqyt(--secundtar[0]))));
                            if(secundtar[0] == 0) {
                                secundtar[0] = 30;
                            }
        secundomer.play();


        trueFalse.setBottom(new StackPane(falseTrue));

        Button nextPage = new Button("Next");

        if(ind == size - 1) {
            nextPage.setText("Finish Test");
        }

        nextPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                indexPage++;
                sec = secundtar[0];
                if(indexPage == pages.size()) {
                    voprosyOkno.setScene(new Scene(songyBet(), 800,550));
                }else voprosyOkno.setScene(scenesPage.get(indexPage));

            }
        });


//        previousPage.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                indexPage--;
//                System.out.println(indexPage);
//                voprosyOkno.setScene(scenesPage.get(indexPage));
//            }
//        });

        //trueFalse.setLeft(new StackPane(previousPage));
        trueFalse.setTop(new StackPane(suraq));
        trueFalse.setRight(new StackPane(nextPage));

        StackPane uaqytPane = new StackPane();
        Circle domalaq = new Circle();
        domalaq.setFill(Color.VIOLET);
        domalaq.setRadius(50);
        uaqytPane.getChildren().addAll(domalaq, uaqytL);

        trueFalse.setLeft(new StackPane(uaqytPane));
        return trueFalse;
    }

    public Pane songyBet(){

        int total = 0;
        Text result = new Text("Your Result:");
        result.setFont(Font.font("Times to Roman", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 18));
        Button durysZhauap = new Button("Show Answers");
        durysZhauap.setStyle("-fx-background-color: blue; -fx-border-width: 1px; -fx-font-weight: bold; "  +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 450; -fx-min-height: 50");
        durysZhauap.setOnAction(e -> voprosyOkno.setScene(new Scene(jauapKor(), 800, 550)));

        Button zhabu = new Button("Close Test");
        zhabu.setStyle("-fx-background-color: red; -fx-border-width: 1px; -fx-font-weight: bold; "  +
                "-fx-text-fill: white; -fx-font-size: 15; -fx-min-width: 450; -fx-min-height: 50");
        zhabu.setOnAction(e -> voprosyOkno.close());

        Image songySuret = new Image("file:C:\\Users\\janhan\\Desktop\\Java\\Project_2\\resources\\resources\\img\\result.png");
        ImageView songy = new ImageView(songySuret);
        songy.setFitHeight(300);
        songy.setFitWidth(450);

        for(int i = 0; i < pages.size(); i++) {
            if (questions.get(i).getDescription().contains("{blank}")) {
                BorderPane f = (BorderPane) pages.get(i);
                VBox fi = (VBox) f.getCenter();
                StackPane fil = (StackPane)fi.getChildren().get(0);
                VBox fill = (VBox)fil.getChildren().get(0);
                TextField textField = (TextField)fill.getChildren().get(2);
                vybory.set(i, textField.getText());
            }
            if(vybory.get(i).equals(questions.get(i).getAnswer())) {
                total++;
            }
        }

        double percent = total  * 1.0 / pages.size() * 100;
        Text correctAnswersPer = new Text(String.valueOf(percent).substring(0,4) + "%");
        correctAnswersPer.setFont(Font.font("Times to Roman", FontWeight.THIN, FontPosture.REGULAR, 21));

        Text correctAnswers = new Text("Number of correct answers: " + total + "/" + pages.size());

        Text finishUaqyt = new Text("Finished in: " + uaqyt(sec));


        FlowPane songyPane = new FlowPane(new StackPane(result),new StackPane(correctAnswersPer),new StackPane(correctAnswers),new StackPane(finishUaqyt),durysZhauap,zhabu,songy);
        songyPane.setOrientation(Orientation.VERTICAL);
        songyPane.setVgap(10);
        songyPane.setAlignment(Pos.CENTER);

        BorderPane songyPage = new BorderPane();
        songyPage.setCenter(new StackPane(songyPane));

        return songyPage;
    }

    public Pane getPages(int i) {
        return pages.get(i);
    }

    public void addPage(Pane pane) {
        pages.add(pane);
    }

    public String uaqyt(int s) {
        String uaqytL = "";

        int sekundy = s % (70 - 10);
        //int minuty = s / (87 - 27);

        String sekundyS = sekundy + "";
        //String minutyS = minuty + "";

        if(sekundy < 10) sekundyS = 0 + "" + sekundyS;
        //if(minuty < 10) minutyS = 0 + "" + minutyS;

        uaqytL = sekundyS;
        return uaqytL;
    }

    public Pane jauapKor() {
        VBox showAns = new VBox();
        for(int i = 0; i < questions.size(); i++) {
            Text show = new Text((i + 1 + ") Question: " + questions.get(i).getDescription() +
                    "\n Your Answer: " + vybory.get(i) +
                    "\n Correct Answer: " + questions.get(i).getAnswer()));
            showAns.getChildren().add(show);
        }
        showAns.setSpacing(5);
        return showAns;
    }

}
