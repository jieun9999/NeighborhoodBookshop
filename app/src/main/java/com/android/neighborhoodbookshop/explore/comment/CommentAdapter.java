package com.android.neighborhoodbookshop.explore.comment;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.neighborhoodbookshop.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    //인터페이스 사용 순서 3. CommentAdapter 클래스에서 인터페이스의 인스턴스와 이를 설정할 메서드를 정의합니다.
    private ChatListener chatListener;
    private ArrayList<CommentItem> commentList;

    public CommentAdapter(ChatListener chatListener) {
        this.chatListener = chatListener;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        //아이템 내용 생성
        holder.onBind(commentList.get(position));

        // 아이템의 삭제 버튼을 누르면, 아이템이 사라짐
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //클릭된 아이템의 위치(position)을 가져옴
            int clickedPosition = holder.getAdapterPosition();
            if(clickedPosition != RecyclerView.NO_POSITION){
                //해당 위치의 아이템을 commentList에서 제거
                commentList.remove(clickedPosition);
                //데이터 셋 변경을 알림 (RecylerView 갱신)
                notifyItemRemoved(clickedPosition);
                // Call the interface method to update chat num
                chatListener.onChatNumUpdated(holder.itemView.getContext());
                // Call the interface method to update chat data
                chatListener.onChatDataUpdated(holder.itemView.getContext());
            }
            }
        });
        // 아이템의 시간(ex. 몇분전) 관리하기
        CommentItem commentItem = commentList.get(position);
        long timestamp = commentItem.getTimestamp();
        String formattedTime = getTimeAgo(timestamp);
        holder.time.setText(formattedTime);
    }

    // 아이템 읽어오기
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
        ImageView deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.imageView16);
            name = itemView.findViewById(R.id.textView24);
            location = itemView.findViewById(R.id.textView25);
            comment = itemView.findViewById(R.id.textView26);
            time = itemView.findViewById(R.id.textView27);
            deleteBtn = itemView.findViewById(R.id.imageView19);
        }

        void onBind(CommentItem item){
            Uri uri = Uri.parse(item.getUserImagePath());
            profile.setImageURI(uri);
            name.setText(item.getUserName());
            location.setText(item.getUserLocation());
            comment.setText(item.getComment());
        }
    }
    public String getTimeAgo(long timestamp){
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;

        // Define time intervals in milliseconds
        long minuteMillis = 60 * 1000;
        long hourMillis = 60 * minuteMillis;
        long dayMillis = 24 * hourMillis;
        long weekMillis = 7 * dayMillis;

        // Convert the time difference to a user-friendly format,
        // such as "X minutes ago," "X hours ago," or "X days ago," depending on the difference in time.

        if (timeDifference < minuteMillis) {
            return "방금";
        } else if (timeDifference < hourMillis) {
            long minutesAgo = timeDifference / minuteMillis;
            return minutesAgo + "분"+ " 전";
        } else if (timeDifference < dayMillis) {
            long hoursAgo = timeDifference / hourMillis;
            return hoursAgo + "시간"+" 전";
        } else if (timeDifference < weekMillis) {
            long daysAgo = timeDifference / dayMillis;
            return daysAgo + "일" + " 전";
        } else {
            // Format as a specific date if it's been more than a week
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return dateFormat.format(new Date(timestamp));
        }
    }
}
