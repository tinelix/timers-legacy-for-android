package dev.tinelix.timers.legacy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import dev.tinelix.timers.legacy.R;
import dev.tinelix.timers.legacy.list_adapters.TimersListAdapter;
import dev.tinelix.timers.legacy.list_items.TimerItem;


public class MainActivity extends Activity {

    public List<Map<String, String>> timersArray = new ArrayList<Map<String, String>>();
    public ArrayList<TimerItem> timersList = new ArrayList<TimerItem>();
    public Timer timer;
    public Handler handler;
    public Runnable updateTimerUI;
    public TimersListAdapter timersListAdapter;
    public ListView timersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button create_timer_btn = (Button) findViewById(R.id.create_timer_btn);

        create_timer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEnterDialog("create_timer");
            }
        });

        handler = new Handler();
        final ListView timersListView = (ListView) findViewById(R.id.timers_list);
        updateTimerUI = new Runnable() {
            @Override
            public void run() {
                updateTimersView(timersListView);
                handler.postDelayed(this, 1000);
            }
        };
        appendTimerItems();
    }

    private void appendTimerItems() {
        timersArray.clear();
        timersList.clear();
        String package_name = getApplicationContext().getPackageName();
        String profile_path = "/data/data/" + package_name + "/shared_prefs";
        File prefs_directory = new File(profile_path);
        File[] prefs_files = prefs_directory.listFiles();
        timersListView = (ListView) findViewById(R.id.timers_list);
        LinearLayout timersLinearLayout = (LinearLayout) findViewById(R.id.empty_list_ll);
        timersArray = new ArrayList<Map<String, String>>();
        String file_extension;
        try {
            for (int i = 0; i < prefs_files.length; i++) {
                if(prefs_files[i].getName().substring(0, (int) (prefs_files[i].getName().length() - 4)).startsWith(getApplicationInfo().packageName + "_preferences"))
                {

                } else {
                    SharedPreferences prefs = getSharedPreferences(prefs_files[i].getName().substring(0, (int) (prefs_files[i].getName().length() - 4)), 0);
                    file_extension = prefs_files[i].getName().substring((int) (prefs_files[i].getName().length() - 4));
                    if (file_extension.contains(".xml") && file_extension.length() == 4) {
                        timersList.add(new TimerItem(prefs_files[i].getName().substring(0, (int) (prefs_files[i].getName().length() - 4)),
                                prefs_files[i].getName().substring(0, (int) (prefs_files[i].getName().length() - 4)), prefs.getString("timerAction", ""), prefs.getLong("timerActionDate", 0), "timer"));
                        Map<String, String> timer_item = new HashMap<>(2);
                        timer_item.put("title", prefs_files[i].getName().substring(0, (int) (prefs_files[i].getName().length() - 4)));
                        timer_item.put("subtitle", getResources().getString(R.string.time_at, new SimpleDateFormat("d MMMM yyyy").format(new Date(prefs.getLong("timerActionDate", 0))),
                                new SimpleDateFormat("HH:mm").format(new Date(prefs.getLong("timerActionDate", 0)))));
                        timersArray.add(timer_item);
                    }
                }
            }
            if(prefs_files == null || timersList.size() == 0) {
                timersListView.setVisibility(View.GONE);
                timersLinearLayout.setVisibility(View.VISIBLE);
            } else {
                timersListView.setVisibility(View.VISIBLE);
                timersLinearLayout.setVisibility(View.GONE);
            }

            timersListAdapter = new TimersListAdapter(MainActivity.this, timersList);
            Log.d("App", "Setting adapter...");
            timersListView.setAdapter(timersListAdapter);

            handler.removeCallbacks(updateTimerUI);
            handler.post(updateTimerUI);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    protected void onPause() {
        handler.removeCallbacks(updateTimerUI);
        super.onPause();
    }

    @Override
    protected void onResume() {
        appendTimerItems();
        super.onPostResume();
    }

    private void updateTimersView(ListView listView) {
        timersListAdapter = new TimersListAdapter(MainActivity.this, timersList);
        listView.setAdapter(timersListAdapter);
    }

    private void openEnterDialog(String action) {
        if (action.equals("create_timer")) {
            LayoutInflater inflater = getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            final View view = inflater.inflate(R.layout.enter_text_layout, null);
            builder.setView(view);
            final EditText value_edit = (EditText) view.findViewById(R.id.enter_value);
            final TextView error_text = (TextView) view.findViewById(R.id.error_text);
            builder.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences prefs = getSharedPreferences(value_edit.getText().toString(), 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("createdDate", System.currentTimeMillis());
                    editor.putString("timerAction", "calculateRemainingTime");
                    editor.putLong("timerActionDate", System.currentTimeMillis());
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, TimerSettingsActivity.class);
                    intent.putExtra("timerName", value_edit.getText().toString());
                    intent.putExtra("packageName", getApplicationContext().getPackageName());
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            final AlertDialog alertDialog = builder.create();
            value_edit.setHint(getResources().getString(R.string.name));
            value_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(value_edit.getText().toString().contains("/")) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            value_edit.setError(getResources().getString(R.string.text_field_wrong_characters));
                        } else {
                            error_text.setText(getResources().getString(R.string.text_field_wrong_characters));
                            error_text.setVisibility(View.VISIBLE);
                        }
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        error_text.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            alertDialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about_app_item) {
            Intent intent = new Intent(MainActivity.this, AboutApplicationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTimerEditorDialog(int position) {
        TimerItem timerItem = timersList.get(position);
        Intent intent = new Intent(MainActivity.this, TimerSettingsActivity.class);
        intent.putExtra("timerName", timerItem.name);
        intent.putExtra("packageName", getApplicationContext().getPackageName());
        startActivity(intent);
    }

    public void deleteTimer(int position) {
        TimerItem timerItem = timersList.get(position);
        try {
            String profile_path = "/data/data/" + getPackageName() + "/shared_prefs/" + timerItem.name + ".xml";
            File file = new File(profile_path);
            file.delete();
            appendTimerItems();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
