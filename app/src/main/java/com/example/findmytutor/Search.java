package com.example.findmytutor;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        TextView titleTextView = (TextView) getActivity().findViewById(R.id.TitleTextView);
        ListView searchListView = (ListView) view.findViewById(R.id.searchList);
        LinearLayout singleTutorLayout = (LinearLayout) view.findViewById(R.id.singleTutor) ;
        titleTextView.setText("Search");

        db.collection("Tutor").orderBy("lastName", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String[] name = new String[task.getResult().size()];
                String[] availability = new String[task.getResult().size()];
                String[] email = new String[task.getResult().size()];
                String[] department = new String[task.getResult().size()];
                String[] description = new String[task.getResult().size()];
                String[] title = new String[task.getResult().size()];
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        name[i] = (document.getString("lastName") + ", " + document.getString("firstName"));
                        availability[i] = document.getString("availability");
                        email[i] = document.getId();
                        department[i] = document.getString("department");
                        description[i] = document.getString("description");
                        title[i] = document.getString("title");
                        i++;
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                ListView lView;

                ListAdapterSearch lAdapter;
                lView = (ListView) view.findViewById(R.id.searchList);

                lAdapter = new ListAdapterSearch(getActivity(), name, availability, email);

                lView.setAdapter(lAdapter);
                TextView SingleTutorName = (TextView) view.findViewById(R.id.singleTutorName);
                TextView SingleTutorAvailabilityText = (TextView) view.findViewById(R.id.singleTutorAvailabilityText);
                TextView SingleTutorTitle = (TextView) view.findViewById(R.id.singleTutorTitle);
                TextView SingleTutorDepartment = (TextView) view.findViewById(R.id.singleTutorDepartment);
                TextView SingleTutorDescription = (TextView) view.findViewById(R.id.singleTutorDescription);
                ImageView SingleTutorAvatar = (ImageView) view.findViewById(R.id.singleTutorAvatar);
                ImageView SingleTutorAvailabilityImage = (ImageView) view.findViewById(R.id.singleTutorAvailabilityImage);

                lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Avatars/"+email[i]+".jpg");
                        GlideApp.with(view).load(storageReference).signature(new ObjectKey(storageReference.getMetadata())).placeholder(R.drawable.baseline_person_24).into(SingleTutorAvatar);

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
                    }
                });
            }
        });
        return view;
    }
}