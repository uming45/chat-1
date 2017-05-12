package com.example.gruper.chatgruperexample.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gruper.chatgruperexample.R;
import com.example.gruper.chatgruperexample.databinding.ListUserBinding;
import com.example.gruper.chatgruperexample.model.User;

import java.util.List;

/**
 * Created by gruper on 09/05/17.
 */

public class ListUserAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    List<User> items;
    private OnItemClickListener mItemClickListener;


    public ListUserAdapter(Context context, List<User> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_user, parent, false);
        return new HolderListUsert(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindListUser((HolderListUsert) holder, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //==================================================================//


    private class HolderListUsert extends RecyclerView.ViewHolder implements View.OnClickListener   {
        ListUserBinding binding;

        public HolderListUsert(ListUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.userLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
//            FragmentManager manager = ((Activity) mContext).getFragmentManager();
//            manager.onItemClick(v, getAdapterPosition());
        }
    }

    private void bindListUser(final HolderListUsert holder, final int position) {
        final User item = items.get(position);
        holder.binding.setModel(item);

        Log.d("LISTUSER 2","LISTUSER 2: "+ item.getUsername().toString());

        holder.binding.userName.setText(item.getUsername());
    }

    public List<User> getAllItem() {
        return items;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
