package com.example.findmytutor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MazeMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MazeMap extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MazeMap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chat.
     */
    // TODO: Rename and change types and number of parameters
    public static MazeMap newInstance(String param1, String param2) {
        MazeMap fragment = new MazeMap();
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
    WebView myWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maze_map, container, false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 201);
        }

        TextView titleTextView = (TextView) getActivity().findViewById(R.id.TitleTextView);
        titleTextView.setText("NTU Maze Map");

        myWebView = (WebView) view.findViewById(R.id.mazemap_webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
            myWebView.loadUrl("https://use.mazemap.com/#v=1&center=-1.185719,52.911951&zoom=15.4&campusid=745");
        return view;
    }
    public void changeUrl(String location, String campus){
        Toast.makeText(getActivity(), location, Toast.LENGTH_SHORT).show();
        int campusId = 0;
        if (campus.equals("City")) campusId = 640;
        else if (campus.equals("Clifton")) campusId = 745;
        else if (campus.equals("Brackenhurst")) campusId = 761;
        else if (campus.equals("Creative Quarter")) campusId = 786;
        else if (campus.equals("Mansfield")) campusId = 787;
        myWebView.loadUrl("https://use.mazemap.com/?campusid="+campusId+"&search="+location);
    }
}