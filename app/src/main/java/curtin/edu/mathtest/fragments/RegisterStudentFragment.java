package curtin.edu.mathtest.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.Student;
import curtin.edu.mathtest.model.TestDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterStudentFragment extends Fragment {

    public static final String REGISTRATION_FRAGMENT = "registrationFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAILS = "email";
    private static final String PHONES = "phone";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button fromContacts;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText phoneField;
    private EditText emailField;
    private Button addStudentButton;
    private Button addEmailButton;
    private Button addPhoneButton;
    private TestDatabase db;
    private FragmentManager parentManager;
    private ActivityResultLauncher<Void> contactLauncher;

    private String firstName;
    private String lastName;
    private String contactEmail;
    private String contactPhone;
    private ArrayList<String> emails;
    private ArrayList<String> phones;
    private String photoUri;

    public RegisterStudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterStudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterStudentFragment newInstance(String param1, String param2) {
        RegisterStudentFragment fragment = new RegisterStudentFragment();
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
        View view = inflater.inflate(R.layout.fragment_register_student, container, false);

        db = TestDatabase.getInstance();
        parentManager = getParentFragmentManager();

        fromContacts = view.findViewById(R.id.fromContactsButton);
        firstNameField = view.findViewById(R.id.firstNameField);
        lastNameField = view.findViewById(R.id.lastNameField);
        phoneField = view.findViewById(R.id.phoneNumField);
        emailField = view.findViewById(R.id.emailField);
        addStudentButton = view.findViewById(R.id.addStudentButton);
        addPhoneButton = view.findViewById(R.id.addNumberButton);
        addEmailButton = view.findViewById(R.id.addEmailButton);

        //Set up pick contact button
        //Modified from practical 5
        contactLauncher = registerForActivityResult(new ActivityResultContracts.PickContact(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //Specify the contact to access
                String[] queryFields = {ContactsContract.Contacts._ID};
                Cursor cursor;
                int contactId;


                //Check if a result was returned
                if (result != null)
                {
                    //Get cursor to the contact
                    cursor = getActivity().getContentResolver().query(result, queryFields, null, null, null);

                    try
                    {
                        if (cursor.getCount() > 0)
                        {
                            cursor.moveToFirst();

                            contactId = cursor.getInt(0);

                            //Get cursor to get first and last name
                            cursor = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                                    ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME}, ContactsContract.CommonDataKinds.StructuredName._ID + "= ?", new String[]{String.valueOf(contactId)}, null, null);

                            if (cursor != null && cursor.moveToFirst())
                            {
                                //Has data. Get first and last name
                                firstName = cursor.getString(0);
                                lastName = cursor.getString(1);
                            }

                            //Get another cursor to get email of the user id
                            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[]{
                                    ContactsContract.CommonDataKinds.Email.ADDRESS}, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?", new String[]{String.valueOf(contactId)}, null, null);

                            if (cursor != null && cursor.moveToFirst())
                            {
                                //Get the email
                                contactEmail = cursor.getString(0);
                            }
                            else
                            {
                                //No email found
                                Toast.makeText(getActivity(), "No Email found", Toast.LENGTH_SHORT);
                            }

                            //Get another cursor for retrieving phone number
                            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?", new String[]{String.valueOf(contactId)}, null, null);

                            if (cursor.moveToFirst())
                            {
                                contactPhone = cursor.getString(0);
                            }
                            else
                            {
                                //No phone found
                                Toast.makeText(getActivity(), "No phone found", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }

            }
        });

        //Set up add phone button
        addPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add phone to list
                phones.add(phoneField.getText().toString());
            }
        });

        addEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add email to list
                emails.add(emailField.getText().toString());
            }
        });

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create student and add to database
                Student newStudent;
                newStudent = new Student(firstName, lastName, photoUri);
                db.addStudent(newStudent);
                Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_SHORT);

                //Pop back stack to return to main menu
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FIRST_NAME, firstName);
        outState.putString(LAST_NAME, lastName);
        outState.putStringArrayList(EMAILS, emails);
        outState.putStringArrayList(PHONES, phones);
    }
}