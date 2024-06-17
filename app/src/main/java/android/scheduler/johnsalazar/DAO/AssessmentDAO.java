package android.scheduler.johnsalazar.DAO;

import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Entity.Term;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("Select * FROM assessments ORDER BY assessmentID ASC")
    List<Assessment> getAllAssessments();

    @Query("DELETE FROM assessments WHERE courseID = :courseID")
    void deleteCourseAssessments(int courseID);

    @Query("SELECT * FROM assessments WHERE assessmentID = :assessmentID ORDER BY assessmentID ASC;")
    Assessment getThisAssessment(int assessmentID);

    @Query("SELECT * FROM assessments WHERE courseID = :courseID ORDER BY assessmentID ASC;")
    List<Assessment> getCourseAssessments(int courseID);
}
