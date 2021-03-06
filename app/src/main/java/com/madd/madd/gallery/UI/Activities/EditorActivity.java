package com.madd.madd.gallery.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.madd.madd.gallery.UI.Fragments.GalleryEditor.GalleryEditorFragment;
import com.madd.madd.gallery.R;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        ArrayList<String> pictureList = intent.getStringArrayListExtra("path_list");

        GalleryEditorFragment editor = new GalleryEditorFragment();
        editor.setPicturePathList(pictureList);
        editor.getSelectedPictureList(selectedPictureList -> {
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("path_list", (ArrayList<String>)selectedPictureList);
            setResult(GalleryActivity.RESULT_OK, resultIntent);
            finish();
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.CTNR_Main, editor).commit();

    }
}
