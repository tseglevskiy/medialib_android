package ru.roscha_akademii.medialib.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

    public abstract class CommonListAdapter<D, B extends ViewDataBinding>
        extends RecyclerView.Adapter<CommonListAdapter.CommonHolder<B>>
{
    List<D> list;
    private int layoutId;
    private OnItemClickListener clickListener;

    public CommonListAdapter(int layoutId,
                             OnItemClickListener clickListener) {
        this.layoutId = layoutId;
        this.clickListener = clickListener;
    }

    public void setList(List<D> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                layoutId,
                parent,
                false
        );

        return new CommonHolder(binding);
    }

    @Override
    public void onBindViewHolder(CommonHolder<B> holder, int position) {
        show(list.get(position), holder.binding, clickListener);
    }

    public abstract void show(D item, B binding, OnItemClickListener clickListener);

    static class CommonHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private B binding;

        CommonHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public B getBinding() {
            return binding;
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(long id);
    }
}
