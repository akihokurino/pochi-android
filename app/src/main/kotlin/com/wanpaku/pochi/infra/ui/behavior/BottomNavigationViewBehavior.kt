package com.wanpaku.pochi.infra.ui.behavior

import android.animation.Animator
import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

class BottomNavigationViewBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

    private var isAnimate = false

    private val animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {
            isAnimate = true
        }

        override fun onAnimationEnd(p0: Animator?) {
            isAnimate = false
        }

        override fun onAnimationCancel(p0: Animator?) {
            isAnimate = false
        }

        override fun onAnimationStart(p0: Animator?) {
            isAnimate = true
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        if (dyConsumed == 0) return
        if (isAnimate) return
        animate(child, dyConsumed < 0)
    }

    private fun animate(bottomNavigationView: BottomNavigationView, isReverse: Boolean) {
        val translationY = if (isReverse) 0F else bottomNavigationView.height.toFloat()
        bottomNavigationView.animate()
                .setDuration(200L)
                .setListener(animatorListener)
                .translationY(translationY)
                .start()
    }

}