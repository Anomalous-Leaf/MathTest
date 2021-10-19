package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import curtin.edu.mathtest.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestResultsListFragment extends Fragment {

    private static final String STUDENT_ID = "studentId";

    private int studentId;

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



        return view;
    }
}