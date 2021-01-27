package au.edu.sydney.comp5216.petsh.ui.subpage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import au.edu.sydney.comp5216.petsh.R;

public class PetLifeDetail extends AppCompatActivity {

    ImageView petLifeImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petlife_detail);

        petLifeImageView = findViewById(R.id.petlife_detail_imageview);
        TextView petlifeDetailNameTextView = findViewById(R.id.petlife_detail_name);
        TextView petlifeDetailCategoryTextView = findViewById(R.id.petlife_detail_category);
        TextView petlifeDetailDescriptionTextView = findViewById(R.id.petlife_detail_description);




        final String petName=getIntent().getStringExtra("petName");
        final String petCategory=getIntent().getStringExtra("petCategory");
        final String petPhotoName=getIntent().getStringExtra("petLifePhoto");
        final String petLifeDescription=getIntent().getStringExtra("petLifeDescription");

        petlifeDetailNameTextView.setText(petName);
        petlifeDetailCategoryTextView.setText(petCategory);
        petlifeDetailDescriptionTextView.setText(petLifeDescription);
        setImageView(petPhotoName);
    }



    public void setImageView(String PetPhotoName){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(PetPhotoName);

        final long ONE_MEGABYTE = 2048*2048;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap =Bytes2Bimap(bytes);
                petLifeImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }


    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
