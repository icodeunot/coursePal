package android.scheduler.johnsalazar.UI;

import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Instructor;
import android.scheduler.johnsalazar.R;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddInstructor extends AppCompatActivity {

    int instructorID;

    String instructorFirst;
    String instructorLast;
    String instructorPhone;
    String instructorEmail;
    EditText addInstructorFirst;
    EditText addInstructorLast;
    EditText addInstructorPhone;
    EditText addInstructorEmail;
    Repository repository;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructor);
        setTitle("Add Instructor");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        instructorID = -1;

        // First Name details
        addInstructorFirst = findViewById(R.id.addInstructorFirst);
        addInstructorLast = findViewById(R.id.addInstructorLast);
        addInstructorPhone = findViewById(R.id.addInstructorPhone);
        addInstructorEmail = findViewById(R.id.addInstructorEmail);

        repository = new Repository(getApplication());

        addInstructorFirst.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addInstructorLast.requestFocus();
                return true;
            }
            return false;
        });
        // Last Name details
        addInstructorLast.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addInstructorPhone.requestFocus();
                return true;
            }
            return false;
        });
        // Phone Details
        addInstructorPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        addInstructorPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        addInstructorPhone.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addInstructorEmail.requestFocus();
                return true;
            }
            return false;
        });
        // Email Details
        addInstructorEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(addInstructorEmail.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_instructor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Assure data is preserved in previous screen
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.addInstructorCancel) {
            setResult(RESULT_CANCELED);
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.addInstructorSave) {
                instructorFirst = addInstructorFirst.getText().toString();
                instructorLast = addInstructorLast.getText().toString();
                instructorPhone = addInstructorPhone.getText().toString();
                instructorEmail = addInstructorEmail.getText().toString();

            if (instructorFirst.isEmpty() || instructorLast.isEmpty() || instructorPhone.isEmpty() || instructorEmail.isEmpty()) {
                Toast.makeText(AddInstructor.this, "Please add new instructor details to save before adding to course.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (instructorID == -1) {
                if (repository.getmAllInstructors().size() == 0) {
                    instructorID = 1;
                } else {
                    instructorID = repository.getmAllInstructors().get(repository.getmAllInstructors().size() - 1).getInstructorID() + 1;
                }
            }
            instructorFirst = addInstructorFirst.getText().toString();
            instructorLast = addInstructorLast.getText().toString();
            instructorPhone = addInstructorPhone.getText().toString();
            instructorEmail = addInstructorEmail.getText().toString();
            Instructor instructor = new Instructor(instructorID, instructorFirst, instructorLast, instructorPhone, instructorEmail);
            repository.insert(instructor);

            Intent intent = new Intent(AddInstructor.this, AddCourse.class);
            intent.putExtra("instructorID", instructorID);
            setResult(RESULT_OK, intent);
            this.finish();
            return true;
        }
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
