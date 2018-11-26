package de.jensklingenberg.sheasy.ui.common

import android.os.Bundle
import android.view.ViewGroup
import de.jensklingenberg.sheasy.R
import de.jensklingenberg.sheasy.model.GenericListHeaderSourceItem
import kotlinx.android.synthetic.main.list_item_header_generic.view.*


class GenericListHeaderViewHolder(viewParent: ViewGroup) :
    BaseViewHolder<GenericListHeaderSourceItem>(viewParent, R.layout.list_item_header_generic) {


    override fun onBindViewHolder(item2: Any, diff: Bundle) {

        val item = (item2 as GenericListHeaderSourceItem).getPayload()



        itemView?.apply {
            headerTitle.text = item

        }


    }


}