package curtin.edu.mathtest.model;

public class TestDatabaseSchema
{
    public static class StudentTable
    {
        public static final String NAME = "Student";
        public static class Cols
        {
            public static final String ID = "id";
            public static final String FIRST_NAME = "firstName";
            public static final String LAST_NAME = "lastName";
            public static final String PHOTO_URI = "photoUri";
        }
    }

    public static class PhoneTable
    {
        public static final String NAME = "PhoneNumbers";
        public static class Cols
        {
            public static final String ID = StudentTable.Cols.ID;
            public static final String PHONE = "phone";
        }
    }

    public static class EmailTable
    {
        public static final String NAME = "Emails";
        public static class Cols
        {
            public static final String ID = StudentTable.Cols.ID;
            public static final String EMAIL = "email";
        }
    }

    public static class TestResults
    {
        public static final String NAME = "TestResults";
        public static final class Cols
        {
            public static final String ID = StudentTable.Cols.ID;
            public static final String START_TIME = "startTime";
            public static final String END_TIME = "endTime";
            public static final String NUM_QUESTIONS = "numQuestions";
            public static final String SCORE = "score";
        }
    }
}
