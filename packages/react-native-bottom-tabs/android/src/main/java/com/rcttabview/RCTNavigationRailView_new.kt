package com.rcttabview

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.assets.ReactFontManager
import com.facebook.react.views.text.ReactTypefaceUtils
import com.google.android.material.navigationrail.NavigationRailView

class ExtendedNavigationRailView(context: Context) : NavigationRailView(context) {
    override fun getMaxItemCount(): Int {
        return 100
    }
}

class ReactNavigationRailView(context: Context) : ExtendedNavigationRailView(context) {
    var onTabSelectedListener: ((key: String) -> Unit)? = null
    var onTabLongPressedListener: ((key: String) -> Unit)? = null
    var items: MutableList<TabInfo> = mutableListOf()
    private val iconSources: MutableMap<Int, ImageSource> = mutableMapOf()
    private val drawableCache: MutableMap<ImageSource, Drawable> = mutableMapOf()

    private var selectedItem: String? = null
    private var activeTintColor: Int? = null
    private var inactiveTintColor: Int? = null
    private val checkedStateSet = intArrayOf(android.R.attr.state_checked)
    private val uncheckedStateSet = intArrayOf(-android.R.attr.state_checked)
    private var hapticFeedbackEnabled = false
    private var fontSize: Int? = null
    private var fontFamily: String? = null
    private var fontWeight: Int? = null
    private var labeled: Boolean? = null
    private var hasCustomAppearance = false

    private val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    init {
        // Set up navigation rail listeners using Material3's built-in methods
        setOnItemSelectedListener { menuItem ->
            try {
                val selectedTab = items.getOrNull(menuItem.itemId)
                selectedTab?.let { 
                    selectedItem = it.key
                    onTabSelectedListener?.invoke(it.key)
                    emitHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                }
            } catch (e: Exception) {
                android.util.Log.e("ReactNavigationRailView", "Error in item selection", e)
            }
            true
        }

        setOnItemReselectedListener { menuItem ->
            val reselectedTab = items.getOrNull(menuItem.itemId)
            reselectedTab?.let {
                // Handle reselection if needed
            }
        }
    }

    private fun getDrawable(imageSource: ImageSource, onDrawableReady: (Drawable?) -> Unit) {
        drawableCache[imageSource]?.let {
            onDrawableReady(it)
            return
        }
        val request = ImageRequest.Builder(context)
            .data(imageSource.getUri(context))
            .target { drawable ->
                post {
                    val stateDrawable = drawable.asDrawable(context.resources)
                    drawableCache[imageSource] = stateDrawable
                    onDrawableReady(stateDrawable)
                }
            }
            .listener(
                onError = { _, result ->
                    android.util.Log.e("ReactNavigationRailView", "Error loading image: ${imageSource.uri}", result.throwable)
                }
            )
            .build()

        imageLoader.enqueue(request)
    }

    fun updateItems(items: MutableList<TabInfo>) {
        // If an item got removed, let's re-add all items
        if (items.size < this.items.size) {
            menu.clear()
        }
        this.items = items
        items.forEachIndexed { index, item ->
            val menuItem = getOrCreateItem(index, item.title)
            if (item.title != menuItem.title) {
                menuItem.title = item.title
            }

            menuItem.isVisible = !item.hidden
            if (iconSources.containsKey(index)) {
                getDrawable(iconSources[index]!!) { drawable ->
                    menuItem.icon = drawable
                }
            }

            // Set up long press listener and testID
            post {
                val itemView = findViewById<View>(menuItem.itemId)
                itemView?.let { view ->
                    view.setOnLongClickListener {
                        onTabLongPressedListener?.invoke(item.key)
                        emitHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        true
                    }
                    
                    item.testID?.let { testId ->
                        view.findViewById<View>(com.google.android.material.R.id.navigation_bar_item_content_container)
                            ?.apply {
                                tag = testId
                            }
                    }
                }
            }
        }
        
        // Update tint colors and text appearance after updating all items
        post {
            updateTextAppearance()
            updateTintColors()
        }
    }

    private fun getOrCreateItem(index: Int, title: String): MenuItem {
        return menu.findItem(index) ?: menu.add(0, index, 0, title)
    }

    fun setSelectedItem(value: String) {
        selectedItem = value
        val index = items.indexOfFirst { it.key == value }
        if (index >= 0) {
            selectedItemId = index
        }
    }

