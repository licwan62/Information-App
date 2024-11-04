package com.example.information_app.ui.quiz_result

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.information_app.R
import com.example.information_app.databinding.FragmentQuizResultBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "result_v"

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {

    private val viewModel: QuizResultViewModel by viewModels()
    private lateinit var binding: FragmentQuizResultBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.generateQuestionList()
        var adapter = QuestionAdapter(requireContext())

        binding = FragmentQuizResultBinding.bind(view)
        binding.apply {
            buttonRetry.setOnClickListener {
                val action =
                    QuizResultFragmentDirections
                        .actionQuizResultFragmentToQuizFragment(1)
                findNavController().navigate(action)
                Log.i(TAG, "on click button retry")
            }
            buttonComplete.setOnClickListener {
                /*viewModel.initDatabase{
                    // navigate to quiz, ensure navigation execute after database init
                    val score = viewModel.score
                    val action =
                        QuizResultFragmentDirections
                            .actionQuizResultFragmentToEscortingFragment(score)
                    findNavController().navigate(action)
                    Log.i(TAG, "on click button complete")
                }*/

                // navigate to quiz, ensure navigation execute after database init
                val score = viewModel.score
                val action =
                    QuizResultFragmentDirections
                        .actionQuizResultFragmentToEscortingFragment(score)
                findNavController().navigate(action)
                Log.i(TAG, "on click button complete")
            }
            textViewReview.text = getScoreText()

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)
        }

        // dynamical applying adapter
        viewModel.questionList.observe(viewLifecycleOwner) { questions ->
            adapter = QuestionAdapter(requireContext(), questions)
            binding.apply {
                recyclerView.adapter = adapter
            }
            Log.d(TAG, "observe questionList: $questions")
        }
    }

    private fun getScoreText(): String {
        val correctCount = viewModel.score.correctCount
        val totalCount = viewModel.score.totalCount
        return "You got $correctCount in $totalCount correct!"
    }

}