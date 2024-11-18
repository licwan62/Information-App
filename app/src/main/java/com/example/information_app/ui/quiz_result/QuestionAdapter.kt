package com.example.information_app.ui.quiz_result

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.information_app.R
import com.example.information_app.data.models.Question
import com.example.information_app.databinding.QuizResultItemBinding

private const val TAG = "Adapter"

class QuestionAdapter(
    private val context: Context,
    private var questionList: List<Question> = listOf()
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
        val question = questionList[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questionList.count()

    @SuppressLint("NotifyDataSetChanged")
    fun updateQuestions(newQuestions: List<Question>) {
        questionList = newQuestions
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: QuizResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        override fun onClick(p0: View?) {

        }

        fun bind(question: Question) {
            binding.apply {

                // question text 1.Is esc...
                val questionText =
                    context.getString(question.question_id)
                textViewQuestion.text =
                    context.getString(
                        R.string.question_text,
                        question.number, questionText
                    )

                // your answer: yes ... cross
                val userAnswerString =
                    if (question.result!!) context.getString(R.string.button_yes)
                    else context.getString(R.string.button_no)
                textViewUserAnswer.text =
                    context.getString(R.string.your_answer, userAnswerString)

                // show explanation in result card
                val explanation =
                    context.getString(question.explanation_id)
                textViewReview.text =
                    context.getString(R.string.correct_answer, explanation)

                // set drawable cross or tick
                val iconRes =
                    if (question.answer == question.result) R.drawable.ic_tick
                    else R.drawable.ic_cross
                val drawable = ContextCompat.getDrawable(context, iconRes)
                textViewUserAnswer.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, drawable, null
                )

                // set card view background
                setGradientCardView(question, binding)
            }
            Log.i(TAG, "$question get bound onto view holder")
        }

        private fun setGradientCardView(question: Question, binding: QuizResultItemBinding) {
            val gradientDrawable =
                if (question.answer == question.result) {
                    GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                            ContextCompat.getColor(context, R.color.white),
                            ContextCompat.getColor(context, R.color.light_green)
                        )
                    )
                } else {
                    GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                            ContextCompat.getColor(context, R.color.white),
                            ContextCompat.getColor(context, R.color.light_red)
                        )
                    )
                }

            gradientDrawable.cornerRadius = 20f  // Match your CardView's corner radius
            binding.frameLayoutColor.background = gradientDrawable
        }

        private fun setSingleColoredCardView(question: Question, binding: QuizResultItemBinding) {
            // bg color for card view
            val colorStateList =
                if (question.answer == question.result) {
                    ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.button_green)
                    )
                } else {
                    ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.button_red)
                    )
                }
            binding.cardView.backgroundTintList = colorStateList
        }
    }
}