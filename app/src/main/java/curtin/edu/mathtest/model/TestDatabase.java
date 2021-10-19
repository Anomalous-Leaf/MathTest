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

        TestCursor cursor = new TestCursor(db.query(TestResults.NAME,
                null,
                null,
                null,
                null,
                null,
                null));

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                results.add(cursor.getTestResult());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }


        return results;
    }

    public List<TestResult> getAllResults()
    {
        List<TestResult> results = new ArrayList<>();

        TestCursor cursor = new TestCursor(db.query(TestResults.NAME,
                null,
                null,
                null,
                null,
                null,
                null));

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                results.add(cursor.getTestResult());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }


        return results;
    }

    public TestResult getResult(int id, String startTime)
    {
        TestResult result;
        String selection = TestResults.Cols.ID + " = ? AND " + TestResults.Cols.START_TIME + " = ?";
        String[] args = {String.valueOf(id), startTime};

        TestCursor cursor = new TestCursor(db.query(TestResults.NAME,
                null,
                selection,
                args,
                null,
                null,
                null));

        try
        {
            cursor.moveToFirst();
            result = cursor.getTestResult();
        }
        finally
        {
            cursor.close();
        }


        return result;
    }

    public Student getStudent(int id)
    {
        Student student;
        String where = StudentTable.Cols.ID + " = ?";
        String[] args = {String.valueOf(id)};

        //Get student that matches id
        TestCursor cursor = new TestCursor(db.query(StudentTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null));

        try
        {
            cursor.moveToFirst();
            student = cursor.getStudent();
        }
        finally {
            cursor.close();
        }

        return student;
    }

    public List<Student> getStudents()
    {
        List<Student> students = new ArrayList<>();

        TestCursor cursor = new TestCursor(db.query(StudentTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null));

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                students.add(cursor.getStudent());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return students;
    }

    public void updateStudent(Student student)
    {
        String where = StudentTable.Cols.ID + " = ?";
        String[] args = {String.valueOf(student.getId())};
        ContentValues emailCv = new ContentValues();
        ContentValues phoneCv = new ContentValues();
        ContentValues studentCv = new ContentValues();

        //Put student data into cv
        studentCv.put(StudentTable.Cols.ID, student.getId());
        studentCv.put(StudentTable.Cols.FIRST_NAME, student.getFirstName());
        studentCv.put(StudentTable.Cols.LAST_NAME, student.getLastName());
        studentCv.put(StudentTable.Cols.PHOTO_URI, student.getPhotoUri());

        //Update student
        db.update(StudentTable.NAME, studentCv, where, args);

        //Remove previous emails of student first
        db.delete(EmailTable.NAME, where, args);

        //Loop to insert all emails. Each loop is a row
        for (String email : student.getEmailList())
        {
            emailCv = new ContentValues();
            emailCv.put(EmailTable.Cols.EMAIL, email);
            emailCv.put(EmailTable.Cols.ID, student.getId());
            db.insert(EmailTable.NAME, null, emailCv);
        }

        //Remove previous phone number of student first
        db.delete(PhoneTable.NAME, where, args);

        //Loop to insert all phone numbers. Each loop is one row
        for (String number : student.getPhoneList())
        {
            phoneCv = new ContentValues();
            phoneCv.put(PhoneTable.Cols.PHONE, number);
            phoneCv.put(PhoneTable.Cols.ID, student.getId());
            db.insert(PhoneTable.NAME, null, phoneCv);
        }



    }

    private class TestCursor extends CursorWrapper
    {
        public TestCursor(Cursor cursor)
        {
            super(cursor);
        }

        public Student getStudent()
        {
            Student student;

            int id = getInt(getColumnIndex(StudentTable.Cols.ID));
            String firstName = getString(getColumnIndex(StudentTable.Cols.FIRST_NAME));
            String lastName = getString(getColumnIndex(StudentTable.Cols.LAST_NAME));
            String photoUri = getString(getColumnIndex(StudentTable.Cols.PHOTO_URI));
            List<String> phones = getPhoneList(id);
            List<String> emails = getEmailList(id);

            student = new Student(id, firstName, lastName, photoUri);
            student.setPhoneList(phones);
            student.setEmailList(emails);

            return student;
        }

        public TestResult getTestResult()
        {
            TestResult result;
            int id = getInt(getColumnIndex(TestResults.Cols.ID));
            String startTime = getString(getColumnIndex(TestResults.Cols.START_TIME));
            String endTime = getString(getColumnIndex(TestResults.Cols.END_TIME));
            int questions = getInt(getColumnIndex(TestResults.Cols.NUM_QUESTIONS));
            int score = getInt(getColumnIndex(TestResults.Cols.SCORE));

            result = new TestResult(id, startTime, endTime, questions, score);


            return result;
        }

        public List<String> getPhoneList(int id)
        {
            List<String> phoneList = new ArrayList<>();
            String[] args = {String.valueOf(id)};
            String where = PhoneTable.Cols.ID + " = ?";

            TestCursor cursor = new TestCursor(db.query(PhoneTable.NAME,
                    null,
                    where,
                    args,
                    null,
                    null,
                    null));

            try
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    phoneList.add(cursor.getPhone());
                    cursor.moveToNext();
                }
            }
            finally
            {
                cursor.close();
            }

            return phoneList;
        }

        private String getPhone()
        {
            return getString(getColumnIndex(PhoneTable.Cols.PHONE));
        }

        public List<String> getEmailList(int id)
        {
            List<String> emailList = new ArrayList<>();

            String[] args = {String.valueOf(id)};
            String where = EmailTable.Cols.ID + " = ?";

            TestCursor cursor = new TestCursor(db.query(EmailTable.NAME,
                    null,
                    where,
                    args,
                    null,
                    null,
                    null));

            try
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    emailList.add(cursor.getEmail());
                    cursor.moveToNext();
                }
            }
            finally
            {
                cursor.close();
            }

            return emailList;
        }

        private String getEmail()
        {
            return getString(getColumnIndex(EmailTable.Cols.EMAIL));
        }
    }


}
