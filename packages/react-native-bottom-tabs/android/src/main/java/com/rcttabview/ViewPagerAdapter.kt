package com.rcttabview

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
  private val childrenViews: ArrayList<View> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FrameLayout(parent.context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
    })
  }

  override fun onBindViewHolder(holder: ViewHolder, index: Int) {
    val container: FrameLayout = holder.container as FrameLayout
    val child = getChildAt(index)
    holder.setIsRecyclable(false)

    if (container.childCount > 0) {
      container.removeAllViews()
    }

    if (child.parent != null) {
      (child.parent as FrameLayout).removeView(child)
    }

    container.addView(child)
  }

  override fun getItemCount(): Int {
    return childrenViews.size
  }

  fun addChild(child: View, index: Int) {
    childrenViews.add(index, child)
    notifyItemInserted(index)
  }

  fun getChildAt(index: Int): View {
    return childrenViews[index]
  }

  fun removeChild(child: View) {
    val index = childrenViews.indexOf(child)

    if(index > -1) {
      removeChildAt(index)
    }
  }

  fun removeAll() {
    for (index in 1..childrenViews.size) {
      val child = childrenViews[index-1]
      if (child.parent?.parent != null) {
        (child.parent.parent as ViewGroup).removeView(child.parent as View)
      }
    }
    val removedChildrenCount = childrenViews.size
    childrenViews.clear()
    notifyItemRangeRemoved(0, removedChildrenCount)
  }

  fun removeChildAt(index: Int) {
    if (index >= 0 && index < childrenViews.size) {
      childrenViews.removeAt(index)
      notifyItemRemoved(index)
    }
  }

  class ViewHolder(val container: View) : RecyclerView.ViewHolder(container)
}
