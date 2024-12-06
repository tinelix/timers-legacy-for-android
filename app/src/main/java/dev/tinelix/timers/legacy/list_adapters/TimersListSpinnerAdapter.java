package dev.tinelix.timers.legacy.list_adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.tinelix.timers.legacy.R;
import dev.tinelix.timers.legacy.list_items.SimpleListItem;
import dev.tinelix.timers.legacy.list_items.TimerItem;

public class TimersListSpinnerAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater inflater;
    ArrayList<TimerItem> objects;

    public int textColor;
    public String from;
    public TimersListSpinnerAdapter(Context context, ArrayList<TimerItem> items) {
        ctx = context;
        objects = items;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TimerItem getListItem(int position) {
        return ((TimerItem) getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.simple_spinner_item, parent, false);
            TimerItem item = getListItem(position);
            TextView item_name = (TextView) view.findViewById(R.id.item_title);
            item_name.setText(item.name);
        }
        return view;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        View view;
        view = convertView;
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.simple_spinner_item, null);// layout for spinner list
        }
        TimerItem item = getListItem(position);
        ((TextView) view.findViewById(R.id.item_title)).setText(item.name);
        ((TextView) view.findViewById(R.id.item_title)).setSingleLine(true);

        return view;
    }

    public class ViewHolder {
        public TextView item_name;
    }
}
