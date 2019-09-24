package com.alpay.codenotes.fragments;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.StudyNoteHelper;
import com.alpay.codenotes.models.StudyNoteItem;
import com.alpay.codenotes.utils.ColorGenerator;
import com.alpay.codenotes.view.TextDrawable;
import com.alpay.codenotes.view.utils.ItemTouchHelperClass;
import com.alpay.codenotes.view.utils.RecyclerViewEmptySupport;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class StudyNotesFragment extends Fragment {

    private View view;
    private Unbinder unbinder;
    public static final String BUNDLE_KEY = "textfromcamera";

    @BindView(R.id.toDoRecyclerView)
    RecyclerViewEmptySupport mRecyclerView;

    @OnClick(R.id.add_new_note_button)
    public void setFABAction() {
        StudyNoteItem item = new StudyNoteItem("");
        int color = ColorGenerator.MATERIAL.getRandomColor();
        item.setTodoColor(color);
        showTextDialog(item);
    }

    public void showTextDialog(StudyNoteItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Yeni Not Gir");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_layout, null);
        alertDialog.setView(dialogView);

        EditText input = dialogView.findViewById(R.id.alert_edit_text);
        if (item.getToDoText() != null && item.getToDoText().length() > 0) {
            input.setText(item.getToDoText());
        }
        alertDialog.setNeutralButton("OK", (dialog, which) -> {
            makeResult(input.getText().toString(), item);
            addNoteList(item);
        });
        alertDialog.show();
    }

    public void makeResult(String mUserEnteredText, StudyNoteItem mUserStudyNoteItem) {
        if (mUserEnteredText.length() > 0) {
            String capitalizedString = Character.toUpperCase(mUserEnteredText.charAt(0)) + mUserEnteredText.substring(1);
            mUserStudyNoteItem.setToDoText(capitalizedString);
        } else {
            mUserStudyNoteItem.setToDoText(mUserEnteredText);
        }
    }


    private ArrayList<StudyNoteItem> mStudyNoteItemsArrayList;
    public static final String TODOITEM = "StudyNotesFragment";
    private BasicListAdapter adapter;
    private static final int REQUEST_ID_TODO_ITEM = 100;
    private StudyNoteItem mJustDeletedStudyNoteItem;
    private int mIndexOfDeletedToDoItem;
    public static final String FILENAME = "todoitems.json";
    private StudyNoteHelper studyNoteHelper;
    public ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_todonotes, container, false);
        unbinder = ButterKnife.bind(this, view);
        studyNoteHelper = new StudyNoteHelper(getActivity(), FILENAME);
        mStudyNoteItemsArrayList = studyNoteHelper.getLocallyStoredData();
        adapter = new BasicListAdapter(mStudyNoteItemsArrayList);

        mRecyclerView.setEmptyView(view.findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        return view;
    }


    public void addNoteList(StudyNoteItem item) {
        boolean existed = false;
        for (int i = 0; i < mStudyNoteItemsArrayList.size(); i++) {
            if (item.getIdentifier().equals(mStudyNoteItemsArrayList.get(i).getIdentifier())) {
                mStudyNoteItemsArrayList.set(i, item);
                existed = true;
                adapter.notifyDataSetChanged();
                break;
            }
        }
        if (!existed) {
            addToDataStore(item);
        }
    }

    private void addToDataStore(StudyNoteItem item) {
        mStudyNoteItemsArrayList.add(item);
        adapter.notifyItemInserted(mStudyNoteItemsArrayList.size() - 1);

    }

    public class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
        private ArrayList<StudyNoteItem> items;

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(items, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            mJustDeletedStudyNoteItem = items.remove(position);
            mIndexOfDeletedToDoItem = position;
        }

        @Override
        public BasicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_circle_try, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BasicListAdapter.ViewHolder holder, final int position) {
            StudyNoteItem item = items.get(position);
            int todoTextColor = getResources().getColor(R.color.textColorDark);
            holder.mToDoTextview.setText(item.getToDoText());
            holder.mToDoTextview.setTextColor(todoTextColor);

            try {
                TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                        .textColor(getResources().getColor(R.color.textColorLight))
                        .useFont(Typeface.DEFAULT)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(item.getToDoText().substring(0, 1), item.getTodoColor());
                holder.mColorImageView.setImageDrawable(myDrawable);
            } catch (StringIndexOutOfBoundsException e) {
                Log.e("StudyNotes", "onBindViewHolder: ", e);
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        BasicListAdapter(ArrayList<StudyNoteItem> items) {
            this.items = items;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView mToDoTextview;
            ImageView mColorImageView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                v.setOnClickListener(v1 -> {
                    StudyNoteItem item = items.get(ViewHolder.this.getAdapterPosition());
                    showTextDialog(item);
                });
                mToDoTextview = v.findViewById(R.id.toDoListItemTextview);
                mColorImageView = v.findViewById(R.id.toDoListItemColorImageView);
                linearLayout = v.findViewById(R.id.listItemLinearLayout);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            studyNoteHelper.saveToFile(mStudyNoteItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}


