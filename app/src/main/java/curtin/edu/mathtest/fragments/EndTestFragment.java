package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.QuestionServer;
import curtin.edu.mathtest.model.Student;
import curtin.edu.mathtest.model.TestDatabase;
import curtin.edu.mathtest.model.TestResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndTestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STUDENT_ID = "studentId";
    private static final String START_TIME = "startTime";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView studentName;
    private TextView numQuestions;
    private TextView startTime;
    private TextView endTime;
    private TextView score;
    private Button mainMenuButton;
    private TestResult result;
    private TestDatabase db;

    private String startTimeString;
    private int studentId;

    public EndTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EndTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EndTestFragment newInstance(String param1, String param2) {
        EndTestFragment fragment = new EndTestFragment();
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
        View view = inflater.inflate(R.layout.fragment_end_test, container, false);
        String fullName;
        Student student;

        studentName = view.findViewById(R.id.studentName);
        numQuestions = view.findViewById(R.id.questionsAnswered);
        startTime = view.findViewById(R.id.startTime);
        endTime = view.findViewById(R.id.endTime);
        score = view.findViewById(R.id.score);
        mainMenuButton = view.findViewById(R.id.toMainMenuButton);

        db = TestDatabase.getInstance();


        if (savedInstanceState != null)
        {
            studentId = savedInstanceState.getInt(STUDENT_ID);
            startTimeString = savedInstanceState.getString(START_TIME);

            //Retrieve result from database
            result = db.getResult(studentId, startTimeString);

        }
        else
        {
            //Finish the test and get result
            result = QuestionServer.getInstance().finishTest();
            studentId = result.getStudentId();
            startTimeString = result.getStartTime();
        }

        //Set up TextViews to display results
        student = db.getStudent(studentId);
        fullName = student.getFirstName() + " " + student.getLastName();

        studentName.setText(fullName);
        numQuestions.setText(String.valueOf(result.getQuestions()));
        startTime.setText(startTimeString);
        endTime.setText(result.getEndTime());
        score.setText(String.valueOf(result.getScore()));


        //Set up button to return to main menu
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace fragment with login fragment
                getParentFragmentManager().beginTransaction().replace(R.id.mainFrame, new LoginFragment()).commit();
            }
        });



        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STUDENT_ID, studentId);
        outState.putString(START_TIME, startTimeString);

    }
}