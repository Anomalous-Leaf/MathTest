package curtin.edu.mathtest.model;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import curtin.edu.mathtest.MainActivity;

public class QuestionServer
{
    private Question currentQuestion;
    private static QuestionServer instance;
    private TestResult userResult;
    private Calendar calendar;
    private Context context;
    private URL url;


    private QuestionServer()
    {

    }

    public static QuestionServer getInstance()
    {
        if (instance == null)
        {
            instance = new QuestionServer();
        }

        return instance;
    }

    public void newTest(int id, Context context)
    {
        //Starts a new test for the user
        userResult = new TestResult(id, Calendar.getInstance().getTime().toString());



        //Set context
        this.context = context;
    }

    public TestResult finishTest()
    {
        String endTime = Calendar.getInstance().getTime().toString();

        //Set the ending time
        userResult.setEndTime(endTime);

        //Save results to database
        TestDatabase.getInstance().addResult(userResult);

        //Return the results
        return userResult;
    }

    public int getScore()
    {
        return userResult.getScore();
    }

    public int getSolvingTime()
    {
        return currentQuestion.getTimeToSolve();
    }

    public void setURL(String ipAddress)
    {
        String urlString;

        urlString = "https://" + ipAddress + ":8000/random/question";
        urlString = Uri.parse(urlString).buildUpon()
                .appendQueryParameter("method", "thedata.getit")
                .appendQueryParameter("api_key", "01189998819991197253")
                .appendQueryParameter("format", "json").build().toString();

        try
        {
            url = new URL(urlString);
        }
        catch (MalformedURLException e)
        {
            //Show error message or throw exception
            Toast.makeText(context, "Failed to create URL", Toast.LENGTH_SHORT);
            Log.e("URL", "MalformedURLException in setURL()");
        }
    }

    public void nextQuestion()
    {
        String text;

        //Start AsyncTask to download next JSON from server
        try
        {
            text = new DownloadQuestionTask().execute(url).get();
            new DownloadQuestionTask().onPostExecute(text);
        }
        catch (ExecutionException executionException)
        {
            Log.e("Download", "Execution Exception: " + executionException.getMessage());
        }
        catch (InterruptedException interruptedException)
        {
            Log.e("Download", "Execution Exception: " + interruptedException.getMessage());
        }
    }

    public String getCurrentQuestion()
    {
        //Return the current question
        return currentQuestion.getQuestion();
    }

    public int currentAnswer()
    {
        //Return the current answer to the current question
        return currentQuestion.getResult();
    }

    public void correctAnswer(boolean correct)
    {
        if(correct == true)
        {
            //Add 10 points to result
            userResult.setScore(userResult.getScore() + 10);
        }
        else
        {
            //If wrong, take 5 points
            userResult.setScore(userResult.getScore() - 5);

            //Check if points are negative. If negative, reset to 0
            if (userResult.getScore() < 0)
            {
                userResult.setScore(0);
            }
        }
    }

    public List<Integer> currentOptions()
    {
        //Returns the current list of options for the current question
        return currentQuestion.getOptions();
    }


    private class DownloadQuestionTask extends AsyncTask<URL, Void, String>
    {
        @Override
        protected String doInBackground(URL... urls)
        {
            //Code modified from Practical 7
            HttpURLConnection conn = null;
            InputStream input;
            ByteArrayOutputStream baos;
            byte[] buffer;
            int bytesRead;
            String result = "";
            int nBytes = 0;


            try
            {
                conn = (HttpsURLConnection) urls[0].openConnection();

                //Add certificate
                DownloadUtils.addCertificate(context, (HttpsURLConnection) conn);

                Log.d("Download", "Code: " + conn.getResponseCode());
                //Check status
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    //Throw exception
                    Log.d("Download", "Failed to open connection");
                }
                else
                {
                    input = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    buffer = new byte[1024];
                    bytesRead = input.read(buffer);
                    while(bytesRead > 0)
                    {
                        nBytes = nBytes + bytesRead;
                        baos.write(buffer, 0, bytesRead);
                        bytesRead = input.read(buffer);
                    }
                    baos.close();
                    result = new String(baos.toByteArray());
                }
            }
            catch (IOException ioEx)
            {
                Log.e("Download", "IOException: " + ioEx.getMessage());
            }
            catch (GeneralSecurityException secEx)
            {
                Log.e("Download", "SecurityException: " + secEx.getMessage());
            }
            finally
            {
                if (conn != null)
                {
                    conn.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String questionString)
        {
            super.onPostExecute(questionString);

            JSONObject baseObject;
            JSONArray optionArray;
            String question;
            int result;
            List<Integer> options = new ArrayList<>();
            int timeToSolve;
            Question questionObject;


            //Parse into Question object
            try
            {
                baseObject = new JSONObject(questionString);

                question = baseObject.getString("question");
                result = baseObject.getInt("result");
                timeToSolve = baseObject.getInt("timetosolve");

                optionArray = baseObject.getJSONArray("options");

                //Loop to get all options and add to list
                for (int ii = 0; ii < optionArray.length(); ii++)
                {
                    options.add(new Integer(optionArray.getInt(ii)));
                }

                //Create and set the new Question object
                questionObject = new Question(question, result, options, timeToSolve);

                currentQuestion = questionObject;


            }
            catch (JSONException e)
            {
                Toast.makeText(context, "Failed to parse JSON", Toast.LENGTH_SHORT);
            }

        }
    }
}
