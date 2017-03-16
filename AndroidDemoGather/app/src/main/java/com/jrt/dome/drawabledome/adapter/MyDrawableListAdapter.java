package com.jrt.dome.drawabledome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jrt.dome.drawabledome.R;
import com.jrt.dome.drawabledome.interf.OnRecyclerViewItemClickListener;

/**
 * Created by lingbug on 2016-12-29.
 */

public class MyDrawableListAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    private String[] mStrData;
    //条目点击事件的回调监听
    private OnRecyclerViewItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener itemClickListener) {
        this.mItemClickListener=itemClickListener;
    }
    public MyDrawableListAdapter(Context context,String[] data){
        this.mContext=context;
        this.mStrData=data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.rec_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_rec.setText(mStrData[position]);
        if (null != mItemClickListener) { //处理相应的点击操作
            holder.tv_rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();//获取当前布局的位置，避免点击条目错误的bug
                    mItemClickListener.onClickItem(holder.tv_rec,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mStrData==null?0:mStrData.length;
    }

}
 class MyViewHolder extends RecyclerView.ViewHolder{
    TextView tv_rec;
    public MyViewHolder(View itemView) {
        super(itemView);
        tv_rec= (TextView)itemView.findViewById(R.id.tv_rec);
    }
}