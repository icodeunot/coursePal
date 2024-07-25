package android.scheduler.studyBuddy.DAO;

import android.scheduler.studyBuddy.Entity.Student;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students ORDER BY studentID ASC")
    List<Student> getAllStudents();

    @Query("SELECT * FROM students WHERE studentID = :studentID ORDER BY studentID ASC;")
    Student getThisStudent(int studentID);

}