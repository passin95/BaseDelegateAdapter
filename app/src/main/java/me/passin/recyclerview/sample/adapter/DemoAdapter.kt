package me.passin.recyclerview.sample.adapter

import me.passin.recyclerview.adapter.BaseDelegateAdapter
import me.passin.recyclerview.adapter.BaseQuickAdapter
import me.passin.recyclerview.sample.bean.Content
import me.passin.recyclerview.sample.bean.Footer
import me.passin.recyclerview.sample.bean.Header
import me.passin.recyclerview.sample.bean.Title

/**
 * @author: zbb
 * @date: 2020/10/17 23:46
 * @desc:
 */
class DemoAdapter : BaseDelegateAdapter() {

    companion object {
        const val VIEW_TYPE_HEADER = 1001
        const val VIEW_TYPE_FOOTER = 1002
        const val VIEW_TYPE_TILTE = 1003
        const val VIEW_TYPE_CONTENT = 1004
    }

    val contentDelegate = ContentDelegate()

    init {
        // 注册 ItemViewType 代理类。
        addDelegate(VIEW_TYPE_HEADER, HeaderDelegate())
        addDelegate(VIEW_TYPE_FOOTER, FooterDelegate())
        addDelegate(VIEW_TYPE_TILTE, TitleDelegate())
        addDelegate(VIEW_TYPE_CONTENT, contentDelegate)
    }

    // items 容器的泛型是 Any（Object），这也是该库的核心思想，能够传入不同的类的对象，分发给相应的代理类处理。
    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        when (item::class) {
            Header::class -> {
                return VIEW_TYPE_HEADER
            }
            Footer::class -> {
                return VIEW_TYPE_FOOTER
            }
            Title::class -> {
                return VIEW_TYPE_TILTE
            }
            Content::class -> {
                return VIEW_TYPE_CONTENT
            }
        }
        return 0
    }

}