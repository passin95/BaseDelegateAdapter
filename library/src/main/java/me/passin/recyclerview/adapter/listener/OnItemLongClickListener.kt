package me.passin.recyclerview.adapter.listener

import android.view.View
import me.passin.recyclerview.adapter.BaseDelegateAdapter

/**
 * @author: zbb
 * @date: 2020/10/6 21:51
 * @desc:
 */
interface OnItemLongClickListener {

    fun onItemLongClick(adapter: BaseDelegateAdapter, view: View, position: Int): Boolean

}