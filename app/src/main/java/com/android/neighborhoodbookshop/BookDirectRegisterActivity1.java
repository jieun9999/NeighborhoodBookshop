package com.android.neighborhoodbookshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BookDirectRegisterActivity1 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 0; // 어떤 정수 값도 사용 가능

    //입력창 사진뷰
    private ImageView picture;
    //입력한 사진의 이미지 경로
    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_direct_register1);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false); //툴바 타이틀 안보이게

        //입력값
        picture = findViewById(R.id.imageView2); //onActivityResult의 결과로 picture.setImageBitmap(bitmap);
        TextInputEditText title = (TextInputEditText) findViewById(R.id.title);
        TextInputEditText writer = (TextInputEditText) findViewById(R.id.writer);
        TextInputEditText company = (TextInputEditText)findViewById(R.id.company);
        TextInputEditText date = (TextInputEditText) findViewById(R.id.date);
        TextInputEditText isbn = (TextInputEditText)findViewById(R.id.ISBN);

        //다음 액티비티로 이동하는 버튼
        Button nextBtn = findViewById(R.id.button2);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용자 입력값 변수에 저장
                String book_title = title.getText().toString();
                String book_writer = writer.getText().toString();
                String book_company = company.getText().toString();
                String book_date = date.getText().toString();
                String book_isbn = isbn.getText().toString();

                Intent showC = new Intent(BookDirectRegisterActivity1.this, BookDirectRigisterActivity2.class);
                //해당 데이터는 sp가 아니라 사용자 입력값이기 때문에 인텐트로 넘겨줘야 한다.
                showC.putExtra("Image", imagePath);
                showC.putExtra("BookTitle", book_title);
                showC.putExtra("BookWriter",book_writer);
                showC.putExtra("BookCompany", book_company);
                showC.putExtra("BookDate", book_date);
                showC.putExtra("BookIsbn", book_isbn);
                // 메서드를 호출하여 활동 C의 결과를 활동 A로 전달하도록 설정합니다.
                // 이 플래그를 사용하면 활동 B를 거치지 않고 직접 결과를 전달할 수 있습니다.
                showC.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                // 활동 B에서 활동 C를 호출하는 부분:
                startActivity(showC);
                //현재 활동 B를 종료합니다.
                finish();
            }
        });


        //다이얼로그에서 갤러리 열기 버튼 클릭
        final ImageView open_gallery = findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //onActivityResult는 갤러리 선택이 끝나고 나서 갤러리 액티비티가 종료된후 실행됨
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // 선택한 이미지를 ImageView에 설정
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                picture.setImageBitmap(bitmap);

                // 이미지를 파일로 저장
                imagePath = saveImageToFile(uri);

                // 이미지 파일의 경로를 저장
                // 이 imagePath를 필요한 곳에서 사용할 수 있습니다.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 이미지를 파일경로(String)으로 저장하는 메서드
    private String saveImageToFile(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File outputDir = getCacheDir();
        File outputFile = File.createTempFile("image", ".jpg", outputDir);

        OutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();

        return outputFile.getAbsolutePath(); // 파일 경로 반환
    }

}