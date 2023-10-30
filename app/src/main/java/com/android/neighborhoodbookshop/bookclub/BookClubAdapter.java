package com.android.neighborhoodbookshop.bookclub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.neighborhoodbookshop.R;

import java.util.ArrayList;

public class BookClubAdapter extends RecyclerView.Adapter<BookClubAdapter.ViewHolder> {

    public Context mContext;
    public ArrayList<BookClubItem> mArrayList; // 데이터 담을 어레이리스트

    public BookClubAdapter(Context mContext, ArrayList<BookClubItem> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    //아이템 클릭 리스너 인터페이스
    // 인터페이스란?: 일종의 계약(contract)으로, 해당 인터페이스를 구현한 클래스는
    // 인터페이스에서 정의한 메서드를 반드시 구현해야 하며, 이를 통해 특정동작을 보장합니다
    public interface OnItemClickListener{
        void onItemClick(View v, int position);// 상세화면 보기
        void onEditClick(View v, int position); //수정
        void onDeleteClick(View v, int position); //삭제
    }

    // 아이템 클릭 이벤트를 처리할 리스너 객체를 저장하기 위한 멤버변수 'mListener'를 선언하고 초기값으로 null을 설정한다
    // 객체 mListener란? : 리사이클러뷰의 각 아이템 클릭 이벤트를 처리하기 위한 OnItemClickListener 인터페이스에서 뽑은 객체이다
    // 위의 인터페이스 OnItemClickListener에서 뽑은 객체인 mListener
    public OnItemClickListener mListener = null;
    //외부에서 아이템 클릭 리스너 객체를 설정하기 위한 메서드를 정의합니다.
    // 이 메서드를 통해 mListener 멤버변수에 클릭 리스너를 할당할 수 있습니다.
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }



    //리스트의 각 항목을 이루는 디자인(xml)을 적용.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_bookclub_card, parent, false);
        //ViewHolder 클래스에서 view라는 인자를 받아서 vh객체를 만든다
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //mArrayList 중에서 해당 위치에 있는 데이터를 가져와서 BookClubItem 클래스의 객체로 저장
        BookClubItem bookClubItem = mArrayList.get(position);

        //뷰홀더에서 각각 어떤 항목을 교체할지 적음
        // 이미지 파일 경로를 문자열로 저장한 후
        String imagePath = bookClubItem.getBookclub_image();
        // 문자열 이미지 파일 경로를 URI로 변환
        Uri imageUri = Uri.parse(imagePath);
        // 이미지 뷰에 URI를 설정
        holder.image.setImageURI(imageUri);
        holder.title.setText(bookClubItem.getBookclub_name());
        holder.online.setText(bookClubItem.getBookclub_online());
        holder.introduce.setText(bookClubItem.getBookclub_introduce());
        //카테고리는 R.drawable 에서 int 형태로 받아온다
        holder.category1.setImageResource(bookClubItem.getBookclub_category1());
        holder.category2.setImageResource(bookClubItem.getBookclub_category2());

    }

    @Override
    public int getItemCount() {
        return  mArrayList.size();
    }

// recyclerView의 ViewHolder 클래스를 정의하는 부분
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, online, introduce;
        ImageView category1,category2;
        ImageView btn_edit, btn_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //뷰홀더에서는 각 영역을 설정헤줘야 한다
            this.image = itemView.findViewById(R.id.image);
            this.title = itemView.findViewById(R.id.title);
            this.online = itemView.findViewById(R.id.online);
            this.introduce = itemView.findViewById(R.id.introduce);
            this.category1 = itemView.findViewById(R.id.category1);
            this.category2 = itemView.findViewById(R.id.category2);
            this.btn_edit = itemView.findViewById(R.id.edit);
            this.btn_remove = itemView.findViewById(R.id.remove);

            //아이템뷰를 클릭할시 mListener객체의 onItemClick메소드 실행
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // getAdapterPosition()은 RecyclerView 내부에서 제공되는 메서드로, 클릭한 아이템의 위치를 얻어온다
                    if(position !=RecyclerView.NO_POSITION){
                        if(mListener!= null){
                            mListener.onItemClick(view, position);
                        }
                    }
                }
            });

            // 수정버튼 클릭시 mListener객체의 onEditClick메소드 실행
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // getAdapterPosition()은 RecyclerView 내부에서 제공되는 메서드로, 클릭한 아이템의 위치를 얻어온다
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onEditClick(view, position);
                            //메인액티비티에서 mListener.onEditClick의 구체적인 코드를 적는다
                        }
                    }
                }
            });

            //삭제버튼 클릭시 mListener객체의 onDeleteClick메소드 실행
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // getAdapterPosition()은 RecyclerView 내부에서 제공되는 메서드로, 클릭한 아이템의 위치를 얻어온다
                    if(position !=RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onDeleteClick(view,position);
                            //메인액티비티에서 mListener.onDeleteClick의 구체적인 코드를 적는다
                        }
                    }
                }
            });

        }
    }
}
