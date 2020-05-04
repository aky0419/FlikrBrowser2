package com.example.flikrbrowser;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private boolean runningOnSameThread = false;

    private final OnDataAvailable mCallback;

    @Override
    protected void onPostExecute(List<Photo> photos) {
        super.onPostExecute(photos);

        if (mCallback != null) {
            mCallback.OnDataAvailable(mPhotoList, DownloadStatus.OK);
        }

    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "List<photo> doInBackground: starts");
        String destinationUri = createUrl(strings[0], mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "getData: ");
        return mPhotoList;
    }

    interface OnDataAvailable{
        void OnDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(String baseURL, String language, boolean matchAll, OnDataAvailable callback) {
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallback = callback;
    }

    public GetFlickrJsonData(OnDataAvailable callback) {
        mCallback = callback;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        Log.d(TAG, "onDownloadComplete starts. Status = " + status);
        if (status== DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();
            try{
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");
                for (int i=0; i< itemsArray.length(); i++) {
                    Photo mPhoto = new Photo();
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    mPhoto.setTitle(jsonPhoto.getString("title"));
                    mPhoto.setAuthor(jsonPhoto.getString("author"));
                    mPhoto.setAuthorID(jsonPhoto.getString("author_id"));
                    mPhoto.setTags(jsonPhoto.getString("tags"));
                    mPhoto.setLink(jsonPhoto.getJSONObject("media").getString("m").replaceFirst("_m.", "_b."));
                    mPhoto.setImage(jsonPhoto.getJSONObject("media").getString("m"));

                    mPhotoList.add(mPhoto);

                    Log.d(TAG, "onDownloadComplete: " + mPhoto);
                }
                Log.d(TAG, "onDownloadComplete: " + mPhotoList);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;

            }
            if (runningOnSameThread && mCallback != null) {
                mCallback.OnDataAvailable(mPhotoList, status);
            }
        }



        Log.d(TAG, "onDownloadComplete: ends");

    }

     void executeOnSameThread(String searchCriteria) {
        String destinationUri = createUrl(searchCriteria, mLanguage, mMatchAll);
        runningOnSameThread = true;
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "getData: ");
    }

//    @Override
//    protected void onPostExecute(List<Photo> photos) {
//        super.onPostExecute(photos);
//    }
//
//    @Override
//    protected List<Photo> doInBackground(String... params) {
//        Log.d(TAG, "doInBackground: starts");
//        String
//        return null;
//    }

    private String createUrl(String searchCriteria, String language, boolean matchAll) {
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();

    }
}
