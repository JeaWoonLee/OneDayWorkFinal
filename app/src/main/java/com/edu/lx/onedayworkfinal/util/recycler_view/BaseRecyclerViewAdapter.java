package com.edu.lx.onedayworkfinal.util.recycler_view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    public ArrayList<T> items;
    public Context context;

    public BaseRecyclerViewAdapter (Context context) {
        this.context = context;
    }

    public View inflateView(Context context, @LayoutRes int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutRes,parent,false);
    }

    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i);

    @Override
    public void onBindViewHolder (@NonNull BaseViewHolder baseViewHodler, int i) {
        baseViewHodler.setItem(items.get(i));
    }

    @Override
    public int getItemCount () {
        return items.size();
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }
}
