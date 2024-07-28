package com.group4.kidomtoystore.Models;

import com.group4.kidomtoystore.Adapters.FAQAdapter;

public class FAQ {
    int id;
    String question;
    String answer;
    FAQCategory category;
    public enum FAQCategory {
        GENERAL,
        PAYMENT,
        SERVICE,
        ACCOUNT,
        POLICY

    }

    public FAQ(int id, String question, String answer, FAQCategory category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public FAQCategory getCategory() {
        return category;
    }

    public void setCategory(FAQCategory category) {
        this.category = category;
    }
}
