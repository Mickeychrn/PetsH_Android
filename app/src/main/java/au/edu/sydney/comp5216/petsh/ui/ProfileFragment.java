package au.edu.sydney.comp5216.petsh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import au.edu.sydney.comp5216.petsh.R;
import au.edu.sydney.comp5216.petsh.ui.subpage.AddPetActivity;
import au.edu.sydney.comp5216.petsh.ui.subpage.MyPetLife;
import au.edu.sydney.comp5216.petsh.ui.subpage.SearchPet;
import au.edu.sydney.comp5216.petsh.viewmodel.MainActivityViewModel;

public class ProfileFragment extends Fragment {

    private MainActivityViewModel mViewModel;
    private static final int RC_SIGN_IN = 9001;

    View view;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, null);

        String name = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
        }
        TextView userName = view.findViewById(R.id.user_name);
        userName.setText(name);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        Button addpetbutton = view.findViewById(R.id.add_pet);
        addpetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AddPetActivity.class);
                startActivity(intent);
            }
        });

        Button postpetlifebutton = view.findViewById(R.id.my_pet);
        postpetlifebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), SearchPet.class);
                intent.putExtra("action","mypet");
                startActivity(intent);
            }
        });

        Button logoutbutton = view.findViewById(R.id.logout);
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mViewModel.setIsSigningIn(false);
                startSignIn();
            }
        });


        Button mynewpet = view.findViewById(R.id.my_new_pet);
        mynewpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), SearchPet.class);
                intent.putExtra("action","mypostpet");
                startActivity(intent);

            }
        });

        Button myPetLife = view.findViewById(R.id.my_pet_life);
        myPetLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), MyPetLife.class);
                intent.putExtra("action","mypetlife");
                startActivity(intent);
            }
        });
        return view;
    }

    private void startSignIn() {
// Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false) .build();
        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true); }


}