package com.alpay.codenotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Group;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    private AppCompatActivity appCompatActivity;
    private ArrayList<Group> groups;

    public GroupViewAdapter(AppCompatActivity appCompatActivity, ArrayList<Group> groups) {
        this.appCompatActivity = appCompatActivity;
        this.groups = groups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.section_custom_row_layout, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, int position) {
        final Group groupModel = groups.get(position);
        holder.sectionLabel.setText("Group: " + groupModel.getName());

        //recycler view for items
        holder.itemRecyclerView.removeAllViews();
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);

        ProgramViewAdapter adapter = new ProgramViewAdapter(appCompatActivity, groupModel.getProgramList(), groupModel.getName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
        holder.itemRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

}

class GroupViewHolder extends RecyclerView.ViewHolder {
    TextView sectionLabel;
    RecyclerView itemRecyclerView;

    GroupViewHolder(View itemView) {
        super(itemView);
        sectionLabel = itemView.findViewById(R.id.section_label);
        itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
    }
}
