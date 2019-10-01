package com.alpay.codenotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.CodeNotesCompilerOld;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.alpay.codenotes.models.GroupHelper.codeList;


public class CodeBlockViewAdapter extends RecyclerView.Adapter<CodeBlockViewHolder> {

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
        holder.mDeleteButton.setOnClickListener(v -> {
            codeList.remove(position);
            ((CodeNotesCompilerOld) appCompatActivity).refreshCodeBlockRecyclerView(position);
        });

        holder.mTitle.setOnClickListener(v -> {
            holder.mTitle.setVisibility(View.GONE);
            holder.mTextEditLayout.setVisibility(View.VISIBLE);
            holder.mTextEdit.setText(codeList.get(position));
        });

        holder.mNewTextButton.setOnClickListener(v -> {
            codeList.set(position, holder.mTextEdit.getText().toString());
            holder.mTitle.setVisibility(View.VISIBLE);
            holder.mTextEditLayout.setVisibility(View.GONE);
            ((CodeNotesCompilerOld) appCompatActivity).refreshCodeBlockRecyclerView(position);
        });
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}

class CodeBlockViewHolder extends RecyclerView.ViewHolder {

    TextView mTitle;
    AppCompatEditText mTextEdit;
    ImageButton mDeleteButton;
    ImageView mNewTextButton;
    RelativeLayout mTextEditLayout;
    CardView mCardView;

    CodeBlockViewHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.codeblock_text);
        mTextEdit = itemView.findViewById(R.id.codeblock_text_edit);
        mNewTextButton = itemView.findViewById(R.id.codeblock_text_input_button);
        mDeleteButton = itemView.findViewById(R.id.codeblock_delete);
        mTextEditLayout = itemView.findViewById(R.id.codeblock_text_input_view);
        mCardView = itemView.findViewById(R.id.codeblock_card);
    }
}