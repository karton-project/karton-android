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
    private int seek1, seek2;
    private String fill1 = "", fill2 = "";

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
    @BindView(R.id.bar3)
    LinearLayout bar3;
    @BindView(R.id.seek1Val)
    TextView seek1Val;
    @BindView(R.id.seek2Val)
    TextView seek2Val;
    @BindView(R.id.varNameET)
    EditText varNameET;
    @BindView(R.id.valNameET)
    EditText valNameET;
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
        String[] varNames = CodeLineHelper.getVariableNames().toArray(new String[0]);
        if (varNames.length > 0) {
            sb1.setVisibility(View.GONE);
            varChipsScroll1.setVisibility(View.VISIBLE);
            findViewById(R.id.bar1VarButton).setVisibility(View.INVISIBLE);
            if (chipGroup1.getChildCount() < varNames.length) {
                for (int i = chipGroup1.getChildCount(); i < varNames.length; i++) {
                    final String varName = varNames[i];
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
        String[] varNames = CodeLineHelper.getVariableNames().toArray(new String[0]);
        if (varNames.length > 0) {
            sb2.setVisibility(View.GONE);
            varChipsScroll2.setVisibility(View.VISIBLE);
            findViewById(R.id.bar2VarButton).setVisibility(View.INVISIBLE);
            if (chipGroup2.getChildCount() < varNames.length) {
                for (int i = chipGroup2.getChildCount(); i < varNames.length; i++) {
                    final String varName = varNames[i];
                    Chip chip = new Chip(chipGroup2.getContext());
                    chip.setText(varName);
                    chip.setCheckable(true);
                    chip.setClickable(true);
                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            fill2 = varName;
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
        if (bar0.getVisibility() == View.VISIBLE)
            codeLine.setFirstInput(varNameET.getText().toString());
        if (bar3.getVisibility() == View.VISIBLE)
            codeLine.setSecondInput(valNameET.getText().toString());

        if (bar1.getVisibility() == View.VISIBLE)
            codeLine.setFirstInput(String.valueOf(seek1));
        if (bar2.getVisibility() == View.VISIBLE)
            codeLine.setSecondInput(String.valueOf(seek2));

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

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.codeblock_detail_card);

        ButterKnife.bind(this);
        bar0.setVisibility(View.GONE);
        bar1.setVisibility(View.GONE);
        bar2.setVisibility(View.GONE);
        bar3.setVisibility(View.GONE);
        codeTitleTW.setText(codeLine.getCommand());
        sb1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sb2.setOnSeekBarChangeListener(onSeekBarChangeListener);

        String firstInput = codeLine.getFirstInput();
        String secondInput = codeLine.getSecondInput();
        if (firstInput.length() > 0) {
            if (isNumeric(firstInput)) {
                bar1.setVisibility(View.VISIBLE);
                seek1 = Integer.parseInt(firstInput);
                sb1.setMax(400);
                sb1.setProgress(seek1);
                sb1.incrementProgressBy(10);
                seek1Val.setText("#" + firstInput);
            } else {
                bar0.setVisibility(View.VISIBLE);
                varNameET.setText(firstInput);
            }
        }

        if (secondInput.length() > 0) {
            if (isNumeric(secondInput)) {
                bar2.setVisibility(View.VISIBLE);
                seek2 = Integer.parseInt(secondInput);
                sb2.setMax(400);
                sb2.setProgress(seek2);
                sb2.incrementProgressBy(10);
                seek2Val.setText("#" + secondInput);
            } else {
                bar3.setVisibility(View.VISIBLE);
                valNameET.setText(secondInput);
            }
        }

        if (codeLine.getType() == CodeLine.Type.X) {
            colorBox.setBackgroundColor(ColorUtils.HSLToColor(new float[]{seek1, 50, 75}));
        }

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
            if (fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                switch (seekBar.getId()) {
                    case R.id.seek1:
                        seek1 = progress;
                        seek1Val.setText("# " + String.valueOf(seek1));
                        break;
                    case R.id.seek2:
                        seek2 = progress;
                        seek2Val.setText("# " + String.valueOf(seek2));
                        break;
                    default:
                        break;
                }
            }

        }
    };
}