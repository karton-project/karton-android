package com.alpay.codenotes.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Frame;
import com.alpay.codenotes.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoryViewActivity extends BaseActivity {

    @BindView(R.id.storyview_imageholder)
    ImageView imageView;

    @BindView(R.id.question_layout)
    LinearLayout questionLayout;

    @BindView(R.id.question_layout_text)
    TextView questionLayoutText;

    @BindView(R.id.question_layout_answer)
    EditText questionLayoutEditText;

    @OnClick(R.id.question_layout_button)
    public void answerAction(){
        answer = questionLayoutEditText.getText().toString();
        handler.postDelayed(imageChanger, 0);
        questionLayout.setVisibility(View.GONE);
    }

    public static final int DEFAULT_DELAY = 5000;
    private List<Frame> frameList;
    private final Handler handler = new Handler();
    private final ImageChanger imageChanger = new ImageChanger();
    private final Finisher finisher = new Finisher();
    private Iterator iterator;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);
        ButterKnife.bind(this);
        frameList = Frame.listAll();
        iterator = frameList.iterator();
        imageChanger.run();
    }

    private class ImageChanger implements Runnable {
        @Override
        public void run() {
            setNextImage();
        }
    }

    private class Finisher implements Runnable {
        @Override
        public void run() {
            finish();
        }

    }

    private void setNextImage() {
        while (iterator.hasNext()) {
            Frame frame = (Frame) iterator.next();
            String generatedCode = frame.getFrameGeneratedCode();
            HashMap<String, String> outputMap = createOutputMap(generatedCode);
            if (frame.getFrameImageName() != null) {
                imageView.setImageDrawable(Utils.getDrawableWithName(this, frame.getFrameImageName()));
                if (outputMap.containsKey("duration")) {
                    handler.postDelayed(imageChanger, Integer.valueOf(outputMap.get("duration")));
                } else if (outputMap.containsKey("question")) {
                    questionLayout.setVisibility(View.VISIBLE);
                    questionLayoutText.setText(outputMap.get("question"));
                    return;
                } else {
                    handler.postDelayed(imageChanger, DEFAULT_DELAY);
                }
                return;
            }
        }
        finisher.run();
    }

    private HashMap<String, String> createOutputMap(String generatedCode) {
        HashMap<String, String> outputMap = new HashMap<>();
        String[] tokens = generatedCode.split("\\, ");
        for (int i = 0; i < tokens.length - 1; i = i + 2) {
            outputMap.put(tokens[i], tokens[i + 1]);
        }
        return outputMap;
    }
}
