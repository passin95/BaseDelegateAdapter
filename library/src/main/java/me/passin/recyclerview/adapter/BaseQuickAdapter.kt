package me.passin.recyclerview.adapter

import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import me.passin.recyclerview.adapter.loadmore.LoadMoreDelegate
import me.passin.recyclerview.adapter.loadmore.LoadMoreView

/**
 * @author: zbb
 * @date: 2020/10/6 20:47
 * @desc:
 */
abstract class BaseQuickAdapter @JvmOverloads constructor(data: MutableList<Any>? = null) : BaseDelegateAdapter(data) {

    var loadMoreDelegate: LoadMoreDelegate? = null

    companion object {
        const val LOAD_MORE_VIEW = 0x10000111
    }

    open fun supporLoadMore(@NonNull loadMoreView: LoadMoreView) {
        loadMoreDelegate = LoadMoreDelegate(loadMoreView)
        addDelegate(LOAD_MORE_VIEW, loadMoreDelegate!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        loadMoreDelegate?.let {
            it.autoLoadMore(position)
            if (position == items.size) {
                it.onBindViewHolder(holder as LoadMoreDelegate.ViewHolder, position, position)
                return
            }
        }
        if (position < items.size) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        loadMoreDelegate?.let {
            return items.size + if (it.hasLoadMoreView()) 1 else 0
        }
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (loadMoreDelegate != null && position == items.size) {
            LOAD_MORE_VIEW
        } else {
            getDefItemViewType(position)
        }
    }

    abstract fun getDefItemViewType(position: Int): Int

}