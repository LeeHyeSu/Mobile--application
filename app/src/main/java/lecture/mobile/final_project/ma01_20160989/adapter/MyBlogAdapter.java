package lecture.mobile.final_project.ma01_20160989.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20160989.R;
import lecture.mobile.final_project.ma01_20160989.model.BlogDto;

public class MyBlogAdapter extends BaseAdapter {

    public static final String TAG = "MyBlogAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<BlogDto> list;
    private MyBlogAdapter.ViewHolder viewHolder = null;

    public MyBlogAdapter(Context context, int resource, ArrayList<BlogDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BlogDto getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;

        if(view == null) {
            viewHolder = new MyBlogAdapter.ViewHolder();
            view = inflater.inflate(layout, parent, false);
            viewHolder.tvBlogTitle = (TextView)view.findViewById(R.id.tvBlogTitle);
            viewHolder.tvBlogDesc = (TextView)view.findViewById(R.id.tvBlogDesc);
            viewHolder.tvPostdate = (TextView)view.findViewById(R.id.tvPostdate);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyBlogAdapter.ViewHolder)view.getTag();
        }

        BlogDto dto = list.get(position);

        viewHolder.tvBlogTitle.setText(dto.getTitle());
        viewHolder.tvBlogDesc.setText(dto.getDescription());
        viewHolder.tvPostdate.setText(dto.getPostdate());

        return view;
    }

    public void setList(ArrayList<BlogDto> list) {
        this.list = list;
    }

    public void clear() {
        this.list = new ArrayList<BlogDto>();
    }

    static class ViewHolder {
        public TextView tvBlogTitle = null;
        public TextView tvBlogDesc = null;
        public TextView tvPostdate = null;
    }
}
