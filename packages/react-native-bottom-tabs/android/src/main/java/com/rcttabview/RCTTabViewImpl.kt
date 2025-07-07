package com.rcttabview

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.MapBuilder
import com.rcttabview.events.OnNativeLayoutEvent
import com.rcttabview.events.OnTabBarMeasuredEvent
import com.rcttabview.events.PageSelectedEvent
import com.rcttabview.events.TabLongPressEvent

data class TabInfo(
  val key: String,
  val title: String,
  val badge: String?,
  val activeTintColor: Int?,
  val hidden: Boolean,
  val testID: String?,
  val role: String?
)

class RCTTabViewImpl {
  fun getName(): String {
    return NAME
  }

  fun setItems(view: ReactBottomNavigationView, items: ReadableArray) {
    val itemsArray = mutableListOf<TabInfo>()
    for (i in 0 until items.size()) {
      items.getMap(i)?.let { item ->
          itemsArray.add(
            TabInfo(
              key = item.getString("key") ?: "",
              title = item.getString("title") ?: "",
              badge = if (item.hasKey("badge")) item.getString("badge") else null,
              activeTintColor = if (item.hasKey("activeTintColor")) item.getInt("activeTintColor") else null,
              hidden = if (item.hasKey("hidden")) item.getBoolean("hidden") else false,
              testID = item.getString("testID")
            )
          )
      }
    }
    view.updateItems(itemsArray)
  }

  fun setSelectedPage(view: ReactBottomNavigationView, key: String) {
    view.setSelectedItem(key)
  }

  fun setLabeled(view: ReactBottomNavigationView, flag: Boolean?) {
    view.setLabeled(flag)
  }

  fun setIcons(view: ReactBottomNavigationView, icons: ReadableArray?) {
    view.setIcons(icons)
  }

  fun setBarTintColor(view: ReactBottomNavigationView, color: Int?) {
    view.setBarTintColor(color)
  }

  fun setRippleColor(view: ReactBottomNavigationView, rippleColor: Int?) {
    if (rippleColor != null) {
      val color = ColorStateList.valueOf(rippleColor)
      view.setRippleColor(color)
    }
  }

  fun setActiveIndicatorColor(view: ReactBottomNavigationView, color: Int?) {
    if (color != null) {
      val color = ColorStateList.valueOf(color)
      view.setActiveIndicatorColor(color)
    }
  }

  fun setActiveTintColor(view: ReactBottomNavigationView, color: Int?) {
    view.setActiveTintColor(color)
  }

  fun setInactiveTintColor(view: ReactBottomNavigationView, color: Int?) {
    view.setInactiveTintColor(color)
  }

  fun setHapticFeedbackEnabled(view: ReactBottomNavigationView, enabled: Boolean) {
   view.isHapticFeedbackEnabled = enabled
  }

  fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? {
    return MapBuilder.of(
      PageSelectedEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onPageSelected"),
      TabLongPressEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onTabLongPress"),
      OnNativeLayoutEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onNativeLayout"),
      OnTabBarMeasuredEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onTabBarMeasured")
    )
  }

  fun getChildCount(parent: ReactBottomNavigationView): Int {
    return parent.layoutHolder.childCount ?: 0
  }

  fun getChildAt(parent: ReactBottomNavigationView, index: Int): View? {
    return parent.layoutHolder.getChildAt(index)
  }

  fun removeView(parent: ReactBottomNavigationView, view: View) {
    parent.layoutHolder.removeView(view)
  }

  fun removeAllViews(parent: ReactBottomNavigationView) {
    parent.layoutHolder.removeAllViews()
  }

  fun removeViewAt(parent: ReactBottomNavigationView, index: Int) {
    parent.layoutHolder.removeViewAt(index)
  }

  fun needsCustomLayoutForChildren(): Boolean {
    return true
  }

  companion object {
    const val NAME = "RNCTabView"
  }
}
