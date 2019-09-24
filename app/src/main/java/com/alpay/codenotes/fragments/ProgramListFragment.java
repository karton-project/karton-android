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
import com.alpay.codenotes.activities.CodeBlocksResultActivity;
import com.alpay.codenotes.activities.CodeNotesCompilerActivity;
import com.alpay.codenotes.adapter.ProgramViewAdapter;
import com.alpay.codenotes.models.Program;
import com.alpay.codenotes.models.ProgramHelper;
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
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProgramListFragment extends Fragment {

    private View view;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Program> programList;
    private Unbinder unbinder;
    GridLayoutManager gridLayoutManager;
    ProgramViewAdapter programViewAdapter;

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
        Intent intent = new Intent(getActivity(), CodeNotesCompilerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.hourofcode_view)
    public void openHourOfCodeMode(){
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
        generateProgramList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        programViewAdapter = new ProgramViewAdapter((AppCompatActivity) getActivity(), programList);
        recyclerView.setAdapter(programViewAdapter);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    public void saveProgram(String name, String code){
        Program program = new Program();
        program.setCode(code);
        program.setName(name);
        ProgramHelper.addNewProgram(program);
        ProgramHelper.saveProgramList(getActivity());
        ProgramHelper.codeList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    protected void initFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("/users/" + BaseApplication.auth.getCurrentUser().getUid() + "/programList");
        databaseReference.keepSynced(true);
    }

    public void refreshCodeBlockRecyclerView(int position) {
        ProgramViewAdapter programViewAdapter = new ProgramViewAdapter((AppCompatActivity) getActivity(), programList);
        recyclerView.setAdapter(programViewAdapter);
        recyclerView.scrollToPosition(position);
        if (programList.size() <= 0){
            showEmptyScreenLayout();
        }else{
            hideEmptyScreenLayout();
            BaseApplication.user.setProgramList(programList);
        }
    }

    private void generateProgramListFromFirebase() {
        programList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Program program = dataSnapshot.getValue(Program.class);
                    programList.add(program);
                }
                if (recyclerView != null) {
                    setUpRecyclerView();
                    refreshCodeBlockRecyclerView((programList.size() > 0 ) ? programList.size() - 1 : 0);
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

    private void generateProgramList() {
        try {
            if (Utils.isInternetAvailable(getContext()) && (BaseApplication.user != null)) {
                initFirebase();
                generateProgramListFromFirebase();
            }else{
                programList = ProgramHelper.readProgramList(getActivity());
            }
            if (recyclerView != null) {
                setUpRecyclerView();
                refreshCodeBlockRecyclerView((programList.size() > 0 ) ? programList.size() - 1 : 0);
                progressBar.setVisibility(View.GONE);
            }
        } catch (FileNotFoundException e) {
            Log.e("Program", "generateProgramListFromGSON: ", e);
        }
    }
}
