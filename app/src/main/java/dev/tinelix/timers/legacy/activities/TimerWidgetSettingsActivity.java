package dev.tinelix.timers.legacy.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import dev.tinelix.timers.legacy.R;
import dev.tinelix.timers.legacy.list_adapters.TimersListSpinnerAdapter;
import dev.tinelix.timers.legacy.list_items.SimpleListItem;
import dev.tinelix.timers.legacy.list_items.TimerItem;
import dev.tinelix.timers.legacy.utils.TimersManager;
import dev.tinelix.timers.legacy.widgets.TimerWidget;

/**
 * The configuration screen for the {@link TimerWidget TimerWidget} AppWidget.
 */
public class TimerWidgetSettingsActivity extends Activity {

    private static final String PREFS_NAME = "dev.tinelix.timers.legacy.widgets.TimerWidget";
    private static final String PREF_PREFIX_KEY = "timer_widget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Spinner timers_spinner;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = TimerWidgetSettingsActivity.this;

            // When the button is clicked, store the string locally
            int spinner_curpos = timers_spinner.getSelectedItemPosition();
            TimerItem item =
                    ((TimersListSpinnerAdapter) timers_spinner.getAdapter())
                        .getListItem(spinner_curpos);
            saveTimerWidgetPref(context, mAppWidgetId, item);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //TimerWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public TimerWidgetSettingsActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTimerWidgetPref(Context context, int appWidgetId, TimerItem item) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, item.name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            prefs.apply();
        } else {
            prefs.commit();
        }
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            prefs.apply();
        } else {
            prefs.commit();
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_timer_widget_settings);
        //mAppWidgetText = (EditText) findViewById(R.id.widget_timer_title);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        ArrayList<TimerItem> timers;
        timers = TimersManager.getTimersList(this);
        timers_spinner = (Spinner) findViewById(R.id.widget_timers_spinner);
        timers_spinner.setAdapter(new TimersListSpinnerAdapter(this, timers));
    }
}

