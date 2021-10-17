package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.QuestionServer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TIME_LEFT = "timeLeft";
    private static final String TOTAL_TIME = "totalTime";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView timeRemaining;
    private TextView score;
    private TextView question;
    private TextView totalTime;
    private QuestionServer server;
    private String currentQuestion;
    private ScheduledThreadPoolExecutor executor;
    private int totalTimePassed;
    private int timeLeft;
    private AnswerFragment answerFragment;


    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
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
        View view = inflater.inflate(R.layout.fragment_question_fragment, container, false);

        //Get references to view objects
        timeRemaining = view.findViewById(R.id.timeLeft);
        score = view.findViewById(R.id.testScore);
        question = view.findViewById(R.id.question);
        totalTime = view.findViewById(R.id.timerView);

        server = QuestionServer.getInstance();

        //Restore time if needed
        if (savedInstanceState != null)
        {
            currentQuestion = server.getCurrentQuestion();
            question.setText(currentQuestion);
            totalTimePassed = savedInstanceState.getInt(TOTAL_TIME);
            timeLeft = savedInstanceState.getInt(TIME_LEFT);
        }
        else
        {
            //First time created. Do initial setup
            server.nextQuestion();
            currentQuestion = server.getCurrentQuestion();
            timeLeft = server.getSolvingTime();
            totalTimePassed = 0;
        }

        //TODO: Update TextViews with the data obtained

        //Start the timer for the test
        executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                //Decrement every second
                timeLeft--;

                //Increment every second
                totalTimePassed++;

                //Update views
                timeRemaining.setText(String.valueOf(timeLeft));
                totalTime.setText(String.valueOf(totalTimePassed));

                //Check if time remaining is 0 or less. If zero, move to next question
                if (timeLeft <= 0)
                {
                    server.nextQuestion();
                    currentQuestion = server.getCurrentQuestion();

                    //Update answer fragment to reflect new options
                    answerFragment.newQuestionUpdate();

                }

            }
        }, 1, TimeUnit.SECONDS);


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        //Save the time passed and time remaining
        outState.putInt(TIME_LEFT, timeLeft);
        outState.putInt(TOTAL_TIME, totalTimePassed);

    }

    public void update()
    {
        //Update score and displayed question
        question.setText(server.getCurrentQuestion());
        score.setText(String.valueOf(server.getScore()));
    }

    public void setAnswerFragment(AnswerFragment answerFragment)
    {
        this.answerFragment = answerFragment;
    }
}