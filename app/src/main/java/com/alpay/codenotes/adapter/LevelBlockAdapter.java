package com.alpay.codenotes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Level;
import com.alpay.codenotes.models.LevelBlock;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class LevelBlockAdapter extends RecyclerView.Adapter<LevelBlockViewHolder>  {

    private AppCompatActivity appCompatActivity;

    public LevelBlockAdapter(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public LevelBlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.level_block_layout, parent, false);
        return new LevelBlockViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final LevelBlockViewHolder holder, int position) {
        if (!Level.levelBlockList.get(position).isContainCode()) {
            Glide.with(appCompatActivity).load(Uri.parse("file:///android_asset/level_img/" + Level.levelBlockList.get(position).getImage())).into(holder.mImage);
        }
    }

    @Override
    public int getItemCount() {
        return Level.levelBlockList.size();
    }

}

class LevelBlockViewHolder extends RecyclerView.ViewHolder {

    View mView;
    CardView mLevelBlockCard;
    ImageView mImage;

    LevelBlockViewHolder(View itemView) {
        super(itemView);
        mView = itemView.findViewById(R.id.level_block_layout);
        mImage = itemView.findViewById(R.id.block_image);
        mLevelBlockCard = itemView.findViewById(R.id.levelblock_card);
    }
}