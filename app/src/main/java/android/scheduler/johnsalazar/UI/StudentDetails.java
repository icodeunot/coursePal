package android.scheduler.johnsalazar.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Student;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Helper.CourseAdapter;
import android.scheduler.johnsalazar.Helper.TermAdapter;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudentDetails extends AppCompatActivity {

    // Class Variables
    private Student currentStudent;

    private TermAdapter termAdapter;
    private int numTerms;
    private int termID;
    private int studentID;
    private String studentFirst;
    private String studentLast;
    private String studentPhone;
    private String studentEmail;
    private TextView tvStudentID;
    private TextView tvStudentFirst;
    private TextView tvStudentLast;
    private TextView tvStudentPhone;
    private TextView tvStudentEmail;
    private RecyclerView termRecycler;
    private FloatingActionButton studentDetailsFAB;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        setTitle("Student Details");

        // Hide the system navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Views
        tvStudentID = findViewById(R.id.studentID);
        tvStudentFirst = findViewById(R.id.studentFirst);
        tvStudentLast = findViewById(R.id.studentLast);
        tvStudentPhone = findViewById(R.id.studentPhone);
        tvStudentEmail = findViewById(R.id.studentEmail);
        termRecycler = findViewById(R.id.studentTermRecycler);
        studentDetailsFAB = findViewById(R.id.studentDetailsFAB);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Get and fill values from StudentList
        studentID = getIntent().getIntExtra("studentID", -1);
        currentStudent = repository.getThisStudent(studentID);
        studentID = currentStudent.getStudentID();
        studentFirst = getIntent().getStringExtra("studentFirst");
        studentLast = getIntent().getStringExtra("studentLast");
        studentPhone = getIntent().getStringExtra("studentPhone");
        studentEmail = getIntent().getStringExtra("studentEmail");
        tvStudentID.setText(String.valueOf(studentID));
        tvStudentFirst.setText(studentFirst);
        tvStudentLast.setText(studentLast);
        tvStudentPhone.setText(studentPhone);
        tvStudentEmail.setText(studentEmail);

        // Setup RecyclerView and filter terms for current students
        List<Term> filteredTerms = new ArrayList<>();
        for (Term t : repository.getmAllTerms()) {
            if (t.getStudentID() == studentID) filteredTerms.add(t);
        }
        termAdapter = new TermAdapter(this);
        termRecycler.setAdapter(termAdapter);
        termRecycler.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(filteredTerms);

        // Student Details FAB - Used to add terms while viewing student details
        studentDetailsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDetails.this, AddTerm.class);
                intent.putExtra("studentID", studentID);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_details, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(StudentDetails.this, StudentList.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.appHome) {
            Intent intent = new Intent(StudentDetails.this, StudentList.class);
            startActivity(intent);
            return true;
        }
        // Send student to EditStudent
        if (item.getItemId() == R.id.studentEdit) {
            currentStudent = repository.getThisStudent(studentID);
            Intent intent = new Intent(StudentDetails.this, EditStudent.class);
            intent.putExtra("studentID", studentID);
            startActivity(intent);
            return true;
        }
        // Delete Student if no terms
        if (item.getItemId() == R.id.studentDelete) {
            for (Student student : repository.getmAllStudents()) {
                if (student.getStudentID() == studentID) {
                    currentStudent = student;
                }
            }
            numTerms = 0;
            for (Term term : repository.getmAllTerms()) {
                if (term.getStudentID() == studentID) {
                    ++numTerms;
                }
            }
            if (numTerms == 0) {
                repository.delete(currentStudent);
                Toast.makeText(StudentDetails.this, currentStudent.getStudentFirst() +
                        " " + currentStudent.getStudentLast() + " was deleted.",
                        Toast.LENGTH_LONG).show();
                StudentDetails.this.finish();
            }
            else {
                waterFallDelete();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    // Delete all associated courses
    private void waterFallDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Student");
        builder.setMessage("This student has associated terms. You can delete all terms here, or " +
                "cancel and delete terms individually.");

        // Selection for waterfall delete
        builder.setPositiveButton("Delete all Terms", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Term> termsToDelete = new ArrayList<>();
                for (Term t : repository.getmAllTerms()) {
                    if (t.getStudentID() == studentID) {
                        termsToDelete.add(t);
                    }
                }
                for (Term t : termsToDelete) {
                    termID = t.getTermID();
                    for (Course c : repository.getmAllCourses()) {
                        if (c.getTermID() == termID) {
                            repository.delete(repository.getThisInstructor(c.getInstructorID()));
                            for (Assessment a : repository.getmAllAssessments()) {
                                if (a.getCourseID() == c.getCourseID()) {
                                    repository.delete(a);
                                }
                            }
                            repository.deleteTermCourses(termID);
                        }
                    }
                    repository.deleteStudentTerms(studentID);
                }
                repository.delete(currentStudent);
                Toast.makeText(StudentDetails.this, currentStudent.getStudentFirst() +
                                " " + currentStudent.getStudentLast() +
                                " and all associated terms have been deleted.",
                        Toast.LENGTH_LONG).show();
                StudentDetails.this.finish();
            }
        });

        // Selection to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}