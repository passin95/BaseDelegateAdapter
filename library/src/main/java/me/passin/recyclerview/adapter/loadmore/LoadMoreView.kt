package me.passin.recyclerview.adapter.loadmore

import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull

/**
 * @author: zbb
 * @date: 2020/10/6 21:28
 * @desc:
 */
abstract class LoadMoreView {

    companion object {

        const val STATUS_DEFAULT = 1
        const val STATUS_LOADING = 2
        const val STATUS_FAIL = 3
        const val STATUS_END = 4
        const val STATUS_END_GONE = 5

    }

    var loadMoreStatus: Int = STATUS_DEFAULT

    @NonNull
    abstract fun getRootView(parent: ViewGroup): View

    abstract fun getLoadingView(holder: LoadMoreDelegate.ViewHolder): View?

    abstract fun getLoadEndView(holder: LoadMoreDelegate.ViewHolder): View?

    abstract fun getLoadFailView(holder: LoadMoreDelegate.ViewHolder): View?

    open fun onBindViewHolder(holder: LoadMoreDelegate.ViewHolder) {
        when (loadMoreStatus) {
            STATUS_LOADING -> {
                getLoadingView(holder)?.visibility = View.VISIBLE
                getLoadFailView(holder)?.visibility = View.GONE
                getLoadEndView(holder)?.visibility = View.GONE
            }
            STATUS_FAIL -> {
                getLoadingView(holder)?.visibility = View.GONE
                getLoadFailView(holder)?.visibility = View.VISIBLE
                getLoadEndView(holder)?.visibility = View.GONE
            }
            STATUS_END -> {
                getLoadingView(holder)?.visibility = View.GONE
                getLoadFailView(holder)?.visibility = View.GONE
                getLoadEndView(holder)?.visibility = View.VISIBLE
            }
            STATUS_DEFAULT, STATUS_END_GONE -> {
                getLoadingView(holder)?.visibility = View.GONE
                getLoadFailView(holder)?.visibility = View.GONE
                getLoadEndView(holder)?.visibility = View.GONE
            }
        }
    }

}