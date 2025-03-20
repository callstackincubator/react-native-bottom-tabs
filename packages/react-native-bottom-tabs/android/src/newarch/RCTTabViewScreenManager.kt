package com.rcttabview

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ReactStylesDiffMap
import com.facebook.react.uimanager.StateWrapper
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.RNCTabViewScreenManagerDelegate
import com.facebook.react.viewmanagers.RNCTabViewScreenManagerInterface
import com.facebook.react.views.view.ReactViewGroup

class RCTTabViewScreen(context: Context): ReactViewGroup(context) {
  private var stateWrapper: StateWrapper? = null

  public fun setStateWrapper(stateWrapper: StateWrapper?) {
    this.stateWrapper = stateWrapper
  }

  init {
    getSystemInsets(this) { left, top, right, bottom ->
      Log.w("TAB_VIEW", "${left} ${top} ${right} ${bottom}")

      stateWrapper?.updateState(Arguments.createMap().apply {
        putDouble("width", Utils.convertPixelsToDp(context, width))
        putDouble("height", Utils.convertPixelsToDp(context, height - bottom))
      })
    }
  }

  fun getSystemInsets(view: View, callback: (left: Int, top: Int, right: Int, bottom: Int) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      callback(
        systemBars.left,   // Left inset (for left-aligned Navigation Rail)
        systemBars.top,    // Top inset
        systemBars.right,  // Right inset (for right-aligned Navigation Rail)
        systemBars.bottom  // Bottom inset (for bottom navigation)
      )
      insets
    }
    // Ensure we request insets
    if (view.isAttachedToWindow) {
      ViewCompat.requestApplyInsets(view)
    } else {
      view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
          ViewCompat.requestApplyInsets(v)
          v.removeOnAttachStateChangeListener(this)
        }
        override fun onViewDetachedFromWindow(v: View) = Unit
      })
    }
  }
}


@ReactModule(name = "RNCTabViewScreen")
class RCTTabViewScreenManager(context: ReactApplicationContext) :
  ViewGroupManager<RCTTabViewScreen>(),
  RNCTabViewScreenManagerInterface<RCTTabViewScreen> {

  private val delegate: RNCTabViewScreenManagerDelegate<RCTTabViewScreen, RCTTabViewScreenManager> =
    RNCTabViewScreenManagerDelegate(this)


  override fun createViewInstance(context: ThemedReactContext): RCTTabViewScreen {
    return RCTTabViewScreen(context);
  }

  override fun updateState(
    view: RCTTabViewScreen,
    props: ReactStylesDiffMap?,
    stateWrapper: StateWrapper?
  ): Any? {
    view.setStateWrapper(stateWrapper)
    return super.updateState(view, props, stateWrapper)
  }

  override fun getDelegate(): ViewManagerDelegate<RCTTabViewScreen> {
    return delegate
  }

  override fun getName(): String {
    return "RNCTabViewScreen"
  }
}
