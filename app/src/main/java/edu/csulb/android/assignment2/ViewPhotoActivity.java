package edu.csulb.android.assignment2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import edu.csulb.android.assignment2.database.Photo;

public class ViewPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        ImageView imageView = (ImageView) findViewById(R.id.imageview_view_photo);
        TextView textViewCaption = (TextView) findViewById(R.id.textview_caption);

        Intent intent = getIntent();
        if (intent.hasExtra("photo_caption")) {
            try {
                String caption = intent.getStringExtra("photo_caption");
                Photo photo = Photo.getInstance(getApplicationContext());
                JSONObject json = photo.get(caption);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;  // Experiment with different
                Bitmap b = BitmapFactory.decodeFile(json.getString("path"), options);
                imageView.setImageBitmap(b);

                textViewCaption.setText(caption);

                hideSystemUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        showSystemUI();
        super.onBackPressed();
    }
}