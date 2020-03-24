/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alpay.codenotes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.ConfigurationUtils;
import com.alpay.codenotes.utils.Utils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.Scopes;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthUiActivity extends BaseActivity {

    private static final String TAG = "AuthUiActivity";

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";

    private static final int RC_SIGN_IN = 1212;

    @BindView(R.id.root)
    View mRootView;


    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, AuthUiActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_ui_layout);
        ButterKnife.bind(this);
        if (ConfigurationUtils.isGoogleMisconfigured(this)) {
            showSnackbar(R.string.configuration_required);
        }
    }

    @OnClick(R.id.sign_in)
    public void signIn() {
        startActivityForResult(buildSignInIntent(), RC_SIGN_IN);
    }

    @OnClick(R.id.continue_wo_sign_in)
    public void continueAnonymus() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    @NonNull
    public Intent buildSignInIntent() {
        AuthUI.SignInIntentBuilder builder = AuthUI.getInstance().createSignInIntentBuilder()
                .setTheme(getSelectedTheme())
                .setLogo(getSelectedLogo())
                .setAvailableProviders(getSelectedProviders());


        if (getSelectedTosUrl() != null && getSelectedPrivacyPolicyUrl() != null) {
            builder.setTosAndPrivacyPolicyUrls(
                    getSelectedTosUrl(),
                    getSelectedPrivacyPolicyUrl());
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        BaseApplication.userID = auth.getUid();

        if (auth.getCurrentUser() != null) {
            Utils.addBooleanToSharedPreferences(this, Utils.USER_LOGIN_KEY, true);
            Utils.addStringToSharedPreferences(this, Utils.USER_NAME_KEY, auth.getCurrentUser().getDisplayName());
            Utils.addStringToSharedPreferences(this, Utils.USER_EMAIL_KEY, auth.getCurrentUser().getEmail());
        }

        return builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null && getIntent().getExtras() == null) {
            startSignedInActivity(null);
            finish();
        }
    }

    private void handleSignInResponse(int resultCode, @Nullable Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startSignedInActivity(response);
            finish();
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    private void startSignedInActivity(@Nullable IdpResponse response) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @StyleRes
    private int getSelectedTheme() {
        return AuthUI.getDefaultTheme();
    }

    @DrawableRes
    private int getSelectedLogo() {
        return R.drawable.sb_byaz;
    }

    private List<IdpConfig> getSelectedProviders() {
        List<IdpConfig> selectedProviders = new ArrayList<>();

        selectedProviders.add(
                new IdpConfig.GoogleBuilder().setScopes(getGoogleScopes()).build());

        selectedProviders.add(new IdpConfig.EmailBuilder()
                .setRequireName(true)
                .setAllowNewAccounts(true)
                .build());

        return selectedProviders;
    }

    @Nullable
    private String getSelectedTosUrl() {
        return GOOGLE_TOS_URL;
    }

    @Nullable
    private String getSelectedPrivacyPolicyUrl() {
        return GOOGLE_PRIVACY_POLICY_URL;
    }


    private List<String> getGoogleScopes() {
        List<String> result = new ArrayList<>();
        result.add(Scopes.DRIVE_FILE);
        return result;
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

}
