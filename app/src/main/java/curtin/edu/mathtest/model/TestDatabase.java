package curtin.edu.mathtest.model;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TestDatabase
{
    private SQLiteDatabase db;

    private TestDatabase()
    {

    }

    public void load(Context context)
    {
        this.db = new TestDbHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public void addResult()
    {

    }

    public void addStudent(Student student)
    {

    }

    public List<TestResult> getResults(int id)
    {
        List<TestResult> results = new ArrayList<>();

        return results;

    }

    public List<Student> getStudents()
    {
        return null;
    }

    private class TestCursor extends CursorWrapper
    {
        public TestCursor(Cursor cursor)
        {
            super(cursor);
        }

        public Student getStudent()
        {
            return null;
        }

        public TestResult getTestResult()
        {
            return null;
        }

        public List<String> getPhoneList()
        {
            return null;
        }

        public List<String> getEmailList()
        {
            return null;
        }
    }


}
