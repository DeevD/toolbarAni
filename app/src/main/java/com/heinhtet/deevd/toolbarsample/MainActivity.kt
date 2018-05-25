package com.heinhtet.deevd.toolbarsample

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val ARGB_EVALUATOR = ArgbEvaluator()
    val INTERPOLATOR_FAST_OUT_SLOW_IN: Interpolator = FastOutSlowInInterpolator()
    private val TOOLBAR_STATE_NORMAL = 0
    private val TOOLBAR_STATE_TRANSPARENT = 1
    private var toolbarState = TOOLBAR_STATE_NORMAL
    var toolbarColor: Int? = null
    lateinit var iv: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv = findViewById(R.id.song_cover)
        toolbarColor = ContextCompat.getColor(this, android.R.color.black)

        app_bar_layout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset < -590) {
                showToolbar(true)
            } else if (verticalOffset == 0) {
                Log.i(TAG, "expaned")
            } else {
                ivAnimate(verticalOffset)
                showToolbar(false)
            }
            // Log.i(TAG, "offset $verticalOffset")
        }
    }


    private fun ivAnimate(y: Int) {
        var padding = (0 - (y / 50) * 7)
        Log.i(TAG, "padding $padding")
        iv.setPadding(padding, padding, padding, 0)
    }

    private fun showToolbar(isShow: Boolean) {
        if (isShow) {
            setToolbarColor(TOOLBAR_STATE_NORMAL)
            setStatusBarColorFade(this, ContextCompat.getColor(this.applicationContext, android.R.color.black), 300)
        } else {
            setToolbarColor(TOOLBAR_STATE_TRANSPARENT)
            // toolbar.animate().translationY(-toolbar.height.toFloat()).setInterpolator(BounceInterpolator()).start()
        }
    }

    @SuppressLint("NewApi")
    fun setStatusBarColorImmediately(activity: Activity?, color: Int) {
        if (!isLollipop21More() || activity == null) {
            return
        }
        val window = activity.window
        if (window != null) {
            if (window.statusBarColor != color) {
                window.statusBarColor = color
            }
        }
    }

    fun setToolbarColor(state: Int) {

        if (state == TOOLBAR_STATE_NORMAL) {
            toolbar.setBackgroundColor(toolbarColor!!)
        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        }
        if (state == TOOLBAR_STATE_NORMAL) {
        } else if (state == TOOLBAR_STATE_TRANSPARENT) {
            //toolbarTitle.setVisibility(View.INVISIBLE)
        }

    }

    @SuppressLint("NewApi")
    fun setStatusBarColorFade(activity: Activity?, toColor: Int, msec: Int) {
        if (activity == null || !isLollipop21More()) {
            return
        }
        val window = activity.window ?: return
        val statusBarColor = window.statusBarColor
        if (statusBarColor != toColor) {
            val statusBarColorAnim = ValueAnimator.ofObject(ARGB_EVALUATOR, statusBarColor, toColor)
            statusBarColorAnim.addUpdateListener { animation ->
                window.statusBarColor = animation
                        .animatedValue as Int
            }
            statusBarColorAnim.duration = msec.toLong()
            statusBarColorAnim.interpolator = INTERPOLATOR_FAST_OUT_SLOW_IN
            statusBarColorAnim.start()
        }
    }

    fun isLollipop21More(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }


}
