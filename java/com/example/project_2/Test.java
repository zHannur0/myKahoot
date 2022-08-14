package com.example.project_2;

import java.util.ArrayList;
import java.util.Collections;

public class Test extends Question{
    private String label;
    private int numOfOptions;
    private ArrayList<String> options = new ArrayList<>();

    public Test() {
        super();
    }

    public void setOptions(String options) {
        this.label = options;
    }

    public String getOptionAt(int option) {
        return String.valueOf(option);
    }

    public String getLabel() {
        return label;
    }

    public void addOptions(String answer) {
        options.add(answer);
    }

    public int size() {return options.size();}

    @Override
    public void shuffleArray() {
        Collections.shuffle(options);
    }

    public String getOptions(int index) {return options.get(index);}

    @Override
    public String toString() {
        return "Test{" +
                "options='" + label + '\'' +
                ", numOfOptions=" + numOfOptions +
                ", labels=" + options +
                '}';
    }
}
