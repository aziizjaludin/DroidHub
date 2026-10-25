package bn.aziiz.droidhub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }

    private GradientDrawable bg(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color); g.setCornerRadius(dp(radius));
        return g;
    }

    private TextView text(String s, int size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(s); v.setTextSize(size); v.setTextColor(color);
        if (bold) v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return v;
    }

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 7);
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(34), dp(24), dp(24));
        root.setBackgroundColor(Color.rgb(8,11,16));

        TextView brand = text("DROIDHUB", 13, Color.rgb(111,231,216), true);
        root.addView(brand);

        TextView title = text("Your drive.\nYour audio.", 38, Color.WHITE, true);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1,-2);
        tp.setMargins(0,dp(18),0,dp(26)); root.addView(title,tp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20),dp(20),dp(20),dp(20));
        card.setBackground(bg(Color.rgb(18,25,35),20));
        card.addView(text("ANDROID AUTO",12,Color.rgb(140,153,171),true));
        TextView ready = text("●  Media service ready",20,Color.rgb(111,231,216),true);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1,-2);
        rp.setMargins(0,dp(12),0,dp(8)); card.addView(ready,rp);
        card.addView(text("DroidHub exposes a supported Android Auto media library and playback session.",15,Color.rgb(205,212,222),false));
        root.addView(card);

        Button play = new Button(this);
        play.setText("START DEMO AUDIO");
        play.setTextSize(15); play.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        play.setTextColor(Color.rgb(8,11,16)); play.setAllCaps(false);
        play.setBackground(bg(Color.rgb(111,231,216),18));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1,dp(58));
        bp.setMargins(0,dp(24),0,dp(12)); root.addView(play,bp);

        Button stop = new Button(this);
        stop.setText("Pause");
        stop.setTextColor(Color.WHITE); stop.setAllCaps(false);
        stop.setBackground(bg(Color.rgb(29,38,51),18));
        root.addView(stop,new LinearLayout.LayoutParams(-1,dp(54)));

        TextView footer = text("v0.1 • Android Auto media prototype",13,Color.rgb(116,128,145),false);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1,-2);
        fp.setMargins(0,dp(28),0,0); footer.setGravity(Gravity.CENTER);
        root.addView(footer,fp);

        play.setOnClickListener(v -> {
            Intent i = new Intent(this, DroidHubMediaService.class);
            i.setAction(DroidHubMediaService.ACTION_PLAY);
            if (android.os.Build.VERSION.SDK_INT >= 26) startForegroundService(i); else startService(i);
        });
        stop.setOnClickListener(v -> {
            Intent i = new Intent(this, DroidHubMediaService.class);
            i.setAction(DroidHubMediaService.ACTION_PAUSE);
            startService(i);
        });
        setContentView(root);
    }
}
