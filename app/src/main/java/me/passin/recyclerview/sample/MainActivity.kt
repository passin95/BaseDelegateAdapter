package me.passin.recyclerview.sample

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.passin.recyclerview.adapter.BaseDelegateAdapter
import me.passin.recyclerview.adapter.listener.OnItemChildClickListener
import me.passin.recyclerview.adapter.listener.OnItemClickListener
import me.passin.recyclerview.adapter.sample.R
import me.passin.recyclerview.sample.adapter.DemoAdapter
import me.passin.recyclerview.sample.adapter.DemoAdapter.Companion.VIEW_TYPE_FOOTER
import me.passin.recyclerview.sample.adapter.FooterDelegate
import me.passin.recyclerview.sample.bean.Content
import me.passin.recyclerview.sample.bean.Footer
import me.passin.recyclerview.sample.bean.Header
import me.passin.recyclerview.sample.bean.Title
import kotlin.collections.ArrayList

/**
 * @author: zbb
 * @date: 2020/10/17 20:43
 * @desc:
 */
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: DemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DemoAdapter()
        adapter.contentDelegate.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseDelegateAdapter, view: View, position: Int) {
                when (view.id) {
                    R.id.iv_image -> {
                        Toast.makeText(view.context, "图片被点击了，position：$position", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        val footerDelegate = adapter.getDelegateByItemViewType<FooterDelegate>(VIEW_TYPE_FOOTER)
        footerDelegate.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseDelegateAdapter, view: View, position: Int) {
                Toast.makeText(view.context, "尾布局被点击了", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.adapter = adapter
        val data = ArrayList<Any>()

        data.add(Header())
        for (i in 0..9) {
            data.add(Title("标题"))
            data.add(Content("内容"))
        }
        data.add(Footer())
        adapter.replaceData(data)
    }

}