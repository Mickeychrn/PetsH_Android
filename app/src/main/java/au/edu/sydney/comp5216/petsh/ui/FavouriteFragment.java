package au.edu.sydney.comp5216.petsh.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import au.edu.sydney.comp5216.petsh.adapter.PetCardAdapeter;
import au.edu.sydney.comp5216.petsh.model.Pet;
import au.edu.sydney.comp5216.petsh.ui.subpage.SearchPetDetail;
import au.edu.sydney.comp5216.petsh.utils.RecyclerItemClickListener;

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private PetCardAdapeter adapter;
    private ArrayList<Pet> petArrayList;


    private FirebaseFirestore mFirestore;
    private Context context;

    View view;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.favourite_recyclerview);
        initView();


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), SearchPetDetail.class);

                        intent.putExtra("petName",petArrayList.get(position).getPetName());
                        intent.putExtra("petCategory",petArrayList.get(position).getPetCategory());
                        intent.putExtra("PetPhotoName",petArrayList.get(position).getPetPhotoName());
                        intent.putExtra("petId",petArrayList.get(position).getPetId());
                        intent.putExtra("petAge",petArrayList.get(position).getPetAge());
                        intent.putExtra("petGender",petArrayList.get(position).getPetGender());
                        intent.putExtra("petDescription",petArrayList.get(position).getPetDescription());
                        intent.putExtra("adoptPerson",petArrayList.get(position).getAdoptPerson());

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


        return view;
    }


    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        petArrayList = new ArrayList<>();
        adapter = new PetCardAdapeter(this.getContext(), petArrayList);
        recyclerView.setAdapter(adapter);
        readUserFavourite();
    }

    public void readUserFavourite() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid = user.getUid();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("favouriteRecord").whereEqualTo("userId", Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Pet pet = document.toObject(Pet.class);
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