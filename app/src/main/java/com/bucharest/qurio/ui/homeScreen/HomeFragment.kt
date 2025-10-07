package com.bucharest.qurio.ui.homeScreen

import StreakDayAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.R

class HomeFragment : Fragment() {

    private lateinit var streakAdapter: StreakDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showStreakCard(view)
    }

    private fun showStreakCard(rootView: View) {
        val streakRecyclerView = rootView.findViewById<RecyclerView>(R.id.streakRecyclerView)
        streakAdapter = StreakDayAdapter(getMockStreakData())

        streakRecyclerView?.apply {
            adapter = streakAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun getMockStreakData(): List<HomeUiState.StreakDayUiState> {
        return listOf(
            HomeUiState.StreakDayUiState("Mon", true),
            HomeUiState.StreakDayUiState("Tue", true),
            HomeUiState.StreakDayUiState("Wed", true),
            HomeUiState.StreakDayUiState("Thu", false),
            HomeUiState.StreakDayUiState("Fri", false),
            HomeUiState.StreakDayUiState("Sat", false),
            HomeUiState.StreakDayUiState("Sun", false)
        )
    }
}
