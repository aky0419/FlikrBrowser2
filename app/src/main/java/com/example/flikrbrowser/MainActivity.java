package com.example.flikrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable,
                                                                RecyclerItemClickListener.OnRecyclerClickListener{
    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;
    private List<Photo> mPhotoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(mPhotoList, this);
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharePref", MODE_PRIVATE);
        String query = sharedPreferences.getString(FLICKR_QUERY, "");
        if (query.length()>0) {
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData("https://www.flickr.com/services/feeds/photos_public.gne","en-us", true, this);
            getFlickrJsonData.execute(query);
        }

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void OnDataAvailable(List<Photo> data, DownloadStatus status) {

        
        if (status == DownloadStatus.OK) {
            mFlickrRecyclerViewAdapter.loadNewData(data);
            Log.d(TAG, "OnDataAvailable: downloaded");
        } else {
            Log.d(TAG, "OnDataAvailable: failed to download");
        }

        Log.d(TAG, "OnDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {



    }

    @Override
    public void onItemLongClick(View view, int position) {

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mFlickrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);

    }
}
