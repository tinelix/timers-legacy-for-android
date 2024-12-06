package dev.tinelix.timers.legacy.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.RemoteViews;

import dev.tinelix.timers.legacy.Global;
import dev.tinelix.timers.legacy.R;
import dev.tinelix.timers.legacy.activities.TimerWidgetSettingsActivity;
import dev.tinelix.timers.legacy.ui.views.AdaptiveTextView;
import dev.tinelix.timers.legacy.utils.TimersManager;

/**
 * Implementation of Timer Widget functionality.
 * App Widget Configuration implemented in {@link TimerWidgetSettingsActivity TimerWidgetSettingsActivity}
 */
public class TimerWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = TimerWidgetSettingsActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_timer);
        views.setTextViewText(R.id.widget_timer_title, widgetText);
        long days = TimersManager.getDaysFromTimer(context, (String) widgetText);

        float dp = context.getResources().getDisplayMetrics().scaledDensity;

        Bitmap bmp = Bitmap.createBitmap(
                (int) (228.0 * dp), (int) (68.0 * dp), Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#6affffff"));
        paint.setTextSize(70);
        paint.setAntiAlias(true);
        canvas.drawText(
                "" + days, (int)(82.0 * dp), (int) (65.0 * dp), paint
        );

        views.setImageViewBitmap(R.id.widget_frame, bmp);

        views.setTextViewText(
                R.id.widget_timer_days_count_label,
                context.getResources().getQuantityString(
                        R.plurals.days_without_count,
                        Global.getEndNumberFromLong(days)
                )
        );

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            TimerWidgetSettingsActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

