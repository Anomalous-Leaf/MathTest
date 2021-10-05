package curtin.edu.mathtest.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import static curtin.edu.mathtest.model.TestDatabaseSchema.*;

import java.util.ArrayList;
import java.util.List;

public class TestDatabase
{
    private SQLiteDatabase db;
    private static TestDatabase instance;

    private TestDatabase()
    {

    }

    public static TestDatabase getInstance()
    {
        if (instance == null)
        {
            instance = new TestDatabase();
        }

        return instance;
    }

    public void load(Context context)
    {
        this.db = new TestDbHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public void addResult(TestResult result)
    {
        ContentValues cv = new ContentValues();

        //Insert values into ContentValues
        cv.put(TestResults.Cols.ID, result.getStudentId());
        cv.put(TestResults.Cols.START_TIME, result.getStartTime());
        cv.put(TestResults.Cols.END_TIME, result.getEndTime());
        cv.put(TestResults.Cols.NUM_QUESTIONS, result.getQuestions());
        cv.put(TestResults.Cols.SCORE, result.getScore());

        db.insert(TestResults.NAME, null, cv);
    }

    public void addStudent(Student student)
    {
        ContentValues studentCv = new ContentValues();
        ContentValues phoneCv;
        ContentValues emailCv = new ContentValues();

        //Put student data into cv
        studentCv.put(StudentTable.Cols.ID, student.getId());
        studentCv.put(StudentTable.Cols.FIRST_NAME, student.getFirstName());
        studentCv.put(StudentTable.Cols.LAST_NAME, student.getLastName());
        studentCv.put(StudentTable.Cols.PHOTO_URI, student.getPhotoUri());

        //Add student to database
        db.insert(StudentTable.NAME, null, studentCv);

        //Loop to insert all phone numbers. Each loop is one row
        for (String number : student.getPhoneList())
        {
            phoneCv = new ContentValues();
            phoneCv.put(PhoneTable.Cols.PHONE, number);
            phoneCv.put(PhoneTable.Cols.ID, student.getId());
            db.insert(PhoneTable.NAME, null, phoneCv);
        }

        //Loop to insert all emails. Each loop is a row
        for (String email : student.getEmailList())
        {
            emailCv = new ContentValues();
            emailCv.put(EmailTable.Cols.EMAIL, email);
            emailCv.put(EmailTable.Cols.ID, student.getId());
            db.insert(EmailTable.NAME, null, emailCv);
        }
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
