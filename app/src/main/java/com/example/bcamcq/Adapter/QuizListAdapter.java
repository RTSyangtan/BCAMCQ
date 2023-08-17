package com.example.bcamcq.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bcamcq.Model.QuizListModel;
import com.example.bcamcq.R;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {

    private List<QuizListModel> quizListModels;
    private OnItemClickedListner onItemClickedListner;

    public void setQuizListModels(List<QuizListModel> quizListModels) {
        this.quizListModels = quizListModels;
    }

    public QuizListAdapter(OnItemClickedListner onItemClickedListner){
        this.onItemClickedListner = onItemClickedListner;
    }

    @NonNull
    @Override
    public QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_quiz, parent, false);
        return new QuizListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListAdapter.QuizListViewHolder holder, int position) {
        QuizListModel model = quizListModels.get(position);
        holder.title.setText(model.getTitle());
        // Use getQuestions() method to retrieve the numeric value
        long questions = 0;
        if (!TextUtils.isEmpty(model.getQuestions())) {
            try {
                questions = Long.parseLong(model.getQuestions());
            } catch (NumberFormatException e) {
                // Handle parsing error gracefully
            }
        }
        Glide.with(holder.itemView).load(model.getImage()).into(holder.quizImage);
    }

    @Override
    public int getItemCount() {
        return quizListModels == null ? 0 : quizListModels.size();
    }

    public class QuizListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView quizImage;
        private ConstraintLayout constraintLayout;

        public QuizListViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.quizTextListId);
            quizImage = itemView.findViewById(R.id.subquizImgId);
            constraintLayout = itemView.findViewById(R.id.constrainLayoutId);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickedListner.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickedListner {
        void onItemClick(int position);
    }
}
