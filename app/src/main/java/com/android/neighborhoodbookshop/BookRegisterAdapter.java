package com.android.neighborhoodbookshop;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class BookRegisterAdapter extends RecyclerView.Adapter<BookRegisterAdapter.ViewHolder> {
    private Context mContext;
    //관리할 어레이리스트 선언
    private ArrayList<ArrayList<String>> mArrayList;


    public BookRegisterAdapter(Context mContext, ArrayList<ArrayList<String>> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    //아이템 클릭 리스터 인터페이스
    //인터페이스란?: 일종의 계약(contract)으로, 해당 인터페이스를 구현한 클리스는
    //인터페이스에서 정의한 메서드를 반드시 구현해야 하며, 이를 통해 특정 동작을 보장합니다

    //주의! 뷰홀더가 아닌 상세내역 내부에다 수정버튼과 삭제버튼을 만들계획임
    // BookRegisterAdapter 클래스에서 클릭 이벤트 리스너를 설정하는 부분은 아이템을 클릭했을 때
    // onItemClickListener.onItemClick(view, position)를 호출하여 클릭한 아이템의 위치(position)와 뷰(view)를 전달하고 있습니다.
    interface OnItemClickListener{
        void onItemClick(View v, int position); //아이템 상세내역 확인
    }

    //아이템 클릭 이벤트를 처리할 리스터 객체를 저장하기 위한 멤버변수 ' onItemClickListener'를 선언하고 초기값으로 null을 설정한다
    // 객체  onItemClickListener란? : 리사이클러뷰의 각 아이템 클릭 이벤트를 처리하기 위한  OnItemClickListener 인터페이스에서 뽑은 객체

    private OnItemClickListener onItemClickListener  = null;

    //외부에서 아이템 클릭 리스너 객체를 설정하기 위한 메서드를 정의합니다.
    // 이 메서드를 통해 mListener 멤버변수에 클릭 리스너를 할당할 수 있습니다.
    public void setOnItemClickListener(BookRegisterAdapter.OnItemClickListener listener){
        this.onItemClickListener = (OnItemClickListener) listener;
    }


    //리스트의 각 항목을 이루는 디자인(xml)을 적용.
    @NonNull
    @Override
    public BookRegisterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bookitem,parent,false);
        //ViewHolder 클래스에서 view라는 인자를 받아서 vh객체를 만든다
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookRegisterAdapter.ViewHolder holder, int position) {
        //onBindViewHolder 메서드 내에서 SharedPreferences에서 데이터를 불러오는 것이 아닌,
        // 뷰홀더에서는 아이템의 위치(position)를 기반으로 데이터를 교체하고 뷰에 표시하는 역할을 합니다.
        // 그러므로 데이터를 불러오는 부분은 메인 액티비티나 해당 어댑터를 초기화할 때 처리되어야 합니다.

        //mArrayList는 json문자열을 원소로 갖는 arrayList
        mArrayList = MainActivity.userReviewList;

            //뷰홀더에서 각각 어떤 항목을 교체할지 적음
            ArrayList bookItem = mArrayList.get(position);
            String imagePath = bookItem.get(0).toString();
                // 문자열 이미지 파일 경로를 URI로 변환
                if(imagePath != null){
                    Uri imageUri = Uri.parse(imagePath);
                    // 이미지 뷰에 URI를 설정
                    holder.image.setImageURI(imageUri);
                }
                String bookTitle = bookItem.get(1).toString();
                String writer = bookItem.get(2).toString();
                holder.title.setText(bookTitle);
                holder.writer.setText(writer);
            }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    // RecyclerView의 ViewHolder 클래스를 정의하는 부분

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, writer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰홀더에서는 각 영역을 지정해줘야 한다
            this.image = itemView.findViewById(R.id.book_image);
            this.title = itemView.findViewById(R.id.book_title);
            this.writer = itemView.findViewById(R.id.book_writer);

            // 아이템뷰 클릭시 mListener객체의 onItemClick메소드 실행
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // getAdapterPosition()은 RecyclerView 내부에서 제공되는 메서드로, 클릭한 아이템의 위치를 얻어온다
                    if(position !=RecyclerView.NO_POSITION){
                        if(onItemClickListener!= null){
                            onItemClickListener.onItemClick(view, position);
                            //  onItemClickListener를 통해 아이템 클릭 이벤트를 감지함
                        }
                    }
                }
            });

        }
    }


}
