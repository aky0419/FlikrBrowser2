package com.example.flikrbrowser;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecuclerViewAdapt";
    private List<Photo> mPhotoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(List<Photo> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
    }


    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // called by layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent,false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        //called by the layout manager when it wants new data in an existing row
        Photo photoItem =getPhoto(position);
        Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " --> " + position);
                holder.title.setText(photoItem.getTitle());
        Picasso.get().load(photoItem.getImage())
                .placeholder(R.drawable.ic_crop_original_black_24dp)
                .error(R.drawable.ic_crop_original_black_24dp)
                .into(holder.photo);

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        //java ternary operator
        return ((mPhotoList!=null) && (mPhotoList.size()!=0) ? mPhotoList.size():0);
    }

    void loadNewData(List<Photo> newPhotos) {
        mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return  ((mPhotoList!=null) && (mPhotoList.size()!=0)? mPhotoList.get(position) :null);
    }


    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView photo;
        TextView title;

        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.photo = itemView.findViewById(R.id.photo);
            this.title = itemView.findViewById(R.id.title);
        }
    }
}
