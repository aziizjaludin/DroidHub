package bn.aziiz.droidhub;

import android.app.*;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.*;

import androidx.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import java.util.*;

public class DroidHubMediaService extends MediaBrowserServiceCompat {
    public static final String ACTION_PLAY="bn.aziiz.droidhub.PLAY";
    public static final String ACTION_PAUSE="bn.aziiz.droidhub.PAUSE";
    private MediaSessionCompat session;
    private AudioTrack tone;
    private Thread toneThread;
    private volatile boolean playing=false;
    private static final String CHANNEL="droidhub_playback";

    @Override public void onCreate() {
        super.onCreate();
        session = new MediaSessionCompat(this,"DroidHub");
        session.setCallback(new MediaSessionCompat.Callback() {
            @Override public void onPlay() { play(); }
            @Override public void onPlayFromMediaId(String id, Bundle extras) { play(); }
            @Override public void onPause() { pause(); }
            @Override public void onStop() { pause(); stopForeground(true); }
        });
        setSessionToken(session.getSessionToken());
        session.setActive(true);
        updateState(false);
        createChannel();
    }

    @Override public int onStartCommand(Intent intent,int flags,int startId) {
        if (intent != null && ACTION_PAUSE.equals(intent.getAction())) pause();
        else play();
        return START_STICKY;
    }

    @Override public BrowserRoot onGetRoot(String clientPackageName,int clientUid,Bundle rootHints) {
        return new BrowserRoot("root",null);
    }

    @Override public void onLoadChildren(String parentId, Result<List<MediaBrowserCompat.MediaItem>> result) {
        List<MediaBrowserCompat.MediaItem> list=new ArrayList<>();
        if ("root".equals(parentId)) {
            MediaDescriptionCompat d=new MediaDescriptionCompat.Builder()
                .setMediaId("demo").setTitle("DroidHub Drive Tone")
                .setSubtitle("Built-in demo audio").build();
            list.add(new MediaBrowserCompat.MediaItem(d,MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
        }
        result.sendResult(list);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT>=26) {
            NotificationChannel c=new NotificationChannel(CHANNEL,"DroidHub playback",NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(c);
        }
    }

    private Notification notification() {
        Intent open=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,open,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(this,CHANNEL):new Notification.Builder(this);
        b.setSmallIcon(android.R.drawable.ic_media_play).setContentTitle("DroidHub")
         .setContentText(playing?"Demo audio playing":"Media service ready").setContentIntent(pi)
         .setOngoing(playing).setCategory(Notification.CATEGORY_TRANSPORT);
        if(Build.VERSION.SDK_INT>=21) b.setStyle(new Notification.MediaStyle().setMediaSession((MediaSession.Token)session.getSessionToken().getToken()));
        return b.build();
    }

    private synchronized void play() {
        if(playing) return;
        playing=true; updateState(true); startForeground(41,notification());
        toneThread=new Thread(() -> {
            int rate=44100, n=rate/10;
            short[] data=new short[n];
            for(int i=0;i<n;i++) data[i]=(short)(Math.sin(2*Math.PI*440*i/rate)*2200);
            AudioTrack t=new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                .setAudioFormat(new AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(rate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                .setBufferSizeInBytes(data.length*2).setTransferMode(AudioTrack.MODE_STREAM).build();
            tone=t; t.play();
            while(playing) t.write(data,0,data.length);
            try { t.stop(); } catch(Exception ignored) {}
            t.release(); tone=null;
        });
        toneThread.start();
    }

    private synchronized void pause() {
        playing=false; updateState(false);
        stopForeground(false);
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(41,notification());
    }

    private void updateState(boolean isPlaying) {
        long actions=PlaybackStateCompat.ACTION_PLAY|PlaybackStateCompat.ACTION_PAUSE|PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID|PlaybackStateCompat.ACTION_STOP;
        session.setPlaybackState(new PlaybackStateCompat.Builder().setActions(actions)
            .setState(isPlaying?PlaybackStateCompat.STATE_PLAYING:PlaybackStateCompat.STATE_PAUSED,0,isPlaying?1f:0f).build());
    }

    @Override public void onDestroy() { pause(); session.release(); super.onDestroy(); }
}
