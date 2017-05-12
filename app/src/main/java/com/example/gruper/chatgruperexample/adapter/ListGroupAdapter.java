package com.example.gruper.chatgruperexample.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gruper.chatgruperexample.R;
import com.example.gruper.chatgruperexample.databinding.ListGroupBinding;
import com.example.gruper.chatgruperexample.model.ChatRoom;


import java.util.List;

/**
 * Created by gruper on 10/05/17.
 */

public class ListGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    List<ChatRoom> items;
    private OnItemClickListener mItemClickListener;


    public ListGroupAdapter(Context context, List<ChatRoom> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_group, parent, false);
        return new HolderListGroup(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindListUser((HolderListGroup) holder, position);
    }

    @Override
        public int getItemCount() {
        return items.size();
    }

    //==================================================================//


    private class HolderListGroup extends RecyclerView.ViewHolder implements View.OnClickListener   {
        ListGroupBinding binding;

        public HolderListGroup(ListGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.userLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    private void bindListUser(final HolderListGroup holder, final int position) {
        final ChatRoom item = items.get(position);
        holder.binding.setModel(item);

        Log.d("LISTUSER 2","LISTUSER 2: "+ item.getRoomName().toString());

        holder.binding.groupName.setText(item.getRoomName());
    }

    public List<ChatRoom> getAllItem() {
        return items;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
