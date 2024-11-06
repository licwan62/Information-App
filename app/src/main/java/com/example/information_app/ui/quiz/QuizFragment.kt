package com.example.information_app.ui.quiz

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.data.Score
import com.example.information_app.databinding.FragmentQuizBinding
import com.example.information_app.ui.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "quiz_v"

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val viewModel: QuizViewModel by viewModels()
    private lateinit var binding: FragmentQuizBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadQuestion()
        //Log.i(TAG, "new question loaded")

        binding = FragmentQuizBinding.bind(view)
        binding.apply {
            groupBeforeAnswer.visibility = View.VISIBLE
            groupOnWrongAnswer.visibility = View.GONE
            buttonNext.setOnClickListener {
                viewModel.onNextClick()
            }

            buttonLeft.setOnClickListener {
                viewModel.onOptionClick(true)
            }

            buttonRight.setOnClickListener {
                viewModel.onOptionClick(false)
            }
        }

        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            if (question == null) {
                Log.e(TAG, "empty question to show!")
            }
            binding.apply {
                textViewTitle.text =
                    getString(
                        R.string.question_idx_in_total,
                        question.id, viewModel.questionsCount.value
                    )
                textViewQuestion.text = question.text
            }
        }

        /*viewModel.isAnswered.observe(viewLifecycleOwner) {
            binding.apply {
                groupOnWrongAnswer.visibility =
                    if (it) View.VISIBLE else View.GONE

                groupBeforeAnswer.visibility =
                    if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.answerReview.observe(viewLifecycleOwner) { review ->
            binding.apply {
                textViewReview.text = review
            }
        }*/

        // handle navigation, event sent after button click
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            // handle navigation
            viewModel.navigationFlow.collect { event ->
                when (event) {
                    is QuizViewModel.NavigationAction.GoToNextQuestion -> {
                        val bundle = navigateToNextQuestion()
                        Log.i(TAG, "navigate to next question with arg: $bundle")
                    }
                    is QuizViewModel.NavigationAction.CompleteQuizWithScore -> {
                        navigateToResult(event.score)
                        Log.i(TAG, "navigate to result with score: ${event.score}")
                    }
                    is QuizViewModel.NavigationAction.ShowExplanation -> {
                        showExplanation(binding, event.explanation)
                        Log.i(TAG, "explanation for current question is shown")
                    }
                }.exhaustive
            }
        }
    }

    private fun navigateToNextQuestion(): Bundle {
        val bundle = Bundle().apply {
            val nextQuestionID =
                viewModel.currentQuestionId + 1
            putInt("questionId", nextQuestionID)
        }
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.quizFragment, true)
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .build()
        findNavController().navigate(
            R.id.quizFragment,
            bundle,
            navOptions
        )
        return bundle
    }

    private fun navigateToResult(score: Score) {
        val action =
            QuizFragmentDirections
                .actionQuizFragmentToQuizResultFragment(score)
        findNavController().navigate(action)
    }

    private fun showExplanation(binding: FragmentQuizBinding, explanation: String) {
        binding.apply {
            groupOnWrongAnswer.visibility = View.VISIBLE
            groupBeforeAnswer.visibility = View.GONE
            textViewReview.text = explanation
        }
    }
    // TODO persist data in lifecycle
}