package com.example.ad_task_5_stopwatch;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton, stopButton, resetButton;

    private Handler handler;
    private long startTime, elapsedTime;
    private boolean running;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "stopwatch_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timer);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        resetButton = findViewById(R.id.reset_button);

        handler = new Handler();

        createNotificationChannel();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopwatch();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopStopwatch();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStopwatch();
            }
        });
    }

    private void startStopwatch() {
        startTime = System.currentTimeMillis();
        handler.post(runnable);
        running = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        resetButton.setEnabled(false);

        startService(new Intent(this, BackgroundMusicService.class));
        showNotification();
    }

    private void stopStopwatch() {
        handler.removeCallbacks(runnable);
        elapsedTime += System.currentTimeMillis() - startTime;
        running = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        resetButton.setEnabled(true);

        stopService(new Intent(this, BackgroundMusicService.class));
        hideNotification();
    }

    private void resetStopwatch() {
        elapsedTime = 0;
        timerTextView.setText("00:00:00");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        resetButton.setEnabled(false);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            long time = elapsedTime + (currentTime - startTime);

            int seconds = (int) (time / 1000) % 60;
            int minutes = (int) (time / (1000 * 60)) % 60;
            int hours = (int) (time / (1000 * 60 * 60));

            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            timerTextView.setText(timeString);

            if (running) {
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Stopwatch Running")
                .setContentText("Tap to return to the stopwatch")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void hideNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Stopwatch Channel";
            String description = "Channel for Stopwatch notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
