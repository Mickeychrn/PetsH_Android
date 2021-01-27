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
import au.edu.sydney.comp5216.petsh.model.Pet;

public class RecommendPetCardAdapeter extends RecyclerView.Adapter<RecommendPetCardAdapeter.PetHolder>{
    private Context context;
    private ArrayList<Pet> pets;

    FirebaseStorage storage;
    StorageReference storageRef;

    public RecommendPetCardAdapeter(Context context, ArrayList<Pet> pets) {
        this.context = context;
        this.pets = pets;

    }

    @NonNull
    @Override
    public PetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_pet, parent, false);
        return  new RecommendPetCardAdapeter.PetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetHolder holder, int position) {
        holder.setDetails(pets.get(position));
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    class PetHolder extends RecyclerView.ViewHolder {
        private ImageView imagePet;
        private TextView txtPetName, txtPetCategory;

        PetHolder(View itemView) {
            super(itemView);
            imagePet=itemView.findViewById(R.id.fra_home_pet_image);
            txtPetName = itemView.findViewById(R.id.fra_home_pet_name);
            txtPetCategory =itemView.findViewById(R.id.fra_home_pet_category);
        }

        void setDetails(Pet pet) {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            StorageReference pathReference = storageRef.child(pet.getPetPhotoName());

            final long ONE_MEGABYTE = 2048 * 2048;
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap =Bytes2Bimap(bytes);
                    imagePet.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
            txtPetName.setText("Pet Name: "+pet.getPetName());
            txtPetCategory.setText("Category: "+pet.getPetCategory());
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
