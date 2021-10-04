package curtin.edu.mathtest.model;

public class TestResult
{
    private int studentId;
    private int questions;
    private String startTime;
    private String endTime;
    private int score;

    public TestResult(int studentId, String startTime)
    {
        this.studentId = studentId;
        this.startTime = startTime;

        questions = 0;
        score = 0;
        //Default end time is start time
        endTime = startTime;
    }

    public int getQuestions() {
        return questions;
    }

    public int getScore() {
        return score;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public void setScore(int score)
    {
        //Each modification of the score assumes
        // a question has been answered
        questions++;
        this.score = score;
    }

}
