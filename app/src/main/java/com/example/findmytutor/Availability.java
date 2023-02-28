package com.example.findmytutor;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Availability#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Availability extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String Availability = "Available";
    private String Email;
    public static final int PICK_IMAGE = 1;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Availability() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Availability.
     */
    // TODO: Rename and change types and number of parameters
    public static Availability newInstance(String param1, String param2) {
        Availability fragment = new Availability();
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
        Bundle bundle = getArguments();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_availability, container, false);
        EditText title = (EditText) view.findViewById(R.id.editTextTitle);
        EditText department = (EditText) view.findViewById(R.id.editTextDepartment);
        EditText description = (EditText) view.findViewById(R.id.editTextDescription);
        EditText location = (EditText) view.findViewById(R.id.editTextLocation);
        EditText time = (EditText) view.findViewById(R.id.editTextEndAfter);
        ImageView imageUpload = (ImageView) view.findViewById(R.id.imageViewAvatar);

        LinearLayout locationLayout = (LinearLayout) view.findViewById(R.id.locationLayout);
        LinearLayout timeLayout = (LinearLayout) view.findViewById(R.id.timeLayout);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.availability_navigation);
        TextView titleTextView = (TextView) getActivity().findViewById(R.id.TitleTextView);
        Email = bundle.getString("email");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Avatars/"+Email+".jpg");
        GlideApp.with(getActivity()).load(storageReference).signature(new ObjectKey(storageReference.getMetadata())).placeholder(R.drawable.baseline_perm_contact_calendar_24).into(imageUpload);

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Available:
                                Availability = "Available";
                                locationLayout.setVisibility(View.VISIBLE);
                                timeLayout.setVisibility(View.VISIBLE);
                                break;
                            case R.id.Tentative:
                                Availability = "Tentative";
                                locationLayout.setVisibility(View.VISIBLE);
                                timeLayout.setVisibility(View.VISIBLE);
                                break;
                            case R.id.Busy:
                            default:
                                Availability = "Busy";
                                locationLayout.setVisibility(View.GONE);
                                timeLayout.setVisibility(View.GONE);
                                break;
                        }
                        return true;
                    }
                });



        DocumentReference docRef = db.collection("Tutor").document(Email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    title.setText(document.getString("title"));
                    department.setText(document.getString("department"));
                    description.setText(document.getString("description"));
                    location.setText(document.getString("location"));
                    titleTextView.setText(document.getString("lastName") + ", " + document.getString("firstName"));
                    try {
                        time.setText(document.getDate("time").toString());
                    } catch (Exception e) {
                        time.setText(new Timestamp(System.currentTimeMillis()).toString());
                    }

                }
            }
        });

        Button buttonSave = (Button) view.findViewById(R.id.saveButton);
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ((Availability.equals("Available") || Availability.equals("Tentative"))&&(title.getText().toString().isEmpty() || department.getText().toString().isEmpty() || description.getText().toString().isEmpty() || location.getText().toString().isEmpty() || time.getText().toString().isEmpty()))
                    Toast.makeText(getActivity(), "Missing fields!", Toast.LENGTH_SHORT).show();
                else if (Availability.equals("Busy")&&(title.getText().toString().isEmpty() || department.getText().toString().isEmpty() || description.getText().toString().isEmpty()))
                    Toast.makeText(getActivity(), "Missing fields!", Toast.LENGTH_SHORT).show();
                else {
//                    try {
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//                        Date parsedDate = dateFormat.parse(document.getString("time"));
//                        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
//                    } catch(Exception e) { //this generic but you can control another types of exception
//                        // look the origin of excption
//                    }

                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> tutor = new HashMap<>();
                                tutor.put("title", title.getText().toString());
                                tutor.put("department", department.getText().toString());
                                tutor.put("description", description.getText().toString());
                                tutor.put("availability", Availability);
                                if (Availability.equals("Busy")) {
                                    tutor.put("location", "");
                                    tutor.put("time", new Timestamp(System.currentTimeMillis()));
                                } else {
                                    tutor.put("location", location.getText().toString());
                                    tutor.put("time", time.getText().toString());
                                }

                                docRef.update(tutor);
                                Toast.makeText(getActivity(), "Successfully changed details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        imageUpload.setOnClickListener(new View.OnClickListener()
        {
            //got from https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Avatars/"+Email+".jpg");
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            storageReference.delete();
            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            storageReference.putStream(inputStream).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        ImageView buttonUpload = (ImageView) getActivity().findViewById(R.id.imageViewAvatar);

                        GlideApp.with(getActivity()).load(storageReference).signature(new ObjectKey(taskSnapshot.getMetadata())).placeholder(R.drawable.baseline_perm_contact_calendar_24).into(buttonUpload);
                        Toast.makeText(getActivity(), "Successfully uploaded!", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}