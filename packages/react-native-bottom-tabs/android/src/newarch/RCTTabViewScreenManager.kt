package com.rcttabview

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
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

class RCTTabViewScreen(context: Context): FrameLayout(context) {
  private var stateWrapper: StateWrapper? = null

  public fun setStateWrapper(stateWrapper: StateWrapper?) {
    this.stateWrapper = stateWrapper
  }

  public fun updateFrame(width: Double, height: Double) {
    Log.w("TABVIEW_SCREEN", "UPDATE FRAME, ${width} ${height}")
    stateWrapper?.updateState(Arguments.createMap().apply {
      putInt("width", width.toInt())
      putInt("height", height.toInt())
    })
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

  override fun addView(parent: RCTTabViewScreen, child: View, index: Int) {
    parent.addView(child, index)
  }

  override fun removeViewAt(parent: RCTTabViewScreen, index: Int) {
    parent.removeViewAt(index)
  }

  override fun getChildCount(parent: RCTTabViewScreen): Int {
    return parent.childCount
  }

  override fun getChildAt(parent: RCTTabViewScreen, index: Int): View {
    return parent.getChildAt(index)
  }
}
