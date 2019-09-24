package com.alpay.codenotes.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.ContentViewAdapter;
import com.alpay.codenotes.models.Content;
import com.alpay.codenotes.models.ContentHelper;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContentListFragment extends Fragment {

    private View view;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Content> contentArrayList;
    private Unbinder unbinder;
    GridLayoutManager gridLayoutManager;

    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.content_progress_bar)
    ProgressBar progressBar;

    @OnClick(R.id.take_note_button)
    public void startTakeNoteAction(){
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
        if(Utils.isInternetAvailable(getContext()) && BaseApplication.connectInternetToGetDB){
            initFirebase();
            generateContentListFromFirebase();
        }else{
            generateContentListFromGSON();
            if (!Utils.noConnectionErrorDisplayed){
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.no_internet_connection)
                        .setMessage(R.string.no_internet_dialog_message)
                        .setNeutralButton(android.R.string.ok, (dialog, which) -> {
                            // Continue with operation
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                Utils.noConnectionErrorDisplayed = true;
            }
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
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    protected void initFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("tr/contents");
        databaseReference.keepSynced(true);
    }

    private void populateRecyclerView() {
        final ContentViewAdapter adapter = new ContentViewAdapter((AppCompatActivity) getActivity(), contentArrayList);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private void generateContentListFromFirebase() {
        contentArrayList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Content content = dataSnapshot.getValue(Content.class);
                    contentArrayList.add(content);
                }
                if (recyclerView != null){
                    setUpRecyclerView();
                    populateRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    private void generateContentListFromGSON() {
        try {
            contentArrayList = ContentHelper.readContentList(getActivity());
            if (recyclerView != null){
                setUpRecyclerView();
                populateRecyclerView();
            }
        } catch (FileNotFoundException e){
            Log.e("Content", "generateContentListFromGSON: ", e);
        }
    }
}
