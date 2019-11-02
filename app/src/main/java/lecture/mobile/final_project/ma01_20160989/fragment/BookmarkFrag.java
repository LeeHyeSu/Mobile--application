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
import android.widget.ListView;

import java.io.Serializable;

import lecture.mobile.final_project.ma01_20160989.helper.BakeryDBHelper;
import lecture.mobile.final_project.ma01_20160989.ViewDetailsActivity;
import lecture.mobile.final_project.ma01_20160989.adapter.MyCursorAdapter;
import lecture.mobile.final_project.ma01_20160989.R;
import lecture.mobile.final_project.ma01_20160989.model.BakeryDto;

public class BookmarkFrag extends Fragment {

    ListView lvList = null;
    BakeryDBHelper helper;
    Cursor cursor;
    MyCursorAdapter myAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmark, null);
        lvList = (ListView)view.findViewById(R.id.lvList);

        helper = new BakeryDBHelper(this.getContext());
        myAdapter = new MyCursorAdapter(this, R.layout.listview_bakery_layout, null, inflater);
        lvList.setAdapter(myAdapter);

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                        db.execSQL("DELETE FROM " + BakeryDBHelper.TABLE_NAME + " WHERE _id = " + position + ";");
                        helper.close();

                        onResume();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
                return true;
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                SQLiteDatabase db = helper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + BakeryDBHelper.TABLE_NAME + " WHERE _id = " + id + ";", null);
                cursor.moveToFirst();
                BakeryDto item = new BakeryDto();
                item.set_id(cursor.getInt(cursor.getColumnIndex(BakeryDBHelper.COL_ID)));
                item.setTitle(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_TITLE)));
                item.setLink(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_LINK)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_DESCRIPTION)));
                item.setTelephone(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_TELEPHONE)));
                item.setAddress(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_ADDRESS)));

                Intent intent = new Intent(getActivity(), ViewDetailsActivity.class);
                intent.putExtra("bakeryDto", (Serializable) item);

                startActivity(intent);
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
//        DB에서 데이터를 읽어와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + BakeryDBHelper.TABLE_NAME, null);

        myAdapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        cursor 사용 종료
        if (cursor != null) cursor.close();
    }
}
