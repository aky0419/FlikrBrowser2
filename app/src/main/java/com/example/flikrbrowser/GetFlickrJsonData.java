package com.example.flikrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;
    private Photo mPhoto;

    private final OnDataAvailable mCallback;

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
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    mPhoto.setTitle(jsonPhoto.getString("title"));
                    mPhoto.setAuthor(jsonPhoto.getString("author"));
                    mPhoto.setAuthorID(jsonPhoto.getString("author_id"));
                    mPhoto.setTags(jsonPhoto.getString("tags"));
                    mPhoto.setLink(jsonPhoto.getString("link"));
                    mPhoto.setImage(jsonPhoto.getJSONObject("media").getString("m"));

                    mPhotoList.add(mPhoto);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }



        }

    }

    private void executeOnSameThread(String searchCriteria) {
        String destinationUri = createUrl(searchCriteria, mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "getData: ");
    }

    private String createUrl(String searchCriteria, String language, boolean matchAll) {
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "Json")
                .build().toString();

    }
}
