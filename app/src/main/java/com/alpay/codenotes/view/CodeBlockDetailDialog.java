package com.alpay.codenotes.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.CodeLine;
import com.alpay.codenotes.models.CodeLineHelper;

public class CodeBlockDetailDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    private LinearLayout cardView;
    private CodeLine codeLine;
    private View codeResult;
    private TextView colorRVal, colorGVal, colorBVal;
    private int seekR, seekG, seekB, seekX, seekY, seekW, seekH;

    public CodeBlockDetailDialog(Activity a, CodeLine codeLine) {
        super(a);
        this.c = a;
        this.codeLine = codeLine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.codeblock_detail_card);

        cardView = findViewById(R.id.codeblock_detail_card);

        TextView codeTitleTW = findViewById(R.id.codedetail_text);
        codeTitleTW.setText(codeLine.getCommand());

        int[] rgb = CodeLineHelper.extractRGB(codeLine);

        colorRVal = findViewById(R.id.color_rval);
        colorRVal = findViewById(R.id.color_gval);
        colorRVal = findViewById(R.id.color_bval);

        codeResult = findViewById(R.id.codedetail_color_box);

        SeekBar sbR = findViewById(R.id.color_r);
        SeekBar sbG = findViewById(R.id.color_g);
        SeekBar sbB = findViewById(R.id.color_b);

        sbR.setProgress(rgb[0]); colorRVal.setText(rgb[0]);
        sbG.setProgress(rgb[1]); colorGVal.setText(rgb[1]);
        sbB.setProgress(rgb[2]); colorBVal.setText(rgb[2]);

        SeekBar sbX = findViewById(R.id.shape_x);
        SeekBar sbY = findViewById(R.id.shape_y);
        SeekBar sbW = findViewById(R.id.shape_w);
        SeekBar sbH = findViewById(R.id.shape_h);

        sbR.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbG.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbB.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbX.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbY.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbW.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbH.setOnSeekBarChangeListener(onSeekBarChangeListener);

    }

    private void changeBackgroundColor() {
        colorRVal.setText(seekR);
        colorGVal.setText(seekG);
        colorBVal.setText(seekB);
        codeResult.setBackgroundColor(Color.rgb(seekR, seekG, seekB));
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.color_r:
                    seekR = progress;
                    break;
                case R.id.color_g:
                    seekG = progress;
                    break;
                case R.id.color_b:
                    seekB = progress;
                    break;
                case R.id.shape_x:
                    seekX = progress;
                    break;
                case R.id.shape_y:
                    seekY = progress;
                    break;
                case R.id.shape_w:
                    seekH = progress;
                    break;
                case R.id.shape_h:
                    seekW = progress;
                    break;

            }
            changeBackgroundColor();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_detail_ok:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}