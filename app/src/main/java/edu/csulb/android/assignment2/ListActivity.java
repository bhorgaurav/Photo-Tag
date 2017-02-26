package edu.csulb.android.assignment2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.csulb.android.assignment2.database.Photo;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final int TAKE_PHOTO = 22;
    private List<String> photos;
    private ArrayAdapter<String> adapter;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddPhotoActivity.class);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        photo = Photo.getInstance(getApplicationContext());
        photos = photo.getAll();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, photos);

        ListView listView = (ListView) findViewById(R.id.listview_photos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uninstall:
                Uri packageUri = Uri.parse("package:edu.csulb.android.assignment2");
                Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO) {
            photos = photo.getAll();
            adapter.clear();
            adapter.addAll(photos);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ViewPhotoActivity.class);
        intent.putExtra("photo_caption", photos.get(i));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        photo.close();
        super.onDestroy();
    }
}