package android.scheduler.johnsalazar.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.Helper.TermAdapter;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;


public class TermList extends AppCompatActivity {

    // Class variables/instances
    private Repository repository;
    private RecyclerView termRecycler;
    private FloatingActionButton termListFAB;
    private TermAdapter termAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        setTitle("Terms");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Initialize UI elements
        termListFAB = findViewById(R.id.termListFAB);
        termRecycler = findViewById(R.id.termListRecycler);

        // Setup Term Recycler and fill with all terms
        List<Term> allTerms = repository.getmAllTerms();
        termAdapter = new TermAdapter(this);
        termRecycler.setAdapter(termAdapter);
        termRecycler.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);

        // Setup termListFAB
        termListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermList.this, AddTerm.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Reset recycler
        List<Term> allTerms = repository.getmAllTerms();
        termAdapter = new TermAdapter(this);
        termRecycler.setAdapter(termAdapter);
        termRecycler.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Go to CourseList
        if(item.getItemId() == R.id.courses) {
            Intent allCourses = new Intent(TermList.this, CourseList.class);
            startActivity(allCourses);
        }

        // Go to AssessmentList
        if(item.getItemId() == R.id.assessments) {
            Intent allAssessments = new Intent(TermList.this, AssessmentList.class);
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