package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.QuestionServer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STUDENT_ID = "studentId";

    // TODO: Rename and change types of parameters
    private int studentId;

    private FragmentManager parentManager;
    private FragmentManager childManager;
    private QuestionServer server;
    private Button endTestButton;

    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(int inId) {
        TestFragment fragment = new TestFragment();
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
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        Fragment answerFragment;
        Fragment questionFragment;


        parentManager = getParentFragmentManager();
        childManager = getChildFragmentManager();
        endTestButton = view.findViewById(R.id.endTestButton);

        //Create new test
        server = QuestionServer.getInstance();
        server.setURL("192.168.0.58");

        server.newTest(studentId, getActivity());
        server.nextQuestion();

        questionFragment = childManager.findFragmentById(R.id.questionFrame);
        answerFragment =  childManager.findFragmentById(R.id.answerFrame);

        if (answerFragment == null)
        {
            answerFragment =  new AnswerFragment();
            childManager.beginTransaction().add(R.id.answerFrame, answerFragment).commit();
        }

        if (questionFragment == null)
        {
            questionFragment = new QuestionFragment();
            childManager.beginTransaction().add(R.id.questionFrame, questionFragment).commit();
        }

        //Set fragment references for communication
        ((QuestionFragment)questionFragment).setAnswerFragment((AnswerFragment) answerFragment);
        ((AnswerFragment)answerFragment).setQuestionFragment((QuestionFragment) questionFragment);

        //Set listener to end test
        endTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndTestFragment endFragment = new EndTestFragment();

                parentManager.beginTransaction().replace(R.id.mainFrame, endFragment).commit();
            }
        });
        return view;
    }
}