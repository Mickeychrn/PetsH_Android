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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.adapter.PetLifeCardAdapter;
import au.edu.sydney.comp5216.petsh.model.PetLife;
import au.edu.sydney.comp5216.petsh.utils.RecyclerItemClickListener;

public class MyPetLife extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<PetLife> petLifeArrayList;
    private PetLifeCardAdapter adapter;
    private FirebaseFirestore mFirestore;
    private Context context;
    private String action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_my_petlife);


        recyclerView = (RecyclerView) findViewById(R.id.my_petlife_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        petLifeArrayList = new ArrayList<>();
        adapter = new PetLifeCardAdapter(this, petLifeArrayList);
        recyclerView.setAdapter(adapter);
        action =getIntent().getStringExtra("action");

        readMyPetLife();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if (action.equals("mypetlife")){
                            Intent intent =new Intent(MyPetLife.this,PetLifeDetail.class);
                            intent.putExtra("petName",petLifeArrayList.get(position).getPetName());
                            intent.putExtra("petCategory",petLifeArrayList.get(position).getPetCategory());
                            intent.putExtra("petLifePhoto",petLifeArrayList.get(position).getPetLifePhoto());
                            intent.putExtra("petLifeDescription",petLifeArrayList.get(position).getPetLifeDescription());
                            startActivity(intent);

                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }



    public void readMyPetLife(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid=user.getUid();
        petLifeArrayList.clear();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("PetLife").whereEqualTo("adoptPerson",Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PetLife petLife =document.toObject(PetLife.class);
                                petLifeArrayList.add(petLife);
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
