package com.zolfagharipour.musicplayers.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.zolfagharipour.musicplayers.R;
import com.zolfagharipour.musicplayers.model.Song;
import com.zolfagharipour.musicplayers.repository.MusicRepository;
import com.zolfagharipour.musicplayers.utils.MusicManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MusicListRecyclerViewAdapter extends RecyclerView.Adapter<MusicListRecyclerViewAdapter.MusicViewHolder> {

    private List<Song> mSongList;
    private Activity mContext;
    private MusicItemListener mMusicItemListener;
    private MusicRepository mRepository;

    public void setSongList(List<Song> songList) {
        mSongList = songList;
    }

    public MusicListRecyclerViewAdapter(List<Song> songList, Activity context, MusicItemListener musicItemListener) {
        mSongList = songList;
        mContext = context;
        mMusicItemListener = musicItemListener;
        mRepository = MusicRepository.getInstance();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item_row, parent, false), mMusicItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicViewHolder holder, int position) {
        final Song song = mSongList.get(position);
        holder.mTextViewTitle.setText(song.getTitle());
        holder.mTextViewSubtitle.setText(song.getAlbum());
        bindSongsCover(holder, song);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mRepository.setCurrentSong(song);

                Pair<View, String> pairTitle = Pair.create((View) holder.mTextViewTitle, "transitionRowTitle");
                Pair<View, String> pairAlbum = Pair.create((View) holder.mTextViewTitle, "transitionRowAlbum");


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, pairTitle, pairAlbum);

/*
                ((MusicPlayersActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(holder.mTextViewTitle,"transitionRowTitle")
                        .replace(R.id.fragmentContainer, PlayFragment.newInstance(song))
                        .addToBackStack(null).commit();

                */


            }
        });
    }

    private void bindSongsCover(MusicViewHolder holder, Song song) {

        byte[] image = MusicManager.getCoverArt(song.getPath());
        Glide.with(mContext).asBitmap().load(image).placeholder(R.drawable.ic_cover_list).into(holder.mImageViewCover);
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImageViewCover;
        private TextView mTextViewTitle, mTextViewSubtitle;
        private LottieAnimationView mLottieAnimationView;
        private MusicItemListener mMusicItemListener;

        public MusicViewHolder(@NonNull View itemView, MusicItemListener musicItemListener) {
            super(itemView);
            mImageViewCover = itemView.findViewById(R.id.itemRowSongCover);
            mTextViewTitle = itemView.findViewById(R.id.itemRowSongTitle);
            mTextViewSubtitle = itemView.findViewById(R.id.itemRowSongAlbum);
            mLottieAnimationView = itemView.findViewById(R.id.itemRowSongAnimation);
            mMusicItemListener = musicItemListener;
        }

        @Override
        public void onClick(View v) {
            //mMusicItemListener.MusicClicked(mSongList.get(getAdapterPosition()));
        }
    }

    public interface MusicItemListener{
        void MusicClicked(Song song);
    }
}
