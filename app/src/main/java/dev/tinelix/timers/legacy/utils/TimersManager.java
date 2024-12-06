package dev.tinelix.timers.legacy.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.tinelix.timers.legacy.R;
import dev.tinelix.timers.legacy.activities.MainActivity;
import dev.tinelix.timers.legacy.list_items.TimerItem;

public class TimersManager {

    public static void deleteTimer(final Context ctx, ArrayList<TimerItem> timersList, int position) {
        final TimerItem timerItem = timersList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.delete_timer_title);
        builder.setMessage(R.string.delete_timer_question);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    String profile_path = "/data/data/" + ctx.getApplicationContext().getPackageName() +
                            "/shared_prefs/" + timerItem.name + ".xml";
                    File file = new File(profile_path);
                    file.delete();
                    if(ctx instanceof MainActivity) {
                        ((MainActivity) ctx).appendTimerItems();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static ArrayList<TimerItem> getTimersList(Context ctx) {
        String package_name = ctx.getApplicationContext().getPackageName();
        String profile_path = "/data/data/" + package_name + "/shared_prefs";
        File prefs_directory = new File(profile_path);
        File[] prefs_files = prefs_directory.listFiles();
        ArrayList<TimerItem> timersList = new ArrayList<>();
        List<Map<String, String>> timersArray = new ArrayList<>();
        String file_extension;
        for (File prefs_file : prefs_files) {
            int start_of_ext = (prefs_file.getName().length() - 4);
            String pref_name = prefs_file.getName().substring(0, (prefs_file.getName().length() - 4));
            if (!pref_name.startsWith(package_name + "_preferences")) {
                SharedPreferences prefs = ctx.getSharedPreferences(pref_name, 0);
                file_extension = prefs_file.getName().substring(start_of_ext);
                if (file_extension.contains(".xml") && file_extension.length() == 4) {
                    timersList.add(
                            new TimerItem(prefs_file.getName().substring(0, start_of_ext),
                                    prefs_file.getName().substring(0, start_of_ext),
                                    prefs.getString("timerAction", ""),
                                    prefs.getLong("timerActionDate", 0), "timer")
                    );
                    Map<String, String> timer_item = new HashMap<>(2);
                    timer_item.put("title", prefs_file.getName().substring(0, start_of_ext));
                    timer_item.put("subtitle",
                            ctx.getResources().getString(R.string.time_at,
                                    new SimpleDateFormat("d MMMM yyyy")
                                            .format(new Date(prefs.getLong("timerActionDate", 0))
                                            ),
                                    new SimpleDateFormat("HH:mm")
                                            .format(new Date(prefs.getLong("timerActionDate", 0)))
                            )
                    );
                    timersArray.add(timer_item);
                }
            }
        }
        return timersList;
    }

    public static long getDaysFromTimer(Context ctx, String timer_name) {
        SharedPreferences prefs = ctx.getSharedPreferences(
                timer_name, 0
        );
        long diff = (new Date().getTime() - new Date(prefs.getLong("timerActionDate", 0)).getTime());
        long elapsed_sec = (int) (diff / 1000);
        long remaining_sec = (int) (-diff / 1000);
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
            if (prefs.getString("timerAction", "").equals("calculateRemainingTime")) {
                if (remaining_sec > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(timer_date);
                    return remaining_days;
                } else {
                    return 0;
                }
            } else {
                return elapsed_sec > 0 ? elapsed_days : 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
