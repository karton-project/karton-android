package com.alpay.codenotes.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.CodeBlocksResultActivity;
import com.alpay.codenotes.activities.CodeNotesCompilerActivity;
import com.alpay.codenotes.models.GroupHelper;
import com.alpay.codenotes.models.Program;
import com.alpay.codenotes.utils.NavigationManager;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_CODE_KEY;


public class ProgramViewAdapter extends RecyclerView.Adapter<ProgramViewHolder> {

    private AppCompatActivity appCompatActivity;
    private ArrayList<Program> mProgramList;
    private String parentName;

    public ProgramViewAdapter(AppCompatActivity appCompatActivity, ArrayList<Program> mContentList, String parentName) {
        this.appCompatActivity = appCompatActivity;
        this.mProgramList = mContentList;
        this.parentName = parentName;
    }

    @Override
    public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.program_card_layout, parent, false);
        return new ProgramViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProgramViewHolder holder, int position) {
        String p5Code = mProgramList.get(position).getCode();
        holder.mTitle.setText(mProgramList.get(position).getName());
        holder.mDetail.setText(p5Code);
        holder.mRunButton.setOnClickListener(v -> {
            Intent intent = new Intent(appCompatActivity, CodeBlocksResultActivity.class);
            String[] p5CodeArr = p5Code.split("\n");
            intent.putExtra(BUNDLE_CODE_KEY, p5CodeArr);
            appCompatActivity.startActivity(intent);
        });
        holder.mChangeButton.setOnClickListener(v -> {
            Intent intent = new Intent(appCompatActivity, CodeNotesCompilerActivity.class);
            String[] p5CodeArr = p5Code.split("\n");
            intent.putExtra(BUNDLE_CODE_KEY, p5CodeArr);
            appCompatActivity.startActivity(intent);
        });
        holder.mDeleteButton.setOnClickListener(v -> {
            GroupHelper.deleteProgram(appCompatActivity, parentName, position);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if (mProgramList != null)
            return mProgramList.size();
        else
            return 0;
    }
}

class ProgramViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    TextView mDetail;
    CardView mCardView;
    Button mChangeButton;
    Button mRunButton;
    Button mDeleteButton;

    ProgramViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.program_card_thumbnail);
        mTitle = itemView.findViewById(R.id.program_card_title);
        mDetail = itemView.findViewById(R.id.program_card_detail);
        mChangeButton = itemView.findViewById(R.id.program_change_code);
        mDeleteButton = itemView.findViewById(R.id.program_delete_code);
        mRunButton = itemView.findViewById(R.id.program_run_code);
        mCardView = itemView.findViewById(R.id.program_card_view);
    }
}