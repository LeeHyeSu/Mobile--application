package lecture.mobile.final_project.ma01_20160989;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import lecture.mobile.final_project.ma01_20160989.helper.MemoDBHelper;

public class ShowMemoActivity extends AppCompatActivity {

    final static String TAG = "ShowMemoActivity";

    MemoDBHelper helper;
    Cursor cursor;
    ImageView ivPhoto;
    TextView tvMemoTitle;
    TextView tvMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memo);
        setTitle("메모");

//        MainActivity 에서 전달 받은 _id 값을 사용하여 DB 레코드를 가져온 후 ImageView 와 TextView 설정
        helper = new MemoDBHelper(this);

        ivPhoto = findViewById(R.id.ivPhoto);
        tvMemoTitle = findViewById(R.id.tvMemoTitle);
        tvMemo = findViewById(R.id.tvMemo);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);

        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + MemoDBHelper.TABLE_NAME + " WHERE _id = " + id + ";", null);
        cursor.moveToFirst();

        // 이미지 파일의 경로를 사용하여 외부저장소에 저장한 이미지 표시
        File imgFile = new File(cursor.getString(cursor.getColumnIndex(MemoDBHelper.PATH)));
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivPhoto.setImageBitmap(myBitmap);
        }

        tvMemoTitle.setText(cursor.getString(cursor.getColumnIndex(MemoDBHelper.TITLE)));
        tvMemo.setText(cursor.getString(cursor.getColumnIndex(MemoDBHelper.MEMO)));

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
