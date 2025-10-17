package com.bucharest.qurio.presentation.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.ItemAnswerButtonBinding

class AnswerAdapter(
    private val answers: List<String>,
    private val onAnswerSelected: (Int) -> Unit
) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    private var selectedPosition: Int? = null
    private var correctAnswer: String? = null
    private var showCorrect = false

    inner class AnswerViewHolder(private val binding: ItemAnswerButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(answer: String, position: Int) {
            binding.answerText.text = answer

            val isSelected = selectedPosition == position
            val isCorrect = showCorrect && answer == correctAnswer
            val isWrong = showCorrect && isSelected && answer != correctAnswer

            when {
                isCorrect -> {
                    binding.answerButton.setBackgroundResource(R.drawable.correct_answer_bg)
                    binding.answerText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.on_primary))
                }
                isWrong -> {
                    binding.answerButton.setBackgroundResource(R.drawable.wrong_answer_bg)
                    binding.answerText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.on_primary))
                }
                isSelected -> {
                    binding.answerButton.setBackgroundResource(R.drawable.selected_answer_bg)
                    binding.answerText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.on_primary))
                }
                else -> {
                    binding.answerButton.setBackgroundResource(R.drawable.answer_button_bg)
                    binding.answerText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.shade_primary))
                }
            }

            binding.answerButton.setOnClickListener {
                if (!showCorrect) {
                    selectedPosition = position
                    notifyDataSetChanged()
                    onAnswerSelected(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val binding = ItemAnswerButtonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(answers[position], position)
    }

    override fun getItemCount(): Int = answers.size

    fun resetSelection() {
        selectedPosition = null
        showCorrect = false
        correctAnswer = null
        notifyDataSetChanged()
    }

    fun showCorrectAnswer(correctAnswer: String) {
        this.correctAnswer = correctAnswer
        this.showCorrect = true
        notifyDataSetChanged()
    }
}
