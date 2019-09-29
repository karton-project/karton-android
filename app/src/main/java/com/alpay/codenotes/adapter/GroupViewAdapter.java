package com.alpay.codenotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Group;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.GroupViewHolder> {


    class GroupViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel, showAllButton;
        private RecyclerView itemRecyclerView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            sectionLabel = itemView.findViewById(R.id.section_label);
            showAllButton = itemView.findViewById(R.id.section_show_all_button);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private AppCompatActivity appCompatActivity;
    private ArrayList<Group> groupList;

    public GroupViewAdapter(AppCompatActivity appCompatActivity, ArrayList<Group> groupList) {
        this.appCompatActivity = appCompatActivity;
        this.groupList = groupList;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_custom_row_layout, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final Group groupModel = groupList.get(position);
        holder.sectionLabel.setText("Group: " + groupModel.getName());

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);

        ProgramViewAdapter adapter = new ProgramViewAdapter(appCompatActivity, groupModel.getProgramList());
        holder.itemRecyclerView.setAdapter(adapter);

        //show toast on click of show all button
        holder.showAllButton.setOnClickListener(v ->
                Toast.makeText(appCompatActivity, "You clicked on Show All of : " + groupModel.getName(), Toast.LENGTH_SHORT).show());

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


}
