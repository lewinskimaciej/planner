package com.atc.planner.extensions

import android.animation.Animator
import android.graphics.Rect
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver

fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: (Snackbar.() -> Unit)? = null) {
    val snack = Snackbar.make(this, message, length)
    if (f != null) {
        snack.f()
    }
    snack.show()
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

inline fun <T : View> T.keyboardListener(crossinline f: T.(Boolean) -> Unit): ViewTreeObserver.OnGlobalLayoutListener {
    val listener = ViewTreeObserver.OnGlobalLayoutListener {
        if (measuredWidth > 0 && measuredHeight > 0) {
            val rect = Rect()
            this.getWindowVisibleDisplayFrame(rect)
            val screenHeight = this.rootView.height
            // rect.bottom is the position above soft keypad or device button.
            // if keypad is shown, the rect.bottom is smaller than that before.
            val keypadHeight = screenHeight - rect.bottom
            // 0.15 ratio is perhaps enough to determine keypad height.
            f(keypadHeight > screenHeight * 0.15)

        }
    }
    viewTreeObserver.addOnGlobalLayoutListener(listener)
    return listener
}

fun View.gone() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    }
}

fun View.invisible() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.INVISIBLE
    }
}

fun View.visible() {
    if (this.visibility == View.INVISIBLE || this.visibility == View.GONE) {
        this.visibility = View.VISIBLE
    }
}

fun View.fadeIn(time: Int) {
    this.animate().setDuration(time.toLong()).alpha(1.0f).start()
}

fun View.fadeOut(time: Int) {
    this.animate().setDuration(time.toLong()).alpha(0f).start()
}

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun View.isGone(): Boolean = this.visibility == View.GONE

fun View.isInvisible(): Boolean = this.visibility == View.INVISIBLE

fun AppCompatEditText.addTextChangedListener(
        beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
        onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null,
        afterTextChanged: ((s: Editable?) -> Unit)? = null
) {

    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged?.invoke(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged?.invoke(s)
        }
    })
}

fun ViewPropertyAnimator.addListener(
        onAnimationRepeat: ((Animator) -> Unit)? = null,
        onAnimationCancel: ((Animator) -> Unit)? = null,
        onAnimationStart: ((Animator) -> Unit)? = null,
        onAnimationEnd: ((Animator) -> Unit)? = null
): ViewPropertyAnimator {
    this.setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animator: Animator) {
            onAnimationRepeat?.invoke(animator)
        }

        override fun onAnimationCancel(animator: Animator) {
            onAnimationCancel?.invoke(animator)
        }

        override fun onAnimationStart(animator: Animator) {
            onAnimationStart?.invoke(animator)
        }

        override fun onAnimationEnd(animator: Animator) {
            onAnimationEnd?.invoke(animator)
        }
    })
    return this
}
