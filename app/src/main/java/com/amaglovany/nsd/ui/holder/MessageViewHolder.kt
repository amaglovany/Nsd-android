package com.amaglovany.nsd.ui.holder

import android.view.Gravity
import android.view.View.TEXT_ALIGNMENT_TEXT_END
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.amaglovany.nsd.R
import com.amaglovany.nsd.ui.base.view.ViewHolder
import kotlinx.android.synthetic.main.view_item_message.view.*

class MessageViewHolder(
        parent: ViewGroup
) : ViewHolder<ViewHolder.OnViewHolderClickListener>(parent, null, R.layout.view_item_message, false) {

    val textView: TextView = itemView.text
    private val params = textView.layoutParams as FrameLayout.LayoutParams

    var alignEnd: Boolean
        get() = params.gravity == Gravity.END
        set(value) {
            params.gravity = if (value) Gravity.END else Gravity.START
            textView.textAlignment = if (value) TEXT_ALIGNMENT_TEXT_END else TEXT_ALIGNMENT_TEXT_START
        }
}