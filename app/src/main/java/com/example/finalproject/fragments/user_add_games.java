package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.finalproject.R;
import com.example.finalproject.activities.MainActivity;
import com.example.finalproject.models.Game;
import com.example.finalproject.models.ItemViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link user_add_games#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user_add_games extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Uri imageUri;

    // Initialize Firebase Storage
    private StorageReference storageReference;

    private Boolean clean=false;


    public user_add_games() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment user_add_games.
     */
    // TODO: Rename and change types and number of parameters
    public static user_add_games newInstance(String param1, String param2) {
        user_add_games fragment = new user_add_games();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_add_games, container, false);

        Button addGame = view.findViewById(R.id.send_game);

        EditText game_name1 = ((EditText)view.findViewById(R.id.game_name));
        EditText game_type1 = ((EditText)view.findViewById(R.id.game_type));
        EditText launch_Date1 = ((EditText)view.findViewById(R.id.launch_data));
        EditText develop_comp1 = ((EditText)view.findViewById(R.id.develop_comp));
        EditText game_desc1 = ((EditText)view.findViewById(R.id.game_desc));
        ImageView selectImg = (ImageView) view.findViewById(R.id.selectImg);
        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        ActivityResultLauncher<Intent> activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d("Image uri:",imageUri.toString());
                        selectImg.setImageURI(imageUri);
                    }
                });;
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               activityResultLauncher.launch(intent);
            }
        });
        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String game_name = game_name1.getText().toString().trim();
                String game_type = game_type1.getText().toString().trim();
                String launch_Date = launch_Date1.getText().toString().trim();
                String develop_comp = develop_comp1.getText().toString().trim();
                String game_desc = game_desc1.getText().toString().trim();

                if(!game_name.isEmpty() && !game_type.isEmpty() && !launch_Date.isEmpty() && !develop_comp.isEmpty() && !game_desc.isEmpty()) {
                    Game game = new Game(game_name, game_type, launch_Date, develop_comp, game_desc);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("games").child(game.getName());
                    myRef.setValue(game);

                    uploadImage(game_name1.getText().toString().trim());

                    clean=true;

                    //navigate to find_games fragment
                    Navigation.findNavController(view).navigate(R.id.action_user_add_games_to_find_games);
                }
            }
        });


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        // Clear EditText fields when fragment is resumed
        EditText game_name1 = requireView().findViewById(R.id.game_name);
        EditText game_type1 = requireView().findViewById(R.id.game_type);
        EditText launch_Date1 = requireView().findViewById(R.id.launch_data);
        EditText develop_comp1 = requireView().findViewById(R.id.develop_comp);
        EditText game_desc1 = requireView().findViewById(R.id.game_desc);

       if(clean) {
           game_name1.setText("");
           game_type1.setText("");
           launch_Date1.setText("");
           develop_comp1.setText("");
           game_desc1.setText("");
       }
    }
    private void uploadImage(String name) {
        name = name.replaceAll("[,:.]", "");
        Log.d("name is:",name);
        if (imageUri != null) {
            // Create a Cloud Storage reference from the app

            StorageReference fileReference = storageReference.child("images/" +name+ ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();

                       //  Handle successful uploads here, e.g., get the download URL
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {

                            ItemViewModel viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
                            viewModel.selectItem(uri);


                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


}