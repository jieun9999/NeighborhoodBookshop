package com.android.neighborhoodbookshop;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookReviewListAdapter extends RecyclerView.Adapter<BookReviewListAdapter.ViewHolder> {

    private ArrayList<BookReviewItem> mArrayList;

    @NonNull
    @Override
    public BookReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookReviewListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(mArrayList.get(position));
        holder.userLocation.setSelected(true);
        // 리사이클러뷰의 전체적인 사항은 메인액티비티에서 다루고, 각 아이템의 뷰홀더에 대한 내용은 어댑터에서 다룹니다.
        // 메인액티비티에서는 리사이클러뷰를 초기화하고 어댑터를 설정하는 등의 작업을 수행하며,
        // 어댑터에서는 각 아이템의 뷰를 생성하고 데이터를 바인딩하는 등의 작업을 수행

        //아이템 클릭시 다른 액티비티로 이동
        //The easiest way to attach a clickListener to items of RecyclerView is within the Adapter as below:
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ExploreBookReviewActivity.class);
                intent.putExtra("position", position); //아이템의 인덱스를 전달함
                intent.putExtra("userName", holder.userName.getText().toString()); //아이템의 인덱스를 전달함
                view.getContext().startActivity(intent);
            }
        });
    }

    public void setArrayList(ArrayList<BookReviewItem> list){
        this.mArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bookImage;
        TextView bookName;
        TextView bookWriter;
        ImageView image;
        TextView userName;
        TextView userLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.bookImage);
            bookName = itemView.findViewById(R.id.bookname);
            bookWriter = itemView.findViewById(R.id.bookWriter);
            image = itemView.findViewById(R.id.image__);
            userName = itemView.findViewById(R.id.user_name);
            userLocation = itemView.findViewById(R.id.user_location);
        }

        void onBind(BookReviewItem item){
            Uri uri = Uri.parse(item.getBook_imagePath());
            bookImage.setImageURI(uri);
            bookName.setText(item.getBookName());
            bookWriter.setText(item.getWriter());
            Uri uri2 = Uri.parse(item.getProfile_imagePath());
            image.setImageURI(uri2);
            userName.setText(item.getProfile_name());
            userLocation.setText(item.getProfile_location().substring(5));
        }

    }
}
