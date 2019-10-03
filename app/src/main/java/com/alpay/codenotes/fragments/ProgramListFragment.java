package com.alpay.codenotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.CodeNotesCompilerActivity;
import com.alpay.codenotes.adapter.GroupViewAdapter;
import com.alpay.codenotes.models.Group;
import com.alpay.codenotes.models.GroupHelper;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.alpay.codenotes.BaseApplication.userID;
import static com.alpay.codenotes.models.GroupHelper.getGroupList;
import static com.alpay.codenotes.models.GroupHelper.getListSize;
import static com.alpay.codenotes.models.GroupHelper.setGroupList;

public class ProgramListFragment extends Fragment {

    private View view;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private GroupViewAdapter groupViewAdapter;
    private Unbinder unbinder;

    @BindView(R.id.program_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.program_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_program_layout)
    LinearLayout emptyProgramLayout;

    @BindView(R.id.empty_program_image)
    ImageView imageView;

    @BindView(R.id.hourofcode_view)
    RelativeLayout hourOfCodeView;

    @OnClick(R.id.close_flappy_bird)
    public void closeFlappyBirdAnnouncement(){
        hourOfCodeView.setVisibility(View.GONE);
        Utils.addBooleanToSharedPreferences((AppCompatActivity) getActivity(), Utils.CLOSE_FLAPPY, true);
    }

    @OnClick(R.id.new_program_button)
    public void createNewProgram() {
        Intent intent = new Intent(getActivity(), CodeNotesCompilerActivity.class);
        startActivity(intent);
    }

    @Nullable
    @OnClick(R.id.hourofcode_view)
    public void openHourOfCodeMode() {
        NavigationManager.openFlappyBirdHourOfCode((AppCompatActivity) getActivity());
    }

    public ProgramListFragment() {
        // default public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_program, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (GroupHelper.getListSize() <= 0){
            if (Utils.isInternetAvailable(getContext()) && userID!= null) {
                generateProgramListFromFirebase();
            } else {
                generateProgramListFromGSON();
            }
        }else{
            generateProgramListFromGSON();
        }
        if (Utils.getBooleanFromSharedPreferences((AppCompatActivity) getActivity(), Utils.CLOSE_FLAPPY)){
            hourOfCodeView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void refreshCodeBlockRecyclerView(int position) {
        groupViewAdapter = new GroupViewAdapter((AppCompatActivity) getActivity(), getGroupList());
        recyclerView.setAdapter(groupViewAdapter);
        recyclerView.scrollToPosition(position);
        if (getListSize() <= 0) {
            showEmptyScreenLayout();
        } else {
            hideEmptyScreenLayout();
        }
    }

    private void generateProgramListFromFirebase() {
        setGroupList(new ArrayList<>());
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("/users/" + BaseApplication.auth.getCurrentUser().getUid() + "/groupList");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Group group = dataSnapshot.getValue(Group.class);
                    GroupHelper.addNewGroup(group);
                }
                if (recyclerView != null) {
                    setUpRecyclerView();
                    progressBar.setVisibility(View.GONE);
                    refreshCodeBlockRecyclerView(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    public void showEmptyScreenLayout() {
        emptyProgramLayout.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load("file:///android_asset/lottie/empty.gif").into(imageView);
    }


    private void hideEmptyScreenLayout() {
        emptyProgramLayout.setVisibility(View.GONE);
    }

    private void generateProgramListFromGSON() {
        GroupHelper.readProgramList(getActivity());
        if (recyclerView != null) {
            setUpRecyclerView();
            refreshCodeBlockRecyclerView(0);
            progressBar.setVisibility(View.GONE);
        }
    }
}
