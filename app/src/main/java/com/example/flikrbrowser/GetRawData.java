package com.example.flikrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK }

public class GetRawData extends AsyncTask<String, Void, String>{
    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;

    public GetRawData() {
        mDownloadStatus = DownloadStatus.IDLE;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter " + s);

        try {
            JSONObject jsonObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        } else {
            mDownloadStatus = DownloadStatus.PROCESSING;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int response = connection.getResponseCode();
                Log.d(TAG, "doInBackground: The response code was " + response);
                InputStream stream = connection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);

                StringBuilder result = new StringBuilder();

                reader = new BufferedReader(streamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                mDownloadStatus = DownloadStatus.OK;
                return result.toString();

            } catch (MalformedURLException e) {
                Log.d(TAG, "doInBackground: Invalid URL " +e.getMessage());
                e.printStackTrace();
            } catch (IOException e){
                Log.d(TAG, "doInBackground: IO Exception reading data " + e.getMessage());
            } catch (SecurityException e) {
                Log.d(TAG, "doInBackground: Security Exception. Needs permission? " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.d(TAG, "doInBackground: Error closing stream " + e.getMessage());
                    }
                }
            }
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            return null;
        }


    }
}
