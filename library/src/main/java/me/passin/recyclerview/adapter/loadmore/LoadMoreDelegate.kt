package me.passin.recyclerview.adapter.loadmore

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import me.passin.recyclerview.adapter.BaseItemViewDelegate
import me.passin.recyclerview.adapter.loadmore.LoadMoreView.Companion.STATUS_LOADING

/**
 * @author: zbb
 * @date: 2020/10/6 16:37
 * @desc:
 */
class LoadMoreDelegate(@NonNull private var loadMoreView: LoadMoreView) : BaseItemViewDelegate<Any, LoadMoreDelegate.ViewHolder>() {

    private var loadMoreListener: OnLoadMoreListener? = null

    private val loadMoreViewPosition: Int
        get() {
            return adapter.items.size
        }

    /**
     * 是否打开加载更多
     */
    var isEnableLoadMore = false
        set(value) {
            val oldHasLoadMore = hasLoadMoreView()
            field = value
            val newHasLoadMore = hasLoadMoreView()

            if (oldHasLoadMore) {
                if (!newHasLoadMore) {
                    adapter.notifyItemRemoved(loadMoreViewPosition)
                }
            } else {
                if (newHasLoadMore) {
                    loadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
                    adapter.notifyItemInserted(loadMoreViewPosition)
                }
            }
        }

    /**
     * 预加载
     */
    var preLoadNumber = 1
        set(value) {
            if (value > 1) {
                field = value
            }
        }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = loadMoreView.getRootView(parent)
        view.setOnClickListener {
            if (loadMoreView.loadMoreStatus == LoadMoreView.STATUS_DEFAULT || loadMoreView.loadMoreStatus == LoadMoreView.STATUS_FAIL) {
                invokeLoadMoreListener()
                adapter.notifyItemChanged(loadMoreViewPosition)
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Any, positon: Int) {
        loadMoreView.onBindViewHolder(holder)
    }

    private val autoLoadMoreListenerSupport = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (hasLoadMoreView() && loadMoreView.loadMoreStatus == LoadMoreView.STATUS_FAIL && !recyclerView.canScrollVertically(1)) {
                invokeLoadMoreListener()
                adapter.notifyItemChanged(loadMoreViewPosition)
            }
        }
    }

    override fun onAdapterAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAdapterAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(autoLoadMoreListenerSupport)
    }

    override fun onAdapterDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onAdapterDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(autoLoadMoreListenerSupport)
    }

    /**
     * 自动加载数据
     * @param position Int
     */
    internal fun autoLoadMore(position: Int) {
        if (!hasLoadMoreView()) {
            return
        }
        if (position < adapter.itemCount - preLoadNumber) {
            return
        }
        if (loadMoreView.loadMoreStatus != LoadMoreView.STATUS_DEFAULT) {
            return
        }
        invokeLoadMoreListener()
    }

    private fun invokeLoadMoreListener() {
        loadMoreView.loadMoreStatus = LoadMoreView.STATUS_LOADING
        adapter.recyclerView?.post {
            loadMoreListener?.onLoadMore()
        }
    }

    fun hasLoadMoreView(): Boolean {
        if (loadMoreListener == null || !isEnableLoadMore) {
            return false
        }
        if (loadMoreView.loadMoreStatus == LoadMoreView.STATUS_END_GONE) {
            return false
        }
        return adapter.items.isNotEmpty()
    }

    @JvmOverloads
    fun loadMoreEnd(gone: Boolean = false) {
        if (!hasLoadMoreView()) {
            return
        }
        if (gone) {
            loadMoreView.loadMoreStatus = LoadMoreView.STATUS_END_GONE
            adapter.notifyItemRemoved(loadMoreViewPosition)
        } else {
            loadMoreView.loadMoreStatus = LoadMoreView.STATUS_END
            adapter.notifyItemChanged(loadMoreViewPosition)
        }
    }

    @JvmOverloads
    fun loadMoreComplete() {
        if (!hasLoadMoreView()) {
            return
        }
        loadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
        adapter.notifyItemChanged(loadMoreViewPosition)
    }

    @JvmOverloads
    fun loadMoreFail() {
        if (!hasLoadMoreView()) {
            return
        }
        loadMoreView.loadMoreStatus = LoadMoreView.STATUS_FAIL
        adapter.notifyItemChanged(loadMoreViewPosition)
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        loadMoreListener = listener
        rest()
    }

    /**
     * 重置状态
     */
    private fun rest() {
        if (loadMoreListener != null) {
            isEnableLoadMore = true
            loadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
        }
    }

    fun isLoading(): Boolean {
        return loadMoreView.loadMoreStatus == STATUS_LOADING
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val views: SparseArray<View> = SparseArray()

        @Suppress("UNCHECKED_CAST")
        fun <T : View> getView(@IdRes viewId: Int): T {
            var view = views.get(viewId)
            if (view == null) {
                view = itemView.findViewById(viewId)
                views.put(viewId, view)
            }
            return view as T
        }
    }
}