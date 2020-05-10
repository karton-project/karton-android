package com.alpay.codenotes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.alpay.codenotes.utils.PlaySound;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SoundButton extends FloatingActionButton implements OnClickListener{

    public SoundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        PlaySound.initSounds(context);
    }

    public SoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoundButton(Context context) {
        super(context);
        init();
    }

    private void init(){
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        PlaySound.playSound(v.getContext(), PlaySound.CLICK);
    }

}