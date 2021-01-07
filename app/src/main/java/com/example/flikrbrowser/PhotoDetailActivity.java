package com.example.flikrbrowser;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailActivity extends BaseActivity {

    TextView photoAuthorTv, photoTitleTv, photoTagTv;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        photoAuthorTv = findViewById(R.id.photo_author);
        photoTitleTv = findViewById(R.id.photo_title);
        photoTagTv = findViewById(R.id.photo_tags);
        mImageView = findViewById(R.id.photo_image);

        Intent intent = getIntent();
        Photo photo = intent.getParcelableExtra(PHOTO_TRANSFER);
        if (photo!=null) {
            photoAuthorTv.setText(photo.getAuthor());
            photoTitleTv.setText(photo.getTitle());
            photoTagTv.setText(photo.getTags());
            Picasso.get().load(photo.getLink()).into(mImageView);

        }


    }

}
