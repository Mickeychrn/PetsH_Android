package au.edu.sydney.comp5216.petsh.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.model.PetLife;

public class PetLifeCardAdapter extends RecyclerView.Adapter<PetLifeCardAdapter.PlanetHolder> {

    private Context context;
    private ArrayList<PetLife> petLives;

    FirebaseStorage storage;
    StorageReference storageRef;

    public PetLifeCardAdapter(Context context, ArrayList<PetLife> petLives) {
        this.context = context;
        this.petLives = petLives;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_petlife, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        PetLife petLife = petLives.get(position);
        holder.setDetails(petLife);
    }

    @Override
    public int getItemCount() {
        return petLives.size();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {
        private ImageView fra_petLifePetImage;


        private TextView fra_petLifePetName, fra_petLifePetCategory;

        PlanetHolder(View itemView) {
            super(itemView);
            fra_petLifePetImage = itemView.findViewById(R.id.fra_petlife_Pet_Image);
            fra_petLifePetName = itemView.findViewById(R.id.fra_petlife_pet_name);
            fra_petLifePetCategory = itemView.findViewById(R.id.fra_petlife_pet_category);
        }

        void setDetails(PetLife petLife) {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            StorageReference pathReference = storageRef.child(petLife.getPetLifePhoto());

            final long ONE_MEGABYTE = 2048 * 2048;
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap =Bytes2Bimap(bytes);
                    fra_petLifePetImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });


            fra_petLifePetName.setText(petLife.getPetName());
            fra_petLifePetCategory.setText(petLife.getPetCategory());
        }




        public Bitmap Bytes2Bimap(byte[] b) {
            if (b.length != 0) {
                return BitmapFactory.decodeByteArray(b, 0, b.length);
            } else {
                return null;
            }
        }
    }
}