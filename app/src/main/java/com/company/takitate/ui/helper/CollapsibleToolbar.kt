package com.company.takitate.ui.helper

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout

// 参考: https://medium.com/google-developers/introduction-to-motionlayout-part-iii-47cd64d51a5
class CollapsibleToolbar @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), AppBarLayout.OnOffsetChangedListener {

  // animation progress を scroll 量で調整している
  override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
    progress = -verticalOffset / appBarLayout?.totalScrollRange?.toFloat()!!
    println(progress)
  }

  // 親のappbarの子、即ち、同階層に配置されたScrollViewのscroll量を検知するlistenerをsetしている
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    (parent as? AppBarLayout)?.addOnOffsetChangedListener(this)
  }
}
