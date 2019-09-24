package com.alpay.codenotes.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpay.codenotes.R;
import com.alpay.codenotes.view.FullScreenVideoView;

import androidx.fragment.app.Fragment;


public class FullScreenVideoFragment extends Fragment {

    private static FullScreenVideoView fullScreenVideoView;
    private static Uri videoURI;
    private ProgressDialog progressDialog;

    public FullScreenVideoFragment() {
    }

    public static FullScreenVideoFragment newInstance(String videoLink) {
        FullScreenVideoFragment fragment = new FullScreenVideoFragment();
        setAnimation(videoLink);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animation, container, false);
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title), getResources().getString(R.string.progress_content), true);
        fullScreenVideoView = view.findViewById(R.id.animationContainer);
        fullScreenVideoView.setVideoURI(videoURI);
        fullScreenVideoView.setOnPreparedListener(mp -> progressDialog.dismiss());
        fullScreenVideoView.setOnCompletionListener(vmp -> {
            // Do something
        });
        fullScreenVideoView.start();
        return view;
    }

    public static void setAnimation(String videoLink) {
        videoURI = Uri.parse(videoLink);
    }

}
