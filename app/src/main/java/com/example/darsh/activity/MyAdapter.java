package com.example.darsh.activity;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.darsh.view.SquareImageView;

import java.util.ArrayList;

/**
 * Created by darshan on 19/4/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<String> datas = null;

    public MyAdapter(ArrayList<String> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        SquareImageView squareImageView = new SquareImageView(viewGroup.getContext());
        squareImageView.setBackgroundColor(Color.RED);
        ViewHolder vh = new ViewHolder(squareImageView);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SquareImageView squareImageView;
        public ViewHolder(View view){
            super(view);
            squareImageView = (SquareImageView) view;
        }
    }
}
