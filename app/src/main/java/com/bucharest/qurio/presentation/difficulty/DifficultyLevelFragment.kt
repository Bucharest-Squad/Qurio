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
        
        // Debug: Check if buttons are found
        android.util.Log.d("DifficultyDialog", "Confirm button found: ${binding.confirmButton != null}")
        android.util.Log.d("DifficultyDialog", "Cancel button found: ${binding.cancelButton != null}")
        android.util.Log.d("DifficultyDialog", "Close button found: ${binding.closeButton != null}")
        android.util.Log.d("DifficultyDialog", "Easy button found: ${binding.difficultyButtons.easyButton != null}")
        
        // Attach presenter
        presenter.attachView(this)
        
        setupListeners()
        // Initially disable confirm button
        binding.confirmButton.setButtonEnabled(false)
    }

    private fun setupListeners() {
        binding.confirmButton.setOnClickListener {
            android.util.Log.d("DifficultyDialog", "Confirm button clicked")
            presenter.onConfirmClicked()
        }

        binding.cancelButton.setOnClickListener {
            android.util.Log.d("DifficultyDialog", "Cancel button clicked")
            presenter.onCancelClicked()
        }

        binding.closeButton.setOnClickListener {
            android.util.Log.d("DifficultyDialog", "Close button clicked")
            presenter.onCancelClicked()
        }

        binding.difficultyButtons.easyButton.setOnClickListener {
            android.util.Log.d("DifficultyDialog", "Easy button clicked")
            // Prevent the button's internal toggle behavior
            binding.difficultyButtons.easyButton.isSelected = false
            presenter.onEasyClicked()
        }

        binding.difficultyButtons.mediumButton.setOnClickListener {
            android.util.Log.d("DifficultyDialog", "Medium button clicked")
            // Prevent the button's internal toggle behavior
            binding.difficultyButtons.mediumButton.isSelected = false
            presenter.onMediumClicked()
        }

        binding.difficultyButtons.hardButton.setOnClickListener {
            android.util.Log.d("DifficultyDialog", "Hard button clicked")
            // Prevent the button's internal toggle behavior
            binding.difficultyButtons.hardButton.isSelected = false
            presenter.onHardClicked()
        }
    }

    override fun onEasySelected() {
        android.util.Log.d("DifficultyDialog", "Setting Easy selected, Medium and Hard unselected")
        binding.difficultyButtons.easyButton.isSelected = true
        binding.difficultyButtons.mediumButton.isSelected = false
        binding.difficultyButtons.hardButton.isSelected = false
    }

    override fun onMediumSelected() {
        android.util.Log.d("DifficultyDialog", "Setting Medium selected, Easy and Hard unselected")
        binding.difficultyButtons.mediumButton.isSelected = true
        binding.difficultyButtons.easyButton.isSelected = false
        binding.difficultyButtons.hardButton.isSelected = false
    }

    override fun onHardSelected() {
        android.util.Log.d("DifficultyDialog", "Setting Hard selected, Easy and Medium unselected")
        binding.difficultyButtons.hardButton.isSelected = true
        binding.difficultyButtons.easyButton.isSelected = false
        binding.difficultyButtons.mediumButton.isSelected = false
    }

    override fun setConfirmButtonEnabled(enabled: Boolean) {
        android.util.Log.d("DifficultyDialog", "setConfirmButtonEnabled: $enabled")
        binding.confirmButton.setButtonEnabled(enabled)
    }

    override fun onConfirmClicked(difficulty: Difficulty) {
        android.util.Log.d("DifficultyDialog", "onConfirmClicked called with difficulty: $difficulty")
        onDifficultySelected?.invoke(difficulty)
        dismiss()
    }

    override fun onCancelClicked() {
        android.util.Log.d("DifficultyDialog", "onCancelClicked called")
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
        _binding = null
    }

    // BaseView implementation - not needed for dialog but required by interface
    override fun showLoading() {
        // Dialog doesn't need loading state
    }

    override fun hideLoading() {
        // Dialog doesn't need loading state
    }

    override fun showError(message: String) {
        // Could show error in dialog if needed
    }

    override fun showMessage(message: String) {
        // Could show message in dialog if needed
    }
}
