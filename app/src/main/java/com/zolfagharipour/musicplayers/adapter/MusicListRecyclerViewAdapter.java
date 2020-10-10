package com.zolfagharipour.musicplayers.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MusicListRecyclerViewAdapter extends RecyclerView.Adapter<MusicListRecyclerViewAdapter.MusicViewHolder> {

    public static final String TAG = "tag";
    private List<Song> mSongList;
    private Context mContext;
    private MusicRepository mRepository;
    private MusicItemListener mMusicItemListener;

    public MusicListRecyclerViewAdapter(List<Song> songList, Context context, MusicItemListener musicItemListener) {
        mSongList = songList;
        mContext = context;
        mRepository = MusicRepository.getInstance();
        mMusicItemListener = musicItemListener;
    }

    public void setSongList(List<Song> songList) {
        mSongList = songList;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item_row, parent, false), mMusicItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicViewHolder holder, int position) {
        Song song = mSongList.get(position);
        holder.mTextViewTitle.setText(song.getTitle());
        holder.mTextViewSubtitle.setText(song.getAlbum());
        bindMusicCover(holder, song);
    }

    private void bindMusicCover(final MusicViewHolder holder, final Song song) {
        final byte[] image = MusicManager.getCoverArt(song.getPath());
        Glide.with(mContext).asBitmap().load(image).placeholder(R.drawable.ic_cover_list).
                apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(holder.mImageViewCover);
    }


 /*   private void bindLottieAnimation(final MusicViewHolder holder, final Song song){
        if (mRepository.getMediaPlayer() != null && mRepository.getMediaPlayer().isPlaying()){
            if (mRepository.getCurrentSong().getPath().equals(song.getPath())){
                holder.mLottieAnimationView.setVisibility(View.VISIBLE);
                holder.mLottieAnimationView.playAnimation();
                holder.mLottieAnimationView.setRepeatCount(50);
            }
        }
    }*/

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public interface MusicItemListener {
        void onMusicItemClicked(Song song);
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageViewCover;
        public TextView mTextViewTitle, mTextViewSubtitle;
        public LottieAnimationView mLottieAnimationView;

        public MusicViewHolder(@NonNull View itemView, MusicItemListener musicItemListener) {
            super(itemView);
            mImageViewCover = itemView.findViewById(R.id.itemRowSongCover);
            mTextViewTitle = itemView.findViewById(R.id.itemRowSongTitle);
            mTextViewTitle.setSelected(true);
            mTextViewSubtitle = itemView.findViewById(R.id.itemRowSongAlbum);
            mTextViewSubtitle.setSelected(true);
            mLottieAnimationView = itemView.findViewById(R.id.itemRowSongAnimation);
            mMusicItemListener = musicItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mMusicItemListener.onMusicItemClicked(mSongList.get(getAdapterPosition()));
        }
    }

}
