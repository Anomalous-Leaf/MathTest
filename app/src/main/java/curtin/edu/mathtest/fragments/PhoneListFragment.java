package curtin.edu.mathtest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
 * Use the {@link PhoneListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneListFragment extends Fragment {


    private static final String STUDENT_ID = "studentId";

    private int studentId;
    private TestDatabase db;
    private List<String> phoneList;
    private EditText phone1;
    private EditText phone2;
    private EditText phone3;
    private EditText phone4;
    private EditText phone5;
    private EditText phone6;
    private EditText phone7;
    private EditText phone8;
    private EditText phone9;
    private EditText phone10;
    private List<EditText> phoneFields;
    private Button saveButton;
    private Student student;
    private List<String> newPhoneList;
    private List<String> currentPhoneList;

    public PhoneListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inId Parameter 1.
     * @return A new instance of fragment PhoneListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneListFragment newInstance(int inId) {
        PhoneListFragment fragment = new PhoneListFragment();
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
        View view = inflater.inflate(R.layout.fragment_phone_list, container, false);

        phoneFields = new ArrayList<>();
        newPhoneList = new ArrayList<>();
        currentPhoneList = new ArrayList<>();
        db = TestDatabase.getInstance();
        student = db.getStudent(studentId);

        phone1 = view.findViewById(R.id.phone1);
        phone2 = view.findViewById(R.id.phone2);
        phone3 = view.findViewById(R.id.phone3);
        phone4 = view.findViewById(R.id.phone4);
        phone5 = view.findViewById(R.id.phone5);
        phone6 = view.findViewById(R.id.phone6);
        phone7 = view.findViewById(R.id.phone7);
        phone8 = view.findViewById(R.id.phone8);
        phone9 = view.findViewById(R.id.phone9);
        phone10 = view.findViewById(R.id.phone10);
        saveButton = view.findViewById(R.id.savePhoneButton);

        phoneFields.add(phone1);
        phoneFields.add(phone2);
        phoneFields.add(phone3);
        phoneFields.add(phone4);
        phoneFields.add(phone5);
        phoneFields.add(phone6);
        phoneFields.add(phone7);
        phoneFields.add(phone8);
        phoneFields.add(phone9);
        phoneFields.add(phone10);

        for (int ii = 0; ii < phoneFields.size() - 1; ii++)
        {
            phoneFields.get(ii).setText(currentPhoneList.get(ii));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Save all the emails in EditText objects to student and save to database
                for (EditText field : phoneFields)
                {
                    //If not blank field, add
                    if (!field.getText().toString().equals(""))
                    {
                        newPhoneList.add(field.getText().toString());
                    }
                }

                student.setEmailList(newPhoneList);

                //Update student in database
                db.updateStudent(student);

            }
        });

        return view;
    }
}