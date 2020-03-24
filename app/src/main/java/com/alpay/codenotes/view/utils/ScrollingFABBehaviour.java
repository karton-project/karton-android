package com.alpay.codenotes.view.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.alpay.codenotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ScrollingFABBehaviour extends CoordinatorLayout.Behavior<FloatingActionButton> {
    private int toolbarHeight;
    private static boolean scrolledUp = false;
    private static boolean scrolledDown = false;

    public  ScrollingFABBehaviour(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.toolbarHeight = getToolbarHeight(context);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return (dependency instanceof Snackbar.SnackbarLayout) || (dependency instanceof Toolbar);
//        return (dependency instanceof Snackbar.SnackbarLayout);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, final FloatingActionButton child, View dependency) {
        if(dependency instanceof Snackbar.SnackbarLayout){
            float finalVal = (float)parent.getHeight() - dependency.getY();
            child.setTranslationY(-finalVal);
        }

        if(dependency instanceof Toolbar){
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = child.getHeight() + fabBottomMargin;
            float finalVal = dependency.getY()/(float)toolbarHeight;
            float distFinal = -distanceToScroll * finalVal;
            child.setTranslationY(distFinal);
        }


        return true;
    }

    private int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

}
