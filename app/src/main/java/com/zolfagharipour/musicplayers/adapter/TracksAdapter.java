package com.zolfagharipour.musicplayers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.SongRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.TrackViewHolder> {

    private List<Song> mSongList;
    private Context mContext;
    private SongRepository mRepository;
    private TrackItemListener mTrackItemListener;

    public TracksAdapter(Context context, List<Song> songList, TrackItemListener trackItemListener) {
        mSongList = songList;
        mContext = context;
        mRepository = SongRepository.getInstance();
        mTrackItemListener = trackItemListener;
    }

    public void setSongList(List<Song> songList) {
        mSongList = songList;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_track, parent, false), mTrackItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrackViewHolder holder, int position) {
        Song song = mSongList.get(position);
        holder.mTextViewTitle.setText(song.getTitle());
        holder.mTextViewSubtitle.setText(song.getAlbum());
        bindMusicCover(holder, song);
    }

    private void bindMusicCover(final TrackViewHolder holder, final Song song) {
        Glide
                .with(mContext)
                .asBitmap()
                .load(song.getImage())
                .placeholder(R.drawable.ic_cover_list)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                .into(holder.mImageViewCover);
    }


    private void bindLottieAnimation(final TrackViewHolder holder, final Song song){
        if (mRepository.getMediaPlayer() != null && mRepository.getMediaPlayer().isPlaying()){
            if (mRepository.getCurrentSong().getPath().equals(song.getPath())){
                holder.mLottieAnimationView.setVisibility(View.VISIBLE);
                holder.mLottieAnimationView.playAnimation();
                holder.mLottieAnimationView.setRepeatCount(50);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public interface TrackItemListener {
        void onTrackItemClicked(Song song);
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageViewCover;
        public TextView mTextViewTitle, mTextViewSubtitle;
        public LottieAnimationView mLottieAnimationView;

        public TrackViewHolder(@NonNull View itemView, TrackItemListener trackItemListener) {
            super(itemView);
            mImageViewCover = itemView.findViewById(R.id.itemRowTrackImageView);
            mTextViewTitle = itemView.findViewById(R.id.itemRowTrackTextViewTitle);
            mTextViewTitle.setSelected(true);
            mTextViewSubtitle = itemView.findViewById(R.id.itemRowTrackTextViewSubtitle);
            mTextViewSubtitle.setSelected(true);
            mLottieAnimationView = itemView.findViewById(R.id.itemRowTrackLottieAnimation);
            mTrackItemListener = trackItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mTrackItemListener.onTrackItemClicked(mSongList.get(getAdapterPosition()));
        }
    }

}
