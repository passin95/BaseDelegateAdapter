package me.passin.recyclerview.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ViewDelegate<T, V : View> : BaseItemViewDelegate<T, ViewDelegate.Holder<V>>() {

    abstract fun onCreateView(context: Context): V

    abstract fun onBindView(view: V, item: T, positon: Int)

    open fun onCreateView(context: Context, parent: ViewGroup): V {
        return onCreateView(context)
    }

    open fun onBindView(holder: Holder<V>, view: V, item: T, positon: Int) {
        onBindView(view, item, positon)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): Holder<V> {
        return Holder(onCreateView(context, parent))
    }

    override fun onBindViewHolder(holder: Holder<V>, item: T, positon: Int) = onBindView(holder, holder.view, item, positon)

    class Holder<V : View>(val view: V) : RecyclerView.ViewHolder(view)

    /**
     * @see RecyclerView.LayoutParams.getViewLayoutPosition
     */
    protected val View.layoutPosition get() = recyclerLayoutParams.viewLayoutPosition

    /**
     * @see RecyclerView.LayoutParams.getViewAdapterPosition
     */
    protected val View.adapterPosition get() = recyclerLayoutParams.viewAdapterPosition

    /**
     * @see RecyclerView.LayoutManager.generateLayoutParams
     */
    private val View.recyclerLayoutParams get() = layoutParams as RecyclerView.LayoutParams
}
