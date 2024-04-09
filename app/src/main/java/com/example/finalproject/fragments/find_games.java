package com.example.finalproject.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.finalproject.R;
import com.example.finalproject.activities.MainActivity;
import com.example.finalproject.models.CustomeAdapter;
import com.example.finalproject.models.Game;
import com.example.finalproject.models.GameCard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link find_games#newInstance} factory method to
 * create an instance of this fragment.
 */
public class find_games extends Fragment  implements AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> gameNames ;
    private ArrayList<String> gameTypes ;
    private ArrayList<String> gameDates ;
    private ArrayList<String> gameDevComp ;
    private ArrayList<String> gameDesc ;
    private ArrayList<GameCard> dataSet;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomeAdapter adapter;
    private String selectedType = "Select All";
    private String selectedYear = "Select All";
    private String selectedDevComp = "Select All";
   private HashMap<String,Uri>imageMap;

   //private  Uri imageUri;

    public find_games() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment find_games.
     */
    // TODO: Rename and change types and number of parameters
    public static find_games newInstance(String param1, String param2) {
        find_games fragment = new find_games();
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

        gameNames = new ArrayList<>();
        gameTypes = new ArrayList<>();
        gameDates = new ArrayList<>();
        gameDevComp = new ArrayList<>();
        gameDesc = new ArrayList<>();
        dataSet = new ArrayList<>();


        // Initialize the HashMap
        imageMap = new HashMap<>();

        gameTypes.add("Select All");
        gameDates.add("Select All");
        gameDevComp.add("Select All");
        gameNames.add("Select All");
        gameDesc.add("Select All");

        // Get reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gamesRef = database.getReference("games");

        FirebaseStorage storage  = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images/");;
        // List all items (images) in the root directory

        imagesRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // Iterate through each item (image)
                for (StorageReference item : listResult.getItems()) {
                    // Get the download URL of the image
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Add the URI to the list
                            Log.d("The uri is:",uri.getLastPathSegment());
                            String imageName=extractImageName(uri.getLastPathSegment());
                            imageMap.put(imageName,uri);
                            // Check if all images are loaded
                            if (imageMap.size() == listResult.getItems().size()) {
                                Spinner types_categories = getView().findViewById(R.id.types);
                                simulateItemSelected( types_categories, 0);
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure of the listAll() operation
                Log.e("Firebase Storage", "Error listing images: " + e.getMessage());

            }
        });;

        // Add ValueEventListener to retrieve data
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Iterate through the children (each child represents a game)
                    Game g = snapshot.getValue(Game.class);
                    if (g != null) {
                        // Do something with the retrieved data
                        Log.d("Game name: " ,g.getName());
                        gameNames.add(g.getName());
                        gameTypes.add(g.getType());
                        gameDates.add(g.getLaunch_Date());
                        gameDevComp.add(g.getDevelop_Comp());
                        gameDesc.add(g.getDescription());
                    }
                }
                if(getView()!=null)
                   setUpSpinners();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // This method will be triggered in the event that this listener either failed at the server, or is removed as a result of the security and Firebase Realtime Database rules.

                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_find_games, container, false);

        recyclerView = view.findViewById(R.id.resView);

        recyclerView.setHasFixedSize(true);

        //layoutManager = new LinearLayoutManager(getContext());
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator((new DefaultItemAnimator()));

        adapter= new CustomeAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//            setUpSpinners();
//
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String type = selectedType, year =selectedYear,dev_comp=selectedDevComp;

        Log.d("Got here: ","");

        if(parent.getId()==R.id.types){
            type = parent.getItemAtPosition(position).toString();
            selectedType = type;
        }
        if(parent.getId()==R.id.years){
            year = parent.getItemAtPosition(position).toString();
            selectedYear = year;
        }
        if(parent.getId()==R.id.dev_comps){
            dev_comp = parent.getItemAtPosition(position).toString();
            selectedDevComp = dev_comp;
        }
        insertDataSet(type, year, dev_comp);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void insertDataSet(String type, String year, String dev_comp)
    {
        dataSet.clear(); // Clear the current dataset
        adapter.notifyDataSetChanged();
        int position=0;

//        Log.d("Type: " ,type);
//        Log.d("Year: " ,year);
//        Log.d("Dec comp: " ,dev_comp);
//        Log.d("Size: " ,String.valueOf(dataSet.size()));

        for(int i=1;i<gameTypes.size();i++){
            if((gameTypes.get(i).equals(type) || type.equals("Select All")) && (gameDates.get(i).equals(year) || year.equals("Select All"))
                    && (gameDevComp.get(i).equals(dev_comp) || dev_comp.equals("Select All"))) {

                // Remove punctuation (",", ".", ":") using regular expression
                    String imageName = gameNames.get(i).replaceAll("[,:.]", "");
                    GameCard g = new GameCard(gameNames.get(i), gameTypes.get(i), gameDates.get(i), gameDevComp.get(i), gameDesc.get(i),imageMap.get(imageName));
                    dataSet.add(position,g);
                    adapter.notifyItemInserted(position);
                    position++;
            }
        }

       // Log.d("Size of GameTypes", String.valueOf(gameTypes.size()));

    }
    private void setUpSpinners() {
        Spinner types_categories = getView().findViewById(R.id.types);
        Spinner years_categories = getView().findViewById(R.id.years);
        Spinner dev_comps_categories = getView().findViewById(R.id.dev_comps);

        ArrayList<String> gameTypes2=removeDuplicates(gameTypes);
        ArrayList<String> gameDates2=removeDuplicates(gameDates);
        ArrayList<String> gameDevComp2=removeDuplicates(gameDevComp);

        // Create an ArrayAdapter using the ArrayList and default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, gameTypes2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, gameDates2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, gameDevComp2);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        adapter2.setDropDownViewResource(R.layout.spinner_item_layout);
        adapter3.setDropDownViewResource(R.layout.spinner_item_layout);
        // Apply the adapter to the spinner
        types_categories.setAdapter(adapter);
        years_categories.setAdapter(adapter2);
        dev_comps_categories.setAdapter(adapter3);


        types_categories.setOnItemSelectedListener(this);
        years_categories.setOnItemSelectedListener(this);
        dev_comps_categories.setOnItemSelectedListener(this);

        // After adding items to the adapter, notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();

//        types_categories.setSelection(0);
//        years_categories.setSelection(0);
//        dev_comps_categories.setSelection(0);
    }
    private ArrayList<String> removeDuplicates(ArrayList<String> list) {
        // Create a HashSet to store unique elements
        HashSet<String> set = new HashSet<>();

        // Create a new ArrayList to store elements without duplicates
        ArrayList<String> result =  new ArrayList<>();

        // Iterate through the list
        for (String element : list) {
            // If the set does not contain the element, add it to both the set and the result list
            if (!set.contains(element)) {
                set.add(element);
                result.add(element);
            }
        }
// Free up the HashSet
        set.clear();
        return result;
    }

    private String extractImageName(String imagePath) {
        // Remove "images/" from the path
        String imageName = imagePath.replace("images/", "");

        // Remove file extension
        int dotIndex = imageName.lastIndexOf('.');
        if (dotIndex != -1) {
            imageName = imageName.substring(0, dotIndex);
        }

        // Remove punctuation (",", ".")
        imageName = imageName.replaceAll("[,:.]", "");

        return imageName;
    }
    private void simulateItemSelected(AdapterView<?> parent, int position) {
        onItemSelected(parent, parent.getChildAt(position), position, parent.getAdapter().getItemId(position));
    }
}