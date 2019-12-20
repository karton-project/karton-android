package com.alpay.codenotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.AuthUiActivity;
import com.alpay.codenotes.utils.Constants;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.firebase.ui.auth.AuthUI.EMAIL_LINK_PROVIDER;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";
    private View view;
    private Unbinder unbinder;
    private FirebaseAuth auth;

    @BindView(R.id.user_profile_picture)
    ImageView mUserProfilePicture;
    @BindView(R.id.user_email)
    TextView mUserEmail;
    @BindView(R.id.user_display_name)
    TextView mUserDisplayName;
    @BindView(R.id.account_view)
    RelativeLayout mAccountView;
    @BindView(R.id.sign_in_view)
    RelativeLayout mSignInView;
    @BindView(R.id.change_language_icon)
    ImageView languageIcon;

    @OnClick(R.id.my_notes)
    public void openNotes(){
        NavigationManager.openFragment((AppCompatActivity) getActivity(), NavigationManager.NOTES);
    }

    @OnClick(R.id.change_language)
    public void openPrograms(){
        if (Utils.getStringFromSharedPreferences((AppCompatActivity) getActivity(), "CODE_LANG").contentEquals("TR")){
            Utils.addStringToSharedPreferences((AppCompatActivity) getActivity(), "CODE_LANG", "UK");
            languageIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_uk));
        }else{
            Utils.addStringToSharedPreferences((AppCompatActivity) getActivity(), "CODE_LANG", "TR");
            languageIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_tr));
        }
    }

    @OnClick(R.id.sign_in)
    public void signIn(){
        Intent intent = new Intent(getActivity(), AuthUiActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.project_website)
    public void openWebpage(){
        NavigationManager.openWebViewFragment((AppCompatActivity) getActivity(), Constants.PROJECT_WEBSITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.signed_in_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (Utils.getStringFromSharedPreferences((AppCompatActivity) getActivity(), "CODE_LANG").contentEquals("UK")){
            languageIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_uk));
        }else{
            languageIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_tr));
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            mAccountView.setVisibility(View.GONE);
            mSignInView.setVisibility(View.VISIBLE);
        } else {
            mAccountView.setVisibility(View.VISIBLE);
            mSignInView.setVisibility(View.GONE);
            populateProfile(currentUser);
        }
        return view;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    @OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        BaseApplication.userID = null;
                        startActivity(AuthUiActivity.createIntent(getActivity()));
                        getActivity().finish();
                    } else {
                        Log.w(TAG, "signOut:failure", task.getException());
                        showSnackbar(R.string.sign_out_failed);
                    }
                });
    }

    private void populateProfile(FirebaseUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .fitCenter()
                    .into(mUserProfilePicture);
        }

        mUserEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
        mUserDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());

        List<String> providers = new ArrayList<>();
        if (user.getProviderData().isEmpty()) {
            providers.add(getString(R.string.providers_anonymous));
        } else {
            for (UserInfo info : user.getProviderData()) {
                switch (info.getProviderId()) {
                    case GoogleAuthProvider.PROVIDER_ID:
                        providers.add(getString(R.string.providers_google));
                        break;
                    case EmailAuthProvider.PROVIDER_ID:
                        providers.add(getString(R.string.providers_email));
                        break;
                    case PhoneAuthProvider.PROVIDER_ID:
                        providers.add(getString(R.string.providers_phone));
                        break;
                    case EMAIL_LINK_PROVIDER:
                        providers.add(getString(R.string.providers_email_link));
                        break;
                    case FirebaseAuthProvider.PROVIDER_ID:
                        // Ignore this provider, it's not very meaningful
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown provider: " + info.getProviderId());
                }
            }
        }
    }


    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(view, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
