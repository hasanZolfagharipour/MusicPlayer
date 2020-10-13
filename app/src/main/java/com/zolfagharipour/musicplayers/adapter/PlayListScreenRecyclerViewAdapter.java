package com.zolfagharipour.musicplayers.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayListScreenRecyclerViewAdapter extends RecyclerView.Adapter<PlayListScreenRecyclerViewAdapter.PlayListScreenViewHolder> {

    public static final String TAG = "tag";
    private Context mContext;
    private List<PlayList> mPlayLists = new ArrayList<>();
    private OnPlayListScreenListener mListener;

    public PlayListScreenRecyclerViewAdapter(Context context, List<PlayList> playLists, OnPlayListScreenListener onPlayListScreenListener) {
        mContext = context;
        mPlayLists.add(new PlayList());
        mPlayLists.addAll(playLists);
        mListener = onPlayListScreenListener;
    }

    @NonNull
    @Override
    public PlayListScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayListScreenViewHolder(LayoutInflater.from(mContext).inflate(R.layout.music_list_item_row, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListScreenViewHolder holder, int position) {
        if (position == 0){
            holder.mTextViewTitle.setText("Create Playlist");
            Glide.with(mContext).asBitmap().load(0).placeholder(R.drawable.ic_add_playlist).into(holder.mImageViewCover);
        }else {
            holder.mTextViewTitle.setText(mPlayLists.get(position).getTitle());
            if (position == 1)
                Glide.with(mContext).asBitmap().load(0).placeholder(R.drawable.ic_collection_favorite).into(holder.mImageViewCover);
            else
                Glide.with(mContext).asBitmap().load(MusicManager.getCoverArt(mPlayLists.get(position).getSongList().get(0).getPath())).apply(RequestOptions.bitmapTransform(new RoundedCorners(32))).apply(new RequestOptions().override((int) mContext.getResources().getDimension(R.dimen.image_album_size), (int) mContext.getResources().getDimension(R.dimen.image_album_size))).placeholder(R.drawable.ic_cover_list).into(holder.mImageViewCover);
        }

    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }

    public void setPlayLists(List<PlayList> playLists) {
        mPlayLists = playLists;
    }

    class PlayListScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageViewCover;
        private TextView mTextViewTitle;
        private OnPlayListScreenListener mListener;

        public PlayListScreenViewHolder(@NonNull View itemView, OnPlayListScreenListener onPlayListScreenListener) {
            super(itemView);

            mImageViewCover = itemView.findViewById(R.id.itemRowSongCover);
            mTextViewTitle  = itemView.findViewById(R.id.itemRowSongTitle);
            mListener = onPlayListScreenListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemSelectedListener(getAdapterPosition(), mPlayLists.get(getAdapterPosition()));
        }
    }


    public interface OnPlayListScreenListener{
        void onItemSelectedListener(int position, PlayList playList);
    }
}
