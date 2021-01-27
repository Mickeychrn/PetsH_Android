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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.adapter.PetLifeCardAdapter;
import au.edu.sydney.comp5216.petsh.model.PetLife;
import au.edu.sydney.comp5216.petsh.ui.subpage.PetLifeDetail;
import au.edu.sydney.comp5216.petsh.utils.RecyclerItemClickListener;

public class PetLifeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PetLifeCardAdapter adapter;
    private ArrayList<PetLife> petLifeArrayList;
    private FirebaseFirestore mFirestore;
    private Context context;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_petlife, null);
        initView();
        return view;

    }


    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        petLifeArrayList = new ArrayList<>();
        adapter = new PetLifeCardAdapter(this.getContext(), petLifeArrayList);
        recyclerView.setAdapter(adapter);
        createListData();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), PetLifeDetail.class);
                        intent.putExtra("petName", petLifeArrayList.get(position).getPetName());
                        intent.putExtra("petCategory", petLifeArrayList.get(position).getPetCategory());
                        intent.putExtra("petLifePhoto", petLifeArrayList.get(position).getPetLifePhoto());
                        intent.putExtra("petLifeDescription", petLifeArrayList.get(position).getPetLifeDescription());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

    }

    private void createListData() {

        mFirestore = FirebaseFirestore.getInstance();
        petLifeArrayList.clear();
        mFirestore.collection("PetLife")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PetLife petLife = document.toObject(PetLife.class);
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