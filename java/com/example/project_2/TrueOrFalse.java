package com.example.project_2;

import java.util.ArrayList;

public class TrueOrFalse extends Question {
    ArrayList<String> trueFalse = new ArrayList<>();

    @Override
    public String getOptions(int index) {
        return null;
    }

    public void addTrueFalse(String object) {
        trueFalse.add(object);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void shuffleArray() {

    }
}
