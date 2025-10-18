package com.bucharest.qurio.presentation.difficulty

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.DifficultyLevelDialogBinding
import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BaseFragment
import javax.inject.Inject

class DifficultyLevelFragment : DialogFragment(), DifficultyLevelView {

    @Inject
    lateinit var presenter: DifficultyLevelPresenter

    @Inject
    lateinit var audioManager: AudioManager

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
        binding.confirmButton.isEnabled = false
    }

    private fun setupListeners() {
        binding.confirmButton.setOnClickListener {
            if (::audioManager.isInitialized) {
                audioManager.playButtonPress()
            }
            presenter.onConfirmClicked()
        }

        binding.cancelButton.setOnClickListener {
            if (::audioManager.isInitialized) {
                audioManager.playButtonPress()
            }
            presenter.onCancelClicked()
        }

        binding.closeButton.setOnClickListener {
            if (::audioManager.isInitialized) {
                audioManager.playButtonPress()
            }
            presenter.onCancelClicked()
        }

        binding.easyButton.setOnClickListener {
            if (::audioManager.isInitialized) {
                audioManager.playButtonPress()
            }
            binding.easyButton.isSelected = false
            presenter.onEasyClicked()
        }

        binding.mediumButton.setOnClickListener {
            if (::audioManager.isInitialized) {
                audioManager.playButtonPress()
            }
            binding.mediumButton.isSelected = false
            presenter.onMediumClicked()
        }

        binding.hardButton.setOnClickListener {
            if (::audioManager.isInitialized) {
                audioManager.playButtonPress()
            }
            binding.hardButton.isSelected = false
            presenter.onHardClicked()
        }
    }

    override fun onEasySelected() {
        binding.easyButton.isSelected = true
        binding.mediumButton.isSelected = false
        binding.hardButton.isSelected = false
    }

    override fun onMediumSelected() {
        binding.mediumButton.isSelected = true
        binding.easyButton.isSelected = false
        binding.hardButton.isSelected = false
    }

    override fun onHardSelected() {
        binding.hardButton.isSelected = true
        binding.easyButton.isSelected = false
        binding.mediumButton.isSelected = false
    }

    override fun setConfirmButtonEnabled(enabled: Boolean) {
        binding.confirmButton.isEnabled = enabled
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
