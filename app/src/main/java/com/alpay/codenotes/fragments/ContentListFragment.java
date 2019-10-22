package com.alpay.codenotes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.ContentViewAdapter;
import com.alpay.codenotes.models.Content;
import com.alpay.codenotes.models.ContentHelper;
import com.alpay.codenotes.utils.NavigationManager;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContentListFragment extends Fragment {

    private View view;
    private ArrayList<Content> contentArrayList;
    private Unbinder unbinder;
    GridLayoutManager gridLayoutManager;

    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.content_progress_bar)
    ProgressBar progressBar;

    @OnClick(R.id.take_note_button)
    public void startTakeNoteAction() {
        NavigationManager.openFragment((AppCompatActivity) getActivity(), NavigationManager.NOTES);
    }

    public ContentListFragment() {
        // default public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        generateContentListFromGSON();
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void populateRecyclerView() {
        final ContentViewAdapter adapter = new ContentViewAdapter((AppCompatActivity) getActivity(), contentArrayList);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private void generateContentListFromGSON() {
        contentArrayList = ContentHelper.readFromAssets(getActivity());
        if (recyclerView != null) {
            setUpRecyclerView();
            populateRecyclerView();
        }
    }
}
