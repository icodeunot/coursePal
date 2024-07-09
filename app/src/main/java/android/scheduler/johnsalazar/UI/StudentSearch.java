package android.scheduler.johnsalazar.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Student;
import android.scheduler.johnsalazar.Helper.SearchAdapter;
import android.scheduler.johnsalazar.R;
import android.view.View;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class StudentSearch extends AppCompatActivity {

    private Repository repository;
    private SearchAdapter searchAdapter;
    private RecyclerView searchRecycler;
    private SearchView studentSearchView;
    private List<Student> allStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);
        setTitle("Student Search");

        // Initialize Views
        searchRecycler = findViewById(R.id.searchRecycler);
        studentSearchView = findViewById(R.id.studentSearchView);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Fetch all students from the repository
        allStudents = repository.getmAllStudents();

        // Setup RecyclerView and Adapter with an empty list initially
        searchAdapter = new SearchAdapter(this);
        searchRecycler.setAdapter(searchAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter.setStudents(new ArrayList<>());  // Set empty list initially

        // Set up SearchView
        studentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // If search is cleared, show empty list
                    searchAdapter.setStudents(new ArrayList<>());
                } else {
                    filter(newText);
                }
                return false;
            }
        });
    }

    private void filter(String text) {
        List<Student> filteredList = new ArrayList<>();
        String searchText = text.toLowerCase().trim();  // Convert search text to lowercase

        for (Student student : allStudents) {
            String firstName = student.getStudentFirst().toLowerCase();
            String phone = student.getStudentPhone().toLowerCase();

            // Check if any of the fields contain the search text
            if (firstName.contains(searchText) ||
                    phone.contains(searchText))
                filteredList.add(student);
            }
        searchAdapter.setStudents(filteredList);
    }
}
