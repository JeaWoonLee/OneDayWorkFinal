package com.edu.lx.onedayworkfinal.util.recycler_view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("ALL")
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private ArrayList<T> items;
    public Context context;

    public BaseRecyclerViewAdapter (Context context) {
        this.context = context;
    }

    protected View inflateView(Context context, @LayoutRes int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutRes,parent,false);
    }

    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i);

    @Override
    public void onBindViewHolder (@NonNull BaseViewHolder baseViewHolder, int i) {
        Objects.requireNonNull(baseViewHolder).setItem(items.get(i));
    }

    @Override
    public int getItemCount () {
        if (null == items) {
            return 0;
        }
        return items.size();
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }
}
