package com.wanpaku.pochi.infra.ui.widget

import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import timber.log.Timber


fun BottomNavigationView.disableShiftingMode() {
    val TAG = BottomNavigationView::class.java.simpleName + " extension"

    val menuView = getChildAt(0) as BottomNavigationMenuView
    try {
        menuView.javaClass.getDeclaredField("mShiftingMode").apply {
            isAccessible = true
            setBoolean(menuView, false)
            isAccessible = false
        }

        0.rangeTo(menuView.childCount - 1)
                .map { menuView.getChildAt(it) as BottomNavigationItemView }
                .forEach {
                    it.setShiftingMode(false)
                    it.setChecked(it.itemData.isChecked)
                }
    } catch (e: NoSuchFieldException) {
        Timber.tag(TAG).e(e, "Unable to get shift mode field")
    } catch (e: IllegalAccessException) {
        Timber.tag(TAG).e(e, "Unable to change value of shift mode")
    }
}