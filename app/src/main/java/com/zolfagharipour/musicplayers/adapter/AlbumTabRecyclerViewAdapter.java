package com.zolfagharipour.musicplayers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumTabRecyclerViewAdapter extends RecyclerView.Adapter<AlbumTabRecyclerViewAdapter.AlbumViewHolder> {

    private List<Song> mSongList;
    private Context mContext;
    private OnAlbumListener mOnAlbumListener;

    public AlbumTabRecyclerViewAdapter(Context context, List<Song> list, OnAlbumListener onAlbumListener) {
        mContext = context;
        mOnAlbumListener = onAlbumListener;
        mSongList = list;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collection_list_item_row, parent, false), mOnAlbumListener);
    }


    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Song song = mSongList.get(position);
        final byte[] image = MusicManager.getCoverArt(song.getPath());
        Glide.with(mContext).asBitmap().load(image).placeholder(R.drawable.ic_cover_list).
                apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                .apply(new RequestOptions().override((int) mContext.getResources().getDimension(R.dimen.image_album_size), (int) mContext.getResources().getDimension(R.dimen.image_album_size)))
                .into(holder.mImageViewAlbum);

        holder.mTextViewAlbumTitle.setText(song.getAlbum());
        /*Spannable spannable = Spannable.Factory.getInstance().newSpannable(song.getAlbum());
        spannable.setSpan(new BackgroundColorSpan(mContext.getResources().getColor(R.color.colorHighLight)), 0, song.getAlbum().length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mTextViewAlbumTitle.setText(spannable);*/


    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public void setList(List<Song> songList) {
        mSongList = songList;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageViewAlbum;
        private TextView mTextViewAlbumTitle;
        private OnAlbumListener mOnAlbumListener;

        public AlbumViewHolder(@NonNull View itemView, OnAlbumListener onAlbumListener) {
            super(itemView);
            mImageViewAlbum = itemView.findViewById(R.id.collectionItemRowImage);
            mTextViewAlbumTitle = itemView.findViewById(R.id.collectionItemRowTitle);
            itemView.setOnClickListener(this);
            mTextViewAlbumTitle.setSelected(true);;
            mOnAlbumListener = onAlbumListener;
        }

        @Override
        public void onClick(View v) {
            mOnAlbumListener.onAlbumClickedListener(mSongList.get(getAdapterPosition()));
        }
    }

    public interface OnAlbumListener{
        void onAlbumClickedListener(Song song);
    }
}
