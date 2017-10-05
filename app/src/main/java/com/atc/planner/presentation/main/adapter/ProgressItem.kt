package com.atc.planner.presentation.main.adapter

import android.view.View
import com.atc.planner.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class ProgressItem()
    : AbstractFlexibleItem<ProgressItem.ProgressItemViewHolder>() {
    override fun getLayoutRes(): Int = R.layout.item_holder_progress

    override fun equals(other: Any?): Boolean = other is ProgressItem

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<out IFlexible<*>>)
            : ProgressItemViewHolder = ProgressItemViewHolder(view, adapter)

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, holder: ProgressItemViewHolder?, position: Int, payloads: MutableList<Any?>?) {
    }

    class ProgressItemViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

    }
}
