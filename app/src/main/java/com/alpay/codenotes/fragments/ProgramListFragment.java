package com.alpay.codenotes.fragments;

import static com.alpay.codenotes.models.GroupHelper.getGroupList;
import static com.alpay.codenotes.models.GroupHelper.getListSize;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.adapter.GroupViewAdapter;
import com.alpay.codenotes.models.CodeLineHelper;
import com.alpay.codenotes.models.GroupHelper;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

    @BindView(R.id.show_examples)
    FloatingActionButton showExamplesButton;

    @BindView(R.id.turtle_mode)
    AppCompatToggleButton turtleModeButton;

    @OnClick(R.id.turtle_mode)
    public void setupTurtleMode(){
        setupTurtleToggle(!Utils.turtleMode);
        CodeLineHelper.codeList = new ArrayList();
    }

    @BindView(R.id.new_program_button)
    FloatingActionButton newProgramButton;

    @OnClick(R.id.new_program_button)
    public void createNewProgram() {
        Intent intent = new Intent(getActivity(), FBVisionActivity.class);
        intent.putExtra(NavigationManager.BUNDLE_TURTLE, Utils.turtleMode);
        startActivity(intent);
    }


    @OnClick(R.id.show_examples)
    public void showExamples() {
        if (!isExampleButtonClicked) {
            generateExampleListFromGSON();
            showExamplesButton.setImageResource(R.drawable.ic_close);
            showExamplesButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
            newProgramButton.hide();
        } else {
            generateProgramListFromGSON();
            showExamplesButton.setImageResource(R.drawable.ic_menu);
            showExamplesButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            newProgramButton.show();
        }
        isExampleButtonClicked = !isExampleButtonClicked;
    }

    @Nullable
    @OnClick(R.id.transfer_learning_button)
    public void openTransferLearningModule() {
        NavigationManager.openTransferLearning((AppCompatActivity) getActivity());
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
        setupTurtleToggle(Utils.turtleMode);
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    private void setupTurtleToggle(Boolean turtleMode) {
        if (turtleMode) {
            showExamplesButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorCode)));
            newProgramButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorCode)));
        } else {
            showExamplesButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            newProgramButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
        Utils.turtleMode = turtleMode;
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
        GroupHelper.readFromAssets((AppCompatActivity) getActivity());
        if (recyclerView != null) {
            setUpRecyclerView();
            refreshCodeBlockRecyclerView(0);
        }
    }
}
