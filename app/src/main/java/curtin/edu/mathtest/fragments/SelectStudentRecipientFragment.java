package curtin.edu.mathtest.fragments;

import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.Student;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectStudentRecipientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectStudentRecipientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentManager parentFragmentManager;

    public SelectStudentRecipientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectStudentRecipientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectStudentRecipientFragment newInstance(String param1, String param2) {
        SelectStudentRecipientFragment fragment = new SelectStudentRecipientFragment();
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
        View view = inflater.inflate(R.layout.fragment_select_student_recipient, container, false);

        parentFragmentManager = getParentFragmentManager();


        //Will also display a list of students. But each row of list will then open a email app to send
        // To student selected in previous fragment


        return view;
    }

    //ViewHolder and Adapter classes
    private class StudentViewHolder extends RecyclerView.ViewHolder
    {
        private Student currStudent;
        private TextView firstName;
        private TextView lastName;
        private ImageView avatarImage;

        public StudentViewHolder(View view)
        {
            super(view);

            //Get references to the objects in row
            firstName = view.findViewById(R.id.listFirstName);
            lastName = view.findViewById(R.id.listLastName);
            avatarImage = view.findViewById(R.id.listStudentAvatar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Create fragment for selecting test result to email to this student
                    Fragment testFragment = TestResultsListFragment.newInstance(currStudent.getId());

                    //Replace fragment
                    parentFragmentManager.beginTransaction().replace(R.id.mainFrame, testFragment).commit();

                }
            });
        }

        public void bind(Student inStudent)
        {
            currStudent = inStudent;
            firstName.setText(currStudent.getFirstName());
            lastName.setText(currStudent.getLastName());

            try
            {
                //Load image from Uri
                avatarImage.setImageBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), Uri.parse(currStudent.getPhotoUri()))));
            }
            catch(IOException ioE)
            {
                Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class StudentListAdapter extends RecyclerView.Adapter<StudentViewHolder>
    {
        private List<Student> students;

        public StudentListAdapter(List<Student> inStudents)
        {
            students = inStudents;
        }

        @Override
        public int getItemCount()
        {
            return students.size();
        }

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.student_row_list, parent, false);

            return new StudentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StudentViewHolder cell, int index)
        {
            cell.bind(students.get(index));
        }
    }
}