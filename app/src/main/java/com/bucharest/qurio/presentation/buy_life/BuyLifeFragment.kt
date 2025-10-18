package com.bucharest.qurio.presentation.buy_life

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.databinding.DifficultyLevelDialogBinding
import com.bucharest.qurio.databinding.FragmentBuyLifeDialogBinding
import com.bucharest.qurio.domain.entity.Difficulty
import javax.inject.Inject

class BuyLifeFragment : DialogFragment(), BuyLifeView {

    @Inject
    lateinit var presenter: BuyLifePresenter
    private var _binding: FragmentBuyLifeDialogBinding? = null
    private val binding get() = _binding!!

    private var onBuyClicked: (() -> Unit)? = null

    companion object {

        fun newInstance(
            onBuyClicked: () -> Unit
        ): BuyLifeFragment {
            val fragment = BuyLifeFragment()
            fragment.onBuyClicked = onBuyClicked
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
        _binding = FragmentBuyLifeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buyButton.setOnClickListener {
            presenter.onBuyClicked()
        }

        binding.cancelButton.setOnClickListener {
            presenter.onCancelClicked()
        }

        binding.closeButton.setOnClickListener {
            presenter.onCancelClicked()
        }

    }

    override fun onBuyClicked() {
        onBuyClicked?.invoke()
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
