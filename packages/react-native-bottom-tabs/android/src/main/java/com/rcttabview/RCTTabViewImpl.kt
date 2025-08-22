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

/**
 * Data class representing tab information
 */
data class TabInfo(
    val key: String,
    val title: String,
    val badge: String?,
    val activeTintColor: Int?,
    val hidden: Boolean,
    val testID: String?
)

/**
 * Implementation class for RCTTabView that handles the bridge between 
 * React Native props and native Android view methods.
 * Supports both phone (BottomNavigationView) and tablet (NavigationRailView) modes.
 */
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
    // Always update the main view items for both tablet and phone
    view.updateItems(itemsArray)
  }

  // MARK: - Selection Management

  fun setSelectedPage(view: ReactBottomNavigationView, key: String) {
    // Always call the main view's setSelectedItem for both modes
    view.setSelectedItem(key)
    
    // The main view will handle the rail navigation updates in tablet mode
  }

  // MARK: - Configuration Methods

  fun setLabeled(view: ReactBottomNavigationView, flag: Boolean?) {
    if (view.isTablet) {
      view.railNavigation?.setLabeled(flag)
    } else {
      view.setLabeled(flag)
    }
  }

  fun setIcons(view: ReactBottomNavigationView, icons: ReadableArray?) {
    if (view.isTablet) {
      view.railNavigation?.setIcons(icons)
    } else {
      view.setIcons(icons)
    }
  }

  // MARK: - Color Configuration

  fun setBarTintColor(view: ReactBottomNavigationView, color: Int?) {
    if (view.isTablet) {
      view.railNavigation?.setBarTintColor(color)
    } else {
      view.setBarTintColor(color)
    }
  }

  fun setRippleColor(view: ReactBottomNavigationView, rippleColor: Int?) {
    if (view.isTablet) {
      view.railNavigation?.setRippleColor(rippleColor)
    } else {
      if (rippleColor != null) {
        val color = ColorStateList.valueOf(rippleColor)
        view.setRippleColor(color)
      }
    }
  }

  fun setActiveIndicatorColor(view: ReactBottomNavigationView, color: Int?) {
    if (view.isTablet) {
      view.railNavigation?.setActiveIndicatorColor(color)
    } else {
      if (color != null) {
        val color = ColorStateList.valueOf(color)
        view.setActiveIndicatorColor(color)
      }
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

  // MARK: - Event Management

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

  // MARK: - Layout Management

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
