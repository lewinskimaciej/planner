package com.atc.planner.presentation.main.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.atc.planner.R
import com.atc.planner.extensions.loadImage
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.IHolder
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_holder_place.view.*

class PlaceItem(var placeItemModel: PlaceItemModel)
    : AbstractFlexibleItem<PlaceItem.PlaceItemViewHolder>(),
        IHolder<PlaceItemModel> {
    override fun getLayoutRes(): Int = R.layout.item_holder_place

    override fun equals(other: Any?): Boolean = other is PlaceItem && other.model.id == this.model.id

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<out IFlexible<*>>)
            : PlaceItemViewHolder = PlaceItemViewHolder(view, adapter)

    override fun getModel(): PlaceItemModel = placeItemModel

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, holder: PlaceItemViewHolder?, position: Int, payloads: MutableList<Any?>?) {
        holder?.icon?.loadImage(model.imageUrl)
        holder?.title?.text = model.title
        holder?.description?.text = model.description
    }

    class PlaceItemViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
        var icon: ImageView = view.item_icon
        var title: TextView = view.item_title
        var description: TextView = view.item_description
    }
}