package lecture.mobile.final_project.ma01_20160989.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class MemoDBHelper extends SQLiteOpenHelper {

    private final static String TAG = "MemoDBHelper";

    private final static String DB_NAME ="memo_db";
    public final static String TABLE_NAME = "memo_table";
    public final static String ID = "_id";
    public final static String PATH = "path";
    public final static String TITLE = "title";
    public final static String MEMO = "memo";

    public MemoDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLE_NAME + " (" + ID + " integer primary key autoincrement, "
                + PATH + " text, " + TITLE + " text, " + MEMO + " text);";
        Log.d(TAG, sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
