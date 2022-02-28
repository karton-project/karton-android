package com.alpay.codenotes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Content;
import com.alpay.codenotes.utils.NavigationManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ContentViewAdapter extends RecyclerView.Adapter<ContentViewHolder> {

    private AppCompatActivity appCompatActivity;
    private ArrayList<Content> mContentList;

    public ContentViewAdapter(AppCompatActivity appCompatActivity, ArrayList<Content> mContentList) {
        this.appCompatActivity = appCompatActivity;
        this.mContentList = mContentList;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.content_card_layout, parent, false);
        return new ContentViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ContentViewHolder holder, int position) {
        Glide.with(appCompatActivity).load(Uri.parse("file:///android_asset/content_img/" + mContentList.get(position).getContentID() + ".png")).into(holder.mImage);
        holder.mTitle.setText(mContentList.get(position).getName());
        holder.mDetail.setText(mContentList.get(position).getDetail());
        holder.mWatchButton.setOnClickListener(v ->
                NavigationManager.openWebViewFragment(appCompatActivity, mContentList.get(position).getDocsLink()));
        holder.mPracticeButton.setOnClickListener(v -> {
            NavigationManager.openWebViewFragment(appCompatActivity, mContentList.get(position).getInstruction());
        });
        if (mContentList.get(position).getCode().length > 1){
            holder.mSeeCodeButton.setVisibility(View.VISIBLE);
            holder.mSeeCodeButton.setOnClickListener(v -> {
                String practiceType = mContentList.get(position).getPracticeType();
                if(practiceType.contentEquals("karton")){
                    NavigationManager.openPracticeWithInstructions(appCompatActivity, mContentList.get(position).getCode(), mContentList.get(position).getCodeExp(), false);
                }else if(practiceType.contentEquals("pen")){
                    NavigationManager.openPracticeWithInstructions(appCompatActivity, mContentList.get(position).getCode(), mContentList.get(position).getCodeExp(), true);
                }else{
                    Toast.makeText(appCompatActivity, appCompatActivity.getResources().getText(R.string.not_ready), Toast.LENGTH_LONG).show();
                }
            });
        }else {
            holder.mSeeCodeButton.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}

class ContentViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    TextView mDetail;
    CardView mCardView;
    Button mWatchButton;
    Button mPracticeButton;
    TextView mSeeCodeButton;

    ContentViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.content_card_thumbnail);
        mTitle = itemView.findViewById(R.id.content_card_title);
        mDetail = itemView.findViewById(R.id.content_card_detail);
        mWatchButton = itemView.findViewById(R.id.content_card_watch_video_button);
        mPracticeButton = itemView.findViewById(R.id.content_card_practice_button);
        mCardView = itemView.findViewById(R.id.content_card_view);
        mSeeCodeButton = itemView.findViewById(R.id.content_code_button);
    }
}