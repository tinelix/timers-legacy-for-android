package dev.tinelix.timers.legacy.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.RemoteViews;

import dev.tinelix.timers.legacy.Global;
import dev.tinelix.timers.legacy.R;
import dev.tinelix.timers.legacy.activities.TimerWidgetSettingsActivity;
import dev.tinelix.timers.legacy.ui.fonts.StemFont;
import dev.tinelix.timers.legacy.ui.fonts.TahomaFont;
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

        long days = TimersManager.getDaysFromTimer(context, (String) widgetText);

        float dp = context.getResources().getDisplayMetrics().scaledDensity;

        Bitmap bmp = Bitmap.createBitmap(
                (int) (311.0 * dp), (int) (72.0 * dp), Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bmp);

        Typeface tf_stem_medium = StemFont.getFont(context, 500);
        Typeface tf_stem_bold = StemFont.getFont(context, 700);

        Typeface tf_tahoma_bold = TahomaFont.getFont(context, 700);

        Paint title_paint = new Paint();

        title_paint.setColor(Color.parseColor("#ffffff"));
        title_paint.setTextSize(22);
        title_paint.setAntiAlias(true);
        title_paint.setTypeface(tf_stem_medium);

        if (widgetText.equals(context.getResources().getString(R.string.new_year))){
            canvas.drawText(
                    context.getResources().getString(R.string.new_year_widget_title),
                    26, 30, title_paint
            );
        } else {
            if(widgetText.length() > 27) {
                canvas.drawText(
                        widgetText.toString().substring(0, 27) + "...",
                        26, 30, title_paint
                );
            } else {
                canvas.drawText(
                        widgetText.toString(),
                        26, 30, title_paint
                );
            }
        }

        Paint counter_paint = new Paint();

        counter_paint.setColor(Color.parseColor("#ffffff"));
        counter_paint.setTextSize(60);
        counter_paint.setAntiAlias(true);
        counter_paint.setTypeface(tf_stem_bold);
        canvas.drawText(
                days + " " +
                        Global.getPluralQuantityString(
                                context.getApplicationContext(),
                                R.plurals.days_without_count,
                                Global.getEndNumberFromLong(days)
                        ),
                26, 90, counter_paint
        );

        Paint branding_paint = new Paint();

        branding_paint.setColor(Color.parseColor("#38ffffff"));
        branding_paint.setTextSize(40);
        branding_paint.setAntiAlias(true);
        branding_paint.setTypeface(tf_tahoma_bold);
        canvas.drawText(
                "daniel",
                240, 76, branding_paint
        );
        canvas.drawText(
                "myslivets",
                260, 107, branding_paint
        );

        views.setImageViewBitmap(R.id.widget_frame, bmp);

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

