package com.example.information_app.ui.quiz_result

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.information_app.R
import com.example.information_app.data.models.Question
import com.example.information_app.databinding.FragmentQuizResultBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "Result"

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {

    private val viewModel: QuizResultViewModel by viewModels()
    private lateinit var binding: FragmentQuizResultBinding

    // more control on observer - handle removing observer
    private var questionsObserver: Observer<List<Question>>? = null
    private lateinit var adapter: QuestionAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQuizResultBinding.bind(view)
        binding.apply {
            buttonRetry.setOnClickListener {
                Log.i(TAG, "on click button retry")

                stopUpdateAdapter()
                viewModel.initDatabase()

                // try quiz again after database already init
                navigateToFirstQuestion()
            }
            buttonComplete.setOnClickListener {
                Log.i(TAG, "on click button complete")

                stopUpdateAdapter()
                viewModel.initDatabase()

                // back to source after database already init
                navigateToFragment()
            }

            // apply reference of adapter - change in sync with adapter
            adapter = QuestionAdapter(requireContext())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            // Observe score and totalQuestions and update the text
            viewModel.score.observe(viewLifecycleOwner) { score ->
                binding.textViewReview.text = getScoreText(score, viewModel.totalQuestions.value ?: 0)
            }

            viewModel.totalQuestions.observe(viewLifecycleOwner) { totalQuestions ->
                binding.textViewReview.text = getScoreText(viewModel.score.value ?: 0, totalQuestions)
            }
        }
        // detach adapter from questions on button clicked
        setAdapter(adapter)
    }

    private fun setAdapter(adapter: QuestionAdapter) {
        viewModel.shouldUpdateAdapter.observe(viewLifecycleOwner) { shouldUpdateAdapter ->
            if (shouldUpdateAdapter) {
                questionsObserver = Observer { questions ->
                    adapter.updateQuestions(questions)
                    Log.w(TAG, "questions observer set up: $questions")
                }
                questionsObserver?.let {
                    viewModel.answeredQuestions.observe(viewLifecycleOwner, it)
                    Log.w(TAG, "questions observer start")
                }
            } else {
                questionsObserver?.let {
                    viewModel.answeredQuestions.removeObserver(it)
                    Log.w(TAG, "questions observer removed")
                }
            }
        }
    }

    private fun navigateToFirstQuestion() {
        viewModel.isDatabaseInitialized.observe(viewLifecycleOwner) { isInit ->
            if (isInit) {
                val action =
                    QuizResultFragmentDirections
                        .actionQuizResultFragmentToQuizFragment(viewModel.quizId, 1)
                findNavController().navigate(action)
            } else {
                Log.e(TAG, "database still initializing")
            }
        }
    }

    private fun stopUpdateAdapter() {
        viewModel.shouldUpdateAdapter.value = false
        Log.d(TAG, "to stop updating adapter")
    }

    private fun navigateToFragment() {
        viewModel.isDatabaseInitialized.observe(viewLifecycleOwner) { isInit ->
            if (isInit) {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_left)
                    .setExitAnim(R.anim.slide_out_right)
                    .setPopUpTo(R.id.quizResultFragment, true)
                    .setLaunchSingleTop(true)
                    .build()

                findNavController().navigate(viewModel.fragmentId, null, navOptions)
            } else {
                Log.e(TAG, "database still initializing")
            }
        }
    }

    private fun updateAdapter(adapter: QuestionAdapter) {
        viewModel.answeredQuestions.observe(viewLifecycleOwner) { questions ->
            adapter.updateQuestions(questions)
            Log.d(TAG, "observe questionList: $questions")
        }
    }

    private fun getScoreText(correctCount: Int, totalCount: Int): String {
        return "You got $correctCount in $totalCount correct!"
    }

}