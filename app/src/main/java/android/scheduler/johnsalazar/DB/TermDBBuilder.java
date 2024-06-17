package android.scheduler.johnsalazar.DB;

import android.content.Context;
import android.scheduler.johnsalazar.DAO.AssessmentDAO;
import android.scheduler.johnsalazar.DAO.CourseDAO;
import android.scheduler.johnsalazar.DAO.InstructorDAO;
import android.scheduler.johnsalazar.DAO.NoteDAO;
import android.scheduler.johnsalazar.DAO.TermDAO;
import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Instructor;
import android.scheduler.johnsalazar.Entity.Note;
import android.scheduler.johnsalazar.Entity.Term;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Assessment.class, Course.class, Instructor.class, Note.class, Term.class}, version = 3, exportSchema = false)
public abstract class TermDBBuilder extends RoomDatabase {
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract InstructorDAO instructorDAO();
    public abstract NoteDAO noteDAO();
    public abstract TermDAO termDAO();

    public static volatile TermDBBuilder INSTANCE;

    static TermDBBuilder getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TermDBBuilder.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TermDBBuilder.class, "MyTermDatabase.db")
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries(); // For Synchronous build
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
