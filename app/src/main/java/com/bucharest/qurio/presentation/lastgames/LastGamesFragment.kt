package com.bucharest.qurio.presentation.lastgames

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.FragmentLastGamesBinding
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.presentation.home.adapter.LastGamesAdapter
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import javax.inject.Inject

class LastGamesFragment : BaseFragment<FragmentLastGamesBinding, LastGamesView, LastGamesPresenter>(),
    LastGamesView {

    @Inject
    override lateinit var presenter: LastGamesPresenter

    @Inject
    lateinit var audioManager: AudioManager

    private val gamesAdapter by lazy {
        LastGamesAdapter { game ->
            presenter.onGameClicked(game)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLastGamesBinding = FragmentLastGamesBinding.inflate(inflater, container, false)

    override fun initViews() {
        updateToolbar(title = "Last Games", showToolbar = false)
        setupTopBar()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadGames()
    }

    private fun setupTopBar() {
        binding.topBar.button.setOnClickListener {
            audioManager.playButtonPress()
            findNavController().navigateUp()
        }
        binding.topBar.numberOfLife.visibility = View.GONE
        val constraintLayout = binding.topBar.root as? ViewGroup
        constraintLayout?.let { layout ->
            for (i in 0 until layout.childCount) {
                val child = layout.getChildAt(i)
                if (child is ImageView && child != binding.topBar.button) {
                    child.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding.gamesRecyclerView) {
            adapter = gamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
    }

    override fun showGames(games: List<GameSessionUiModel>) {
        gamesAdapter.submitList(games)
        
        if (games.isEmpty()) {
            binding.gamesRecyclerView.visibility = View.GONE
            binding.emptyStateLayout.visibility = View.VISIBLE
        } else {
            binding.gamesRecyclerView.visibility = View.VISIBLE
            binding.emptyStateLayout.visibility = View.GONE
        }
    }

    override fun showLoading() {
        binding.gamesRecyclerView.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.noConnectionLayout.root.visibility = View.GONE
        binding.emptyStateLayout.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.gamesRecyclerView.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
        binding.noConnectionLayout.root.visibility = View.GONE
    }

    override fun showError(message: String) {
        binding.gamesRecyclerView.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.GONE
        binding.noConnectionLayout.root.visibility = View.VISIBLE
        binding.emptyStateLayout.visibility = View.GONE
        
        binding.noConnectionLayout.retryButton.setOnClickListener {
            presenter.loadGames()
        }
    }

    override fun showMessage(message: String) {
    }
}
