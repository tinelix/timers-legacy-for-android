package dev.tinelix.timers.legacy;

import android.app.Application;
import android.os.Build;

import com.seppius.i18n.plurals.PluralResources;

public class App extends Application {
    public String version = "0.0.2 Alpha";
    public String build_date = "2023-12-23";
    public PluralResources pluralResources;

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            try {
                pluralResources = new PluralResources(getResources());
            } catch (SecurityException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }
    }
}
