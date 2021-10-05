package curtin.edu.mathtest.model;

import java.util.List;

public class Question
{
    private String question;
    private int result;
    private List<Integer> options;
    private int timeToSolve;

    public Question(String question, int result, List<Integer> options, int timeToSolve)
    {
        this.question = question;
        this.result = result;
        this.options = options;
        this.timeToSolve = timeToSolve;
    }

    public String getQuestion() {
        return question;
    }

    public int getResult() {
        return result;
    }

    public int getTimeToSolve() {
        return timeToSolve;
    }

    public List<Integer> getOptions() {
        return options;
    }
}
