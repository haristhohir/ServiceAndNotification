package com.harisuddin.serviceandnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    public static final int SUCCESS = 200;
    public static final int PROGRESS = 201;
    public static final int FAILED = 404;
    private ProgressBar progressBar;
    private TextView keteranganText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button basicNotification = (Button)findViewById(R.id.basicNotification_button);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        keteranganText = (TextView)findViewById(R.id.keterangan_textView);
        Button serviceButton = (Button)findViewById(R.id.serviceAndNotification_button);

        basicNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicNotification(v);
            }
        });

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentService = new Intent(getApplicationContext(), SomeService.class);
                intentService.putExtra("result", resultReceiver);
                intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intentService);
            }
        });

    }


    public void basicNotification(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://asc.harisuddin.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.ic_stat_name);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        builder.setContentTitle("BasicNotifications Sample");
        builder.setContentText("Time to learn about notifications!");
        builder.setSubText("Tap to view documentation about notifications.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void customNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);

        builder.setTicker(getResources().getString(R.string.custom_notification));

        builder.setSmallIcon(R.drawable.ic_stat_name);

        builder.setAutoCancel(true);

        Notification notification = builder.build();

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        final String text = getResources().getString(R.string.collapsed, time);
        contentView.setTextViewText(R.id.textView, text);

        notification.contentView = contentView;

        if (Build.VERSION.SDK_INT >= 16) {
            RemoteViews expandedView =
                    new RemoteViews(getPackageName(), R.layout.notification_expanded);
            notification.bigContentView = expandedView;
        }

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(2, notification);

    }


    public void showCustomNotificatio(View v) {
        customNotification();
    }

    public void showLNotification(View v) {
        LNotification();
    }




    private void LNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(intent);
        builder.setTicker("Custom Notification");
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setAutoCancel(true);


        builder.setLights(Color.BLUE, 333, 333);

        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        builder.setVibrate(new long[]{1000, 1000, 500, 0, 1000, 0, 500});


        Notification notification = builder.build();

        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification);

        String currentTime = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        int toDay = calendar.get(Calendar.DAY_OF_WEEK);


        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.notification_expanded);
        if (Build.VERSION.SDK_INT >= 16) {
            if(Build.VERSION.SDK_INT >= 21){
                Intent push = new Intent();
                push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                push.setClass(this, MainActivity.class);
//                        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
//                                push, PendingIntent.FLAG_CANCEL_CURRENT);
                builder
                        .setContent(expandedView)
                        .setFullScreenIntent(intent, true);
            }
            notification = builder.build();
        }

        if (Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = expandedView;
        }

        if(Build.VERSION.SDK_INT<21){

            LayoutInflater mInflater = LayoutInflater.from(this);
            View layout = mInflater.inflate(R.layout.custom_toast, null);
            TextView lessonName = (TextView) layout.findViewById(R.id.lesson_item_name);
            TextView lessonClass = (TextView) layout.findViewById(R.id.lesson_item_class);
            TextView lessonTime = (TextView) layout.findViewById(R.id.lesson_item_time);


            Toast toast = new Toast(this);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }

        notification.contentView = contentView;
        NotificationManager nm = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        nm.notify(3, notification);
    }


    public void serviceNotification(String title, String contentText, String subText) {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://asc.harisuddin.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setLights(Color.BLUE, 333, 777);
        builder.setContentTitle(title);
        builder.setContentText(contentText);
        builder.setSubText(subText);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(4, builder.build());
    }

    Handler handler = new Handler();
    final ResultReceiver resultReceiver = new ResultReceiver(handler) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == SUCCESS){
//                Intent intent = new Intent(getApplicationContext(), Main.class);
//                intent.putExtra("from", "login");
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(),resultData.getString("result"), Toast.LENGTH_LONG).show();
                String result = resultData.getString("result");
                progressBar.setVisibility(View.GONE);
                keteranganText.setVisibility(View.GONE);
//                keteranganText.setText(result);
                try {
                    JSONObject jsonString = new JSONObject(result);
                    JSONObject user = jsonString.getJSONObject("user");
                    String name = user.getString("name");
                    String email = user.getString("email");
                    serviceNotification("User Data", name, email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (resultCode == PROGRESS){
                progressBar.setVisibility(View.VISIBLE);
                keteranganText.setVisibility(View.VISIBLE);
                keteranganText.setText(resultData.getString("result"));
            }else if(resultCode == FAILED){
                progressBar.setVisibility(View.INVISIBLE);
                keteranganText.setVisibility(View.VISIBLE);
                keteranganText.setText(resultData.getString("result"));
            }
        }
    };


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
