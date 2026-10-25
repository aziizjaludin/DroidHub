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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;

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

        ytOpen.setOnClickListener(v ->
            openBrowser("https://www.youtube.com/")
        );

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

            openBrowser(target.toString());
        });

        TextView footer = text("v0.3 • DroidHub Browser + Media",13,Color.rgb(116,128,145),false);
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

    private void openBrowser(String initialUrl) {
        final Activity activity = this;

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(Color.rgb(8,11,16));

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(8),dp(8),dp(8),dp(8));
        toolbar.setBackgroundColor(Color.rgb(18,25,35));

        Button back = new Button(this);
        back.setText("‹");
        back.setTextSize(24);
        back.setTextColor(Color.WHITE);
        back.setAllCaps(false);
        back.setBackground(bg(Color.rgb(29,38,51),12));

        Button forward = new Button(this);
        forward.setText("›");
        forward.setTextSize(24);
        forward.setTextColor(Color.WHITE);
        forward.setAllCaps(false);
        forward.setBackground(bg(Color.rgb(29,38,51),12));

        Button home = new Button(this);
        home.setText("⌂");
        home.setTextSize(20);
        home.setTextColor(Color.WHITE);
        home.setAllCaps(false);
        home.setBackground(bg(Color.rgb(29,38,51),12));

        EditText address = new EditText(this);
        address.setSingleLine(true);
        address.setTextColor(Color.WHITE);
        address.setHintTextColor(Color.rgb(115,127,143));
        address.setHint("Search or enter address");
        address.setText(initialUrl);
        address.setPadding(dp(14),0,dp(14),0);
        address.setBackground(bg(Color.rgb(29,38,51),12));

        Button go = new Button(this);
        go.setText("GO");
        go.setTextColor(Color.rgb(8,11,16));
        go.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        go.setAllCaps(false);
        go.setBackground(bg(Color.rgb(111,231,216),12));

        LinearLayout.LayoutParams small =
            new LinearLayout.LayoutParams(dp(52),dp(48));
        small.setMargins(dp(3),0,dp(3),0);

        toolbar.addView(back,small);
        toolbar.addView(forward,small);
        toolbar.addView(home,small);

        LinearLayout.LayoutParams addressParams =
            new LinearLayout.LayoutParams(0,dp(48),1);
        addressParams.setMargins(dp(5),0,dp(5),0);
        toolbar.addView(address,addressParams);

        toolbar.addView(go,
            new LinearLayout.LayoutParams(dp(58),dp(48)));

        WebView web = new WebView(this);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        web.setWebChromeClient(new WebChromeClient());

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view,String url) {
                super.onPageFinished(view,url);
                address.setText(url);
            }
        });

        back.setOnClickListener(v -> {
            if (web.canGoBack()) web.goBack();
            else showMainScreen();
        });

        forward.setOnClickListener(v -> {
            if (web.canGoForward()) web.goForward();
        });

        home.setOnClickListener(v ->
            web.loadUrl("https://www.youtube.com/")
        );

        View.OnClickListener navigate = v -> {
            String q = address.getText().toString().trim();

            if (q.isEmpty()) return;

            if (!q.startsWith("http://") &&
                !q.startsWith("https://")) {

                if (q.contains(".") && !q.contains(" ")) {
                    q = "https://" + q;
                } else {
                    q = "https://www.google.com/search?q=" +
                        Uri.encode(q);
                }
            }

            web.loadUrl(q);
        };

        go.setOnClickListener(navigate);

        address.setOnEditorActionListener((v,action,event) -> {
            navigate.onClick(v);
            return true;
        });

        page.addView(toolbar,
            new LinearLayout.LayoutParams(-1,dp(64)));

        page.addView(web,
            new LinearLayout.LayoutParams(-1,0,1));

        setContentView(page);

        web.loadUrl(initialUrl);
    }

    private void showMainScreen() {
        recreate();
    }

    @Override
    public void onBackPressed() {
        showMainScreen();
    }

}
