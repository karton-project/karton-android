package com.alpay.codenotes.adapter;

import android.content.Intent;
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
import com.alpay.codenotes.activities.CodeBlocksResultActivity;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.models.CodeLine;
import com.alpay.codenotes.models.CodeLineHelper;
import com.alpay.codenotes.models.GroupHelper;
import com.alpay.codenotes.models.Program;
import com.alpay.codenotes.utils.Utils;

import java.util.ArrayList;

import static com.alpay.codenotes.models.CodeLineHelper.codeList;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_CODE_KEY;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_TURTLE;


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
        String p5Code = "";
        for (CodeLine codeLine : mProgramList.get(position).getCode()) {
            p5Code = p5Code + CodeLineHelper.prettyPrintCodeLine(codeLine);
        }
        if (mProgramList.get(position).isTurtle())
            holder.mCardView.setBackgroundColor(appCompatActivity.getResources().getColor(R.color.colorCode));
        else
            holder.mCardView.setBackgroundColor(appCompatActivity.getResources().getColor(R.color.colorAccent));
        holder.mTitle.setText(mProgramList.get(position).getName());
        holder.mDetail.setText(p5Code);
        holder.mRunButton.setOnClickListener(v -> {
            Intent intent = new Intent(appCompatActivity, CodeBlocksResultActivity.class);
            String[] p5CodeArr = (String[]) mProgramList.get(position).getCodeArray();
            intent.putExtra(BUNDLE_CODE_KEY, p5CodeArr);
            intent.putExtra(BUNDLE_TURTLE, mProgramList.get(position).isTurtle());
            appCompatActivity.startActivity(intent);
        });
        holder.mChangeButton.setOnClickListener(v -> {
            codeList = new ArrayList<>();
            codeList.addAll(mProgramList.get(position).getCode());
            Intent intent = new Intent(appCompatActivity, FBVisionActivity.class);
            appCompatActivity.startActivity(intent);
        });
        holder.mAddToCodeButton.setOnClickListener(v -> {
            codeList.addAll(mProgramList.get(position).getCode());
            Toast.makeText(appCompatActivity, R.string.code_added, Toast.LENGTH_SHORT).show();
        });
        holder.mDeleteButton.setOnClickListener(v -> {
            GroupHelper.deleteProgram(appCompatActivity, parentName, position);
            this.notifyDataSetChanged();
        });
        if (mProgramList.get(position).getBitmap() != null){
            holder.mImage.setImageBitmap(Utils.bitmapFromBase64(mProgramList.get(position).getBitmap()));
        }
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
    Button mAddToCodeButton;
    Button mDeleteButton;

    ProgramViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.program_thumbnail);
        mTitle = itemView.findViewById(R.id.program_card_title);
        mDetail = itemView.findViewById(R.id.program_card_detail);
        mChangeButton = itemView.findViewById(R.id.program_change_code);
        mDeleteButton = itemView.findViewById(R.id.program_delete_code);
        mAddToCodeButton = itemView.findViewById(R.id.program_addto_code);
        mRunButton = itemView.findViewById(R.id.program_run_code);
        mCardView = itemView.findViewById(R.id.program_card_view);
    }
}