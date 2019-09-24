package com.alpay.codenotes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Feedback;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SendFeedbackFragment extends Fragment {

    private View view;
    private Unbinder unbinder;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @BindView(R.id.feedbackTitleEditText)
    EditText feedbackTitleEditText;

    @BindView(R.id.feedbackDetailEditText)
    EditText feedbackDetailEditText;

    @BindView(R.id.formView)
    ScrollView formView;

    @BindView(R.id.successView)
    LinearLayout successView;

    public SendFeedbackFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_send_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.keepSynced(true);
    }

    @Nullable
    @OnClick(R.id.sendFeedBackButton)
    protected void sendFeedBack() {
        String feedbackTitle = feedbackTitleEditText.getText().toString();
        String feedbackDetail = feedbackDetailEditText.getText().toString();

        if (feedbackTitle.equals("")) {
            Utils.showWarningToast((AppCompatActivity) getActivity(), R.string.sendfeedback_formtitle_required, Toast.LENGTH_SHORT);
            return;
        }
        if (feedbackDetail.equals("")) {
            Utils.showWarningToast((AppCompatActivity) getActivity(), R.string.sendfeedback_formcontent_required, Toast.LENGTH_SHORT);
            return;
        }
        sendFeedbackToFirebase(feedbackTitle, feedbackDetail);
        changeToSuccessView();
    }

    @Nullable
    @OnClick(R.id.feedbackSentOkButton)
    protected void backToLatestScreen() {
        NavigationManager.openFragment((AppCompatActivity) getActivity(), NavigationManager.CONTENT);
    }

    protected void sendFeedbackToFirebase(String title, String detail) {
        Feedback feedback = new Feedback(title, detail);
        databaseReference.child("userFeedback").push().setValue(feedback);
    }

    protected void changeToSuccessView() {
        formView.setVisibility(View.GONE);
        successView.setVisibility(View.VISIBLE);
    }

}
