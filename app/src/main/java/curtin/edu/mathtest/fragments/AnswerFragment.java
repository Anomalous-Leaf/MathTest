package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.QuestionServer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PAGE_NUMBER = "pageNumber";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;
    private EditText manualAnswerBox;
    private Button submitButton;
    private Button backButton;
    private Button nextButton;
    private List<Button> buttonList;
    private QuestionFragment questionFragment;
    private int pageNumber;

    private QuestionServer server;


    public AnswerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnswerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnswerFragment newInstance(String param1, String param2) {
        AnswerFragment fragment = new AnswerFragment();
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
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        //Get references to views
        answerButton1 = view.findViewById(R.id.answerButton1);
        answerButton2 = view.findViewById(R.id.answerButton2);
        answerButton3 = view.findViewById(R.id.answerButton3);
        answerButton4 = view.findViewById(R.id.answerButton4);
        manualAnswerBox = view.findViewById(R.id.manualAnswerBox);
        submitButton = view.findViewById(R.id.submitButton);
        backButton = view.findViewById(R.id.backButton);
        nextButton = view.findViewById(R.id.nextButton);

        //Add all buttons to list
        buttonList.add(answerButton1);
        buttonList.add(answerButton2);
        buttonList.add(answerButton3);
        buttonList.add(answerButton4);

        server = QuestionServer.getInstance();

        if (savedInstanceState != null)
        {
            //Restore page number
            pageNumber = savedInstanceState.getInt(PAGE_NUMBER);

        }
        else
        {
            //Set default to 1 page number
            pageNumber = 1;
        }

        if (pageNumber == 1)
        {
            backButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            backButton.setVisibility(View.VISIBLE);
        }

        //If last page
        if (pageNumber * buttonList.size() >= server.currentOptions().size())
        {
            //Hide next button
            nextButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            nextButton.setVisibility(View.VISIBLE);
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_NUMBER, pageNumber);

    }

    private void update()
    {
        int jj = 0;
        List<Integer> options = server.currentOptions();
        Button currentButton;
        Integer currentOption;
        int fromOptionNumber = (pageNumber * buttonList.size()) - buttonList.size();

        //Update answer options
        //Get options and check whether manual input is needed
        if (options.size()> 0)
        {
            //Loops as many times as there are buttons, or until the end of option list, whichever is less
            for (int ii = fromOptionNumber - 1; ii < buttonList.size() && ii < options.size() - 1; ii++)
            {
                //Set the text for the button
                currentButton = buttonList.get(jj);
                currentOption = options.get(ii);
                currentButton.setText(currentOption.toString());

                //Set onclick listeners based on whether option is right answer
                if (currentButton.equals(server.currentAnswer()))
                {
                    //This option is the correct answer
                    currentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            server.correctAnswer(true);

                            //Get next question
                            server.nextQuestion();

                            //Update question fragment
                            questionFragment.update();

                            //Reset page number
                            pageNumber = 1;

                            backButton.setVisibility(View.INVISIBLE);

                            update();
                        }
                    });
                }
                else
                {
                    currentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Not correct answer
                            server.correctAnswer(false);

                            //Get next question
                            server.nextQuestion();

                            //Update question fragment
                            questionFragment.update();

                            //Reset page number
                            pageNumber = 1;

                            backButton.setVisibility(View.INVISIBLE);

                            update();
                        }
                    });
                }
            }
        }
        else
        {
            //Hide all buttons
            for (Button button : buttonList)
            {
                button.setVisibility(View.INVISIBLE);
            }

            //Show manual input box
            manualAnswerBox.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Check if answer is correct
                    int answer;

                    try
                    {
                        answer = Integer.parseInt(manualAnswerBox.getText().toString());

                        if (answer == server.currentAnswer())
                        {
                            //Correct answer
                            server.correctAnswer(true);
                        }
                        else
                        {
                            //Incorrect answer
                            server.correctAnswer(false);
                        }


                        server.nextQuestion();

                        questionFragment.update();

                        //Reset page number
                        pageNumber = 1;

                        update();

                    }
                    catch (NumberFormatException e)
                    {
                        //Invalid number entered
                        Toast.makeText(getActivity(), "Invalid number entered", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void newQuestionUpdate()
    {
        pageNumber = 1;
        update();
    }

    public void setQuestionFragment(QuestionFragment questionFragment)
    {
        this.questionFragment = questionFragment;
    }
}