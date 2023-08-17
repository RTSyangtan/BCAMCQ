package com.example.bcamcq.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bcamcq.Model.QuestionModel;
import com.example.bcamcq.R;
import com.example.bcamcq.viewmodel.QuestionViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class QuizFragment extends Fragment implements View.OnClickListener {
    private QuestionViewModel viewModel;
    private NavController navController;
    private ProgressBar progressBar;
    private Button option1Btn,option2Btn,option3Btn,option4Btn,nextQueBtn;
    private TextView questionTv,ansFeedBackTv,questionNumberTv,timerCountTv;
    private ImageView closeQuizBtn;
    private String quizId;
    private long totalQuestions;
    private int currentQueNo = 1;
    private boolean canAnswer = false;
    private long timer;
    private int notAnswered = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private String answer = "";
    private CountDownTimer countDownTimer;
    Random random = new Random();
    ArrayList<Integer> completedQuestion = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        closeQuizBtn = view.findViewById(R.id.closeQuizBtn);
        option1Btn = view.findViewById(R.id.option1Id);
        option2Btn = view.findViewById(R.id.option2Id);
        option3Btn = view.findViewById(R.id.option3Id);
        option4Btn = view.findViewById(R.id.option4Id);
        nextQueBtn = view.findViewById(R.id.nextQuesBtnId);
        questionTv = view.findViewById(R.id.questionId);
        timerCountTv = view.findViewById(R.id.progBarInTextRltId);
        ansFeedBackTv = view.findViewById(R.id.answerPopId);
        questionNumberTv = view.findViewById(R.id.questionNoId);
        progressBar = view.findViewById(R.id.progressBarRltId);

        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQuestions = QuizFragmentArgs.fromBundle(getArguments()).getTotalQueCount();
        viewModel.setQuizId(quizId);
        viewModel.getQuestions();

        option1Btn.setOnClickListener(this);
        option2Btn.setOnClickListener(this);
        option3Btn.setOnClickListener(this);
        option4Btn.setOnClickListener(this);
        nextQueBtn.setOnClickListener(this);

        closeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_quizFragment_to_listFragment);
            }
        });

        loadData();
    }
    private void loadData(){
        enableOptions();
        int questoinNo = random.nextInt(10);
        completedQuestion.add(questoinNo);
        loadQuestions(questoinNo);
    }
    private void enableOptions(){
        option1Btn.setVisibility(View.VISIBLE);
        option2Btn.setVisibility(View.VISIBLE);
        option3Btn.setVisibility(View.VISIBLE);
        option4Btn.setVisibility(View.VISIBLE);

        option1Btn.setEnabled(true);
        option2Btn.setEnabled(true);
        option3Btn.setEnabled(true);
        option4Btn.setEnabled(true);

        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.INVISIBLE);
    }
    private void loadQuestions(int i){

        resetOptions();
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                System.out.println("Question no"+i);
                questionTv.setText(questionModels.get(i - 1).getQuestion());
                option1Btn.setText(questionModels.get(i - 1).getOption_a());
                option2Btn.setText(questionModels.get(i - 1).getOption_b());
                option3Btn.setText(questionModels.get(i - 1).getOption_c());
                option4Btn.setText(questionModels.get(i - 1).getOption_d());
                timer = questionModels.get(i - 1).getTimer();
                answer = questionModels.get(i - 1).getAnswer();

                questionNumberTv.setText(String.valueOf(currentQueNo));
                timer=15;
                startTimer();
            }
        });

        canAnswer = true;
    }
    private void startTimer(){
        timerCountTv.setText(String.valueOf(timer));
        progressBar.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timer*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerCountTv.setText(millisUntilFinished/ 1000 + "");
                Long percent = millisUntilFinished/(timer*10);
                progressBar.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                ansFeedBackTv.setText("Times Up !! No answer selected");
                notAnswered++;
                showNextBtn();
            }
        }.start();
    }
    private void showNextBtn(){
        if(currentQueNo == totalQuestions){
            nextQueBtn.setText("Submit");
            nextQueBtn.setEnabled(true);
            nextQueBtn.setVisibility(View.VISIBLE);
        }
        else{
            nextQueBtn.setVisibility(View.VISIBLE);
            nextQueBtn.setEnabled(true);
            ansFeedBackTv.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View v) {
        int clickedViewId = v.getId();

        if (clickedViewId == R.id.option1Id) {
            verifyAnswer(option1Btn);
        } else if (clickedViewId == R.id.option2Id) {
            verifyAnswer(option2Btn);
        } else if (clickedViewId == R.id.option3Id) {
            verifyAnswer(option3Btn);
        } else if (clickedViewId == R.id.option4Id) {
            verifyAnswer(option4Btn);
        } else if (clickedViewId == R.id.nextQuesBtnId) {
            if (currentQueNo == totalQuestions) {
                submitResult();
            } else {
                currentQueNo++;
                int newquestionNo;
                boolean isCompleted;
                System.out.println("current ques"+currentQueNo);
                System.out.println("total question"+totalQuestions);
                do{
                    newquestionNo = random.nextInt(10)+1;
                    isCompleted = completedQuestion.contains(newquestionNo);
                }while(isCompleted);
                completedQuestion.add(newquestionNo);
                loadQuestions(newquestionNo);
                resetOptions();
            }
        }
    }

    private void submitResult() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswer);
        resultMap.put("wrong", wrongAnswer);
        resultMap.put("notAnswered", notAnswered);

        viewModel.addResults(resultMap);

        // Set the quizId argument in the action
        QuizFragmentDirections.ActionQuizFragmentToResultFragment action =
                QuizFragmentDirections.actionQuizFragmentToResultFragment();
        action.setQuizId(quizId);
        // Navigate to the ResultFragment using the action
        navController.navigate(action);
    }

    private void resetOptions(){
        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.INVISIBLE);
        nextQueBtn.setEnabled(false);
        option1Btn.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.btnColor));
        option2Btn.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.btnColor));
        option3Btn.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.btnColor));
        option4Btn.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.btnColor));
    }
    private void verifyAnswer(Button button){
        if(canAnswer) {
            if (answer.equals(button.getText())) {
                button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.correctAnswer));
                correctAnswer++;
                ansFeedBackTv.setText("Correct Answer");
            }else {
                button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.wrongAnswer));
                wrongAnswer++;
                ansFeedBackTv.setText("Wrong Answer: \nCorrect Answer :"+answer);
            }
        }
        canAnswer = false;
        countDownTimer.cancel();
        showNextBtn();
    }
}