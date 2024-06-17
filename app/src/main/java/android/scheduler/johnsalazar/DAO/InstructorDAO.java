package android.scheduler.johnsalazar.DAO;

import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Instructor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InstructorDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Instructor instructor);

    @Update
    void update(Instructor instructor);

    @Delete
    void delete(Instructor instructor);

    @Query("Select * FROM instructors ORDER BY instructorID")
    List<Instructor> getAllInstructors();

    @Query("SELECT * FROM  instructors WHERE instructorID = :instructorID;")
    Instructor getThisInstructor(int instructorID);
}
