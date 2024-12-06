package dev.tinelix.timers.legacy.list_adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dev.tinelix.timers.legacy.R;
import dev.tinelix.timers.legacy.activities.MainActivity;
import dev.tinelix.timers.legacy.list_items.TimerItem;
import dev.tinelix.timers.legacy.utils.TimersManager;

public class TimersListAdapter extends BaseAdapter {

    private ArrayList<TimerItem> items = new ArrayList<>();
    LayoutInflater inflater;
    private Context ctx;

    public TimersListAdapter(Context context, ArrayList<TimerItem> array) {
        ctx = context;
        items = array;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public TimerItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.layout_timer_item, parent, false);
        }
        Holder holder = new Holder(view);
        holder.bind(position);

        return view;
    }

    public class Holder {

        public final TextView item_title;
        public final TextView item_subtitle;
        public final TextView item_time_counter;
        public final ImageView item_icon;
        public final ImageButton edit_button;
        public final ImageButton delete_button;
        public final View convertView;

        public Holder(View view) {
            item_title = (TextView) view.findViewById(R.id.item_name);
            item_subtitle = (TextView) view.findViewById(R.id.item_subtitle);
            item_time_counter = (TextView) view.findViewById(R.id.remaining_time);
            item_icon = (ImageView) view.findViewById(R.id.icon);
            edit_button = (ImageButton) view.findViewById(R.id.edit_button);
            delete_button = (ImageButton) view.findViewById(R.id.delete_button);
            convertView = view;
        }

        void bind(final int position) {
            TimerItem item = getItem(position);
            item_title.setText(item.name);
            item_subtitle.setText(ctx.getResources().getString(R.string.time_at, new SimpleDateFormat("d MMMM yyyy").format(new Date(item.actionDate)),
                    new SimpleDateFormat("HH:mm").format(new Date(item.actionDate))));
            long diff = (new Date().getTime() - new Date(item.actionDate).getTime());
            long elapsed_sec = (int) (diff / 1000);
            long remaining_sec = (int) (-diff / 1000);
            updateTimeUI(item, diff, elapsed_sec, remaining_sec);

            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Tinelix Timers", "Clicked edit button!");
                    if(ctx.getClass().getSimpleName().equals("MainActivity")) {
                        ((MainActivity) ctx).showTimerEditorDialog(position);
                    }
                }
            });
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ctx.getClass().getSimpleName().equals("MainActivity")) {
                        TimersManager.deleteTimer(ctx, items, position);
                    }
                }
            });
        }

        private void updateTimeUI(final TimerItem item, long diff, long elapsed_sec, final long remaining_sec) {
            try {
                long elapsed_days = 0;
                long remaining_days = 0;
                elapsed_days = (int) (diff / (1000 * 60 * 60 * 24));
                remaining_days = (int) (-diff / (1000 * 60 * 60 * 24));
                Date timer_date = new Date();
                if (diff >= 0) {
                    timer_date = new Date(diff + 61200000);
                } else {
                    timer_date = new Date(-diff + 61200000);
                }
                if (item.action.equals("calculateRemainingTime")) {
                    if (remaining_sec > 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timer_date);
                        item_time_counter.setText(ctx.getResources().getString(R.string.days_counter_remaining, remaining_days, calendar.get(Calendar.HOUR_OF_DAY), timer_date.getMinutes(), timer_date.getSeconds()));
                    } else {
                        item_time_counter.setText(ctx.getResources().getString(R.string.days_counter_over));
                    }
                } else {
                    if (elapsed_sec > 0) {
                        item_time_counter.setText(ctx.getResources().getString(R.string.days_counter_elapsed, elapsed_days, timer_date.getHours(), timer_date.getMinutes(), timer_date.getSeconds()));
                    } else {
                        item_time_counter.setText(ctx.getResources().getString(R.string.days_counter_over));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
