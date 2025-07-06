package com.rcttabview

import android.view.View
import android.view.ViewGroup
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.RNCTabViewManagerDelegate
import com.facebook.react.viewmanagers.RNCTabViewManagerInterface
import com.rcttabview.events.OnNativeLayoutEvent
import com.rcttabview.events.OnTabBarMeasuredEvent
import com.rcttabview.events.PageSelectedEvent
import com.rcttabview.events.TabLongPressEvent


@ReactModule(name = RCTTabViewImpl.NAME)
class RCTTabViewManager(context: ReactApplicationContext) :
  ViewGroupManager<ReactBottomNavigationView>(),
  RNCTabViewManagerInterface<ReactBottomNavigationView> {

  private val delegate: RNCTabViewManagerDelegate<ReactBottomNavigationView, RCTTabViewManager> =
    RNCTabViewManagerDelegate(this)
  private val tabViewImpl: RCTTabViewImpl = RCTTabViewImpl()

  override fun createViewInstance(context: ThemedReactContext): ReactBottomNavigationView {
    val view = ReactBottomNavigationView(context)
    val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(context, view.id)
    view.onTabSelectedListener = { key ->
      eventDispatcher?.dispatchEvent(PageSelectedEvent(viewTag = view.id, key))
    }

    view.onTabLongPressedListener = { key ->
      eventDispatcher?.dispatchEvent(TabLongPressEvent(viewTag = view.id, key))
    }

    view.onNativeLayoutListener = { width, height ->
      eventDispatcher?.dispatchEvent(OnNativeLayoutEvent(viewTag = view.id, width, height))
    }
    view.onTabBarMeasuredListener = { height ->
      eventDispatcher?.dispatchEvent(OnTabBarMeasuredEvent(viewTag = view.id, height))
    }
    return view

  }

  override fun onDropViewInstance(view: ReactBottomNavigationView) {
    super.onDropViewInstance(view)
    view.onDropViewInstance()
  }

  override fun getName(): String {
    return tabViewImpl.getName()
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

  override fun setItems(view: ReactBottomNavigationView?, value: ReadableArray?) {
    if (view != null && value != null)
      tabViewImpl.setItems(view, value)
  }

  override fun setSelectedPage(view: ReactBottomNavigationView?, value: String?) {
    if (view != null && value != null)
      tabViewImpl.setSelectedPage(view, value)
  }

  override fun setIcons(view: ReactBottomNavigationView?, value: ReadableArray?) {
    if (view != null)
      tabViewImpl.setIcons(view, value)
  }

  override fun setLabeled(view: ReactBottomNavigationView?, value: Boolean) {
    if (view != null)
      tabViewImpl.setLabeled(view, value)
  }

  override fun setRippleColor(view: ReactBottomNavigationView?, value: Int?) {
    if (view != null && value != null)
      tabViewImpl.setRippleColor(view, value)
  }

  override fun setBarTintColor(view: ReactBottomNavigationView?, value: Int?) {
    if (view != null && value != null)
      tabViewImpl.setBarTintColor(view, value)
  }

  override fun setActiveTintColor(view: ReactBottomNavigationView?, value: Int?) {
    if (view != null && value != null)
      tabViewImpl.setActiveTintColor(view, value)
  }

  override fun setInactiveTintColor(view: ReactBottomNavigationView?, value: Int?) {
    if (view != null && value != null)
      tabViewImpl.setInactiveTintColor(view, value)
  }

  override fun setActiveIndicatorColor(view: ReactBottomNavigationView?, value: Int?) {
    if (view != null && value != null)
      tabViewImpl.setActiveIndicatorColor(view, value)
  }

  override fun getDelegate(): ViewManagerDelegate<ReactBottomNavigationView> {
    return delegate
  }

  override fun setHapticFeedbackEnabled(view: ReactBottomNavigationView?, value: Boolean) {
    if (view != null)
      tabViewImpl.setHapticFeedbackEnabled(view, value)
  }

  override fun setFontFamily(view: ReactBottomNavigationView?, value: String?) {
    view?.setFontFamily(value)
  }

  override fun setFontWeight(view: ReactBottomNavigationView?, value: String?) {
    view?.setFontWeight(value)
  }

  override fun setFontSize(view: ReactBottomNavigationView?, value: Int) {
    view?.setFontSize(value)
  }

  override fun setDisablePageAnimations(view: ReactBottomNavigationView?, value: Boolean) {
    view?.disablePageAnimations = value
  }

  override fun setTabBarHidden(view: ReactBottomNavigationView?, value: Boolean) {
    view?.setTabBarHidden(value)
  }

  // iOS Methods
  override fun setTranslucent(view: ReactBottomNavigationView?, value: Boolean) {
  }

  override fun setSidebarAdaptable(view: ReactBottomNavigationView?, value: Boolean) {
  }

  override fun setScrollEdgeAppearance(view: ReactBottomNavigationView?, value: String?) {
  }

  override fun setMinimizeBehavior(view: ReactBottomNavigationView?, value: String?) {
  }
}
