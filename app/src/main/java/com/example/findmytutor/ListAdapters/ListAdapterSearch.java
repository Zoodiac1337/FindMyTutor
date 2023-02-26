package com.example.findmytutor.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findmytutor.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListAdapterSearch extends BaseAdapter {
    Context context;
    private final String [] name;
    private final String [] availability;

    public ListAdapterSearch(Context context, String [] names, String [] availabilities){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.name = names;
        this.availability = availabilities;
    }

    @Override
    public int getCount() {
        return name.length;
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
    public View getView(int position, View convertView,  ViewGroup parent) {

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_list_item_search, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.imageAvailability = (ImageView) convertView.findViewById(R.id.availability_imageView);
            viewHolder.imageAvatar = (ImageView) convertView.findViewById(R.id.avatar_imageView);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        viewHolder.txtName.setText(name[position]);
        viewHolder.imageAvatar.setImageResource(R.drawable.baseline_person_24);
        if (availability[position].equals("Available"))
            viewHolder.imageAvailability.setImageResource(R.drawable.baseline_event_available_24);
        else if (availability[position].equals("Tentative"))
            viewHolder.imageAvailability.setImageResource(R.drawable.baseline_event_24);
        else
            viewHolder.imageAvailability.setImageResource(R.drawable.baseline_event_busy_24);

        return convertView;
    }

    private static class ViewHolder {

        ImageView imageAvatar;
        TextView txtName;
        ImageView imageAvailability;
    }
}