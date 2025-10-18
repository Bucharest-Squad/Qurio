package com.bucharest.qurio.presentation.buy_life

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
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
        val density = context?.resources?.displayMetrics?.density ?: 1f
        dialog.window?.setLayout((328 * density).toInt(), (314 * density).toInt())

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
        // Initialize button as disabled until we load user data
        setBuyButtonEnabled(false)
        presenter.loadUserCoins()
    }

    private fun setupListeners() {
        binding.btnBuy.setOnClickListener {
            presenter.onBuyClicked()
        }

        binding.btnCancel.setOnClickListener {
            presenter.onCancelClicked()
        }

        binding.closeShape.setOnClickListener {
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

    override fun setBuyButtonEnabled(enabled: Boolean) {
        binding.btnBuy.isEnabled = enabled
        binding.btnBuy.alpha = if (enabled) 1.0f else 0.5f
        // Keep the same "Buy" text regardless of enabled state
        binding.btnBuy.text = getString(R.string.buy)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
        _binding = null
    }

    override fun showLoading() {}
    override fun hideLoading() {}
    override fun showError(message: String) {
        // Show error message to user - keep button text as "Buy" but disable it
        binding.btnBuy.isEnabled = false
        binding.btnBuy.alpha = 0.5f
        binding.btnBuy.text = getString(R.string.buy)
    }
    override fun showMessage(message: String) {}
}
