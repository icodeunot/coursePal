package android.scheduler.studyBuddy.DAO;

import android.scheduler.studyBuddy.Entity.Course;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY courseID ASC")
    List<Course> getAllCourses();

    @Query("DELETE FROM courses WHERE termID = :termID")
    void deleteTermCourses(int termID);

    @Query("SELECT * FROM  courses WHERE courseID = :courseID;")
    Course getThisCourse(int courseID);

}
