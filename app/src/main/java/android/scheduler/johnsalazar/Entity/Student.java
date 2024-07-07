package android.scheduler.johnsalazar.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey(autoGenerate = true)
    private int studentID;
    private String studentFirst;
    private String studentLast;
    private String studentPhone;
    private String studentEmail;

    public Student(int studentID, String studentFirst, String studentLast, String studentPhone,
                   String studentEmail) {
        this.studentID = studentID;
        this.studentFirst = studentFirst;
        this.studentLast = studentLast;
        this.studentPhone = studentPhone;
        this.studentEmail = studentEmail;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getStudentFirst() {
        return studentFirst;
    }

    public void setStudentFirst(String studentFirst) {
        this.studentFirst = studentFirst;
    }

    public String getStudentLast() {
        return studentLast;
    }

    public void setStudentLast(String studentLast) {
        this.studentLast = studentLast;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
}
