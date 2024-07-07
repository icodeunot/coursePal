package android.scheduler.johnsalazar.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.Helper.CourseAdapter;
import android.scheduler.johnsalazar.Helper.TermAdapter;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CourseList extends AppCompatActivity {

    // Class Variables
    private Repository repository;
    private RecyclerView courseRecycler;
    private FloatingActionButton courseListFAB;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        setTitle("Courses");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize UI elements
        courseListFAB = findViewById(R.id.courseListFAB);
        courseRecycler = findViewById(R.id.courseListRecycler);

        // Initialize Repository
        repository = new Repository(getApplication());

        //Setup Course Recycler and fill with all courses
        List<Course> allCourses = repository.getmAllCourses();
        courseAdapter = new CourseAdapter(this);
        courseRecycler.setAdapter(courseAdapter);
        courseRecycler.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(allCourses);

        // Setup CourseList FAB (Redirect to TermList)
        courseListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseList.this, TermList.class);
                startActivity(intent);
                Toast.makeText(CourseList.this, "Please choose a term to add a course.", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_list, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Reset recycler
        List<Course> allCourses = repository.getmAllCourses();
        courseAdapter = new CourseAdapter(this);
        courseRecycler.setAdapter(courseAdapter);
        courseRecycler.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(allCourses);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Go to terms
        if(item.getItemId() == R.id.terms) {
            Intent allTerms = new Intent(CourseList.this, TermList.class);
            startActivity(allTerms);
        }
        // Go to assessments
        if(item.getItemId() == R.id.assessments) {
            Intent allAssessments = new Intent(CourseList.this, AssessmentList.class);
            startActivity(allAssessments);
        }
        // Go to students
        if(item.getItemId() == R.id.students) {
            Intent allStudents = new Intent(CourseList.this, StudentList.class);
            startActivity(allStudents);
        }
        // Exit App
        if(item.getItemId() == R.id.closeApp) {
            finishAffinity();
            return true;
        }
        if(item.getItemId() == android.R.id.home) {
            this.finish();
            Intent intent = new Intent(CourseList.this, TermList.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

}