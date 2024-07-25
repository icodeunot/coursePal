package android.scheduler.studyBuddy.DB;

import android.content.Context;
import android.scheduler.studyBuddy.DAO.AssessmentDAO;
import android.scheduler.studyBuddy.DAO.CourseDAO;
import android.scheduler.studyBuddy.DAO.InstructorDAO;
import android.scheduler.studyBuddy.DAO.StudentDAO;
import android.scheduler.studyBuddy.DAO.TermDAO;
import android.scheduler.studyBuddy.Entity.Assessment;
import android.scheduler.studyBuddy.Entity.Course;
import android.scheduler.studyBuddy.Entity.Instructor;
//import android.scheduler.johnsalazar.Entity.Login;
import android.scheduler.studyBuddy.Entity.Student;
import android.scheduler.studyBuddy.Entity.Term;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Assessment.class, Course.class, Instructor.class, Student.class, Term.class},
          version = 5, exportSchema = false)
public abstract class TermDBBuilder extends RoomDatabase {
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract InstructorDAO instructorDAO();
//    public abstract LoginDAO loginDAO();
    public abstract StudentDAO studentDAO();
    public abstract TermDAO termDAO();

    public static volatile TermDBBuilder INSTANCE;

    static TermDBBuilder getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TermDBBuilder.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TermDBBuilder.class,
                                    "MyTermDatabase.db")
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries(); // For Synchronous build
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
