package android.scheduler.johnsalazar.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Student;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.Helper.StudentAdapter;
import android.scheduler.johnsalazar.Helper.TermAdapter;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;


public class StudentList extends AppCompatActivity {

    // Class variables/instances
    private Repository repository;
    private RecyclerView studentRecycler;
    private FloatingActionButton studentListFAB;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        setTitle("Students");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Initialize UI elements
        studentListFAB = findViewById(R.id.studentListFAB);
        studentRecycler = findViewById(R.id.studentListRecycler);

        // Setup Student Recycler and fill with all students
        List<Student> allStudents = repository.getmAllStudents();
        studentAdapter = new StudentAdapter(this);
        studentRecycler.setAdapter(studentAdapter);
        studentRecycler.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter.setStudents(allStudents);

        // Setup studentListFAB
        studentListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentList.this, AddStudent.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Reset recycler
        List<Student> allStudents = repository.getmAllStudents();
        studentAdapter = new StudentAdapter(this);
        studentRecycler.setAdapter(studentAdapter);
        studentRecycler.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter.setStudents(allStudents);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Go to terms
        if(item.getItemId() == R.id.terms) {
            Intent allTerms = new Intent(StudentList.this, TermList.class);
            startActivity(allTerms);
        }
        // Go to CourseList
        if(item.getItemId() == R.id.courses) {
            Intent allCourses = new Intent(StudentList.this, CourseList.class);
            startActivity(allCourses);
        }
        // Go to AssessmentList
        if(item.getItemId() == R.id.assessments) {
            Intent allAssessments = new Intent(StudentList.this, AssessmentList.class);
            startActivity(allAssessments);
        }
        // Exit App
        if(item.getItemId() == R.id.closeApp) {
            finishAffinity();
            return true;
        }
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }
}