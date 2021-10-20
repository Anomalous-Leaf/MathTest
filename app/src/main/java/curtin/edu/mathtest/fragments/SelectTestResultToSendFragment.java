package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.TestDatabase;
import curtin.edu.mathtest.model.TestResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectTestResultToSendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTestResultToSendFragment extends Fragment {

    private static final String STUDENT_ID = "studentId";
    private static final String START_TIME = "startTime";

    private int studentId;
    private String startTime;

    private TestDatabase db;
    private ResultListAdapter adapter;
    private FragmentManager parentFragmentManager;
    private RecyclerView rv;

    public SelectTestResultToSendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @param inTime Parameter 2.
     * @return A new instance of fragment SelectTestResultToSendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectTestResultToSendFragment newInstance(int inId, String inTime) {
        SelectTestResultToSendFragment fragment = new SelectTestResultToSendFragment();
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

        // Create callback for handling the back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // When pressed, remove this fragment and pop backstack
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().remove(SelectTestResultToSendFragment.this).commit();
                manager.popBackStack();
            }
        };


        //Add the call back
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_results_list, container, false);

        parentFragmentManager = getParentFragmentManager();
        db = TestDatabase.getInstance();

        //Set up the results list
        adapter = new ResultListAdapter(db.getAllResults());
        rv = view.findViewById(R.id.resultList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        return view;
    }

    private class TestResultViewHolder extends RecyclerView.ViewHolder
    {
        private TestResult currResult;
        private TextView firstName;
        private TextView lastName;
        private TextView startTime;
        private TestDatabase db;

        public TestResultViewHolder(View view)
        {
            super(view);

            //Get references to the objects in row
            firstName = view.findViewById(R.id.resultListFirstName);
            lastName = view.findViewById(R.id.resultListLastName);
            startTime = view.findViewById(R.id.resultListStartTime);

            db = TestDatabase.getInstance();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Open fragment to show test result details
                    Fragment selectRecipientFragment = SelectStudentRecipientFragment.newInstance(currResult.getStudentId(), currResult.getStartTime());

                    getParentFragmentManager().beginTransaction().replace(R.id.mainFrame, selectRecipientFragment).commit();


                }
            });
        }

        public void bind(TestResult inResult)
        {
            currResult = inResult;
            firstName.setText(db.getStudent(currResult.getStudentId()).getFirstName());
            lastName.setText(db.getStudent(currResult.getStudentId()).getLastName());
            startTime.setText(currResult.getStartTime());
        }
    }

    private class ResultListAdapter extends RecyclerView.Adapter<TestResultViewHolder>
    {
        private List<TestResult> testResultList;

        public ResultListAdapter(List<TestResult> inResults)
        {
            testResultList = inResults;
        }

        @Override
        public int getItemCount()
        {
            return testResultList.size();
        }

        @Override
        public TestResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.test_result_row, parent, false);

            return new TestResultViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TestResultViewHolder cell, int index)
        {
            cell.bind(testResultList.get(index));
        }
    }
}