package com.bucharest.qurio.presentation.difficulty

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.databinding.DifficultyLevelDialogBinding
import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BaseFragment
import javax.inject.Inject

class DifficultyLevelFragment : DialogFragment(), DifficultyLevelView {

    @Inject
    lateinit var presenter: DifficultyLevelPresenter

    private var _binding: DifficultyLevelDialogBinding? = null
    private val binding get() = _binding!!

    private var onDifficultySelected: ((Difficulty) -> Unit)? = null

    companion object {
        private const val ARG_CATEGORY_ID = "category_id"
        private const val ARG_TOTAL_QUESTIONS = "total_questions"

        fun newInstance(
            categoryId: Int,
            totalQuestions: Int,
            onDifficultySelected: (Difficulty) -> Unit
        ): DifficultyLevelFragment {
            val fragment = DifficultyLevelFragment()
            fragment.onDifficultySelected = onDifficultySelected
            val args = Bundle()
            args.putInt(ARG_CATEGORY_ID, categoryId)
            args.putInt(ARG_TOTAL_QUESTIONS, totalQuestions)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DifficultyLevelDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        setupListeners()
        binding.confirmButton.setButtonEnabled(false)
    }

    private fun setupListeners() {
        binding.confirmButton.setOnClickListener {
            presenter.onConfirmClicked()
        }

        binding.cancelButton.setOnClickListener {
            presenter.onCancelClicked()
        }

        binding.closeButton.setOnClickListener {
            presenter.onCancelClicked()
        }

        binding.difficultyButtons.easyButton.setOnClickListener {
            binding.difficultyButtons.easyButton.isSelected = false
            presenter.onEasyClicked()
        }

        binding.difficultyButtons.mediumButton.setOnClickListener {
            binding.difficultyButtons.mediumButton.isSelected = false
            presenter.onMediumClicked()
        }

        binding.difficultyButtons.hardButton.setOnClickListener {
            binding.difficultyButtons.hardButton.isSelected = false
            presenter.onHardClicked()
        }
    }

    override fun onEasySelected() {
        binding.difficultyButtons.easyButton.isSelected = true
        binding.difficultyButtons.mediumButton.isSelected = false
        binding.difficultyButtons.hardButton.isSelected = false
    }

    override fun onMediumSelected() {
        binding.difficultyButtons.mediumButton.isSelected = true
        binding.difficultyButtons.easyButton.isSelected = false
        binding.difficultyButtons.hardButton.isSelected = false
    }

    override fun onHardSelected() {
        binding.difficultyButtons.hardButton.isSelected = true
        binding.difficultyButtons.easyButton.isSelected = false
        binding.difficultyButtons.mediumButton.isSelected = false
    }

    override fun setConfirmButtonEnabled(enabled: Boolean) {
        binding.confirmButton.setButtonEnabled(enabled)
    }

    override fun onConfirmClicked(difficulty: Difficulty) {
        onDifficultySelected?.invoke(difficulty)
        dismiss()
    }

    override fun onCancelClicked() {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
        _binding = null
    }

    override fun showLoading() {}
    override fun hideLoading() {}
    override fun showError(message: String) {}
    override fun showMessage(message: String) {}
}
