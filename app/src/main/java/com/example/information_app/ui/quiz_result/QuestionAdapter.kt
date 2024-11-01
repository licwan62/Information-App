package com.example.information_app.ui.quiz_result

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.information_app.R
import com.example.information_app.data.Question
import com.example.information_app.databinding.QuizResultItemBinding

private const val TAG = "adapter"

class QuestionAdapter(
    private val questionList: List<Question> = listOf()
) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    // specify inflated view or viewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuizResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    // manage binding of ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questionList.get(position)
        holder.bind(question)
    }

    override fun getItemCount(): Int = questionList.count()

    inner class ViewHolder(
        private val binding: QuizResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        override fun onClick(p0: View?) {

        }

        fun bind(question: Question) {
            binding.apply {
                textViewQuestion.text =
                    "${question.id}.${question.text}"

                val userAnswerString = if (question.userAnswer) "Yes" else "No"
                textViewUserAnswer.text =
                    "Your Answer: ${userAnswerString}"

                textViewReview.text =
                    "Correct Answer: ${question.correctAnswer}, ${question.explanation}"

                val iconRes =
                    if (question.isAnswerCorrect) R.drawable.ic_tick
                    else R.drawable.ic_cross
                imageViewSign.setImageResource(iconRes)
            }
            Log.i(TAG, "question get bound onto view holder: $question")
        }
    }
}