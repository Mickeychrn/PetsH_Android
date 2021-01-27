package au.edu.sydney.comp5216.petsh.ui.subpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import au.edu.sydney.comp5216.petsh.MainActivity;
import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.model.FavouriteRecord;

public class SearchPetDetail extends AppCompatActivity {

    ImageView petDetailImageView;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private String petId;
    private String Uid;
    ImageView petDetailFavourite;
    private String favouriteID;
    private Context context;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);


        TextView petDetailNameTextView= findViewById(R.id.pet_detail_name);
        TextView petDetailCategoryTextView = findViewById(R.id.pet_detail_category);
        TextView petDetailAgeTextView = findViewById(R.id.pet_detail_age);
        TextView petDetailGenderTextView = findViewById(R.id.pet_detail_gender);
        TextView petDetailDescriptionTextView = findViewById(R.id.pet_detail_description);
        petDetailFavourite = findViewById(R.id.pet_detail_favourite_button);
        petDetailImageView = findViewById(R.id.pet_detail_imageview);
        Button petDetailAdoptButton = findViewById(R.id.pet_detail_adopt_button);
        context =this;

        petId=getIntent().getStringExtra("petId");
        final String petName=getIntent().getStringExtra("petName");
        final String petCategory=getIntent().getStringExtra("petCategory");
        final String petPhotoName=getIntent().getStringExtra("PetPhotoName");
        final String petAge=getIntent().getStringExtra("petAge");
        final String petGender=getIntent().getStringExtra("petGender");
        final String petDescription=getIntent().getStringExtra("petDescription");
        final String adoptPerson=getIntent().getStringExtra("adoptPerson");

        if (!adoptPerson.equals("nobody")){
            petDetailAdoptButton.setVisibility(View.INVISIBLE);
        }

        setImageView(petPhotoName);
        petDetailNameTextView.setText(petName);
        petDetailCategoryTextView.setText(petCategory);
        petDetailAgeTextView.setText(petAge);
        petDetailGenderTextView.setText(petGender);
        petDetailDescriptionTextView.setText(petDescription);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Uid=user.getUid();
        isAddedFavourite();

        petDetailFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (petDetailFavourite.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.ic_favourite_red_24dp).getConstantState())){
                    mFirestore.collection("favouriteRecord").document(favouriteID).delete();
                    Toast.makeText(context,"You have cancelled favourite",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SearchPetDetail.this, MainActivity.class));

                }else {
                    FavouriteRecord favouriteRecord = new FavouriteRecord(petId, petName, petCategory, petGender, petAge, petDescription, adoptPerson, petPhotoName, Uid);
                    mFirestore.collection("favouriteRecord").add(favouriteRecord);
                    Toast.makeText(context,"You have added favourite",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SearchPetDetail.this, MainActivity.class));
                }

            }
        });

        petDetailAdoptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirestore.collection("Pet").document(petId).update("adoptPerson",Uid);
                startActivity(new Intent(SearchPetDetail.this, MainActivity.class));

            }
        });
    }


    public void isAddedFavourite(){
        mFirestore.collection("favouriteRecord").whereEqualTo("userId", Uid)
                .whereEqualTo("petId",petId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                petDetailFavourite.setImageResource(R.drawable.ic_favourite_red_24dp);
                                favouriteID=document.getId();
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    public void setImageView(String PetPhotoName){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(PetPhotoName);

        final long ONE_MEGABYTE = 2048 * 2048;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap =Bytes2Bimap(bytes);
                petDetailImageView.setImageBitmap(bitmap);
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
