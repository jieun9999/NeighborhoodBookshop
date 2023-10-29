package com.android.neighborhoodbookshop;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<CommentItem> commentList;
    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.onBind(commentList.get(position));
    }

    public void setCommentList(ArrayList<CommentItem> list){
        this.commentList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView name;
        TextView location;
        TextView comment;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.imageView16);
            name = itemView.findViewById(R.id.textView24);
            location = itemView.findViewById(R.id.textView25);
            comment = itemView.findViewById(R.id.textView26);
            time = itemView.findViewById(R.id.textView27);
        }

        void onBind(CommentItem item){
            Uri uri = Uri.parse(item.getUserImagePath());
            profile.setImageURI(uri);
            name.setText(item.getUserName());
            location.setText(item.getUserLocation());
            comment.setText(item.getComment());
            time.setText(item.getTime());
        }
    }
}
