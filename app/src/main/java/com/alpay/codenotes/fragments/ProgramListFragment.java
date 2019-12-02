package com.alpay.codenotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.CodeNotesCompilerActivity;
import com.alpay.codenotes.adapter.GroupViewAdapter;
import com.alpay.codenotes.models.GroupHelper;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.alpay.codenotes.models.GroupHelper.getGroupList;
import static com.alpay.codenotes.models.GroupHelper.getListSize;

public class ProgramListFragment extends Fragment {

    private View view;
    private GroupViewAdapter groupViewAdapter;
    private Unbinder unbinder;
    private boolean isExampleButtonClicked = false;

    @BindView(R.id.program_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.program_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_program_layout)
    LinearLayout emptyProgramLayout;

    @BindView(R.id.hourofcode_view)
    RelativeLayout hourOfCodeView;

    @BindView(R.id.show_examples)
    FloatingActionButton showExamplesButton;

    @OnClick(R.id.close_flappy_bird)
    public void closeFlappyBirdAnnouncement() {
        hourOfCodeView.setVisibility(View.GONE);
        Utils.addBooleanToSharedPreferences((AppCompatActivity) getActivity(), Utils.CLOSE_FLAPPY, true);
    }

    @OnClick(R.id.new_program_button)
    public void createNewProgram() {
        Intent intent = new Intent(getActivity(), CodeNotesCompilerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.show_examples)
    public void showExamples(){
        if (!isExampleButtonClicked){
            generateExampleListFromGSON();
            showExamplesButton.setImageResource(R.drawable.ic_close);
        }else{
            generateProgramListFromGSON();
            showExamplesButton.setImageResource(R.drawable.ic_menu);
        }
        isExampleButtonClicked = !isExampleButtonClicked;
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
        generateProgramListFromGSON();
        setUpRecyclerView();
        refreshCodeBlockRecyclerView(0);
        if (Utils.getBooleanFromSharedPreferences((AppCompatActivity) getActivity(), Utils.CLOSE_FLAPPY)) {
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
        progressBar.setVisibility(View.GONE);
        if (getListSize() <= 0) {
            showEmptyScreenLayout();
        } else {
            hideEmptyScreenLayout();
        }
    }

    public void showEmptyScreenLayout() {
        emptyProgramLayout.setVisibility(View.VISIBLE);
    }


    private void hideEmptyScreenLayout() {
        emptyProgramLayout.setVisibility(View.GONE);
    }

    private void generateProgramListFromGSON() {
        GroupHelper.readProgramList(getActivity());
        if (recyclerView != null) {
            setUpRecyclerView();
            refreshCodeBlockRecyclerView(0);
        }
    }

    private void generateExampleListFromGSON() {
        GroupHelper.readFromAssets(getActivity());
        if (recyclerView != null) {
            setUpRecyclerView();
            refreshCodeBlockRecyclerView(0);
        }
    }
}
