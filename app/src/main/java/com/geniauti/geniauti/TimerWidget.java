package com.geniauti.geniauti;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class TimerWidget extends AppWidgetProvider {

    private static boolean timerOn = false;
    private static int second = 0;
    private static int minute = 0;
    public static String TIMER_START_BUTTON = "com.geniauti.geniauti.TIMER_START_BUTTON";
    public static String TIMER_RESET_BUTTON = "com.geniauti.geniauti.TIMER_RESET_BUTTON";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);

        Intent intentStart = new Intent(context, TimerWidget.class);
        intentStart.setAction(TIMER_START_BUTTON);
        intentStart.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingStart = PendingIntent.getBroadcast(context, appWidgetId,
                intentStart, 0);
        views.setOnClickPendingIntent(R.id.widget_start_button, pendingStart);

        Intent intentRest = new Intent(context, TimerWidget.class);
        intentRest.setAction(TIMER_RESET_BUTTON);
        intentRest.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingReset = PendingIntent.getBroadcast(context, appWidgetId,
                intentRest, 0);
        views.setOnClickPendingIntent(R.id.widget_reset_button, pendingReset);

        if(timerOn) {
            views.setTextViewText(R.id.widget_start_button, "종료");
            views.setTextViewText(R.id.widget_timer, String.valueOf(second));
        } else {
            views.setTextViewText(R.id.widget_start_button, "시작");
            views.setTextViewText(R.id.widget_timer, "00 : 00");
        }

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
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        // RENEW
        if (action != null && action.equals(TIMER_START_BUTTON)) {
            final int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if(timerOn) {


            } else {
                timerOn = true;
                updateAppWidget(context, AppWidgetManager.getInstance(context), id);
            }

            return;
        }

        if (action != null && action.equals(TIMER_RESET_BUTTON)) {
            int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if(timerOn) {
                timerOn = false;
                second = 0;
                minute = 0;
                updateAppWidget(context, AppWidgetManager.getInstance(context), id);
            }

            return;
        }

        super.onReceive(context, intent);
    }

}