    fun setLabeled(labeled: Boolean?) {
        this.labeled = labeled
        labelVisibilityMode = when (labeled) {
            false -> com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_UNLABELED
            true -> com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_LABELED
            else -> com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_AUTO
        }
    }

    fun setIcons(icons: ReadableArray?) {
        if (icons == null || icons.size() == 0) {
            return
        }

        for (idx in 0 until icons.size()) {
            val source = icons.getMap(idx)
            val uri = source?.getString("uri")
            if (uri.isNullOrEmpty()) {
                continue
            }

            val imageSource = ImageSource(context, uri)
            this.iconSources[idx] = imageSource

            // Update existing item if exists
            menu.findItem(idx)?.let { menuItem ->
                getDrawable(imageSource) { drawable ->
                    menuItem.icon = drawable
                }
            }
        }
    }

    fun setBarTintColor(color: Int?) {
        val backgroundColor = color ?: Utils.getDefaultColorFor(context, android.R.attr.colorPrimary) ?: return
        val colorDrawable = android.graphics.drawable.ColorDrawable(backgroundColor)
        itemBackground = colorDrawable
        backgroundTintList = android.content.res.ColorStateList.valueOf(backgroundColor)
        hasCustomAppearance = true
    }

    fun setActiveTintColor(color: Int?) {
        activeTintColor = color
        updateTintColors()
    }

    fun setInactiveTintColor(color: Int?) {
        inactiveTintColor = color
        updateTintColors()
    }

    fun setFontSize(fontSize: Int?) {
        this.fontSize = fontSize
        updateTextAppearance()
    }

    fun setFontFamily(fontFamily: String?) {
        this.fontFamily = fontFamily
        updateTextAppearance()
    }

    fun setFontWeight(fontWeight: Int?) {
        this.fontWeight = fontWeight
        updateTextAppearance()
    }

    override fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean) {
        this.hapticFeedbackEnabled = hapticFeedbackEnabled
    }

    fun updateTextAppearance() {
        // Early return if there is no custom text appearance
        if (fontSize == null && fontFamily == null && fontWeight == null) {
            return
        }

        val typeface = if (fontFamily != null || fontWeight != null) {
            ReactFontManager.getInstance().getTypeface(
                fontFamily ?: "",
                Utils.getTypefaceStyle(fontWeight),
                context.assets
            )
        } else null
        val size = fontSize?.toFloat()?.takeIf { it > 0 }

        val menuView = getChildAt(0) as? android.view.ViewGroup ?: return
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i)
            val largeLabel =
                item.findViewById<TextView>(com.google.android.material.R.id.navigation_bar_item_large_label_view)
            val smallLabel =
                item.findViewById<TextView>(com.google.android.material.R.id.navigation_bar_item_small_label_view)

            listOf(largeLabel, smallLabel).forEach { label ->
                label?.apply {
                    size?.let { setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, it) }
                    typeface?.let { setTypeface(it) }
                }
            }
        }
    }

    fun updateTintColors() {
        val currentItemTintColor = items.firstOrNull { it.key == selectedItem }?.activeTintColor
        val colorPrimary = currentItemTintColor ?: activeTintColor ?: Utils.getDefaultColorFor(
            context,
            android.R.attr.colorPrimary
        ) ?: return
        val colorSecondary =
            inactiveTintColor ?: Utils.getDefaultColorFor(context, android.R.attr.textColorSecondary)
            ?: return
        val states = arrayOf(uncheckedStateSet, checkedStateSet)
        val colors = intArrayOf(colorSecondary, colorPrimary)

        android.content.res.ColorStateList(states, colors).apply {
            itemTextColor = this
            itemIconTintList = this
        }
    }

    private fun emitHapticFeedback(feedbackConstants: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && hapticFeedbackEnabled) {
            this.performHapticFeedback(feedbackConstants)
        }
    }

    fun handleConfigurationChanged(newConfig: Configuration?) {
        if (hasCustomAppearance) {
            return
        }

        // User has hidden the navigation rail, don't re-attach it
        if (visibility == View.GONE) {
            return
        }

        // Re-setup after configuration change
        updateItems(items)
        setLabeled(this.labeled)
        this.selectedItem?.let { setSelectedItem(it) }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handleConfigurationChanged(newConfig)
    }

    fun onDropViewInstance() {
        imageLoader.shutdown()
    }
}
