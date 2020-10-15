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
import com.zolfagharipour.musicplayers.model.PlayList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.CollectionsViewHolder> {

    private List<PlayList> mPlayLists;
    private Context mContext;
    private CollectionListener mListener;

    public CollectionsAdapter(Context context, List<PlayList> playLists, CollectionListener collectionListener) {
        mContext = context;
        mPlayLists = playLists;
        mListener = collectionListener;
    }

    public void setPlayLists(List<PlayList> playLists) {
        mPlayLists = playLists;
    }

    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_row_collection, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int position) {
        PlayList playList = mPlayLists.get(position);
        holder.mTextViewPlayListTitle.setText(playList.getTitle());
        if (mPlayLists.get(0).getTitle().equals("Favorite"))
            holder.mTextViewPlayListSubtitle.setText(playList.getSongList().size() + " " + mContext.getString(R.string.tracks));
        bindCollectionsImage(holder, position, playList);
    }

    private void bindCollectionsImage(CollectionsViewHolder holder, int position, PlayList playList) {
        int resId;
        byte[] image;
        if (position == 0 && playList.getTitle().equals("Favorite")) {
            resId = R.drawable.ic_collection_favorite;
            image = new byte[]{};
        } else {
            resId = R.drawable.ic_cover_list;
            image = playList.getSongList().get(0).getImage();
        }

        Glide
                .with(mContext)
                .asBitmap()
                .load(image)
                .placeholder(resId)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                .apply(new RequestOptions().override((int) mContext.getResources().getDimension(R.dimen.image_album_size), (int) mContext.getResources().getDimension(R.dimen.image_album_size)))
                .into(holder.mImageViewPlayListCover);
    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }

    public interface CollectionListener {
        void onCollectionClicked(PlayList playList);
    }

    class CollectionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageViewPlayListCover;
        private TextView mTextViewPlayListTitle, mTextViewPlayListSubtitle;
        private CollectionListener mListener;

        public CollectionsViewHolder(@NonNull View itemView, CollectionListener collectionListener) {
            super(itemView);
            mImageViewPlayListCover = itemView.findViewById(R.id.itemRowCollectionImageView);
            mTextViewPlayListTitle = itemView.findViewById(R.id.itemRowCollectionTextViewTitle);
            mTextViewPlayListSubtitle = itemView.findViewById(R.id.itemRowCollectionTextViewSubtitle);
            mListener = collectionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onCollectionClicked(mPlayLists.get(getAdapterPosition()));
        }
    }
}
