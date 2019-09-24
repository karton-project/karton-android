package com.alpay.codenotes.utils;

import android.os.AsyncTask;
import android.os.Environment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import androidx.annotation.NonNull;

public class DownloadFileTask extends AsyncTask<String, Void, Void> {

    protected Void doInBackground(String... ref) {
        downloadFile(ref[0], ref[1]);
        return null;
    }

    private void downloadFile(String ref, String image) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef = mStorageRef.child(ref+image);
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!directoryPath.isEmpty()) {
            directoryPath += "/CodeNotes/Drawable";
        } else {
            directoryPath = "storage/self/primary/CodeNotes/Drawable";
        }
        File file = new File(directoryPath + "/" + image);
        if (file != null) {
            mStorageRef.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }
            });
        }
    }
}
