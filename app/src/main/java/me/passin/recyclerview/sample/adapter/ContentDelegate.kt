package me.passin.recyclerview.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import me.passin.recyclerview.adapter.BaseItemViewDelegate
import me.passin.recyclerview.adapter.sample.R
import me.passin.recyclerview.sample.bean.Content

/**
 * @author: zbb
 * @date: 2020/10/18 21:06
 * @desc:
 */
class ContentDelegate : BaseItemViewDelegate<Content, ContentDelegate.ViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        // 具体的视图或布局文件在代理类中设置，做到高内聚。
        val viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demo_content, parent, false))
        // 每一个 ViewHolder 只在 onCreateViewHolder 绑定一次点击事件，避免内存抖动。
        bindChildClickListener(viewHolder, viewHolder.ivImage, viewHolder.tvDesc)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Content, position: Int) {
        when (holder.adapterPosition % 3) {
            0 -> holder.ivImage.setImageResource(R.drawable.animation_img1)
            1 -> holder.ivImage.setImageResource(R.drawable.animation_img2)
            2 -> holder.ivImage.setImageResource(R.drawable.animation_img3)
        }
        holder.tvDesc.text = item.desc + holder.adapterPosition
    }

    override fun setOnItemChildClick(v: View, viewHolder: ViewHolder, position: Int) {
        // 可以在代理对象内部处理点击逻辑，也可以在代理类外部监听处理（setOnItemChildClickListener），能做到灵活处理。
        if (v.id == R.id.tv_desc) {
            Toast.makeText(v.context, "文字被点击了，position：$position", Toast.LENGTH_SHORT).show()
            return
        }
        super.setOnItemChildClick(v, viewHolder, position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage = itemView.findViewById<ImageView>(R.id.iv_image)!!
        val tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)!!
    }

}
