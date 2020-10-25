package me.passin.recyclerview.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import me.passin.recyclerview.adapter.listener.OnItemChildClickListener
import me.passin.recyclerview.adapter.listener.OnItemChildLongClickListener
import me.passin.recyclerview.adapter.listener.OnItemClickListener
import me.passin.recyclerview.adapter.listener.OnItemLongClickListener

abstract class BaseItemViewDelegate<T, VH : ViewHolder> {

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null
    private var onItemChildClickListener: OnItemChildClickListener? = null
    private var onItemChildLongClickListener: OnItemChildLongClickListener? = null

    @Suppress("PropertyName")
    internal var _adapter: BaseDelegateAdapter? = null

    val adapter: BaseDelegateAdapter
        get() {
            if (_adapter == null) {
                throw NullPointerException("This $this has not been attached to MultiTypeAdapter yet. " +
                        "You should not call the method before registering the delegate.")
            }
            return _adapter!!
        }

    abstract fun onCreateViewHolder(context: Context, parent: ViewGroup): VH

    fun onCreateViewHolderInner(context: Context, parent: ViewGroup): VH {
        val viewHolder = onCreateViewHolder(context, parent)
        bindItemClickListener(viewHolder)
        return viewHolder
    }

    abstract fun onBindViewHolder(holder: VH, item: T, position: Int)

    open fun onBindViewHolder(holder: VH, item: T, position: Int, payloads: List<Any>) {
        onBindViewHolder(holder, item, position)
    }

    /**
     * 绑定 item 点击事件
     * @param viewHolder VH
     */
    protected open fun bindItemClickListener(viewHolder: ViewHolder) {
        onItemClickListener?.let {
            viewHolder.itemView.setOnClickListener { v ->
                val position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                setOnItemClick(v, position)
            }
        }
        onItemLongClickListener?.let {
            viewHolder.itemView.setOnLongClickListener { v ->
                val position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                setOnItemLongClick(v, position)
            }
        }
    }

    protected open fun bindChildClickListener(@NonNull viewHolder: ViewHolder, vararg childViews: View) {
        onItemChildClickListener?.let {
            for (childView in childViews) {
                childView.setOnClickListener { v ->
                    val position = viewHolder.adapterPosition
                    if (position == RecyclerView.NO_POSITION) {
                        return@setOnClickListener
                    }
                    setOnItemChildClick(v, position)
                }
            }
        }
    }

    protected open fun bindChildLongClickListener(@NonNull viewHolder: ViewHolder, vararg childViews: View) {
        onItemChildLongClickListener?.let {
            for (childView in childViews) {
                childView.setOnLongClickListener { v ->
                    val position = viewHolder.adapterPosition
                    if (position == RecyclerView.NO_POSITION) {
                        return@setOnLongClickListener false
                    }
                    setOnItemChildLongClick(v, position)
                }
            }
        }
    }

    protected open fun setOnItemClick(v: View, position: Int) {
        onItemClickListener?.onItemClick(adapter, v, position)
    }

    protected open fun setOnItemLongClick(v: View, position: Int): Boolean {
        return onItemLongClickListener?.onItemLongClick(adapter, v, position) ?: false
    }

    protected open fun setOnItemChildClick(v: View, position: Int) {
        onItemChildClickListener?.onItemChildClick(adapter, v, position)
    }

    protected open fun setOnItemChildLongClick(v: View, position: Int): Boolean {
        return onItemChildLongClickListener?.onItemChildLongClick(adapter, v, position) ?: false
    }

    open fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.onItemClickListener = listener
    }

    open fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.onItemLongClickListener = listener
    }

    open fun setOnItemChildClickListener(listener: OnItemChildClickListener?) {
        this.onItemChildClickListener = listener
    }

    open fun setOnItemChildLongClickListener(listener: OnItemChildLongClickListener?) {
        this.onItemChildLongClickListener = listener
    }

    fun getPosition(holder: ViewHolder): Int {
        return holder.adapterPosition
    }

    @Suppress("UNUSED_PARAMETER")
    open fun getItemId(item: T): Long = RecyclerView.NO_ID

    /**
     * @see RecyclerView.Adapter.onViewRecycled
     */
    open fun onViewRecycled(holder: VH) {}

    /**
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    open fun onFailedToRecycleView(holder: VH): Boolean {
        return false
    }

    /**
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    open fun onViewAttachedToWindow(holder: VH) {}

    /**
     * @see RecyclerView.Adapter.onViewDetachedFromWindow
     */
    open fun onViewDetachedFromWindow(holder: VH) {}

    /**
     * @see RecyclerView.Adapter.onAttachedToRecyclerView
     */
    open fun onAdapterAttachedToRecyclerView(recyclerView: RecyclerView) {}

    /**
     * @see RecyclerView.Adapter.onDetachedFromRecyclerView
     */
    open fun onAdapterDetachedFromRecyclerView(recyclerView: RecyclerView) {}

}
