package com.atc.planner.presentation.place_details.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.atc.planner.R
import com.atc.planner.extensions.loadImage
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.adapter_item_picture.view.*

class PictureItem(private var url: String?) : AbstractItem<PictureItem, PictureItem.ViewHolder>() {

    override fun getLayoutRes(): Int = R.layout.adapter_item_picture

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun getType(): Int = 0

    override fun bindView(holder: ViewHolder?, payloads: MutableList<Any>?) {
        super.bindView(holder, payloads)
        holder?.picture?.loadImage(url)
    }

    override fun unbindView(holder: ViewHolder?) {
        super.unbindView(holder)
        holder?.picture?.setImageDrawable(null)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val picture: ImageView? = view.adapter_item_picture_image_view
    }
}