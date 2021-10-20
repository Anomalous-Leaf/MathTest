package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import curtin.edu.mathtest.R;
import curtin.edu.mathtest.model.Student;
import curtin.edu.mathtest.model.TestDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmailListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailListFragment extends Fragment {

    private static final String STUDENT_ID = "studentId";

    private int studentId;
    private TestDatabase db;
    private List<String> emailList;
    private EditText email1;
    private EditText email2;
    private EditText email3;
    private EditText email4;
    private EditText email5;
    private EditText email6;
    private EditText email7;
    private EditText email8;
    private EditText email9;
    private EditText email10;
    private List<EditText> emailFields;
    private Button saveButton;
    private Student student;
    private List<String> newEmailList;
    private List<String> currentEmailList;

    public EmailListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @return A new instance of fragment EmailListFragment.
     */

    public static EmailListFragment newInstance(int inId) {
        EmailListFragment fragment = new EmailListFragment();
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
        View view = inflater.inflate(R.layout.fragment_email_list, container, false);

        emailFields = new ArrayList<>();
        newEmailList = new ArrayList<>();
        db = TestDatabase.getInstance();
        student = db.getStudent(studentId);

        currentEmailList = student.getEmailList();

        email1 = view.findViewById(R.id.emailField1);
        email2 = view.findViewById(R.id.emailField2);
        email3 = view.findViewById(R.id.emailField3);
        email4 = view.findViewById(R.id.emailField4);
        email5 = view.findViewById(R.id.emailField5);
        email6 = view.findViewById(R.id.emailField6);
        email7 = view.findViewById(R.id.emailField7);
        email8 = view.findViewById(R.id.emailField8);
        email9 = view.findViewById(R.id.emailField9);
        email10 = view.findViewById(R.id.emailField10);
        saveButton = view.findViewById(R.id.saveEmailsButton);

        //Add EditText objs to list
        emailFields.add(email1);
        emailFields.add(email2);
        emailFields.add(email3);
        emailFields.add(email4);
        emailFields.add(email5);
        emailFields.add(email6);
        emailFields.add(email7);
        emailFields.add(email8);
        emailFields.add(email9);
        emailFields.add(email10);

        //Display emails in EditText objs
        for (int ii = 0; ii < emailFields.size() && ii < currentEmailList.size(); ii++)
        {
            emailFields.get(ii).setText(currentEmailList.get(ii));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Save all the emails in EditText objects to student and save to database
                for (EditText field : emailFields)
                {
                    //If not blank field, add
                    if (!field.getText().toString().equals(""))
                    {
                        newEmailList.add(field.getText().toString());
                    }
                }

                student.setEmailList(newEmailList);

                //Update student in database
                db.updateStudent(student);

                getParentFragmentManager().popBackStack();

            }
        });


        return view;
    }
}