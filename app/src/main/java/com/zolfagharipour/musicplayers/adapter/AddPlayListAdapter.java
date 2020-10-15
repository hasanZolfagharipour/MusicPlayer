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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddPlayListAdapter extends RecyclerView.Adapter<AddPlayListAdapter.AddPlayListViewHolder> {

    private Context mContext;
    private List<PlayList> mPlayLists = new ArrayList<>();
    private AddPlayListListener mListener;

    public AddPlayListAdapter(Context context, List<PlayList> playLists, AddPlayListListener addPlayListListener) {
        mContext = context;
        mPlayLists.add(new PlayList());
        mPlayLists.addAll(playLists);
        mListener = addPlayListListener;
    }

    @NonNull
    @Override
    public AddPlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddPlayListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_row_track, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPlayListViewHolder holder, int position) {
        if (position == 0){
            holder.mTextViewTitle.setText("Create Playlist");
            Glide.with(mContext).asBitmap().load(0).placeholder(R.drawable.ic_add_playlist).into(holder.mImageViewCover);
        }else {
            holder.mTextViewTitle.setText(mPlayLists.get(position).getTitle());
            if (position == 1)
                Glide.with(mContext).asBitmap().load(0).placeholder(R.drawable.ic_collection_favorite).into(holder.mImageViewCover);
            else
                Glide.with(mContext).asBitmap().load(mPlayLists.get(position).getSongList().get(0).getImage()).apply(RequestOptions.bitmapTransform(new RoundedCorners(32))).apply(new RequestOptions().override((int) mContext.getResources().getDimension(R.dimen.image_album_size), (int) mContext.getResources().getDimension(R.dimen.image_album_size))).placeholder(R.drawable.ic_cover_list).into(holder.mImageViewCover);
        }

    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }

    public void setPlayLists(List<PlayList> playLists) {
        mPlayLists = playLists;
    }

    class AddPlayListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageViewCover;
        private TextView mTextViewTitle;
        private AddPlayListListener mListener;

        public AddPlayListViewHolder(@NonNull View itemView, AddPlayListListener addPlayListListener) {
            super(itemView);

            mImageViewCover = itemView.findViewById(R.id.itemRowTrackImageView);
            mTextViewTitle  = itemView.findViewById(R.id.itemRowTrackTextViewTitle);
            mListener = addPlayListListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemSelectedListener(getAdapterPosition(), mPlayLists.get(getAdapterPosition()));
        }
    }

    public interface AddPlayListListener {
        void onItemSelectedListener(int position, PlayList playList);
    }
}
