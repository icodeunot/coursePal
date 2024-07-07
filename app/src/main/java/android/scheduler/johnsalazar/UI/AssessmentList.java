package android.scheduler.johnsalazar.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Helper.AssessmentAdapter;
import android.scheduler.johnsalazar.Helper.CourseAdapter;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AssessmentList extends AppCompatActivity {

    private Repository repository;
    private RecyclerView assessmentRecycler;
    private FloatingActionButton assessmentListFAB;
    private AssessmentAdapter assessmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Assessments");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize UI Elements
        assessmentListFAB = findViewById(R.id.assessmentListFAB);
        assessmentRecycler = findViewById(R.id.assessmentListRecycler);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Setup AssessmentRecycler and fill with all assessments
        List<Assessment> allAssessments = repository.getmAllAssessments();
        assessmentAdapter = new AssessmentAdapter(this);
        assessmentRecycler.setAdapter(assessmentAdapter);
        assessmentRecycler.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(allAssessments);

        // Setup AssessmentList FAB (Redirect to Course List)
        assessmentListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentList.this, CourseList.class);
                startActivity(intent);
                Toast.makeText(AssessmentList.this, "Please choose a course to add an assessment.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reset Recycler
        List<Assessment> allAssessments = repository.getmAllAssessments();
        assessmentAdapter = new AssessmentAdapter(this);
        assessmentRecycler.setAdapter(assessmentAdapter);
        assessmentRecycler.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(allAssessments);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.courses) {
            Intent allCourses = new Intent(AssessmentList.this, CourseList.class);
            startActivity(allCourses);
        }
        else if(item.getItemId() == R.id.terms) {
            Intent allTerms = new Intent(AssessmentList.this, TermList.class);
            startActivity(allTerms);
        }
        else if(item.getItemId() == R.id.students) {
            Intent allStudents = new Intent(AssessmentList.this, StudentList.class);
            startActivity(allStudents);
        }
        // Exit App
        if(item.getItemId() == R.id.closeApp) {
            finishAffinity();
            return true;
        }
        else if(item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }

}