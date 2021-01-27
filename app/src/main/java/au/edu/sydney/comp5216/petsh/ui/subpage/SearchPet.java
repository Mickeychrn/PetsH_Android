package au.edu.sydney.comp5216.petsh.ui.subpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.adapter.PetCardAdapeter;
import au.edu.sydney.comp5216.petsh.model.Pet;
import au.edu.sydney.comp5216.petsh.utils.RecyclerItemClickListener;

public class SearchPet extends AppCompatActivity{


    private RecyclerView recyclerView;
    private PetCardAdapeter adapter;
    private ArrayList<Pet> petArrayList;
    private FirebaseFirestore mFirestore;
    private Context context;
    private String action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_search);
        action =getIntent().getStringExtra("action");



        initView();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if (!action.equals("mypet")){
                            Intent intent = new Intent(SearchPet.this,SearchPetDetail.class);
                            intent.putExtra("petName",petArrayList.get(position).getPetName());
                            intent.putExtra("petCategory",petArrayList.get(position).getPetCategory());
                            intent.putExtra("PetPhotoName",petArrayList.get(position).getPetPhotoName());
                            intent.putExtra("petId",petArrayList.get(position).getPetId());
                            intent.putExtra("petAge",petArrayList.get(position).getPetAge());
                            intent.putExtra("petGender",petArrayList.get(position).getPetGender());
                            intent.putExtra("petDescription",petArrayList.get(position).getPetDescription());
                            intent.putExtra("adoptPerson",petArrayList.get(position).getAdoptPerson());

                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(SearchPet.this, PostPetLifeActivity.class);
                            intent.putExtra("petName",petArrayList.get(position).getPetName());
                            intent.putExtra("petCategory",petArrayList.get(position).getPetCategory());
                            intent.putExtra("AdoptPerson",petArrayList.get(position).getAdoptPerson());
                            intent.putExtra("petAge",petArrayList.get(position).getPetAge());
                            intent.putExtra("petGender",petArrayList.get(position).getPetGender());
                            intent.putExtra("petDescription",petArrayList.get(position).getPetDescription());
                            intent.putExtra("adoptPerson",petArrayList.get(position).getAdoptPerson());
                            startActivity(intent);
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }



    private void initView() {
        recyclerView = findViewById(R.id.pet_search_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        petArrayList = new ArrayList<>();
        adapter = new PetCardAdapeter(this, petArrayList);
        recyclerView.setAdapter(adapter);
        if (action.equals("mypet")){
            readMyPet();
        }else if(action.equals("mypostpet")){
            readMyPostPet();
        }else{
            readDatabyName();
        }

    }


    public void readDatabyName() {
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
        petArrayList.clear();
        Query query = null;
        if (action.equals("searchByCategoryCat")){
            query =  mFirestore.collection("Pet").whereEqualTo("petCategory","dog");
        }else if (action.equals("searchByCategoryDog")){
            query =  mFirestore.collection("Pet").whereEqualTo("petCategory","cat");
        }else if(action.equals("searchByCategoryOther")){
            query =  mFirestore.collection("Pet").whereNotEqualTo("petCategory","cat");
        }else {
            query =  mFirestore.collection("Pet");
        }
        query.limit(20).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String petId = document.getId();
                                Pet pet = document.toObject(Pet.class);
                                pet.setPetId(petId);
                                if (pet.getAdoptPerson().equals("nobody")){
                                    if (action.equals("searchByName")){
                                        if (pet.getPetName().contains(getIntent().getStringExtra("searchString"))){
                                            petArrayList.add(pet);
                                        }
                                    }else if (action.equals("searchByCategoryOther")){
                                        if (pet.getPetCategory().equals("dog")==false){
                                            petArrayList.add(pet);
                                        }
                                    }else {
                                        petArrayList.add(pet);
                                    }
                                }

                                adapter.notifyDataSetChanged();

                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    public void readMyPet() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid=user.getUid();
        petArrayList.clear();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Pet").whereEqualTo("adoptPerson",Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String petId = document.getId();
                                Pet pet = document.toObject(Pet.class);
                                pet.setPetId(petId);
                                petArrayList.add(pet);
                                adapter.notifyDataSetChanged();
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void readMyPostPet(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid=user.getUid();
        petArrayList.clear();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Pet").whereEqualTo("addPerson",Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String petId = document.getId();
                                Pet pet = document.toObject(Pet.class);
                                pet.setPetId(petId);
                                petArrayList.add(pet);
                                adapter.notifyDataSetChanged();
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
