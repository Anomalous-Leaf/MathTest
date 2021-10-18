package curtin.edu.mathtest.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static curtin.edu.mathtest.model.TestDatabaseSchema.*;

public class TestDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "MathTest.db";

    public TestDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + StudentTable.NAME + "(" +
                StudentTable.Cols.ID + " INTEGER, " +
                StudentTable.Cols.FIRST_NAME + " TEXT, " +
                StudentTable.Cols.LAST_NAME + " TEXT, " +
                StudentTable.Cols.PHOTO_URI + " TEXT, " +
                "PRIMARY KEY(" + StudentTable.Cols.ID + "))");

        //Create phone number table
        sqLiteDatabase.execSQL("CREATE TABLE " + PhoneTable.NAME + "(" +
                PhoneTable.Cols.ID + " INTEGER, " +
                PhoneTable.Cols.PHONE + " TEXT, " +
                "FOREIGN KEY(" + PhoneTable.Cols.ID + ") REFERENCES " + StudentTable.NAME + "(" + StudentTable.Cols.ID + ") " +
                "ON DELETE CASCADE ON UPDATE CASCADE, " +
                "PRIMARY KEY(" + PhoneTable.Cols.ID + ", " + PhoneTable.Cols.PHONE + "))");

        //Create email table
        sqLiteDatabase.execSQL("CREATE TABLE " + EmailTable.NAME + "(" +
                EmailTable.Cols.ID + "INTEGER, " +
                EmailTable.Cols.EMAIL + "TEXT, " +
                "FOREIGN KEY(" + EmailTable.Cols.ID + ") REFERENCES " + StudentTable.NAME + "(" + StudentTable.Cols.ID + ") " +
                "ON DELETE CASCADE ON UPDATE CASCADE, " +
                "PRIMARY KEY(" + EmailTable.Cols.ID + ", " + EmailTable.Cols.EMAIL + "))");

        //Create TestResults Table
        sqLiteDatabase.execSQL("CREATE TABLE " + TestResults.NAME + "(" +
                TestResults.Cols.ID + "INTEGER, " +
                TestResults.Cols.START_TIME + "TEXT, " +
                TestResults.Cols.END_TIME + "TEXT, " +
                TestResults.Cols.SCORE + "INTEGER, " +
                TestResults.Cols.NUM_QUESTIONS + "INTEGER, " +
                "FOREIGN KEY(" + TestResults.Cols.ID + ") REFERENCES " + StudentTable.NAME + "(" + StudentTable.Cols.ID + ") " +
                "ON DELETE CASCADE ON UPDATE CASCADE, " +
                "PRIMARY KEY(" + TestResults.Cols.ID + ", " + TestResults.Cols.START_TIME + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
