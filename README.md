# BaseDelegateAdapter

高性能、易维护、易拓展地处理 RecyclerView multiple item types。

## 使用方式

（1）注册 ItemViewType 代理类。

```
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
```

（2）处理代理类的逻辑。

```
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

    override fun setOnItemChildClick(v: View, position: Int) {
        // 可以在代理对象内部处理点击逻辑，也可以在代理类外部监听处理（setOnItemChildClickListener），能做到灵活处理。
        if (v.id == R.id.tv_desc) {
            Toast.makeText(v.context, "文字被点击了，position：$position", Toast.LENGTH_SHORT).show()
            return
        }
        super.setOnItemChildClick(v, position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage = itemView.findViewById<ImageView>(R.id.iv_image)!!
        val tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)!!
    }

}
```

（3）代理类拥有 Adapter 的所有回调。

```java
abstract class BaseItemViewDelegate<T, VH : ViewHolder> {
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
```

## 安装

```gradle
// 基于 Android X。
implementation 'me.passin:basedelegateadapter:1.0.0'
```


## License

    Copyright (C) 2020 Passin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
