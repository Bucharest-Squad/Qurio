package com.bucharest.qurio.presentation.game.component

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.bucharest.qurio.R
import kotlin.random.Random

class ScoreIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    fun showScore(isCorrect: Boolean) {
        val count = Random.nextInt(6, 10)
        repeat(count) {
            postDelayed({
                addFloatingScore(isCorrect)
            }, Random.nextLong(100, 200))
        }
    }

    private fun addFloatingScore(isCorrect: Boolean) {
        val itemView = createScoreItem(isCorrect) ?: return
        positionItemRandomly(itemView)
        addView(itemView)
        animateItem(itemView)
    }

    private fun createScoreItem(isCorrect: Boolean): View? {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.component_score_indicator, this, false)
        val rootLayout = itemView.findViewById<LinearLayout>(R.id.scoreIndicatorRoot)
        val text = itemView.findViewById<TextView>(R.id.scoreText)

        text.text = if (isCorrect) "+100" else "0"
        rootLayout.setBackgroundResource(R.drawable.circle_shape)
        rootLayout.background?.setTint(
            if (isCorrect) context.getColor(R.color.green)
            else context.getColor(R.color.red)
        )

        return itemView
    }

    private fun positionItemRandomly(itemView: View) {
        val maxX = width - itemView.measuredWidth
        val maxY = height - itemView.measuredHeight
        if (maxX <= 0 || maxY <= 0) return
        val startX = Random.nextInt(0, maxX)
        val startY = Random.nextInt(0, maxY)
        itemView.translationX = startX.toFloat()
        itemView.translationY = startY.toFloat()
        itemView.alpha = 0f
    }

    private fun animateItem(itemView: View) {
        val startY = itemView.translationY
        val endY = startY + Random.nextInt(-80, -30)
        val fadeIn = ObjectAnimator.ofFloat(itemView, View.ALPHA, 0f, 1f)
        val fadeOut = ObjectAnimator.ofFloat(itemView, View.ALPHA, 1f, 0f)
        val move = ObjectAnimator.ofFloat(itemView, View.TRANSLATION_Y, startY, endY)
        val scaleX = ObjectAnimator.ofFloat(itemView, View.SCALE_X, 1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(itemView, View.SCALE_Y, 1f, 1f)

        val animSet = AnimatorSet().apply {
            playTogether(fadeIn, move, scaleX, scaleY)
            duration = Random.nextLong(1000, 1500)
            start()
        }

        val fadeOutSet = AnimatorSet().apply {
            play(fadeOut)
            startDelay = animSet.duration - 300
            duration = 400
            start()
        }

        postDelayed({ removeView(itemView) }, animSet.duration + fadeOutSet.duration)
    }
}

