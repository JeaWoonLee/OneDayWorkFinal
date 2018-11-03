package com.edu.lx.onedayworkfinal.util.recycler_view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder (@NonNull View itemView) {
        super(itemView);
    }

    public abstract void  setItem(T t);
}
