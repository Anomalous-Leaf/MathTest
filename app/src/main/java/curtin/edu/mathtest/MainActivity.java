package curtin.edu.mathtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import curtin.edu.mathtest.fragments.LoginFragment;
import curtin.edu.mathtest.model.Student;
import curtin.edu.mathtest.model.TestDatabase;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;
    private Fragment loginFrag;
    private TestDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();

        //Load database
        db = TestDatabase.getInstance();
        db.load(this);
        Student.updateNextId(db);

        //Create login fragment
        loginFrag = manager.findFragmentById(R.id.mainFrame);

        if (loginFrag == null)
        {
            loginFrag = new LoginFragment();
            //Assign mainframe the fragment
            manager.beginTransaction().add(R.id.mainFrame, loginFrag).commit();
        }

    }
}