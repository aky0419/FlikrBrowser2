package com.example.flikrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity {
    private static final String TAG = "SearchActivity";

    private SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);

        mSearchView.setIconified(true);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextChange: called");
                SharedPreferences SHP = getSharedPreferences("MySharePref", MODE_PRIVATE);
                SHP.edit().putString(FLICKR_QUERY, query).apply();
                mSearchView.clearFocus();
                finish();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;

            }
        });



        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activateToolbar(true);


    }

}
