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
import com.squareup.picasso.Picasso;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.PlayList;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter<PlayListRecyclerViewAdapter.PlayListViewHolder> {

    private List<PlayList> mPlayLists;
    private Context mContext;
    private OnCollectionListener mListener;

    public PlayListRecyclerViewAdapter(Context context, List<PlayList> playLists, OnCollectionListener onCollectionListener) {
        mContext = context;
        mPlayLists = playLists;
        mListener = onCollectionListener;
    }

    public void setPlayLists(List<PlayList> playLists) {
        mPlayLists = playLists;
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collection_list_item_row, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {
        PlayList playList = mPlayLists.get(position);
        holder.mTextViewPlayListTitle.setText(playList.getTitle());
        holder.mTextViewPlayListSubtitle.setText(playList.getSongList().size() + " " + mContext.getString(R.string.tracks));
        bindCollectionsImage(holder, position, playList);


    }

    private void bindCollectionsImage(PlayListViewHolder holder, int position, PlayList playList){
        int resId;
        byte[] image;
        if (position == 0)
        {
            resId = R.drawable.ic_collection_favorite;
            image = new byte[]{};
        }else
        {
            resId = R.drawable.ic_cover_list;
            image = MusicManager.getCoverArt(playList.getSongList().get(0).getPath());
        }

        Glide.with(mContext).asBitmap().load(image).placeholder(resId)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                .apply(new RequestOptions().override((int) mContext.getResources().getDimension(R.dimen.image_album_size), (int) mContext.getResources().getDimension(R.dimen.image_album_size)))
                .into(holder.mImageViewPlayListCover);
    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }

    class PlayListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageViewPlayListCover;
        private TextView mTextViewPlayListTitle, mTextViewPlayListSubtitle;
        private OnCollectionListener mListener;

        public PlayListViewHolder(@NonNull View itemView, OnCollectionListener onCollectionListener) {
            super(itemView);
            mImageViewPlayListCover = itemView.findViewById(R.id.collectionItemRowImage);
            mTextViewPlayListTitle = itemView.findViewById(R.id.collectionItemRowTitle);
            mTextViewPlayListSubtitle = itemView.findViewById(R.id.collectionItemRowSubtitle);
            mListener = onCollectionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onCollectionClicked(mPlayLists.get(getAdapterPosition()));
        }
    }

    public interface OnCollectionListener{
        void onCollectionClicked(PlayList playList);
    }
}
