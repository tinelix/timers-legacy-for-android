package dev.tinelix.timers.legacy.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dev.tinelix.timers.legacy.App;
import dev.tinelix.timers.legacy.R;

public class AboutApplicationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_app);
        Button repoButton = (Button) findViewById(R.id.repo_button);
        Button websiteButton = (Button) findViewById(R.id.website_button);
        repoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/tinelix/timers-for-android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        TextView license_label = (TextView) findViewById(R.id.license_label);
        license_label.setMovementMethod(LinkMovementMethod.getInstance());
        TextView version_label = (TextView) findViewById(R.id.version_label);
        version_label.setText(getResources().getString(R.string.version_str, ((App) getApplicationContext()).version, ((App) getApplicationContext()).build_date));
    }
}
