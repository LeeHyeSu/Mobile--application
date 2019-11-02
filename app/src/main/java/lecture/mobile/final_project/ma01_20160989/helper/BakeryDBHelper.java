package lecture.mobile.final_project.ma01_20160989.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lecture.mobile.final_project.ma01_20160989.fragment.BookmarkFrag;

public class BakeryDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "bakery_db";
    public final static String TABLE_NAME = "bakery_table";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_LINK = "link";
    public final static String COL_DESCRIPTION =  "description";
    public final static String COL_TELEPHONE = "telephone";
    public final static String COL_ADDRESS = "address";

    public BakeryDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " ( _id integer primary key autoincrement, "
                + COL_TITLE + " TEXT, " + COL_LINK + " TEXT, " + COL_DESCRIPTION + " TEXT, "
                + COL_TELEPHONE + " TEXT, " + COL_ADDRESS  + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
