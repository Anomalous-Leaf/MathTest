package curtin.edu.mathtest.fragments;

import android.os.Bundle;

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
 * Use the {@link TestResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestResultsListFragment extends Fragment {

    private static final String STUDENT_ID = "studentId";

    private int studentId;
    private TestDatabase db;
    private ResultListAdapter adapter;
    private FragmentManager parentFragmentManager;
    private RecyclerView rv;


    public TestResultsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @return A new instance of fragment TestResultsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestResultsListFragment newInstance(int inId) {
        TestResultsListFragment fragment = new TestResultsListFragment();
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
        View view = inflater.inflate(R.layout.fragment_test_results_list, container, false);


        //Is just a list that shows all test results. On click, it will open the test result fragment
        //For that result
        parentFragmentManager = getParentFragmentManager();
        db = TestDatabase.getInstance();

        //Set up the results list
        adapter = new ResultListAdapter(db.getAllResults());
        rv = view.findViewById(R.id.resultList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);



        return view;
    }

    //ViewHolder and Adapter classes
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
                    Fragment detailsFragment = TestResultsDetailsFragment.newInstance(currResult.getStudentId(), currResult.getStartTime());

                    getParentFragmentManager().beginTransaction().replace(R.id.mainFrame, detailsFragment).commit();


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