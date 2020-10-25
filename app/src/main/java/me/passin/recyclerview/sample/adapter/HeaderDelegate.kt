package me.passin.recyclerview.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import me.passin.recyclerview.adapter.BaseDelegateAdapter
import me.passin.recyclerview.adapter.BaseItemViewDelegate
import me.passin.recyclerview.adapter.listener.OnItemChildClickListener
import me.passin.recyclerview.adapter.listener.OnItemClickListener
import me.passin.recyclerview.adapter.sample.R
import me.passin.recyclerview.sample.bean.Header

/**
 * @author: zbb
 * @date: 2020/10/18 0:42
 * @desc:
 */
internal class HeaderDelegate : BaseItemViewDelegate<Header, HeaderDelegate.ViewHolder>(), OnItemClickListener {

    init {
        setOnItemClickListener(this)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demo_header, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Header, position: Int) {

    }

    override fun onItemClick(adapter: BaseDelegateAdapter, view: View, position: Int) {
        Toast.makeText(view.context, "头布局被点击了", Toast.LENGTH_SHORT).show()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}