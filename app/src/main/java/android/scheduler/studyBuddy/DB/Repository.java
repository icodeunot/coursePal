package android.scheduler.studyBuddy.DB;

import android.app.Application;
import android.scheduler.studyBuddy.DAO.AssessmentDAO;
import android.scheduler.studyBuddy.DAO.CourseDAO;
import android.scheduler.studyBuddy.DAO.InstructorDAO;
//import android.scheduler.johnsalazar.DAO.NoteDAO;
import android.scheduler.studyBuddy.DAO.StudentDAO;
import android.scheduler.studyBuddy.DAO.TermDAO;
import android.scheduler.studyBuddy.Entity.Assessment;
import android.scheduler.studyBuddy.Entity.Course;
import android.scheduler.studyBuddy.Entity.Instructor;
//import android.scheduler.johnsalazar.Entity.Note;
import android.scheduler.studyBuddy.Entity.Student;
import android.scheduler.studyBuddy.Entity.Term;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private AssessmentDAO mAssessmentDAO;
    private CourseDAO mCourseDAO;
    private InstructorDAO mInstructorDAO;
//    private NoteDAO mNoteDAO;
    private StudentDAO mStudentDAO;
    private TermDAO mTermDAO;

    private List<Assessment> mAllAssessments;
    private List<Course> mAllCourses;
    private List<Instructor> mAllInstructors;
//    private List<Note> mAllNotes;
    private List<Student> mAllStudents;
    private List<Term> mAllTerms;

    private Course mCourse;
    private Assessment mAssessment;
    private Instructor mInstructor;
    private Student mStudent;
    private Term mTerm;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        TermDBBuilder db = TermDBBuilder.getDatabase(application);
        mAssessmentDAO = db.assessmentDAO();
        mCourseDAO = db.courseDAO();
        mInstructorDAO = db.instructorDAO();
//        mNoteDAO = db.noteDAO();
        mStudentDAO = db.studentDAO();
        mTermDAO = db.termDAO();
    }

    public List<Assessment> getmAllAssessments() {
        databaseExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllAssessments;
    }
    public List<Assessment> getCourseAssessments(int courseID) {
        databaseExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getCourseAssessments(courseID);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllAssessments;
    }
    public void insert(Assessment assessment) {
        databaseExecutor.execute( ()-> {
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Assessment assessment) {
        databaseExecutor.execute( ()-> {
            mAssessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Assessment assessment) {
        databaseExecutor.execute( ()-> {
            mAssessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deleteCourseAssessments(int courseID) {
        databaseExecutor.execute(() -> {
            mAssessmentDAO.deleteCourseAssessments(courseID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Assessment getThisAssessment(int assessmentID) {
        databaseExecutor.execute(() -> {
            mAssessment = mAssessmentDAO.getThisAssessment(assessmentID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAssessment;
    }

    public List<Course> getmAllCourses() {
        databaseExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getAllCourses();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }
    public void insert(Course course) {
        databaseExecutor.execute( ()-> {
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Course course) {
        databaseExecutor.execute( ()-> {
            mCourseDAO.update(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Course course) {
        databaseExecutor.execute( ()-> {
            mCourseDAO.delete(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deleteTermCourses(int termID) {
        databaseExecutor.execute(() -> {
            mCourseDAO.deleteTermCourses(termID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Course getThisCourse(int courseID) {
        databaseExecutor.execute(() -> {
            mCourse = mCourseDAO.getThisCourse(courseID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mCourse;
    }

    public List<Instructor> getmAllInstructors() {
        databaseExecutor.execute(() -> {
            mAllInstructors = mInstructorDAO.getAllInstructors();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllInstructors;
    }
    public void insert(Instructor instructor) {
        databaseExecutor.execute( ()-> {
            mInstructorDAO.insert(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Instructor instructor) {
        databaseExecutor.execute( ()-> {
            mInstructorDAO.update(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Instructor instructor) {
        databaseExecutor.execute( ()-> {
            mInstructorDAO.delete(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Instructor getThisInstructor(int instructorID) {
        databaseExecutor.execute(() -> {
            mInstructor = mInstructorDAO.getThisInstructor(instructorID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mInstructor;
    }

//    public List<Note> getmAllNotes() {
//        databaseExecutor.execute(() -> {
//            mAllNotes = mNoteDAO.getAllNotes();
//        });
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return mAllNotes;
//    }
//    public void insert(Note note) {
//        databaseExecutor.execute( ()-> {
//            mNoteDAO.insert(note);
//        });
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    public void update(Note note) {
//        databaseExecutor.execute( ()-> {
//            mNoteDAO.update(note);
//        });
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    public void delete(Note note) {
//        databaseExecutor.execute( ()-> {
//            mNoteDAO.delete(note);
//        });
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public List<Student> getmAllStudents() {
        databaseExecutor.execute(() -> {
            mAllStudents = mStudentDAO.getAllStudents();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllStudents;
    }
    public void insert(Student student) {
        databaseExecutor.execute( ()-> {
            mStudentDAO.insert(student);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Student student) {
        databaseExecutor.execute( ()-> {
            mStudentDAO.update(student);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Student student) {
        databaseExecutor.execute( ()-> {
            mStudentDAO.delete(student);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Student getThisStudent(int studentID) {
        databaseExecutor.execute(() -> {
            mStudent = mStudentDAO.getThisStudent(studentID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mStudent;
    }

    public List<Term> getmAllTerms() {
        databaseExecutor.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllTerms;
    }
    public void insert(Term term) {
        databaseExecutor.execute( ()-> {
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Term term) {
        databaseExecutor.execute( ()-> {
            mTermDAO.update(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Term term) {
        databaseExecutor.execute( ()-> {
            mTermDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deleteStudentTerms(int studentID) {
        databaseExecutor.execute(() -> {
            mTermDAO.deleteStudentTerms(studentID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Term getThisTerm(int termID) {
        databaseExecutor.execute(() -> {
            mTerm = mTermDAO.getThisTerm(termID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mTerm;
    }
}
