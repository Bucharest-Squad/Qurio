package com.bucharest.qurio.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.R
import com.bucharest.qurio.presentation.game.component.OutlineTextView
import com.bucharest.qurio.presentation.onboarding.OnboardingPage

class OnboardingAdapter(private val pages: List<OnboardingPage>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun getItemCount(): Int = pages.size

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.onboarding_image)
        private val title: TextView = view.findViewById(R.id.onboarding_title)
        private val description: TextView = view.findViewById(R.id.onboarding_description)
        private val outlineTitle: OutlineTextView = view.findViewById(R.id.outline_title)

        fun bind(page: OnboardingPage) {
            image.setImageResource(page.imageRes)
            title.setText(page.title)
            description.setText(page.description)
            outlineTitle.setText(page.title)
        }
    }
}
