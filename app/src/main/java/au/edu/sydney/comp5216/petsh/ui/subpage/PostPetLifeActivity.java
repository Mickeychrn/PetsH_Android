package au.edu.sydney.comp5216.petsh.ui.subpage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import au.edu.sydney.comp5216.petsh.MainActivity;
import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.model.PetLife;
import au.edu.sydney.comp5216.petsh.utils.MarshmallowPermission;

public class PostPetLifeActivity extends AppCompatActivity {


    private FirebaseFirestore mFirestore;

    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 200;

    MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);
    private File file;
    private String photoFileName;
    private String petName;
    private String petCategory;
    private Boolean isPhotoTaken = false;
    private Button addPetLifeButton;
    private Button petLifeTakePetPhotoButton;
    private EditText petlifeDescriptionEditText;
    private Bitmap takenImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_petlife);

        petName = getIntent().getStringExtra("petName");
        petCategory = getIntent().getStringExtra("petCategory");

        TextView petNameTextView = findViewById(R.id.petlife_pet_name);
        TextView petCategoryTextView = findViewById(R.id.petlife_pet_category);
        petlifeDescriptionEditText = findViewById(R.id.petlife_description);

        petCategoryTextView.setText(petCategory);
        petNameTextView.setText(petName);

        petLifeTakePetPhotoButton = findViewById(R.id.petlife_take_pet_photo);
        petLifeTakePetPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePhotoClick(view);
            }
        });

        addPetLifeButton = findViewById(R.id.add_petlife_button);
        addPetLifeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    clickAdd();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public void clickAdd() throws FileNotFoundException {
        String petlifeDescription = petlifeDescriptionEditText.getText().toString();
        if (petlifeDescription.equals("")){
            Toast.makeText(this, "Please input the description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isPhotoTaken == false) {
            Toast.makeText(this, "Please take the photo", Toast.LENGTH_SHORT).show();
            return;
        }

        mFirestore = FirebaseFirestore.getInstance();
        CollectionReference petLives = mFirestore.collection("PetLife");


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid = user.getUid();
        PetLife petLife = new PetLife(petName, petCategory, Uid, photoFileName,petlifeDescription);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        final StorageReference storageRef = storage.getReference();
        final StorageReference mountainsRef = storageRef.child(photoFileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        takenImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        petLives.add(petLife);
        Toast.makeText(this, "You have posted successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(PostPetLifeActivity.this, MainActivity.class));
    }


    public void onTakePhotoClick(View v) {
        if (!marshmallowPermission.checkPermissionForCamera()
                || !marshmallowPermission.checkPermissionForExternalStorage()) {
            marshmallowPermission.requestPermissionForCamera();
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            photoFileName = "PETLIFE_IMG_" + timeStamp + ".jpg";
            Uri file_uri = getFileUri(photoFileName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
        }
    }

    public Uri getFileUri(String fileName) {
        Uri fileUri = null;
        try {
            String typestr = "/images/";
            File mediaStorageDir = new File(this.getExternalFilesDir(null).getAbsolutePath(),
                    typestr + fileName);
            if (!mediaStorageDir.getParentFile().exists() && !mediaStorageDir.getParentFile().mkdirs()) {
                Log.d("APP_TAG", "failed to create directory");
            }
            file = new File(mediaStorageDir.getParentFile().getPath() + File.separator + fileName);
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(
                        this.getApplicationContext(),
                        "au.edu.sydney.comp5216.petsh.fileProvider", file);
            } else {
                fileUri = Uri.fromFile(mediaStorageDir);
            }
        } catch (Exception ex) {
            Log.d("getFileUri", ex.getStackTrace().toString());
        }
        return fileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSIONS_REQUEST_OPEN_CAMERA) {
            if (resultCode == RESULT_OK) {
                takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                ImageView imageView = findViewById(R.id.take_petlife_photo_image);


                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                takenImage = Bitmap.createBitmap(takenImage, 0, 0, takenImage.getWidth(), takenImage.getHeight(),
                        matrix, true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                takenImage.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] bytes = baos.toByteArray();
                takenImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


                imageView.setImageBitmap(takenImage);
                petLifeTakePetPhotoButton.setText(photoFileName);
                isPhotoTaken = true;

            } else {
                Toast.makeText(this, "Picture wasn't taken AAA!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
