package curtin.edu.mathtest.model;

import java.util.ArrayList;
import java.util.List;

public class Student
{
    private int id;
    private String firstName;
    private String lastName;
    private String photoUri;
    private List<String> phoneList;
    private List<String> emailList;

    //Used as a generator of student ids
    private static int nextId = 1;

    public Student(int id, String firstName, String lastName, String photoUri)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUri = photoUri;

        phoneList = new ArrayList<>();
        emailList = new ArrayList<>();

        if (id > nextId)
        {
            //In case of recreating students from database
            nextId = id + 1;
        }
    }

    public Student(String firstName, String lastName, String photoUri)
    {
        this.id = nextId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUri = photoUri;

        phoneList = new ArrayList<>();
        emailList = new ArrayList<>();
    }

    public static int getNextId()
    {
        return nextId;
    }

    public static void updateNextId(TestDatabase db)
    {
        List<Student> students = db.getStudents();

        //Loop to find the highest id in database
        for (Student curr : students)
        {
            if (curr.getId() > nextId)
            {
                nextId = curr.getId() + 1;
            }
        }
    }

    public int getId() {
        return id;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public List<String> getPhoneList() {
        return phoneList;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }


    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }


}
