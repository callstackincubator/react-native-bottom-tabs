package com.rcttabview

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources

class Utils {
  companion object {
    fun convertPixelsToDp(context: Context, value: Int): Double {
      val displayDensity = context.resources.displayMetrics.density
      return (value / displayDensity).toDouble()
    }

    fun getTypefaceStyle(weight: Int?) = when (weight) {
      700 -> Typeface.BOLD
      else -> Typeface.NORMAL
    }

    fun getDefaultColorFor(context: Context, baseColorThemeAttr: Int): Int? {
      val value = TypedValue()
      if (!context.theme.resolveAttribute(baseColorThemeAttr, value, true)) {
        return null
      }
      val baseColor = AppCompatResources.getColorStateList(
        context, value.resourceId
      )
      return baseColor.defaultColor
    }

    // Detect `react-native-edge-to-edge` (https://github.com/zoontek/react-native-edge-to-edge)
    fun isEdgeToEdge() = try {
      Class.forName("com.zoontek.rnedgetoedge.EdgeToEdgePackage")
      true
    } catch (exception: ClassNotFoundException) {
      false
    }
  }
}
