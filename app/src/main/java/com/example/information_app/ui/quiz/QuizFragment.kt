package com.example.information_app.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentQuizBinding
import com.example.information_app.ui.ui.exhaustive
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "quiz_v"

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private val viewModel: QuizViewModel by viewModels()

    init {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadQuestion()
        //Log.i(TAG, "new question loaded")

        val binding = FragmentQuizBinding.bind(view)
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
        }

        viewModel.question.observe(viewLifecycleOwner) { question ->
            binding.apply {
                textViewTitle.text =
                    "Question ${question.id} in ${viewModel.questionCount}"
                textViewQuestion.text = question.text
            }
        }

        viewModel.isAnswerWrong.observe(viewLifecycleOwner) {
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
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            // handle navigation
            viewModel.navigationFlow.collect { event ->
                when (event) {
                    is QuizViewModel.NavigationAction.GoToNextQuestion -> {
                        val bundle = Bundle().apply {
                            putInt("questionId", viewModel.questionId + 1)
                        }
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.quizFragment, true)
                            .build()
                        findNavController().navigate(
                            R.id.quizFragment,
                            bundle,
                            navOptions
                        )
                        Log.i(TAG, "navigate to next question with arg: $bundle")
                    }
                    is QuizViewModel.NavigationAction.CompleteQuizWithScore -> {
                        val action =
                            QuizFragmentDirections
                                .actionQuizFragmentToQuizResultFragment(event.score)
                        findNavController().navigate(action)
                        Log.i(TAG, "navigate to result with score: ${event.score}")
                    }
                }.exhaustive
            }
        }
    }

    /*override fun onDestroy() {
        super.onDestroy()

    }*/
}