package com.alpay.codenotes.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.ColorUtils;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.models.CodeLine;
import com.alpay.codenotes.models.CodeLineHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeBlockDetailDialog extends Dialog {

    public Activity c;
    public Dialog d;
    int position;
    private CodeLine codeLine;
    private int seek1, seek2, seek3, seek4;
    private String fill1 = "", fill2 = "", fill3 = "", fill4 = "";

    @BindView(R.id.codedetail_text)
    TextView codeTitleTW;
    @BindView(R.id.seek1)
    SeekBar sb1;
    @BindView(R.id.seek2)
    SeekBar sb2;
    @BindView(R.id.bar0)
    LinearLayout bar0;
    @BindView(R.id.bar1)
    LinearLayout bar1;
    @BindView(R.id.bar2)
    LinearLayout bar2;
    @BindView(R.id.seek1Val)
    TextView seek1Val;
    @BindView(R.id.seek2Val)
    TextView seek2Val;
    @BindView(R.id.varNameET)
    EditText varNameET;
    @BindView(R.id.codedetail_color_box)
    View colorBox;
    @BindView(R.id.varChipsScroll1)
    HorizontalScrollView varChipsScroll1;
    @BindView(R.id.varChipsScroll2)
    HorizontalScrollView varChipsScroll2;
    @BindView(R.id.varChipGroup1)
    ChipGroup chipGroup1;
    @BindView(R.id.varChipGroup2)
    ChipGroup chipGroup2;

    @OnClick(R.id.bar1VarButton)
    public void showVariableChips1() {
        if (CodeLineHelper.varNames.size() > 0) {
            sb1.setVisibility(View.GONE);
            varChipsScroll1.setVisibility(View.VISIBLE);
            findViewById(R.id.bar1VarButton).setVisibility(View.INVISIBLE);
            if (chipGroup1.getChildCount() < CodeLineHelper.varNames.size()) {
                for (int i = chipGroup1.getChildCount(); i < CodeLineHelper.varNames.size(); i++) {
                    final String varName = CodeLineHelper.varNames.get(i);
                    Chip chip = new Chip(chipGroup1.getContext());
                    chip.setText(varName);
                    chip.setCheckable(true);
                    chip.setClickable(true);
                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            fill1 = varName;
                        }
                    });
                    chipGroup1.addView(chip);
                }
            }
        } else {
            Toast.makeText(c, "No var", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.bar2VarButton)
    public void showVariableChips2() {
        if (CodeLineHelper.varNames.size() > 0) {
            sb2.setVisibility(View.GONE);
            varChipsScroll2.setVisibility(View.VISIBLE);
            findViewById(R.id.bar2VarButton).setVisibility(View.INVISIBLE);
            if (chipGroup2.getChildCount() < CodeLineHelper.varNames.size()) {
                for (int i = chipGroup2.getChildCount(); i < CodeLineHelper.varNames.size(); i++) {
                    final String varName = CodeLineHelper.varNames.get(i);
                    Chip chip = new Chip(chipGroup2.getContext());
                    chip.setText(varName);
                    chip.setCheckable(true);
                    chip.setClickable(true);
                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            fill1 = varName;
                        }
                    });
                    chipGroup2.addView(chip);
                }
            }
        } else {
            Toast.makeText(c, "No var", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.card_detail_ok)
    public void changeInputVals() {
        fill1 = (fill1.length() > 1) ? fill1 : String.valueOf(seek1);
        fill2 = (fill2.length() > 1) ? fill2 : String.valueOf(seek2);
        if (codeLine.getType() == CodeLine.Type.XY) {
            codeLine.setInput(fill1, fill2);
        } else if (codeLine.getType() == CodeLine.Type.NV) {
            codeLine.setInput(varNameET.getText().toString(), fill1);
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

        int[] vals = CodeLineHelper.extractValues(codeLine);
        if (codeLine.getType() == CodeLine.Type.XY) {
            bar0.setVisibility(View.GONE);
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.VISIBLE);
            seek1 = vals[0];
            seek2 = vals[1];
            sb1.setMax(600);
            sb2.setMax(400);
            sb1.setProgress(vals[0]);
            seek1Val.setText("x: " + String.valueOf(vals[0]));
            sb2.setProgress(vals[1]);
            seek2Val.setText("y: " + String.valueOf(vals[1]));
        } else if (codeLine.getType() == CodeLine.Type.X) {
            bar0.setVisibility(View.GONE);
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.GONE);
            seek1 = vals[0];
            sb1.setMax(600);
            sb1.setProgress(vals[0]);
            seek1Val.setText("x: " + String.valueOf(vals[0]));
            colorBox.setBackgroundColor(ColorUtils.HSLToColor(new float[] {seek1, 50, 75}));
        } else if (codeLine.getType() == CodeLine.Type.TURTLE_NUM) {
            bar0.setVisibility(View.GONE);
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.GONE);
            seek1 = vals[0];
            sb1.setMax(100);
            sb1.setProgress(vals[0]);
            seek1Val.setText(" : " + String.valueOf(vals[0]));
        } else if (codeLine.getType() == CodeLine.Type.NV) {
            bar0.setVisibility(View.VISIBLE);
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.GONE);
            varNameET.setText(codeLine.getVarName());
            seek1 = vals[0];
            sb1.setMax(600);
            sb1.setProgress(vals[0]);
            seek1Val.setText("x: " + String.valueOf(vals[0]));
        } else if (codeLine.getType() == CodeLine.Type.N) {
            bar0.setVisibility(View.VISIBLE);
            bar1.setVisibility(View.GONE);
            bar2.setVisibility(View.GONE);
            varNameET.setText(codeLine.getVarName());
        } else {
            bar0.setVisibility(View.GONE);
            bar1.setVisibility(View.GONE);
            bar2.setVisibility(View.GONE);
        }

    }

    private void changeXY() {
        seek1Val.setText("x: " + String.valueOf(seek1));
        seek2Val.setText("y: " + String.valueOf(seek2));
    }

    private void changeX() {
        seek1Val.setText("x: " + String.valueOf(seek1));
    }

    private void changeTurtleNum() {
        seek1Val.setText(" : " + String.valueOf(seek1));
    }

    private void changeNV() {
        seek1Val.setText("v: " + String.valueOf(seek1));
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
                default:
                    break;
            }
            if (codeLine.getType() == CodeLine.Type.XY) {
                changeXY();
            }
            if (codeLine.getType() == CodeLine.Type.X) {
                changeX();
            }
            if (codeLine.getType() == CodeLine.Type.TURTLE_NUM) {
                changeTurtleNum();
            }
            if (codeLine.getType() == CodeLine.Type.NV) {
                changeNV();
            }
        }
    };
}