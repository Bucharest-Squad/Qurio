package com.bucharest.qurio.presentation.games

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.FragmentGamesBinding
import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.presentation.constants.PresentationConstants
import com.bucharest.qurio.presentation.difficulty.DifficultyLevelFragment
import com.bucharest.qurio.presentation.home.adapter.GamesGridAdapter
import com.bucharest.qurio.presentation.home.adapter.GridSpacingItemDecoration
import com.bucharest.qurio.presentation.home.state.CategoryUiModel
import javax.inject.Inject

class GamesFragment : BaseFragment<FragmentGamesBinding, GamesView, GamesPresenter>(), GamesView {

    @Inject
    override lateinit var presenter: GamesPresenter

    @Inject
    lateinit var audioManager: AudioManager

    private val gamesAdapter by lazy {
        GamesGridAdapter(
            onCategoryClicked = { categoryId ->
                presenter.onCategoryClicked(categoryId)
            },
            audioManager = audioManager
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGamesBinding = FragmentGamesBinding.inflate(inflater, container, false)

    override fun initViews() {
        updateToolbar(title = "Games", showToolbar = false)
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
                if (child is android.widget.ImageView && child != binding.topBar.button) {
                    child.visibility = View.GONE
                }
            }
        }
        
        // Set the title text
        binding.topBar.titleText.text = "Games"
    }

    private fun setupRecyclerView() {
        with(binding.gamesRecyclerView) {
            adapter = gamesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                // Add spacing between items
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int = 1
                }
            }
            setHasFixedSize(false)
            clipToPadding = false
            clipChildren = false
            // Add spacing between items
            addItemDecoration(GridSpacingItemDecoration(2, 8, 16, true))
        }
    }

    override fun showGames(categories: List<CategoryUiModel>) {
        gamesAdapter.submitList(categories)
        
        if (categories.isEmpty()) {
            binding.gamesRecyclerView.visibility = View.GONE
            binding.emptyStateLayout.visibility = View.VISIBLE
        } else {
            binding.gamesRecyclerView.visibility = View.VISIBLE
            binding.emptyStateLayout.visibility = View.GONE
        }
    }

    override fun navigateToCategoryGame(categoryId: Int) {
        showDifficultyDialog(categoryId)
    }

    private fun showDifficultyDialog(categoryId: Int) {
        val dialog = DifficultyLevelFragment.newInstance(
            categoryId = categoryId,
            totalQuestions = PresentationConstants.DEFAULT_TOTAL_QUESTIONS
        ) { difficulty ->
            navigateToGameWithDifficulty(categoryId, difficulty)
        }
        dialog.show(childFragmentManager, "DifficultyLevelDialog")
    }

    private fun navigateToGameWithDifficulty(categoryId: Int, difficulty: Difficulty) {
        val action = GamesFragmentDirections.actionGamesFragmentToGameFragment(
            categoryId = categoryId,
            difficulty = difficulty.name,
            totalQuestions = PresentationConstants.DEFAULT_TOTAL_QUESTIONS
        )
        findNavController().navigate(action)
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
        // Handle messages if needed
    }
}
