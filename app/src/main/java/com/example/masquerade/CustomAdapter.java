package com.example.masquerade;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by arthonsystechnologiesllp on 10/03/17.
 */

public class CustomAdapter extends BaseAdapter {

    Activity activity;
    List<Tag> tags;
    LayoutInflater inflater;

    //short to create constructer using command+n for mac & Alt+Insert for window


    public CustomAdapter(Activity activity) {
        this.activity = activity;
    }

    public CustomAdapter(Activity activity, List<Tag> tags) {
        this.activity   = activity;
        this.tags      = tags;

        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.listview_tag, viewGroup, false);

            holder = new ViewHolder();

            holder.tvTagName = (TextView)view.findViewById(R.id.tv_tag_name);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        Tag model = tags.get(i);

        holder.tvTagName.setText(model.getTagName());

        if (model.isSelected()) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);
        }
        else {
            holder.ivCheckBox.setBackgroundResource(R.color.fui_transparent);
            holder.ivCheckBox.setVisibility(View.INVISIBLE);
        }
        return view;

    }

    public void updateRecords(List<Tag> tags){
        this.tags = tags;

        notifyDataSetChanged();
    }

    class ViewHolder{

        TextView tvTagName;
        ImageView ivCheckBox;

    }
}