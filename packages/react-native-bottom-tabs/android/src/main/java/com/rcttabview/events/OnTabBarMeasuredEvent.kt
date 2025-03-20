package com.rcttabview.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.RCTEventEmitter

class OnTabBarMeasuredEvent(viewTag: Int, private val height: Int) :
  Event<TabLongPressEvent>(viewTag) {

  companion object {
    const val EVENT_NAME = "onTabBarMeasured"
  }

  override fun getEventName(): String {
    return EVENT_NAME
  }

  override fun dispatch(rctEventEmitter: RCTEventEmitter) {
    val event = Arguments.createMap().apply {
      putInt("height", height)
    }
    rctEventEmitter.receiveEvent(viewTag, eventName, event)
  }
}
