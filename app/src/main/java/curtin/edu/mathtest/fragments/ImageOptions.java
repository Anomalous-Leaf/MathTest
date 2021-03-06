package curtin.edu.mathtest.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.TestDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageOptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageOptions extends Fragment {

    public static final String IMAGE_KEY = "imageKey";
    public static final String IMAGE_SELECTOR_FRAGMENT = "imageFrag";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STUDENT_ID = "studentId";


    // TODO: Rename and change types of parameters
    private int studentId;

    private Button takePhotoButton;
    private Button pickPhotoButton;
    private Button searchOnlineButton;
    private EditText searchTermBox;
    private FragmentManager parentManager;
    private Uri imageUri;
    private ActivityResultLauncher<Uri> photoLauncher;
    private ActivityResultLauncher<String> pickPhotoLauncher;
    private ActivityResultLauncher<String> permissionRequestLauncher;
    private TestDatabase db;

    public ImageOptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @return A new instance of fragment ImageOptions.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageOptions newInstance(int inId) {
        ImageOptions fragment = new ImageOptions();
        Bundle args = new Bundle();
        args.putInt(STUDENT_ID, inId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentId = getArguments().getInt(STUDENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_options, container, false);

        parentManager = getParentFragmentManager();
        db = TestDatabase.getInstance();

        takePhotoButton = view.findViewById(R.id.takePhotoButton);
        pickPhotoButton = view.findViewById(R.id.pickPhotoButton);
        searchOnlineButton = view.findViewById(R.id.searchPhotoButton);
        searchTermBox = view.findViewById(R.id.searchTermBox);

        //Set up callbacks for each button

        //Setup callback for taking photo button
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File photoFile = new File(getActivity().getFilesDir(), studentId + ".jpg");

                //Build a Uri to pass to photo app
                imageUri = FileProvider.getUriForFile(getActivity(), "curtin.edu.fileprovider", photoFile);

                //Launch activity
                photoLauncher.launch(imageUri);

            }
        });

        //Launcher for taking a full photo
        photoLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result)
            {
                Bundle bundle = new Bundle();

                if (result == true)
                {
                    //imageUri is the Uri passed to the photo app
                    bundle.putParcelable(IMAGE_KEY, imageUri);

                    //Return Uri to the parent fragment and pop backstack
                    parentManager.setFragmentResult(IMAGE_SELECTOR_FRAGMENT, bundle);
                    parentManager.popBackStack();

                }
            }
        });

        //Reused from practical 5
        permissionRequestLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result)
                {
                    //Granted permission
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pickPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check for permission to read from storage
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Launch activity
                    pickPhotoLauncher.launch("image/*");
                } else {
                    //Get permission if not obtained
                    permissionRequestLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        pickPhotoLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(IMAGE_KEY, result);

                //Return Uri to the parent fragment and pop backstack
                parentManager.setFragmentResult(IMAGE_SELECTOR_FRAGMENT, bundle);
                parentManager.popBackStack();
            }
        });

        searchOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start new fragment to search online
                OnlineImageList listFrag;
                String searchTerm;
                Editable text = searchTermBox.getText();

                //Check if search term is not null

                if (text != null)
                {
                    searchTerm = text.toString();
                    if (searchTerm != null)
                    {
                        listFrag = OnlineImageList.newInstance(studentId, searchTerm);
                        parentManager.beginTransaction().replace(R.id.mainFrame, listFrag).commit();
                    }
                }
            }
        });

        parentManager.setFragmentResultListener(OnlineImageList.ONLINE_LIST_FRAGMENT, getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                Bundle bundle = new Bundle();

                bundle.putParcelable(IMAGE_KEY, result.getParcelable(IMAGE_KEY));

                //Return Uri to the parent fragment and pop backstack
                parentManager.setFragmentResult(IMAGE_SELECTOR_FRAGMENT, bundle);
                parentManager.popBackStack();
            }
        });


        return view;
    }
}