package com.bucharest.qurio.presentation.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.databinding.ItemQuestionBinding
import com.bucharest.qurio.domain.entity.Question

class QuestionAdapter : ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(QuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QuestionViewHolder(
        private val binding: ItemQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(question: Question) = with(binding) {
            tvQuestionNumber.text = "Question ${bindingAdapterPosition + 1}"
            tvCategory.text = question.category.name
            tvDifficulty.text = question.difficulty.name.uppercase()
            tvQuestion.text = Html.fromHtml(question.question, Html.FROM_HTML_MODE_LEGACY)

            val answersText = question.answers.mapIndexed { index, answer ->
                val letter = ('A' + index)
                val decodedText =
                    Html.fromHtml(answer.text, Html.FROM_HTML_MODE_LEGACY)
                "$letter. $decodedText"
            }.joinToString("\n\n")

            tvAnswers.text = answersText
        }
    }

    private class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
    }
}