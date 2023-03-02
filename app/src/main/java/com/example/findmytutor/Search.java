package com.example.findmytutor;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.signature.ObjectKey;
import com.example.findmytutor.ListAdapters.ListAdapterSearch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = "";
    String type = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        currentUser = bundle.getString("email");
        if (currentUser.endsWith("@my.ntu.ac.uk"))
            type = "Student";
        else if (currentUser.endsWith("@ntu.ac.uk"))
            type = "Tutor";

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        TextView titleTextView = (TextView) getActivity().findViewById(R.id.TitleTextView);

        titleTextView.setText("Search");

        getListItems(view);

        return view;
    }
    public void getListItems(View view){
        db.collection("Tutor").orderBy("lastName", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String[] name = new String[task.getResult().size()];
                String[] availability = new String[task.getResult().size()];
                String[] email = new String[task.getResult().size()];
                String[] department = new String[task.getResult().size()];
                String[] description = new String[task.getResult().size()];
                String[] title = new String[task.getResult().size()];
                Long[] avatarVersion = new Long [task.getResult().size()];
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        name[i] = (document.getString("lastName") + ", " + document.getString("firstName"));
                        availability[i] = document.getString("availability");
                        email[i] = document.getId();
                        department[i] = document.getString("department");
                        description[i] = document.getString("description");
                        title[i] = document.getString("title");
                        avatarVersion[i] = document.getLong("avatarVersion");
                        i++;
                    }
                    populateListWithItems(view, name, availability, email, department, description, title, avatarVersion);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });
    }
    public void populateListWithItems(View view, String[] name, String[] availability, String[] email, String[] department, String[] description, String[] title, Long [] avatarVersion) {
        ListView searchListView = (ListView) view.findViewById(R.id.searchList);
        LinearLayout singleTutorLayout = (LinearLayout) view.findViewById(R.id.singleTutor) ;

        ListAdapterSearch lAdapter;

        lAdapter = new ListAdapterSearch(getActivity(), name, availability, email, avatarVersion);

        searchListView.setAdapter(lAdapter);
        TextView SingleTutorName = (TextView) view.findViewById(R.id.singleTutorName);
        TextView SingleTutorAvailabilityText = (TextView) view.findViewById(R.id.singleTutorAvailabilityText);
        TextView SingleTutorTitle = (TextView) view.findViewById(R.id.singleTutorTitle);
        TextView SingleTutorDepartment = (TextView) view.findViewById(R.id.singleTutorDepartment);
        TextView SingleTutorDescription = (TextView) view.findViewById(R.id.singleTutorDescription);
        ImageView SingleTutorAvatar = (ImageView) view.findViewById(R.id.singleTutorAvatar);
        ImageView SingleTutorAvailabilityImage = (ImageView) view.findViewById(R.id.singleTutorAvailabilityImage);


        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Avatars/"+email[i]+".jpg");
                GlideApp.with(view).load(storageReference).signature(new ObjectKey(email[i]+avatarVersion[i])).placeholder(R.drawable.baseline_person_24).into(SingleTutorAvatar);

                if (availability[i].equals("Available"))
                    SingleTutorAvailabilityImage.setImageResource(R.drawable.baseline_event_available_24);
                else if (availability[i].equals("Tentative"))
                    SingleTutorAvailabilityImage.setImageResource(R.drawable.baseline_event_24);
                else
                    SingleTutorAvailabilityImage.setImageResource(R.drawable.baseline_event_busy_24);

                SingleTutorName.setText(name[i]);
                SingleTutorAvailabilityText.setText(availability[i]);
                SingleTutorTitle.setText(title[i]);
                SingleTutorDepartment.setText(department[i]);
                SingleTutorDescription.setText(description[i]);

                searchListView.setVisibility(View.GONE);
                singleTutorLayout.setVisibility(View.VISIBLE);

                getFavourites(view, email[i]);

                Button EmailButton = (Button) getActivity().findViewById(R.id.singleTutorEmailButton);
                EmailButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.parse("mailto:"+email[i]+"?subject=" + Uri.encode("Sent by Find My Tutor app."));
                            intent.setData(data);
                            startActivity(intent);
                        } catch (android.content.ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void deleteButton(View view, String email, List favourites){
        Button FavouritesButton = (Button) getActivity().findViewById(R.id.singleTutorFavouritesButton);
        FavouritesButton.setText("Remove from favourites");
        FavouritesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                favourites.remove(email);
                db.document(type+"/"+currentUser).update("favourites", favourites);
                getFavourites(view, email);
            }
        });
    }

    public void addButton(View view, String email, List favourites){
        Button FavouritesButton = (Button) getActivity().findViewById(R.id.singleTutorFavouritesButton);
        FavouritesButton.setText("Add to favourites");
        FavouritesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                favourites.add(email);
                db.document(type+"/"+currentUser).update("favourites", favourites);
                getFavourites(view, email);
            }
        });
    }

    public void getFavourites(View view, String email){
        db.document(type+"/"+currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<String> favourites = (List<String>) document.get("favourites");
                    if (favourites != null && favourites.contains(email))
                        deleteButton(view, email,favourites);
                    else addButton(view, email,favourites);
                }
            }
        });
    }

}