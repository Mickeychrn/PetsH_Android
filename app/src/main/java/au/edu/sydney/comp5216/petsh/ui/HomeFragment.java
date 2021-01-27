package au.edu.sydney.comp5216.petsh.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.adapter.RecommendPetCardAdapeter;
import au.edu.sydney.comp5216.petsh.model.Pet;
import au.edu.sydney.comp5216.petsh.ui.subpage.SearchPet;
import au.edu.sydney.comp5216.petsh.ui.subpage.SearchPetDetail;
import au.edu.sydney.comp5216.petsh.utils.RecyclerItemClickListener;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecommendPetCardAdapeter adapter;
    private ArrayList<Pet> petArrayList;

    private FirebaseFirestore mFirestore;
    private Context context;

    View view;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        final EditText editText =view.findViewById(R.id.home_serach_textview);

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView = (RecyclerView) view.findViewById(R.id.home_recommend_recycleview);

        recyclerView.setLayoutManager(manager);

        petArrayList = new ArrayList<>();


        adapter = new RecommendPetCardAdapeter(this.getContext(), petArrayList);
        recyclerView.setAdapter(adapter);

        readRecommend();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), SearchPetDetail.class);
                        intent.putExtra("petName", petArrayList.get(position).getPetName());
                        intent.putExtra("petCategory", petArrayList.get(position).getPetCategory());
                        intent.putExtra("PetPhotoName", petArrayList.get(position).getPetPhotoName());
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


        ImageButton homeSearchButton = view.findViewById(R.id.home_search_button);
        Button homeSearchCatButton = view.findViewById(R.id.home_search_cat_button);
        Button homeSearchDogButton = view.findViewById(R.id.home_search_dog_button);
        Button homeSearchOtherButton = view.findViewById(R.id.home_search_other_button);
        Button homeSearchAllButton = view.findViewById(R.id.home_search_all_button);

        homeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchString = editText.getText().toString();
                Intent intent =new Intent(getActivity(), SearchPet.class);
                intent.putExtra("action","searchByName");
                intent.putExtra("searchString",searchString);
                startActivity(intent);
            }
        });
        homeSearchCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), SearchPet.class);
                intent.putExtra("action","searchByCategoryCat");
                startActivity(intent);
            }
        });

        homeSearchDogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), SearchPet.class);
                intent.putExtra("action","searchByCategoryDog");
                startActivity(intent);
            }
        });

        homeSearchOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), SearchPet.class);
                intent.putExtra("action","searchByCategoryOther");
                startActivity(intent);
            }
        });

        homeSearchAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), SearchPet.class);
                intent.putExtra("action","searchByCategoryAll");
                startActivity(intent);
            }
        });

        return view;
    }


    public void readRecommend(){

        petArrayList.clear();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Pet").whereEqualTo("adoptPerson","nobody").limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String petId = document.getId();
                                Pet pet=document.toObject(Pet.class);
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