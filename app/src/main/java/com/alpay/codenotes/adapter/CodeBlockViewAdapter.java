package com.alpay.codenotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.models.CodeLine;
import com.alpay.codenotes.models.CodeLineHelper;
import com.alpay.codenotes.view.CodeBlockDetailDialog;

import java.util.Collections;

import static com.alpay.codenotes.models.CodeLineHelper.codeList;

public class CodeBlockViewAdapter extends RecyclerView.Adapter<CodeBlockViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private AppCompatActivity appCompatActivity;


    public  CodeBlockViewAdapter(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public CodeBlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.codeblock_card, parent, false);
        return new CodeBlockViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final CodeBlockViewHolder holder, int position) {
        holder.mTitle.setText(CodeLineHelper.prettyPrintCodeLine(codeList.get(position)));
        holder.mCardView.setBackgroundTintList(appCompatActivity.getResources().getColorStateList(R.color.command_color));
        holder.mCardView.setOnClickListener(v -> {
            showDialogPrompt(codeList.get(position), position);
        });
    }

    public void showDialogPrompt(CodeLine codeLine, int position){
        CodeBlockDetailDialog codeBlockDetailDialog = new CodeBlockDetailDialog(appCompatActivity, codeLine, position);
        codeBlockDetailDialog.show();
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
    LinearLayout mCardView;

    CodeBlockViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        mTitle = itemView.findViewById(R.id.codeblock_text);
        mCardView = itemView.findViewById(R.id.codeblock_standard);
    }
}