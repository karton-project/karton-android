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

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.CodeNotesCompilerOld;
import com.alpay.codenotes.adapter.GroupViewAdapter;
import com.alpay.codenotes.adapter.ProgramViewAdapter;
import com.alpay.codenotes.models.Group;
import com.alpay.codenotes.models.GroupHelper;
import com.alpay.codenotes.models.Program;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProgramListFragment extends Fragment {

    private View view;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private GroupViewAdapter groupViewAdapter;
    private ArrayList<Group> groupList;
    private Unbinder unbinder;

    @BindView(R.id.program_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.program_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_program_layout)
    LinearLayout emptyProgramLayout;

    @BindView(R.id.empty_program_image)
    ImageView imageView;

    @OnClick(R.id.new_program_button)
    public void createNewProgram() {
        Intent intent = new Intent(getActivity(), CodeNotesCompilerOld.class);
        startActivity(intent);
    }

    @OnClick(R.id.hourofcode_view)
    public void openHourOfCodeMode() {
        NavigationManager.openFlappyBirdHourOfCode((AppCompatActivity) getActivity());
    }

    public ProgramListFragment() {
        // default public constructor
    }

    public static ProgramListFragment newInstance() {
        return new ProgramListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_program, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (Utils.isInternetAvailable(getContext())) {
            generateProgramListFromFirebase();
        } else {
            generateProgramListFromGSON();
        }
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    public void saveProgram(String name, String code) {
        Program program = new Program();
        program.setCode(code);
        program.setName(name);
        int id = GroupHelper.getGroupIndex(Utils.groupId);
        groupList.get(id).getProgramList().add(program);
        GroupHelper.saveProgramList(getActivity(), groupList);
        GroupHelper.codeList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void refreshCodeBlockRecyclerView(int position) {
        groupViewAdapter = new GroupViewAdapter((AppCompatActivity) getActivity(), groupList);
        recyclerView.setAdapter(groupViewAdapter);
        recyclerView.scrollToPosition(position);
        if (groupList.size() <= 0) {
            showEmptyScreenLayout();
        } else {
            hideEmptyScreenLayout();
        }
    }

    private void generateProgramListFromFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("/users/" + BaseApplication.auth.getCurrentUser().getUid() + "/groupList");
        databaseReference.keepSynced(true);
        groupList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Group group = dataSnapshot.getValue(Group.class);
                    groupList.add(group);
                }
                if (recyclerView != null) {
                    setUpRecyclerView();
                    progressBar.setVisibility(View.GONE);
                    refreshCodeBlockRecyclerView((groupList.size() > 0) ? groupList.size() - 1 : 0);
                    GroupHelper.saveProgramList(getActivity(), groupList);
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
        groupList = GroupHelper.readProgramList(getActivity());
        if (recyclerView != null) {
            setUpRecyclerView();
            refreshCodeBlockRecyclerView((groupList.size() > 0) ? groupList.size() - 1 : 0);
            progressBar.setVisibility(View.GONE);
        }
    }
}
