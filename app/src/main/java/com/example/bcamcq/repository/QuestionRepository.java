package com.example.bcamcq.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bcamcq.Model.QuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class QuestionRepository {

    private FirebaseFirestore firebaseFirestore;
    private String quizId;
    private HashMap<String ,Long> resultMap = new HashMap<>();
    private OnQuestionLoad onQuestionLoad;
    private OnResultAdded onResultAdded;
    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private OnResultLoad onResultLoad;
    public void getResults() {
        Log.d("QuestionRepository", "Fetching results data for quizId: " + quizId + " and userID: " + currentUserID);
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("results").document(currentUserID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Long correct = document.getLong("correct");
                                Long wrong = document.getLong("wrong");
                                Long notAnswered = document.getLong("notAnswered");

                                if (correct != null && wrong != null && notAnswered != null) {
                                    resultMap.put("correct", correct);
                                    resultMap.put("wrong", wrong);
                                    resultMap.put("notAnswered", notAnswered);
                                    onResultLoad.onResultLoad(resultMap);
                                } else {
                                    Log.d("QuestionRepository", "Missing or null fields in results document");
                                    onResultLoad.onError(new Exception("Missing or null fields"));
                                }
                            } else {
                                Log.d("QuestionRepository", "Document not found or null document");
                                onResultLoad.onError(new Exception("Document not found or null document"));
                            }
                        } else {
                            Log.d("QuestionRepository", "Error fetching results data: " + task.getException().getMessage());
                            onResultLoad.onError(task.getException());
                        }
                    }
                });
    }
    public void addResults(HashMap<String, Object> resultMap){
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("results").document(currentUserID)
                .set(resultMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onResultAdded.onSubmit();
                        }else {
                            onResultAdded.onError(task.getException());
                        }
                    }
                });
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public QuestionRepository(OnQuestionLoad onQuestionLoad, OnResultAdded onResultAdded, OnResultLoad onResultLoad){
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.onQuestionLoad=onQuestionLoad;
        this.onResultAdded=onResultAdded;
        this.onResultLoad=onResultLoad;
    }
    public void getQuestions (){
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            onQuestionLoad.onLoad(task.getResult().toObjects(QuestionModel.class));
                        }else{
                            onQuestionLoad.onError(task.getException());
                        }
                    }
                });
    }

    public void addResult(HashMap<String, Object> resultMap) {
    }
    public interface OnResultLoad{
        void onResultLoad(HashMap<String, Long> resultMap);
        void onError(Exception e);
    }

    public interface OnQuestionLoad{
        void onLoad(List<QuestionModel> questionModels);
        void onError(Exception e);
    }
    public interface OnResultAdded{
        boolean onSubmit();
        void onError(Exception e);
    }
}
