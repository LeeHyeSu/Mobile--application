package lecture.mobile.final_project.ma01_20160989.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import lecture.mobile.final_project.ma01_20160989.helper.BakeryDBHelper;
import lecture.mobile.final_project.ma01_20160989.R;
import lecture.mobile.final_project.ma01_20160989.fragment.BookmarkFrag;

public class MyCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;
    Cursor cursor;
    int layout;

    public MyCursorAdapter(BookmarkFrag context, int layout, Cursor c, LayoutInflater inflater) {
        super(context.getContext(), c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.layout = layout;
        cursor = c;
        this.inflater = inflater;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBakeryTitle = (TextView)view.findViewById(R.id.tvBakeryTitle);
        TextView tvBakeryTel = (TextView)view.findViewById(R.id.tvBakeryTel);
        TextView tvBakeryAddr = (TextView)view.findViewById(R.id.tvBakeryAddress);
        tvBakeryTitle.setText(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_TITLE)));
        tvBakeryTel.setText(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_TELEPHONE)));
        tvBakeryAddr.setText(cursor.getString(cursor.getColumnIndex(BakeryDBHelper.COL_ADDRESS)));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View listItemLayout = inflater.inflate(layout, parent, false);
        return listItemLayout;
    }
}
