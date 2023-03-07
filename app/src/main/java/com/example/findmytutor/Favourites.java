package com.example.findmytutor;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favourites extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Favourites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favourites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favourites newInstance(String param1, String param2) {
        Favourites fragment = new Favourites();
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

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        TextView titleTextView = (TextView) getActivity().findViewById(R.id.TitleTextView);

        titleTextView.setText("Favourites");


        db.document(type+"/"+currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<String> favourites = (List<String>) document.get("favourites");
                    if (favourites.isEmpty()) Toast.makeText(getActivity(), "You don't have any favourites!", Toast.LENGTH_SHORT).show();
                    else getListItems(view, favourites);
                }
            }
        });


        return view;
    }
    public void getListItems(View view, List favourites ){
        db.collection("Tutor").orderBy("lastName", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String[] name = new String[favourites.size()];
                String[] availability = new String[favourites.size()];
                String[] email = new String[favourites.size()];
                String[] department = new String[favourites.size()];
                String[] description = new String[favourites.size()];
                String[] title = new String[favourites.size()];
                String[] location = new String[favourites.size()];
                String[] campus = new String[favourites.size()];
                Long [] avatarVersion = new Long [favourites.size()];
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (favourites.contains(document.getId())) {
                            name[i] = (document.getString("lastName") + ", " + document.getString("firstName"));
                            availability[i] = document.getString("availability");
                            email[i] = document.getId();
                            department[i] = document.getString("department");
                            description[i] = document.getString("description");
                            title[i] = document.getString("title");
                            avatarVersion[i] = document.getLong("avatarVersion");
                            location[i] = document.getString("location");
                            campus[i] = document.getString("campus");
                            i++;
                        }
                    }
                    populateListWithItems(view, favourites, name, availability, email, department, description, title, avatarVersion, location, campus);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    public void populateListWithItems(View view, List favourites, String[] name, String[] availability, String[] email, String[] department, String[] description, String[] title, Long [] avatarVersion, String [] location, String [] campus){
        LinearLayout singleTutorLayout = (LinearLayout) view.findViewById(R.id.singleTutor) ;

        ListAdapterSearch lAdapter;
        ListView searchListView = (ListView) view.findViewById(R.id.favouritesList);

        lAdapter = new ListAdapterSearch(getContext(), name, availability, email, avatarVersion);

        searchListView.setAdapter(lAdapter);
        TextView SingleTutorName = (TextView) view.findViewById(R.id.singleTutorName);
        TextView SingleTutorAvailabilityText = (TextView) view.findViewById(R.id.singleTutorAvailabilityText);
        TextView SingleTutorTitle = (TextView) view.findViewById(R.id.singleTutorTitle);
        TextView SingleTutorDepartment = (TextView) view.findViewById(R.id.singleTutorDepartment);
        TextView SingleTutorDescription = (TextView) view.findViewById(R.id.singleTutorDescription);
        ImageView SingleTutorAvatar = (ImageView) view.findViewById(R.id.singleTutorAvatar);
        ImageView SingleTutorAvailabilityImage = (ImageView) view.findViewById(R.id.singleTutorAvailabilityImage);
        Button FavouritesButton = (Button) view.findViewById(R.id.singleTutorFavouritesButton);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Avatars/"+email[i]+".jpg");
                GlideApp.with(view).load(storageReference).signature(new ObjectKey(email[i]+avatarVersion[i])).placeholder(R.drawable.baseline_person_24).into(SingleTutorAvatar);

                if (availability[i].equals("Available")) {
                    SingleTutorAvailabilityImage.setImageResource(R.drawable.baseline_event_available_24);
                    SingleTutorAvailabilityText.setText(availability[i]+" at: "+location[i]+" on "+campus[i]+" Campus");
                }
                else if (availability[i].equals("Tentative")) {
                    SingleTutorAvailabilityImage.setImageResource(R.drawable.baseline_event_24);
                    SingleTutorAvailabilityText.setText(availability[i]+" at: "+location[i]+" on "+campus[i]+" Campus");
                }
                else {
                    SingleTutorAvailabilityImage.setImageResource(R.drawable.baseline_event_busy_24);
                    SingleTutorAvailabilityText.setText(availability[i]);
                }

                SingleTutorName.setText(name[i]);

                SingleTutorTitle.setText(title[i]);
                SingleTutorDepartment.setText(department[i]);
                SingleTutorDescription.setText(description[i]);

                searchListView.setVisibility(View.GONE);
                singleTutorLayout.setVisibility(View.VISIBLE);

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

                Button Location = (Button) getActivity().findViewById(R.id.singleTutorMazeMapButton);
                if (availability[i].equals("Available") || availability[i].equals("Tentative")) {
                    Location.setVisibility(View.VISIBLE);
                    Location.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            FragmentManager fm = getParentFragmentManager();
                            MazeMap frag = (MazeMap)fm.findFragmentByTag("MazeMap");
                            if (type.equals("Tutor"))
                                ((TutorNavigationActivity) getActivity()).switchToMazeMap();
                            else
                                ((StudentNavigationActivity) getActivity()).switchToMazeMap();
                            frag.changeUrl(location[i], campus[i]);
                        }
                    });
                }
                else Location.setVisibility(View.GONE);

                FavouritesButton.setText("Remove from favourites");
                FavouritesButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        singleTutorLayout.setVisibility(View.GONE);
                        searchListView.setVisibility(View.VISIBLE);
                        favourites.remove(email[i]);
                        getListItems(view, favourites);

                        db.document(type+"/"+currentUser).update("favourites", favourites);
                    }
                });
            }
        });
    }
}