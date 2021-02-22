package com.alpay.codenotes.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.CodeLine;
import com.alpay.codenotes.models.CodeLineHelper;
import com.alpay.codenotes.models.GroupHelper;
import com.alpay.codenotes.utils.NavigationManager;

import static com.alpay.codenotes.models.GroupHelper.groupId;

public class SaveProgramDialog extends Dialog implements
        android.view.View.OnClickListener {

    public AppCompatActivity appCompatActivity;

    AppCompatEditText editText;
    Button saveButton;
    TextView saveViaCamera;
    ImageView closeButton;

    public void saveCode() {
        String programName = editText.getText().toString().trim().replaceAll(" +", " ");
        for (CodeLine codeLine : CodeLineHelper.codeList){
            if (codeLine.getCommand().contains("group")){
                groupId = codeLine.getFirstInput();
            }
        }
        GroupHelper.saveProgram(appCompatActivity, GroupHelper.groupId, programName, CodeLineHelper.codeList);
    }

    public SaveProgramDialog(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        // TODO Auto-generated constructor stub
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.save_program_dialog);
        editText = findViewById(R.id.program_name_edit);
        saveButton = findViewById(R.id.save_program_button);
        saveViaCamera = findViewById(R.id.save_via_camera);
        closeButton = findViewById(R.id.save_program_close);

        saveButton.setOnClickListener(this);
        saveViaCamera.setOnClickListener(this);
        closeButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_program_button:
                saveCode();
                NavigationManager.openFragment(appCompatActivity, NavigationManager.PROGRAM_LIST);
                break;
            case R.id.save_via_camera:
                NavigationManager.openTransferLearning(appCompatActivity);
                break;
            case R.id.save_program_close:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}