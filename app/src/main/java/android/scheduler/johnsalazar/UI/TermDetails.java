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
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Helper.CourseAdapter;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermDetails extends AppCompatActivity {

    // Class Variables
    private Term currentTerm;

    private CourseAdapter courseAdapter;
    private int numCourses;
    private int termID;
    private int studentID;
    private String termTitle;
    private String termStart;
    private String termEnd;
    private TextView editTitle;
    private TextView editStart;
    private TextView editEnd;
    private TextView editTermID;
    private RecyclerView courseRecycler;
    private FloatingActionButton termDetailsFAB;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        setTitle("Term Details");

        // Hide the system navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Views
        editTermID = findViewById(R.id.termID);
        editTitle = findViewById(R.id.termTitle);
        editStart = findViewById(R.id.termStart);
        editEnd = findViewById(R.id.termEnd);
        courseRecycler = findViewById(R.id.termCourseRecylcer);
        termDetailsFAB = findViewById(R.id.termDetailsFAB);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Get and fill values from TermList
        termID = getIntent().getIntExtra("termID", -1);
        currentTerm = repository.getThisTerm(termID);
        studentID = currentTerm.getStudentID();
        termTitle = getIntent().getStringExtra("termTitle");
        termStart = getIntent().getStringExtra("termStart");
        termEnd = getIntent().getStringExtra("termEnd");
        editTermID.setText(String.valueOf(termID));
        editTitle.setText(termTitle);
        editStart.setText(termStart);
        editEnd.setText(termEnd);

        // Setup RecyclerView and filter courses for current term
        List<Course> filteredCourses = new ArrayList<>();
        for (Course c : repository.getmAllCourses()) {
            if (c.getTermID() == termID) filteredCourses.add(c);
        }
        courseAdapter = new CourseAdapter(this);
        courseRecycler.setAdapter(courseAdapter);
        courseRecycler.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(filteredCourses);

        // Term Details FAB - Used to add courses through EditTerm
        termDetailsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetails.this, AddCourse.class);
                intent.putExtra("termID", termID);
                intent.putExtra("termTitle", termTitle);
                intent.putExtra("termStart", termStart);
                intent.putExtra("termEnd", termEnd);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_details, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(TermDetails.this, TermList.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.appHome) {
            Intent intent = new Intent(TermDetails.this, StudentList.class);
            startActivity(intent);
            return true;
        }
        // Send term to EditTerm
        if (item.getItemId() == R.id.termEdit) {
            currentTerm = repository.getThisTerm(termID);
            Intent intent = new Intent(TermDetails.this, EditTerm.class);
            intent.putExtra("termID", termID);
            intent.putExtra("studentID", currentTerm.getStudentID());
            intent.putExtra("termTitle", termTitle);
            intent.putExtra("termStart", termStart);
            intent.putExtra("termEnd", termEnd);
            startActivity(intent);
            return true;
        }
        // Delete Term if no courses
        if (item.getItemId() == R.id.termDelete) {
            for (Term term : repository.getmAllTerms()) {
                if (term.getTermID() == termID) {
                    currentTerm = term;
                }
            }
            numCourses = 0;
            for (Course course : repository.getmAllCourses()) {
                if (course.getTermID() == termID) {
                    ++numCourses;
                }
            }
            if (numCourses == 0) {
                repository.delete(currentTerm);
                Toast.makeText(TermDetails.this, currentTerm.getTermTitle() + " was deleted", Toast.LENGTH_LONG).show();
                TermDetails.this.finish();
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
        builder.setTitle("Delete Term");
        builder.setMessage("This term has associated courses. You can delete all courses here, or " +
                "cancel and delete courses individually.");

        // Selection for waterfall delete
        builder.setPositiveButton("Delete all courses", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Course c : repository.getmAllCourses()) {
                    if (c.getTermID() == termID) {
                        repository.delete(repository.getThisInstructor(c.getInstructorID()));
                        for (Assessment a : repository.getmAllAssessments()) {
                            if (a.getCourseID() == c.getCourseID()) {
                                repository.delete(a);
                            }
                        }
                    }
                }
                repository.deleteTermCourses(termID);
                repository.delete(currentTerm);
                Toast.makeText(TermDetails.this, currentTerm.getTermTitle() + " and all associated courses have been deleted.",
                        Toast.LENGTH_LONG).show();
                TermDetails.this.finish();
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