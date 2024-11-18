package com.example.information_app.ui.quiz

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentQuizBinding
import com.example.information_app.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "Quiz"

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private val viewModel: QuizViewModel by viewModels()
    private lateinit var binding: FragmentQuizBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQuizBinding.bind(view)

        binding.apply {
            buttonNext.setOnClickListener {
                viewModel.onNextClick()
            }
            buttonLeft.setOnClickListener {
                viewModel.onOptionClick(true)
            }
            buttonRight.setOnClickListener {
                viewModel.onOptionClick(false)
            }
            showReviewViews(binding, false)
        }

        showReviewViews(binding, false)

        // observe emissions of answerSheet (question, count of questions in database)
        viewModel.answerSheet.observe(viewLifecycleOwner) { sheet ->
            val question = sheet.first
            val sum = sheet.second

            if (question == null || sum == null) {
                Log.e(
                    TAG, "observe invalid answerSheet, " +
                            "sum: $sum, " +
                            "question: $question"
                )

                return@observe
            }

            Log.w(
                TAG, "observe answerSheet, " +
                        "sum: $sum, " +
                        "question: $question"
            )

            binding.apply {
                textViewTitle.text = getString(
                    R.string.question_idx_in_total, question.id, sum
                )
                textViewQuestion.text = requireContext().getString(question.question_id)
            }
        }

        // handle navigation, event sent after button click
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            // handle navigation
            viewModel.navigationFlow.collect { event ->
                when (event) {
                    is QuizViewModel.NavigationAction.GoToNextQuestion -> {
                        val bundle = navigateToNextQuestion()
                        Log.i(TAG, "navigate to next question with arg: $bundle")
                    }
                    is QuizViewModel.NavigationAction.CompleteQuiz -> {
                        navigateToResult()
                    }
                    is QuizViewModel.NavigationAction.ShowExplanation -> {
                        showReviewViews(
                            binding, true,
                            event.feedbackRes, event.explanationId
                        )

                        setGradientCardView(binding)

                        Log.i(TAG, "explanation for current question is shown")
                    }
                }.exhaustive
            }
        }
    }

    private fun showReviewViews(
        binding: FragmentQuizBinding,
        show: Boolean,
        @StringRes feedbackRes: Int = 0,
        @StringRes explanationId: Int = 0
    ) {
        binding.apply {
            if (show) {
                linearLayoutButtons.visibility = View.GONE
                textViewReview.visibility = View.VISIBLE
                textViewExplanation.visibility = View.VISIBLE
                buttonNext.visibility = View.VISIBLE

                textViewReview.text = requireContext().getString(feedbackRes)
                textViewExplanation.text = requireContext().getString(explanationId)

            } else {
                linearLayoutButtons.visibility = View.VISIBLE
                textViewReview.visibility = View.GONE
                textViewExplanation.visibility = View.GONE
                buttonNext.visibility = View.GONE
            }
        }
    }

    private fun navigateToNextQuestion(): Bundle {
        val bundle = Bundle().apply {
            putInt("fragment_id", viewModel.fragmentId)
            putInt("quiz_id", viewModel.quizId)
            putInt("question_number", viewModel.questionNumber + 1)
        }
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.quizFragment, true)
            .setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left).build()
        findNavController().navigate(
            R.id.quizFragment, bundle, navOptions
        )
        return bundle
    }

    private fun setGradientCardView(binding: FragmentQuizBinding) {
        val context = requireContext()
        val gradientDrawable =
            GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    ContextCompat.getColor(context, R.color.white),
                    ContextCompat.getColor(context, R.color.light_red)
                )
            )

        gradientDrawable.cornerRadius = 20f  // Match your CardView's corner radius
        binding.frameLayoutColor.background = gradientDrawable
    }

    private fun navigateToResult() {
        val action = QuizFragmentDirections.actionQuizFragmentToQuizResultFragment(viewModel.fragmentId, viewModel.quizId)
        findNavController().navigate(action)
    }
}