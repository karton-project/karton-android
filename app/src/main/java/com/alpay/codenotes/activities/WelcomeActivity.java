package com.alpay.codenotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.listener.OnSwipeTouchListener;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.welcome_frame)
    FrameLayout welcomeFrame;
    @BindView(R.id.welcome_next_button)
    ImageView nextButton;
    @BindView(R.id.welcome_previous_button)
    ImageView previousButton;

    Animation fadeInAnimation;

    int[] pageArray = {
            R.layout.welcome_page_1,
            R.layout.welcome_page_2,
            R.layout.welcome_page_3,
            R.layout.welcome_page_4,
            R.layout.welcome_page_5,
            R.layout.welcome_page_6,
            R.layout.welcome_page_7,
            R.layout.welcome_page_8,
            R.layout.welcome_page_9
    };
    int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousPage();
            }
        });
        changeFrameContent(pageArray[0]);
        welcomeFrame.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                nextPage();
                super.onSwipeLeft();
            }

            @Override
            public void onSwipeRight() {
                previousPage();
                super.onSwipeRight();
            }
        });
    }

    protected void changeFrameContent(int frameID) {
        welcomeFrame.removeAllViews();
        LayoutInflater.from(this).inflate(frameID, welcomeFrame, true);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        findViewById(R.id.page_text).setAnimation(fadeInAnimation);
        if (pageIndex == 0) {
            previousButton.setVisibility(View.GONE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
        }

        if (pageIndex == pageArray.length - 1) {
            nextButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_ok));
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }

    }

    public void nextPage() {
        if (pageIndex < pageArray.length - 1 && pageIndex >= 0) {
            pageIndex++;
            changeFrameContent(pageArray[pageIndex]);
        } else {
            endTutorial();
        }
    }

    public void previousPage() {
        if (pageIndex <= pageArray.length && pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        changeFrameContent(pageArray[pageIndex]);
    }

    private void endTutorial() {
        if (!isFinishing()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

}
