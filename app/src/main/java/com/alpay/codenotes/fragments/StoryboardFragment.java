package com.alpay.codenotes.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.StoryViewActivity;
import com.alpay.codenotes.adapter.StoryViewAdapter;
import com.alpay.codenotes.models.Frame;
import com.alpay.codenotes.models.FrameHelper;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.view.utils.MarginDecoration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StoryboardFragment extends Fragment {

    private View view;
    private List<Frame> frameList = new ArrayList<>();
    private StoryViewAdapter storyViewAdapter;
    private String directoryPath;
    private String fileName;
    private Unbinder unbinder;
    public static final int PICK_IMAGE = 1;
    private static int currentFrameID = -1;
    private boolean frameLayoutVisible = false;

    @BindView(R.id.new_frame_layout)
    RelativeLayout newFrameLayout;

    @BindView(R.id.storyboard_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.storyboard_progressbar)
    ProgressBar progressBar;

    @OnClick(R.id.add_new_frame_button)
    public void showNewFrameOptions(){
        if (!frameLayoutVisible){
            newFrameLayout.setVisibility(View.VISIBLE);
            newFrameLayout.bringToFront();
            frameLayoutVisible = true;
        }else{
            newFrameLayout.setVisibility(View.GONE);
            frameLayoutVisible = false;
        }
    }

    @OnClick(R.id.run_button)
    public void startStoryView(){
        Intent intent = new Intent(getActivity(), StoryViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.new_chapter_button)
    public void createNewChapterHeader(){
        Frame.addNewEmptyHeader();
        refreshStoryBoard(frameList.size() - 1);
        newFrameLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.new_frame_button)
    public void createNewFrame(){
        Frame.addNewEmptyFrame();
        refreshStoryBoard(frameList.size() - 1);
        newFrameLayout.setVisibility(View.GONE);
    }

    public StoryboardFragment() {
        // Required empty public constructor
    }

    public static void setCurrentFrameID(int frameID) {
        currentFrameID = frameID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_storyboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        frameList = Frame.listAll();
        if(frameList.size() == 0){
            Frame.initEmptyStoryBoard((AppCompatActivity) getActivity());
            FrameHelper.saveFrameList(this.getContext());
        }
        setUpRecyclerView();
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStoryBoard(currentFrameID-1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            fileName = "stry" + currentFrameID;
            if (!directoryPath.isEmpty()) {
                directoryPath += "/CodeNotes/Drawable";
            } else {
                directoryPath = "storage/self/primary/CodeNotes/Drawable";
            }
            if (data != null) {
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = Utils.decodeSampledBitmapFromStream(inputStream, 600, 600);
                    File file = new File(directoryPath + "/" + fileName);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.flush();
                    fo.close();
                    Frame.updateFrameImageName(currentFrameID, fileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setUpRecyclerView() {
        recyclerView.addItemDecoration(new MarginDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    public void refreshStoryBoard(int position){
        frameList = Frame.listAll();
        storyViewAdapter = new StoryViewAdapter(this, frameList);
        recyclerView.setAdapter(storyViewAdapter);
        recyclerView.scrollToPosition(position);
        progressBar.setVisibility(View.GONE);
    }

}
