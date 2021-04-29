package com.app.namllahprovider.presentation.fragments.wizard.on_boarding

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import java.util.*

internal class OnBoardingAdapter : PagerAdapter() {
    private var viewList: MutableList<View>
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val view = viewList[position]
        collection.addView(view)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return viewList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    var data: List<View>?
        get() {
            if (viewList == null) {
                viewList = ArrayList()
            }
            return viewList
        }
        set(list) {
            viewList.clear()
            if (list != null && !list.isEmpty()) {
                viewList.addAll(list)
            }
            notifyDataSetChanged()
        }

    init {
        viewList = ArrayList()
    }
}