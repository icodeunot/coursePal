package android.scheduler.johnsalazar.Entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseID;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private String courseStatus;
    private String courseNote;

    private int termID;
    private int instructorID;

    public Course(int courseID, String courseTitle, String courseStart, String courseEnd, String courseStatus, String courseNote, int termID, int instructorID) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.courseNote = courseNote;
        this.termID = termID;
        this.instructorID = instructorID;
    }

    @Ignore
    public Course(String courseTitle, String courseStart, String courseEnd, String courseStatus, String courseNote, int termID, int instructorID) {
        this.courseTitle = courseTitle;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.courseNote = courseNote;
        this.termID = termID;
        this.instructorID = instructorID;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseStart() {
        return courseStart;
    }

    public String getCourseEnd() {
        return courseEnd;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public String getCourseNote() {
        return courseNote;
    }

    public int getTermID() {
        return termID;
    }

    public int getInstructorID() {
        return instructorID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public void setCourseNote(String courseNote) {
        this.courseNote = courseNote;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }
}
