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
import au.edu.sydney.comp5216.petsh.model.Pet;
import au.edu.sydney.comp5216.petsh.utils.MarshmallowPermission;

public class AddPetActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 101;

    MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);
    private File file;
    private String photoFileName;
    private Boolean photoIsTaken = false;

    private Bitmap takenImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        Button button = findViewById(R.id.add_pet_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    clickAdd();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        Button takePetPhotoButton = findViewById(R.id.take_pet_photo);
        takePetPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePhotoClick(view);
            }
        });


    }


    public void clickAdd() throws FileNotFoundException {


        EditText addPetName = findViewById(R.id.add_pet_name);
        EditText addPetCategory = findViewById(R.id.add_pet_category);
        EditText addPetGender = findViewById(R.id.add_pet_gender);
        EditText addPetAge = findViewById(R.id.add_pet_age);
        EditText addPetDescription = findViewById(R.id.add_pet_description);


        String petName = addPetName.getText().toString();
        String petCategory = addPetCategory.getText().toString().toLowerCase();
        String petGender = addPetGender.getText().toString();
        String petAge = addPetAge.getText().toString();
        String petDescription = addPetDescription.getText().toString();

        if (petName.equals("")) {
            Toast.makeText(this, "Please input pet name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (petCategory.equals("")) {
            Toast.makeText(this, "Please input pet category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (petGender.equals("")) {
            Toast.makeText(this, "Please input pet gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if (petAge.equals("")) {
            Toast.makeText(this, "Please input pet age", Toast.LENGTH_SHORT).show();
            return;
        }
        if (petDescription.equals("")) {
            Toast.makeText(this, "Please input pet description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoIsTaken) {
            Toast.makeText(this, "Please take the photo", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = user.getUid();
        mFirestore = FirebaseFirestore.getInstance();
        CollectionReference pets = mFirestore.collection("Pet");

        Pet pet = new Pet(petName,petCategory,petGender,petAge,petDescription,photoFileName,"nobody",Uid);
        pets.add(pet);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference mountainsRef = storageRef.child(photoFileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        takenImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = mountainsRef.putBytes(data);
        Toast.makeText(this, "You have posted successfully", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(AddPetActivity.this, MainActivity.class));
    }


    public void onTakePhotoClick(View v) {
        if (!marshmallowPermission.checkPermissionForCamera()
                || !marshmallowPermission.checkPermissionForExternalStorage()) {
            marshmallowPermission.requestPermissionForCamera();
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            photoFileName = "IMG_" + timeStamp + ".jpg";
            Uri file_uri = getFileUri(photoFileName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
        }
    }

    public Uri getFileUri(String fileName) {
        Uri fileUri = null;
        try {
            String typeStr = "/images/";
            File mediaStorageDir = new File(this.getExternalFilesDir(null).getAbsolutePath(),
                    typeStr + fileName);
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

                ImageView imageView = findViewById(R.id.take_pet_photo_image);


                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                takenImage = Bitmap.createBitmap(takenImage, 0, 0, takenImage.getWidth(), takenImage.getHeight(),
                        matrix, true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                takenImage.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] bytes = baos.toByteArray();
                takenImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


                imageView.setImageBitmap(takenImage);
                Button takePetPhotoButton = findViewById(R.id.take_pet_photo);
                takePetPhotoButton.setText(photoFileName);
                photoIsTaken = true;

            } else {
                Toast.makeText(this, "Picture wasn't taken AAA!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
