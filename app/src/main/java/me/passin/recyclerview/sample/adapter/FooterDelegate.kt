package me.passin.recyclerview.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.passin.recyclerview.adapter.BaseItemViewDelegate
import me.passin.recyclerview.adapter.sample.R
import me.passin.recyclerview.sample.bean.Footer

/**
 * @author: zbb
 * @date: 2020/10/18 7:33
 * @desc:
 */
internal class FooterDelegate : BaseItemViewDelegate<Footer, FooterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demo_footer, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Footer, position: Int) {

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
