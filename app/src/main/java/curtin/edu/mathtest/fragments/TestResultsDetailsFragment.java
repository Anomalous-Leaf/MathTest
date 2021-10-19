package curtin.edu.mathtest.fragments;

import android.os.Bundle;

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
 * Use the {@link TestResultsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestResultsDetailsFragment extends Fragment {
    private static final String STUDENT_ID = "studentId";
    private static final String START_TIME = "startTime";

    // TODO: Rename and change types of parameters
    private int studentId;
    private String startTime;

    private TextView studentName;
    private TextView numQuestions;
    private TextView endTime;
    private TextView score;
    private Button mainMenuButton;
    private TestResult result;
    private TestDatabase db;
    private TextView startTimeView;

    public TestResultsDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @param inTime Parameter 2.
     * @return A new instance of fragment TestResultsDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestResultsDetailsFragment newInstance(int inId, String inTime) {
        TestResultsDetailsFragment fragment = new TestResultsDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(STUDENT_ID, inId);
        args.putString(START_TIME, inTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentId = getArguments().getInt(STUDENT_ID);
            startTime = getArguments().getString(START_TIME);
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
        startTimeView = view.findViewById(R.id.startTime);
        endTime = view.findViewById(R.id.endTime);
        score = view.findViewById(R.id.score);
        mainMenuButton = view.findViewById(R.id.toMainMenuButton);

        db = TestDatabase.getInstance();

        result = db.getResult(studentId, startTime);
        student = db.getStudent(studentId);


        //Set up TextViews to display results
        fullName = student.getFirstName() + " " + student.getLastName();

        studentName.setText(fullName);
        numQuestions.setText(String.valueOf(result.getQuestions()));
        startTimeView.setText(startTime);
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
}