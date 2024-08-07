package android.scheduler.studyBuddy.DAO;

import android.scheduler.studyBuddy.Entity.Term;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM terms ORDER BY termID ASC")
    List<Term> getAllTerms();

    @Query("DELETE FROM terms WHERE studentID = :studentID")
    void deleteStudentTerms(int studentID);

    @Query("SELECT * FROM terms WHERE termID = :termID ORDER BY termID ASC;")
    Term getThisTerm(int termID);

}
