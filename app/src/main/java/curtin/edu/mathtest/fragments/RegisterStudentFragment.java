package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import curtin.edu.mathtest.R;
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

        return view;
    }
}