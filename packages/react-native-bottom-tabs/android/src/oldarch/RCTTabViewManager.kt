package com.rcttabview

import android.view.View
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.ViewGroupManager
import com.rcttabview.events.OnNativeLayoutEvent
import com.rcttabview.events.OnTabBarMeasuredEvent
import com.rcttabview.events.PageSelectedEvent
import com.rcttabview.events.TabLongPressEvent

@ReactModule(name = RCTTabViewImpl.NAME)
class RCTTabViewManager(context: ReactApplicationContext) : ViewGroupManager<ReactBottomNavigationView>() {
  private lateinit var eventDispatcher: EventDispatcher
  private var tabViewImpl = RCTTabViewImpl()

  override fun getName(): String {
    return tabViewImpl.getName()
  }

  public override fun createViewInstance(context: ThemedReactContext): ReactBottomNavigationView {
    eventDispatcher = context.getNativeModule(UIManagerModule::class.java)!!.eventDispatcher
    val view = ReactBottomNavigationView(context)
    view.onTabSelectedListener = { key ->
      eventDispatcher.dispatchEvent(PageSelectedEvent(viewTag = view.id, key))
    }

    view.onTabLongPressedListener = { key ->
      eventDispatcher.dispatchEvent(TabLongPressEvent(viewTag = view.id, key))
    }

    view.onNativeLayoutListener = { width, height ->
      eventDispatcher.dispatchEvent(OnNativeLayoutEvent(viewTag = view.id, width, height))
    }

    view.onTabBarMeasuredListener = { height ->
      eventDispatcher.dispatchEvent(OnTabBarMeasuredEvent(viewTag = view.id, height))
    }
    return view
  }

  override fun onDropViewInstance(view: ReactBottomNavigationView) {
    super.onDropViewInstance(view)
    view.onDropViewInstance()
  }

  override fun getChildCount(parent: ReactBottomNavigationView): Int {
    return tabViewImpl.getChildCount(parent)
  }

  override fun getChildAt(parent: ReactBottomNavigationView, index: Int): View? {
    return tabViewImpl.getChildAt(parent, index)
  }

  override fun removeView(parent: ReactBottomNavigationView, view: View) {
    tabViewImpl.removeView(parent, view)
  }

  override fun removeAllViews(parent: ReactBottomNavigationView) {
    tabViewImpl.removeAllViews(parent)
  }

  override fun removeViewAt(parent: ReactBottomNavigationView, index: Int) {
    tabViewImpl.removeViewAt(parent, index)
  }

  override fun needsCustomLayoutForChildren(): Boolean {
    return tabViewImpl.needsCustomLayoutForChildren()
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? {
    return tabViewImpl.getExportedCustomDirectEventTypeConstants()
  }

  @ReactProp(name = "items")
  fun setItems(view: ReactBottomNavigationView, items: ReadableArray) {
    tabViewImpl.setItems(view, items)
  }

  @ReactProp(name = "selectedPage")
  fun setSelectedPage(view: ReactBottomNavigationView, key: String) {
    tabViewImpl.setSelectedPage(view, key)
  }

  @ReactProp(name = "labeled")
  fun setLabeled(view: ReactBottomNavigationView, flag: Boolean?) {
    tabViewImpl.setLabeled(view, flag)
  }

  @ReactProp(name = "icons")
  fun setIcons(view: ReactBottomNavigationView, icons: ReadableArray?) {
    tabViewImpl.setIcons(view, icons)
  }

  @ReactProp(name = "barTintColor", customType = "Color")
  fun setBarTintColor(view: ReactBottomNavigationView, color: Int?) {
    tabViewImpl.setBarTintColor(view, color)
  }

  @ReactProp(name = "rippleColor", customType = "Color")
  fun setRippleColor(view: ReactBottomNavigationView, rippleColor: Int?) {
    tabViewImpl.setRippleColor(view, rippleColor)
  }

  @ReactProp(name = "activeTintColor", customType = "Color")
  fun setActiveTintColor(view: ReactBottomNavigationView, color: Int?) {
    tabViewImpl.setActiveTintColor(view, color)
  }

  @ReactProp(name = "inactiveTintColor", customType = "Color")
  fun setInactiveTintColor(view: ReactBottomNavigationView, color: Int?) {
    tabViewImpl.setInactiveTintColor(view, color)
  }

  @ReactProp(name = "activeIndicatorColor", customType = "Color")
  fun setActiveIndicatorColor(view: ReactBottomNavigationView, color: Int?) {
    tabViewImpl.setActiveIndicatorColor(view, color)
  }

  @ReactProp(name = "disablePageAnimations")
  fun setDisablePageAnimations(view: ReactBottomNavigationView, flag: Boolean) {
    view.disablePageAnimations = flag
  }

  @ReactProp(name = "tabBarHidden")
  fun setTabBarHidden(view: ReactBottomNavigationView, flag: Boolean) {
    view.setTabBarHidden(flag)
  }

  // iOS Props
  @ReactProp(name = "sidebarAdaptable")
  fun setSidebarAdaptable(view: ReactBottomNavigationView, flag: Boolean) {
  }

  @ReactProp(name = "hapticFeedbackEnabled")
  fun setHapticFeedbackEnabled(view: ReactBottomNavigationView, value: Boolean) {
      tabViewImpl.setHapticFeedbackEnabled(view, value)
  }

  @ReactProp(name = "fontFamily")
  fun setFontFamily(view: ReactBottomNavigationView?, value: String?) {
    view?.setFontFamily(value)
  }

  @ReactProp(name = "fontWeight")
  fun setFontWeight(view: ReactBottomNavigationView?, value: String?) {
    view?.setFontWeight(value)
  }

  @ReactProp(name = "fontSize")
  fun setFontSize(view: ReactBottomNavigationView?, value: Int) {
    view?.setFontSize(value)
  }
}
