package com.example.bcamcq.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bcamcq.Model.QuizListModel;
import com.example.bcamcq.R;
import com.example.bcamcq.viewmodel.QuizListViewModel;

import java.util.List;

public class DetailFragment extends Fragment {

    private TextView title,semester,totalQuestions;
    private ImageView topicImg;
    private Button startQuizBtn;
    private NavController navController;
    private int position;
    private ProgressBar progressBar;
    private QuizListViewModel viewModel;
    private String quizId;
    private long totalQuestionCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(QuizListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.subjTextId);
        semester = view.findViewById(R.id.semId);
        totalQuestions = view.findViewById(R.id.totalQuestionId);
        startQuizBtn = view.findViewById(R.id.startQuizbuttonId);
        progressBar = view.findViewById(R.id.detailprogressBar);
        topicImg = view.findViewById(R.id.detailfragImgId);

        navController = Navigation.findNavController(view);
        position = DetailFragmentArgs.fromBundle(getArguments()).getPosition();

        viewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                QuizListModel quiz = quizListModels.get(position);
                semester.setText(quiz.getSemester());
                title.setText(quiz.getTitle());
                totalQuestions.setText(String.valueOf(quiz.getQuestions()));
                Glide.with(view).load(quiz.getImage()).into(topicImg);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                },2000);
                if (!TextUtils.isEmpty(quiz.getQuestions())) {
                    try {
                        totalQuestionCount = Long.parseLong(quiz.getQuestions()); // Convert the string value to a long
                    } catch (NumberFormatException e) {
                        // Handle parsing error gracefully
                        totalQuestionCount = 0; // Default value or another appropriate choice
                    }
                } else {
                    totalQuestionCount = 0; // Default value or another appropriate choice
                }
                quizId = quiz.getQuizId();
            }
        });
        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailFragmentDirections.ActionDetailFragmentToQuizFragment action =
                        DetailFragmentDirections.actionDetailFragmentToQuizFragment();
                action.setQuizId(quizId);
                action.setTotalQueCount(totalQuestionCount);
                navController.navigate(action);
            }
        });
    }
}
