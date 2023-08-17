package com.example.bcamcq.Model;

import com.google.firebase.firestore.DocumentId;

public class QuizListModel {

    @DocumentId
    private String quizId;
    private String title,image, semester;
    private String questions;

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public QuizListModel(){}
    public QuizListModel(String quizId, String title, String image, String semester, String questions) {
        this.quizId = quizId;
        this.title = title;
        this.image = image;
        this.semester = semester;
        this.questions = questions;
    }
}
