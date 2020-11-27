package com.alpay.codenotes.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.seek3)
    SeekBar sb3;
    @BindView(R.id.seek4)
    SeekBar sb4;
    @BindView(R.id.bar0)
    LinearLayout bar0;
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
    @BindView(R.id.varNameET)
    EditText varNameET;
    @BindView(R.id.codedetail_color_box)
    View colorBox;
    @BindView(R.id.varChipsScroll1)
    HorizontalScrollView varChipsScroll1;
    @BindView(R.id.varChipsScroll2)
    HorizontalScrollView varChipsScroll2;
    @BindView(R.id.varChipsScroll3)
    HorizontalScrollView varChipsScroll3;
    @BindView(R.id.varChipsScroll4)
    HorizontalScrollView varChipsScroll4;
    @BindView(R.id.varChipGroup1)
    ChipGroup chipGroup1;
    @BindView(R.id.varChipGroup2)
    ChipGroup chipGroup2;
    @BindView(R.id.varChipGroup3)
    ChipGroup chipGroup3;
    @BindView(R.id.varChipGroup4)
    ChipGroup chipGroup4;

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

    @OnClick(R.id.bar3VarButton)
    public void showVariableChips3() {
        if (CodeLineHelper.varNames.size() > 0) {
            sb3.setVisibility(View.GONE);
            varChipsScroll3.setVisibility(View.VISIBLE);
            findViewById(R.id.bar3VarButton).setVisibility(View.INVISIBLE);
            if (chipGroup3.getChildCount() < CodeLineHelper.varNames.size()) {
                for (int i = chipGroup3.getChildCount(); i < CodeLineHelper.varNames.size(); i++) {
                    final String varName = CodeLineHelper.varNames.get(i);
                    Chip chip = new Chip(chipGroup3.getContext());
                    chip.setText(varName);
                    chip.setCheckable(true);
                    chip.setClickable(true);
                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            fill1 = varName;
                        }
                    });
                    chipGroup3.addView(chip);
                }
            }
        } else {
            Toast.makeText(c, "No var", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.bar4VarButton)
    public void showVariableChips4() {
        if (CodeLineHelper.varNames.size() > 0) {
            sb4.setVisibility(View.GONE);
            varChipsScroll4.setVisibility(View.VISIBLE);
            findViewById(R.id.bar4VarButton).setVisibility(View.INVISIBLE);
            if (chipGroup4.getChildCount() < CodeLineHelper.varNames.size()) {
                for (int i = chipGroup4.getChildCount(); i < CodeLineHelper.varNames.size(); i++) {
                    final String varName = CodeLineHelper.varNames.get(i);
                    Chip chip = new Chip(chipGroup4.getContext());
                    chip.setText(varName);
                    chip.setCheckable(true);
                    chip.setClickable(true);
                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            fill1 = varName;
                        }
                    });
                    chipGroup4.addView(chip);
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
        fill3 = (fill3.length() > 1) ? fill3 : String.valueOf(seek3);
        fill4 = (fill4.length() > 1) ? fill4 : String.valueOf(seek4);
        if (codeLine.getType() == CodeLine.Type.RGB) {
            codeLine.setInput("r: " + fill1 + " g: " + fill2 + " b: " + fill3);
        } else if (codeLine.getType() == CodeLine.Type.XYWH) {
            codeLine.setInput("x: " + fill1 + " y: " + fill2 + " w: " + fill3 + " h: " + fill4);
        } else if (codeLine.getType() == CodeLine.Type.XY) {
            codeLine.setInput("x: " + fill1 + " y: " + fill2);
        } else if (codeLine.getType() == CodeLine.Type.NV) {
            codeLine.setInput("n: " + varNameET.getText().toString() + " v: " + fill1);
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
            bar0.setVisibility(View.GONE);
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
        } else if (codeLine.getType() == CodeLine.Type.XYWH) {
            bar0.setVisibility(View.GONE);
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
        } else if (codeLine.getType() == CodeLine.Type.XY) {
            bar0.setVisibility(View.GONE);
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
        } else if (codeLine.getType() == CodeLine.Type.X) {
            bar0.setVisibility(View.GONE);
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.GONE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            seek1 = vals[0];
            sb1.setMax(600);
            sb1.setProgress(vals[0]);
            seek1Val.setText("x: " + String.valueOf(vals[0]));
        } else if (codeLine.getType() == CodeLine.Type.TURTLE_NUM) {
            bar0.setVisibility(View.GONE);
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.GONE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            seek1 = vals[0];
            sb1.setMax(100);
            sb1.setProgress(vals[0]);
            seek1Val.setText(" : " + String.valueOf(vals[0]));
        } else if (codeLine.getType() == CodeLine.Type.NV) {
            bar0.setVisibility(View.VISIBLE);
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.GONE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            varNameET.setText(codeLine.getVarName());
            seek1 = vals[0];
            sb1.setMax(600);
            sb1.setProgress(vals[0]);
            seek1Val.setText("x: " + String.valueOf(vals[0]));
        } else if (codeLine.getType() == CodeLine.Type.N) {
            bar0.setVisibility(View.VISIBLE);
            bar1.setVisibility(View.GONE);
            bar2.setVisibility(View.GONE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            varNameET.setText(codeLine.getInput());
        } else {
            bar0.setVisibility(View.GONE);
            bar1.setVisibility(View.GONE);
            bar2.setVisibility(View.GONE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
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