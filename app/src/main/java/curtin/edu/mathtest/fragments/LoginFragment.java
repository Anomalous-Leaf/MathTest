package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import curtin.edu.mathtest.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String INITIAL_SCREEN = "initialScreen";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button teacherButton;
    private Button studentButton;
    private FragmentManager parentManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        studentButton = view.findViewById(R.id.studentButton);
        teacherButton = view.findViewById(R.id.teacherButton);
        parentManager = getParentFragmentManager();

        //Set listeners for buttons
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open student selector fragment
                SelectStudentFragment selectorFragment = new SelectStudentFragment();

                parentManager.beginTransaction().addToBackStack(INITIAL_SCREEN).replace(R.id.mainFrame, selectorFragment).commit();

            }
        });

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open teacher options fragment
                TeacherFragment teacherFragment = new TeacherFragment();

                parentManager.beginTransaction().addToBackStack(INITIAL_SCREEN).replace(R.id.mainFrame, teacherFragment).commit();

            }
        });


        return view;
    }
}