package lecture.mobile.final_project.ma01_20160989.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.File;

import lecture.mobile.final_project.ma01_20160989.AddMemoActivity;
import lecture.mobile.final_project.ma01_20160989.R;
import lecture.mobile.final_project.ma01_20160989.ShowMemoActivity;
import lecture.mobile.final_project.ma01_20160989.helper.MemoDBHelper;

import static android.app.Activity.RESULT_CANCELED;

public class MemoFrag extends Fragment {

    private static final int ADD_MEMO_CODE = 1000;

    SimpleCursorAdapter memoAdapter;
    Cursor cursor;
    MemoDBHelper helper;
    ListView lvMemo;
    Button btnAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, null);

        helper = new MemoDBHelper(getContext());

//        어댑터에 SimpleCursorAdapter 연결 (메모에 포함한 사진의 경로를 표시)
        memoAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2, cursor, new String[] {"title", "memo"}, new int[] {android.R.id.text1, android.R.id.text2});

        btnAdd = (Button)view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAdd:
//                       AddMemoActivity 호출
//               (사진 추가 후 취소를 누를 경우 저장한 사진을 삭제하기 위해 startActivityForResult() 로 작성)
                        Intent intent = new Intent(getActivity(), AddMemoActivity.class);
                        startActivityForResult(intent, ADD_MEMO_CODE);
                        break;
                }
            }
        });

        lvMemo = (ListView)view.findViewById(R.id.lvMemo);
        lvMemo.setAdapter(memoAdapter);

        lvMemo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                final long position = id;

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("삭제");
                builder.setMessage("정말로 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("DELETE FROM " + MemoDBHelper.TABLE_NAME + " WHERE _id = " + position + ";");
                        helper.close();

                        onResume();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
                return true;
            }
        });

        lvMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long _id) {
//                ShowMemoActivity 호출
                Intent intent = new Intent(getActivity(), ShowMemoActivity.class);
                intent.putExtra("id", _id);
                startActivity(intent);
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* AddMemoActivity 에서 사진 추가 후 취소를 눌러 메모를 저장안 할 경우
         * AddMemoActivity 에서 받은 Intent 에 들어있는 파일 경로를 이용해
         * 저장한 사진을 저장소에서 삭제
         */
        if(requestCode == ADD_MEMO_CODE && resultCode == RESULT_CANCELED && data != null) {
            File imgFile = new File(data.getStringExtra("path"));
            imgFile.delete();
        }
    }

    public void onResume() {
        super.onResume();
//        DB 에서 모든 레코드를 가져와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + MemoDBHelper.TABLE_NAME, null);

        memoAdapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }
}
