package com.example.bcamcq.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bcamcq.R;
import com.example.bcamcq.viewmodel.QuestionViewModel;

import java.util.HashMap;

public class ResultFragment extends Fragment {

    private NavController navController;
    private QuestionViewModel viewModel;
    private TextView correctAnswer, wrongAnswer, notAnswered;
    private TextView percentTv;
    private ProgressBar scoreProgressBar;
    private String quizId;
    private Button homeBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        correctAnswer = view.findViewById(R.id.correctRltId);
        wrongAnswer = view.findViewById(R.id.wrongansRltId);
        notAnswered = view.findViewById(R.id.notAnswerRltId);
        percentTv = view.findViewById(R.id.progBarInTextRltId);
        scoreProgressBar = view.findViewById(R.id.progressBarRltId);
        homeBtn = view.findViewById(R.id.goHomeBtnId);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_resultFragment_to_listFragment);
            }
        });

        quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();

        viewModel.setQuizId(quizId);
        viewModel.getResults();
        viewModel.getResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                Long correct = stringLongHashMap.get("correct");
                Long wrong = stringLongHashMap.get("wrong");
                Long noAnswer = stringLongHashMap.get("notAnswered");

                if (correct != null) {
                    correctAnswer.setText(String.valueOf(correct));
                } else {
                    correctAnswer.setText("null"); // Set a default value or handle it appropriately
                }
                if(wrong!=null){
                    wrongAnswer.setText(wrong.toString());
                }else {
                    wrongAnswer.setText("null"); // Set a default value or handle it appropriately
                }
                if(noAnswer!=null){
                    notAnswered.setText(noAnswer.toString());
                }else {
                    notAnswered.setText("0"); // Set a default value or handle it appropriately
                }

                Long total = correct+wrong+noAnswer;
                double percent = (correct * 100.0) / total;

                percentTv.setText(String.valueOf(percent));
                scoreProgressBar.setProgress((int) percent);
            }
        });

    }
}