package curtin.edu.mathtest.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.Student;
import curtin.edu.mathtest.model.TestDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentDetailsFragment extends Fragment {

    private static final String STUDENT_ID = "studentId";
    public static final String STUDENT_DETAILS_FRAGMENT = "studentDetailsFragment";

    private int studentId;

    private EditText firstName;
    private EditText lastName;
    private Button viewPhonesButton;
    private Button viewEmailsButton;
    private Button saveButton;
    private ImageView profileImage;
    private TestDatabase db;
    private String uriString;


    public StudentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @return A new instance of fragment StudentDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentDetailsFragment newInstance(int inId) {
        StudentDetailsFragment fragment = new StudentDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_details, container, false);

        Student student;

        //Set up EditTexts and buttons
        firstName = view.findViewById(R.id.studentDetailFirstName);
        lastName = view.findViewById(R.id.studentDetailLastName);
        viewPhonesButton = view.findViewById(R.id.viewPhonesButton);
        viewEmailsButton = view.findViewById(R.id.viewEmailsButton);
        saveButton = view.findViewById(R.id.saveUpdatesButton);
        profileImage = view.findViewById(R.id.studentProfilePicture);

        db = TestDatabase.getInstance();

        student = db.getStudent(studentId);

        //Update EditTexts with student first and last names
        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());

        //Get the Uri of the photo
        uriString = student.getPhotoUri();


        //Set listeners for buttons
        viewPhonesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open fragment to view and edit phones
                PhoneListFragment phoneFragment = PhoneListFragment.newInstance(studentId);

                getParentFragmentManager().beginTransaction().replace(R.id.mainFrame, phoneFragment).commit();

            }
        });

        viewEmailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open fragment to view and edit emails
                EmailListFragment emailListFragment = EmailListFragment.newInstance(studentId);

                getParentFragmentManager().beginTransaction().replace(R.id.mainFrame, emailListFragment).commit();
            }
        });

        profileImage.setImageURI(Uri.parse(student.getPhotoUri()));

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open image options fragment to choose new image
                ImageOptions optionsFragment = ImageOptions.newInstance(studentId);

                getParentFragmentManager().beginTransaction().addToBackStack(STUDENT_DETAILS_FRAGMENT).replace(R.id.mainFrame, optionsFragment).commit();

            }
        });

        getParentFragmentManager().setFragmentResultListener(ImageOptions.IMAGE_SELECTOR_FRAGMENT, getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String newUri;

                newUri = result.getParcelable(ImageOptions.IMAGE_KEY).toString();

                if (newUri != null)
                {
                    //Set the new Uri string
                    uriString = newUri;
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save new details to database
                String newFirstName;
                String newLastName;
                Editable firstNameText = firstName.getText();
                Editable lastNameText = lastName.getText();

                if (firstNameText != null && !firstNameText.toString().equals(""))
                {
                    if (lastNameText != null && !lastNameText.toString().equals(""))
                    {
                        newFirstName = firstNameText.toString();
                        newLastName = lastNameText.toString();

                        //Update and add to database
                        student.setFirstName(newFirstName);
                        student.setLastName(newLastName);
                        student.setPhotoUri(uriString);

                        db.updateStudent(student);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Last name cannot be blank", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "First name cannot be blank", Toast.LENGTH_SHORT).show();
                }



            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Refresh profile image
        profileImage.setImageURI(Uri.parse(uriString));
        profileImage.invalidate();


    }
}