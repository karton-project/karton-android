package com.alpay.codenotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.models.Function;
import com.alpay.codenotes.transfer.TransferLearningFragment;

import java.util.ArrayList;

import static com.alpay.codenotes.models.FunctionHelper.currentFunction;
import static com.alpay.codenotes.models.FunctionHelper.functionList;

public class FunctionViewAdapter extends RecyclerView.Adapter<FunctionViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private AppCompatActivity appCompatActivity;
    private ArrayList<Function> mFunctionList;


    public FunctionViewAdapter(AppCompatActivity appCompatActivity, ArrayList<Function> mFunctionList) {
        this.appCompatActivity = appCompatActivity;
        this.mFunctionList = mFunctionList;
    }

    @Override
    public FunctionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.functionblock_card, parent, false);
        return new FunctionViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final FunctionViewHolder holder, int position) {
        holder.mTitle.setText(mFunctionList.get(position).getName());
        holder.mImage.setImageBitmap(mFunctionList.get(position).getImage());
        holder.view.setOnClickListener(v -> {
            TransferLearningFragment.addSampleRequests.add(Integer.toString(position));
            currentFunction = position;
        });
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemoved(final int position) {
        functionList.remove(position);
        ((FBVisionActivity) appCompatActivity).refreshCodeBlockRecyclerView(position);
    }
}

class FunctionViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView mTitle;
    AppCompatImageView mImage;

    FunctionViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        mTitle = itemView.findViewById(R.id.function_text);
        mImage = itemView.findViewById(R.id.function_image);
    }
}