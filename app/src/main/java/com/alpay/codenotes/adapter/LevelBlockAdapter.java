package com.alpay.codenotes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Level;
import com.bumptech.glide.Glide;


public class LevelBlockAdapter extends RecyclerView.Adapter<LevelBlockAdapter.LevelBlockViewHolder>  {

    private AppCompatActivity appCompatActivity;
    private int selected_position = RecyclerView.NO_POSITION;

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
        holder.itemView.setSelected(selected_position == position);
        if (!Level.levelBlockList.get(position).isContainCode()) {
            addCurrentPicture(holder, position);
        }
    }

    public void addCurrentPicture(LevelBlockViewHolder holder, int position) {
        Glide.with(appCompatActivity).load(Uri.parse("file:///android_asset/level_img/" + Level.levelBlockList.get(position).getImage())).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return Level.levelBlockList.size();
    }


    public class LevelBlockViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView mImage;

        LevelBlockViewHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.level_block_layout);
            mImage = itemView.findViewById(R.id.block_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selected_position);
                    selected_position = getLayoutPosition();
                    notifyItemChanged(selected_position);
                }
            });
        }
    }


}

