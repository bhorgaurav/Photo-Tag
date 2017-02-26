package edu.csulb.android.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.csulb.android.assignment2.database.Photo;

public class AddPhotoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    private EditText editTextCaption;
    private TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        editTextCaption = (EditText) findViewById(R.id.edittext_caption);
        textViewMessage = (TextView) findViewById(R.id.textview_image_captured);
        textViewMessage.setVisibility(View.INVISIBLE);

        ImageButton buttonTakePhoto = (ImageButton) findViewById(R.id.imagebutton_take_photo);
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button buttonSave = (Button) findViewById(R.id.button_save_image);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caption = editTextCaption.getText().toString();
                if (TextUtils.isEmpty(caption)) {
                    editTextCaption.setError("Please enter a caption");
                } else {
                    if (TextUtils.isEmpty(mCurrentPhotoPath)) {
                        Toast.makeText(getApplicationContext(), "Use the button to capture image first.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save image path and caption
                        Photo photo = Photo.getInstance(getApplicationContext());
                        photo.saveNew(caption, mCurrentPhotoPath);
                        photo.close();

                        Toast.makeText(getApplicationContext(), "Image and caption saved!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);

                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            textViewMessage.setVisibility(View.VISIBLE);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageCaptureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                Uri photoURI = FileProvider.getUriForFile(this, "edu.csulb.android.fileprovider", photoFile);
                imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(imageCaptureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
