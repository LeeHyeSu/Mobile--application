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
import lecture.mobile.final_project.ma01_20160989.fragment.BakerySearchFrag;
import lecture.mobile.final_project.ma01_20160989.model.BakeryDto;

public class MyBakeryAdapter extends BaseAdapter {

    public static final String TAG = "MyBakeryAdapter";

    private LayoutInflater inflater;
    private BakerySearchFrag context;
    private int layout;
    private ArrayList<BakeryDto> list;
    private ViewHolder viewHolder = null;

    public MyBakeryAdapter(BakerySearchFrag context, int resource, ArrayList<BakeryDto> list, LayoutInflater inflater) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BakeryDto getItem(int position) {
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
            viewHolder = new ViewHolder();
            view = inflater.inflate(layout, parent, false);
            viewHolder.tvBakeryTitle = (TextView)view.findViewById(R.id.tvBakeryTitle);
            viewHolder.tvBakeryAddress = (TextView)view.findViewById(R.id.tvBakeryAddress);
            viewHolder.tvBakeryTel = (TextView)view.findViewById(R.id.tvBakeryTel);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        BakeryDto dto = list.get(position);

        viewHolder.tvBakeryTitle.setText(dto.getTitle());
        viewHolder.tvBakeryAddress.setText(dto.getAddress());
        viewHolder.tvBakeryTel.setText(dto.getTelephone());

        return view;
    }

    public void setList(ArrayList<BakeryDto> list) {
        this.list = list;
    }

    public void clear() {
        this.list = new ArrayList<BakeryDto>();
    }

    static class ViewHolder {
        public TextView tvBakeryTitle = null;
        public TextView tvBakeryAddress = null;
        public TextView tvBakeryTel= null;
    }
}
