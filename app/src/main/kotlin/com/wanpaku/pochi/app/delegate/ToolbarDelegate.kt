package com.wanpaku.pochi.app.delegate

import android.graphics.drawable.Drawable
import android.support.annotation.StringRes
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.internal.ActivityLifecycleDelegate
import com.wanpaku.pochi.app.internal.BaseActivity
import com.wanpaku.pochi.domain.ZipCode
import com.wanpaku.pochi.infra.ui.widget.TwoStateButton

open class ToolbarDelegate(title: String? = null,
                           titleEnabled: Boolean = true,
                           subTitle: String? = null,
                           displayHomeAsUpEnabled: Boolean = true,
                           iconClickedListener: (BaseActivity) -> Unit = { it.finish() },
                           icon: Drawable? = null) : ActivityLifecycleDelegate {

    var title = title
        set(value) {
            if (value != field) {
                field = value
                refresh()
            }
        }

    var titleEnabled = titleEnabled
        set(value) {
            if (value != field) {
                field = value
                refresh()
            }
        }

    var subTitle = subTitle
        set(value) {
            if (value != field) {
                field = value
                refresh()
            }
        }

    var displayHomeAsUpEnabled = displayHomeAsUpEnabled
        set(value) {
            if (value != field) {
                field = value
                refresh()
            }
        }

    var iconClickedListener = iconClickedListener
        set(value) {
            if (value != field) {
                field = value
                refresh()
            }
        }

    var icon = icon
        set(value) {
            if (value != field) {
                field = value
                refresh()
            }
        }


    protected var toolbar: Toolbar? = null
    protected var supportActionBar: ActionBar? = null

    override fun onOptionsItemSelected(activity: BaseActivity, item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            iconClickedListener(activity)
        }
        return super.onOptionsItemSelected(activity, item)
    }

    override fun onContentChanged(activity: BaseActivity) {
        activity.apply {
            toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            this@ToolbarDelegate.supportActionBar = supportActionBar
        }
        refresh()
    }

    private fun refresh() {
        toolbar?.apply {
            icon?.let { navigationIcon = it }
        }
        supportActionBar?.apply {
            title = this@ToolbarDelegate.title
            subtitle = this@ToolbarDelegate.subTitle
            setDisplayShowTitleEnabled(this@ToolbarDelegate.titleEnabled)
            setDisplayHomeAsUpEnabled(this@ToolbarDelegate.displayHomeAsUpEnabled)
        }
    }

}

class SearchResultToolbarDelegate(title: String,
                                  subTitle: String,
                                  private val changeZipCodeClickedListener: () -> Unit)
    : ToolbarDelegate(displayHomeAsUpEnabled = true, title = title, subTitle = subTitle) {

    override fun onContentChanged(activity: BaseActivity) {
        super.onContentChanged(activity)
        toolbar?.findViewById<View>(R.id.change_zip_code)?.setOnClickListener {
            changeZipCodeClickedListener()
        }
    }

}

open class MainTabToolbarDelegate(@StringRes private val titleResId: Int)
    : ToolbarDelegate(displayHomeAsUpEnabled = false, titleEnabled = true) {

    open fun select(activity: BaseActivity) {
        activity.supportActionBar?.setTitle(titleResId)
    }

    open fun unselect(activity: BaseActivity) = Unit

}

class SearchToolbarDelegate(@StringRes titleResId: Int, private val searchClickedListener: (zipCode: ZipCode) -> Unit)
    : MainTabToolbarDelegate(titleResId) {

    private var searchWidget: View? = null

    override fun onContentChanged(activity: BaseActivity) {
        super.onContentChanged(activity)
        searchWidget = activity.findViewById(R.id.search_widget)
        val zipCode = activity.findViewById<EditText>(R.id.zip_code)
        val search = activity.findViewById<TwoStateButton>(R.id.search)
        zipCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) = Unit

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                val isValid = ZipCode.Validator(p0.toString()).isValid()
                search.isActive = isValid
            }
        })
        search.setOnClickListener {
            ZipCode.Validator(zipCode.text.toString()).to()?.let {
                searchClickedListener(it)
            }
        }
    }

    override fun select(activity: BaseActivity) {
        super.select(activity)
        searchWidget?.visibility = View.VISIBLE
    }

    override fun unselect(activity: BaseActivity) {
        super.unselect(activity)
        searchWidget?.visibility = View.GONE
    }

}

class BookingToolbarDelegate(@StringRes titleResId: Int)
    : MainTabToolbarDelegate(titleResId) {

    private var appBar: AppBarLayout? = null

    override fun onContentChanged(activity: BaseActivity) {
        super.onContentChanged(activity)
        appBar = activity.findViewById<AppBarLayout>(R.id.app_bar)
    }

    override fun select(activity: BaseActivity) {
        super.select(activity)
        appBar?.let { ViewCompat.setElevation(it, 0f) }
        toolbar?.let { ViewCompat.setElevation(it, 0f) }
    }

    override fun unselect(activity: BaseActivity) {
        super.unselect(activity)
        val elevation = activity.resources.getDimensionPixelSize(R.dimen.elevation_appbar).toFloat()
        appBar?.let { ViewCompat.setElevation(it, elevation) }
        toolbar?.let { ViewCompat.setElevation(it, elevation) }
    }

}