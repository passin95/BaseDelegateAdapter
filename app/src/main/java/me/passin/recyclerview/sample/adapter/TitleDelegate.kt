package me.passin.recyclerview.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.passin.recyclerview.adapter.BaseItemViewDelegate
import me.passin.recyclerview.adapter.sample.R
import me.passin.recyclerview.sample.bean.Title

/**
 * @author: zbb
 * @date: 2020/10/18 21:06
 * @desc:
 */
class TitleDelegate : BaseItemViewDelegate<Title, TitleDelegate.ViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demo_title, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Title, position: Int) {
        holder.tvTitle.text = item.name + holder.adapterPosition
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)!!

    }

}
