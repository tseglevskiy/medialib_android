package ru.roscha_akademii.medialib.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class CommonListAdapter<D, B : ViewDataBinding>(private val layoutId: Int,
                                                         private val clickListener: CommonListAdapter.OnItemClickListener)
: RecyclerView.Adapter<CommonListAdapter.CommonHolder<B>>() {
    var list: List<D>? = null
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonHolder<B> {
        val binding = DataBindingUtil.inflate<B>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false)

        return CommonHolder(binding)
    }

    override fun onBindViewHolder(holder: CommonHolder<B>, position: Int) {
        show(list!![position], holder.binding, clickListener)
    }

    abstract fun show(item: D, binding: B, clickListener: OnItemClickListener)

    class CommonHolder<out B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClicked(id: Long)
    }
}
