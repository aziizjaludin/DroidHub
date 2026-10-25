package bn.aziiz.droidhub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ScrollView;
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

        LinearLayout ytCard = new LinearLayout(this);
        ytCard.setOrientation(LinearLayout.VERTICAL);
        ytCard.setPadding(dp(20),dp(20),dp(20),dp(20));
        ytCard.setBackground(bg(Color.rgb(18,25,35),20));

        TextView ytLabel = text("YOUTUBE",12,Color.rgb(255,100,100),true);
        ytCard.addView(ytLabel);

        TextView ytTitle = text("YouTube",24,Color.WHITE,true);
        LinearLayout.LayoutParams yttp = new LinearLayout.LayoutParams(-1,-2);
        yttp.setMargins(0,dp(8),0,dp(5));
        ytCard.addView(ytTitle,yttp);

        ytCard.addView(text(
            "Search YouTube or paste a video link. YouTube opens on your phone using the normal YouTube experience.",
            14,Color.rgb(190,199,211),false));

        EditText ytInput = new EditText(this);
        ytInput.setHint("Search or paste YouTube link");
        ytInput.setHintTextColor(Color.rgb(115,127,143));
        ytInput.setTextColor(Color.WHITE);
        ytInput.setSingleLine(true);
        ytInput.setPadding(dp(15),0,dp(15),0);
        ytInput.setBackground(bg(Color.rgb(29,38,51),14));
        LinearLayout.LayoutParams ytip = new LinearLayout.LayoutParams(-1,dp(52));
        ytip.setMargins(0,dp(16),0,dp(10));
        ytCard.addView(ytInput,ytip);

        Button ytSearch = new Button(this);
        ytSearch.setText("Search YouTube");
        ytSearch.setAllCaps(false);
        ytSearch.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        ytSearch.setTextColor(Color.WHITE);
        ytSearch.setBackground(bg(Color.rgb(190,35,45),14));
        ytCard.addView(ytSearch,new LinearLayout.LayoutParams(-1,dp(52)));

        Button ytOpen = new Button(this);
        ytOpen.setText("Open YouTube");
        ytOpen.setAllCaps(false);
        ytOpen.setTextColor(Color.WHITE);
        ytOpen.setBackground(bg(Color.rgb(29,38,51),14));
        LinearLayout.LayoutParams ytop = new LinearLayout.LayoutParams(-1,dp(50));
        ytop.setMargins(0,dp(10),0,0);
        ytCard.addView(ytOpen,ytop);

        LinearLayout.LayoutParams ytcardp = new LinearLayout.LayoutParams(-1,-2);
        ytcardp.setMargins(0,dp(24),0,0);
        root.addView(ytCard,ytcardp);

        ytOpen.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
            startActivity(i);
        });

        ytSearch.setOnClickListener(v -> {
            String q = ytInput.getText().toString().trim();
            if (q.isEmpty()) {
                Toast.makeText(this,"Enter a search or YouTube link",Toast.LENGTH_SHORT).show();
                return;
            }

            Uri target;
            if (q.startsWith("http://") || q.startsWith("https://")) {
                target = Uri.parse(q);
            } else {
                target = Uri.parse("https://www.youtube.com/results?search_query=" +
                    Uri.encode(q));
            }

            startActivity(new Intent(Intent.ACTION_VIEW,target));
        });

        TextView footer = text("v0.2 • DroidHub Media Hub",13,Color.rgb(116,128,145),false);
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
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(root);
        setContentView(scroll);
    }
}
