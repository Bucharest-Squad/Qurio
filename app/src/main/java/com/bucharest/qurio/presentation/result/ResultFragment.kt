package com.bucharest.qurio.presentation.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bucharest.qurio.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // Use default values for now
        binding.scoreText.text = "Score: 0"
        binding.correctAnswersText.text = "Correct: 0"
        binding.wrongAnswersText.text = "Wrong: 0"
    }

    private fun setupListeners() {
        binding.playAgainButton.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToGameFragment()
            findNavController().navigate(action)
        }

        binding.homeButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
