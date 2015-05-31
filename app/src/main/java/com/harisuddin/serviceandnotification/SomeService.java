package com.harisuddin.serviceandnotification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SomeService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.harisuddin.serviceandnotification.action.FOO";
    private static final String ACTION_BAZ = "com.harisuddin.serviceandnotification.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.harisuddin.serviceandnotification.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.harisuddin.serviceandnotification.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SomeService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SomeService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    private final String LOG_TAG = this.getClass().getSimpleName().toString();

    public SomeService() {
        super("SomeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }

        Log.d(LOG_TAG, "Start Service");
        ResultReceiver resultReceiver = intent.getParcelableExtra("result");
        Bundle result = new Bundle();
        result.putString("result", "loading...");

        resultReceiver.send(MainActivity.PROGRESS, result);



        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String scheduleJsonStr = null;
        try {
            URL url = new URL("http://api.harisuddin.com/mySchedule/user/id/125150200111103/format/json");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            Log.d(LOG_TAG, "Connecting...");


            result.putString("result", "connecting...");
            resultReceiver.send(MainActivity.PROGRESS, result);

            urlConnection.connect();
            Log.d(LOG_TAG, "Connected");

            result.putString("result", "connected");
            resultReceiver.send(MainActivity.PROGRESS, result);

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {

                return;
            }
            scheduleJsonStr = buffer.toString();

            if(!scheduleJsonStr.equals("")){
                result.putString("result", scheduleJsonStr);
                resultReceiver.send(MainActivity.SUCCESS, result);

            }else {
                result.putString("result", "Failed!");
                resultReceiver.send(MainActivity.FAILED, result);
            }



        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }catch (NullPointerException e){
            Log.e(LOG_TAG, "Null Pointer Exception!");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
