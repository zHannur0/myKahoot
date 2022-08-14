package com.example.project_2;

import javafx.scene.Parent;

import java.util.ArrayList;

public abstract class Question  {
    private String description;
    private String answer;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getOptions(int index);
    public abstract int size();
    public abstract void shuffleArray();
}
