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

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.models.CodeLine;
import com.alpay.codenotes.models.CodeLineHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeBlockDetailDialog extends Dialog{

    public Activity c;
    public Dialog d;
    int position;
    private CodeLine codeLine;
    private int seek1, seek2, seek3, seek4;
    private String ntext1 = "";

    @BindView(R.id.codedetail_text)
    TextView codeTitleTW;
    @BindView(R.id.seek1)
    SeekBar sb1;
    @BindView(R.id.seek2)
    SeekBar sb2;
    @BindView(R.id.seek3)
    SeekBar sb3;
    @BindView(R.id.seek4)
    SeekBar sb4;
    @BindView(R.id.bar1)
    LinearLayout bar1;
    @BindView(R.id.bar2)
    LinearLayout bar2;
    @BindView(R.id.bar3)
    LinearLayout bar3;
    @BindView(R.id.bar4)
    LinearLayout bar4;
    @BindView(R.id.seek1Val)
    TextView seek1Val;
    @BindView(R.id.seek2Val)
    TextView seek2Val;
    @BindView(R.id.seek3Val)
    TextView seek3Val;
    @BindView(R.id.seek4Val)
    TextView seek4Val;
    @BindView(R.id.codedetail_color_box)
    View colorBox;

    @OnClick(R.id.card_detail_ok)
    public void changeInputVals(){
        if (codeLine.getType() == CodeLine.Type.RGB) {
            codeLine.setInput("r: " + seek1 + " g: " + seek2 + " b: " + seek3);
        }else if (codeLine.getType() == CodeLine.Type.XYWH) {
            codeLine.setInput("x: " + seek1 + " y: " + seek2 + " w: " + seek3 + " h: " + seek3);
        }else if (codeLine.getType() == CodeLine.Type.XY) {
            codeLine.setInput("x: " + seek1 + " y: " + seek2);
        }else if (codeLine.getType() == CodeLine.Type.NV) {
            codeLine.setInput("n: " + ntext1 + " v: " + seek1);
        }
        CodeLineHelper.codeList.set(position, codeLine);
        ((FBVisionActivity) c).refreshCodeBlockRecyclerView(position);
        super.dismiss();
    }

    public CodeBlockDetailDialog(Activity a, CodeLine codeLine, int pos) {
        super(a);
        this.c = a;
        this.codeLine = codeLine;
        this.position = pos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.codeblock_detail_card);

        ButterKnife.bind(this);
        codeTitleTW.setText(codeLine.getCommand());
        sb1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sb2.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sb3.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sb4.setOnSeekBarChangeListener(onSeekBarChangeListener);

        int[] vals = CodeLineHelper.extractValues(codeLine);
        if (codeLine.getType() == CodeLine.Type.RGB) {
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.VISIBLE);
            bar3.setVisibility(View.VISIBLE);
            bar4.setVisibility(View.GONE);
            seek1 = vals[0];
            seek2 = vals[1];
            seek3 = vals[2];
            sb1.setMax(255);
            sb2.setMax(255);
            sb3.setMax(255);
            sb1.setProgress(vals[0]);
            seek1Val.setText("r: " + String.valueOf(vals[0]));
            sb2.setProgress(vals[1]);
            seek2Val.setText("g: " + String.valueOf(vals[1]));
            sb3.setProgress(vals[2]);
            seek3Val.setText("b: " + String.valueOf(vals[2]));
        }
        if (codeLine.getType() == CodeLine.Type.XYWH) {
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.VISIBLE);
            bar3.setVisibility(View.VISIBLE);
            bar4.setVisibility(View.VISIBLE);
            seek1 = vals[0];
            seek2 = vals[1];
            seek3 = vals[2];
            seek4 = vals[3];
            sb1.setMax(600);
            sb2.setMax(400);
            sb3.setMax(600);
            sb4.setMax(400);
            sb1.setProgress(vals[0]);
            seek1Val.setText("x: " + String.valueOf(vals[0]));
            sb2.setProgress(vals[1]);
            seek2Val.setText("y: " + String.valueOf(vals[1]));
            sb3.setProgress(vals[2]);
            seek3Val.setText("w: " + String.valueOf(vals[2]));
            sb4.setProgress(vals[3]);
            seek4Val.setText("h: " + String.valueOf(vals[3]));
        }
        if (codeLine.getType() == CodeLine.Type.XY) {
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.VISIBLE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            seek1 = vals[0];
            seek2 = vals[1];
            sb1.setMax(600);
            sb2.setMax(400);
            sb1.setProgress(vals[0]);
            seek1Val.setText("x: " + String.valueOf(vals[0]));
            sb2.setProgress(vals[1]);
            seek2Val.setText("y: " + String.valueOf(vals[1]));
        }

    }

    private void changeRGB() {
        seek1Val.setText("r: " + String.valueOf(seek1));
        seek2Val.setText("g: " + String.valueOf(seek2));
        seek3Val.setText("b: " + String.valueOf(seek3));
        colorBox.setBackgroundColor(Color.rgb(seek1, seek2, seek3));
    }
    private void changeXYWH() {
        seek1Val.setText("x: " + String.valueOf(seek1));
        seek2Val.setText("y: " + String.valueOf(seek2));
        seek3Val.setText("w: " + String.valueOf(seek3));
        seek4Val.setText("h: " + String.valueOf(seek4));
    }
    private void changeXY() {
        seek1Val.setText("x: " + String.valueOf(seek1));
        seek2Val.setText("y: " + String.valueOf(seek2));
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
                case R.id.seek1:
                    seek1 = progress;
                    break;
                case R.id.seek2:
                    seek2 = progress;
                    break;
                case R.id.seek3:
                    seek3 = progress;
                    break;
                case R.id.seek4:
                    seek4 = progress;
                    break;
                default:
                    break;

            }
            if (codeLine.getType() == CodeLine.Type.RGB) {
                changeRGB();
            }
            if (codeLine.getType() == CodeLine.Type.XYWH) {
                changeXYWH();
            }
            if (codeLine.getType() == CodeLine.Type.XY) {
                changeXY();
            }
        }
    };
}