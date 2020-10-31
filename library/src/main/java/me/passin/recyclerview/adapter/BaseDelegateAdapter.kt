package me.passin.recyclerview.adapter

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


abstract class BaseDelegateAdapter @JvmOverloads constructor(data: MutableList<Any>? = null) : RecyclerView.Adapter<ViewHolder>() {

    private var typeDelegateMap = SparseArray<BaseItemViewDelegate<*, *>>()

    lateinit var context: Context
        private set

    var recyclerView: RecyclerView? = null

    var items: MutableList<Any> = data ?: arrayListOf()
        private set

    //region 代理支持

    fun <T> addDelegate(itemViewType: Int, delegateItem: BaseItemViewDelegate<T, *>) {
        typeDelegateMap.put(itemViewType, delegateItem)
        delegateItem._adapter = this
    }

    fun <T> removeDelegate(itemViewType: Int) {
        typeDelegateMap.remove(itemViewType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return typeDelegateMap.get(viewType).onCreateViewHolderInner(parent.context, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        getOutDelegateByViewHolder(holder).onBindViewHolder(holder, items[position], position, payloads)
    }

    override fun getItemCount(): Int = items.size

    @SuppressWarnings("unchecked")
    override fun getItemId(position: Int): Long {
        val item = items[position]
        val itemViewType = getItemViewType(position)
        return (typeDelegateMap.get(itemViewType) as BaseItemViewDelegate<Any, ViewHolder>).getItemId(item)
    }

    //endregion

    //region 设置数据

    fun addData(@NonNull data: Any) {
        items.add(data)
        notifyItemInserted(items.size)
    }

    fun addData(@IntRange(from = 0) position: Int, data: Any) {
        items.add(position, data)
        notifyItemInserted(position)
    }

    fun addData(@NonNull newData: Collection<Any>) {
        items.addAll(newData)
        notifyItemRangeInserted(items.size - newData.size, newData.size)
    }

    fun addData(@IntRange(from = 0) position: Int, newData: Collection<Any>) {
        items.addAll(position, newData)
        notifyItemRangeInserted(position, newData.size)
    }

    fun remove(@IntRange(from = 0) position: Int) {
        if (position >= items.size) {
            return
        }
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setData(@IntRange(from = 0) position: Int, data: Any, @Nullable payload: Any) {
        if (position >= items.size) {
            return
        }
        items[position] = data
        notifyItemChanged(position, payload)
    }

    fun replaceData(newData: Collection<Any>) {
        // 不是同一个引用才清空列表
        if (newData !== this.items) {
            items.clear()
            items.addAll(newData)
        }
        notifyDataSetChanged()
    }

    //endregion

    //region 相关系统 API 回调

    /**
     * @see RecyclerView.Adapter.onViewRecycled
     */
    override fun onViewRecycled(holder: ViewHolder) {
        getOutDelegateByViewHolder(holder).onViewRecycled(holder)
    }

    /**
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    override fun onFailedToRecycleView(holder: ViewHolder): Boolean {
        return getOutDelegateByViewHolder(holder).onFailedToRecycleView(holder)
    }

    /**
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        getOutDelegateByViewHolder(holder).onViewAttachedToWindow(holder)
    }

    /**
     * @see RecyclerView.Adapter.onViewDetachedFromWindow
     */
    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        getOutDelegateByViewHolder(holder).onViewDetachedFromWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.context = recyclerView.context
        val delegateSize = typeDelegateMap.size()
        for (i in 0 until delegateSize) {
            val itemviewdelegate = typeDelegateMap.valueAt(i)
            itemviewdelegate.onAdapterAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        val delegateSize = typeDelegateMap.size()
        for (i in 0 until delegateSize) {
            val itemViewDelegate = typeDelegateMap.valueAt(i)
            itemViewDelegate.onAdapterDetachedFromRecyclerView(recyclerView)
        }
        this.recyclerView = null
    }

    //endregion

    private fun getOutDelegateByViewHolder(holder: ViewHolder): BaseItemViewDelegate<Any, ViewHolder> {
        @Suppress("UNCHECKED_CAST")
        return typeDelegateMap.get(holder.itemViewType) as BaseItemViewDelegate<Any, ViewHolder>
    }

    @SuppressWarnings("unchecked")
    open fun <T : BaseItemViewDelegate<*, *>> getDelegateByItemViewType(viewType: Int): T {
        return typeDelegateMap.get(viewType) as T
    }

}
