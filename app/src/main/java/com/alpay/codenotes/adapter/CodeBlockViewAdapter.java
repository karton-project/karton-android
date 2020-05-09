package com.alpay.codenotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.FBVisionActivity;

import java.util.ArrayList;
import java.util.Collections;

import static com.alpay.codenotes.models.GroupHelper.codeList;

public class CodeBlockViewAdapter extends RecyclerView.Adapter<CodeBlockViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private AppCompatActivity appCompatActivity;
    private ArrayList<String> mContentList;


    public  CodeBlockViewAdapter(AppCompatActivity appCompatActivity, ArrayList<String> mContentList) {
        this.appCompatActivity = appCompatActivity;
        this.mContentList = mContentList;
    }

    @Override
    public CodeBlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.codeblock_card, parent, false);
        return new CodeBlockViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final CodeBlockViewHolder holder, int position) {
        holder.mTitle.setText(mContentList.get(position));

        holder.mTitle.setOnClickListener(v -> {
            holder.mTitle.setVisibility(View.GONE);
            holder.mEditLayout.setVisibility(View.VISIBLE);
            holder.mTextEdit.setText(codeList.get(position));
        });

        holder.mNewTextButton.setOnClickListener(v -> {
            codeList.set(position, holder.mTextEdit.getText().toString());
            holder.mTitle.setVisibility(View.VISIBLE);
            holder.mEditLayout.setVisibility(View.GONE);
            ((FBVisionActivity) appCompatActivity).refreshCodeBlockRecyclerView(position);
        });
    }

    @Override
    public int getItemCount() {
        return codeList.size();
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(codeList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(codeList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemoved(final int position) {
        codeList.remove(position);
        ((FBVisionActivity) appCompatActivity).refreshCodeBlockRecyclerView(position);
    }
}

class CodeBlockViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView mTitle;
    AppCompatEditText mTextEdit;
    ImageButton mNewTextButton;
    LinearLayout mEditLayout;
    CardView mCardView;

    CodeBlockViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        mTitle = itemView.findViewById(R.id.codeblock_text);
        mTextEdit = itemView.findViewById(R.id.codeblock_text_edit);
        mNewTextButton = itemView.findViewById(R.id.codeblock_text_input_button);
        mEditLayout = itemView.findViewById(R.id.codeblock_edit);
        mCardView = itemView.findViewById(R.id.codeblock_card);
    }
}