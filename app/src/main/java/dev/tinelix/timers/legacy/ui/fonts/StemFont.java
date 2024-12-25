package dev.tinelix.timers.legacy.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;

public class StemFont {

    public static Typeface getFont(Context ctx, int weight) {
        Typeface typeface;
        switch(weight) {
            case 100:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/stem_light.otf");
                break;
            case 200:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/stem_regular.otf");
                break;
            case 500:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/stem_medium.otf");
                break;
            case 700:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/stem_bold.otf");
                break;
            default:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/stem_regular.otf");
                break;
        }

        return typeface;
    }
}
