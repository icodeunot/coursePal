package android.scheduler.johnsalazar.UI;

import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Instructor;
import android.scheduler.johnsalazar.Entity.Student;
import android.scheduler.johnsalazar.Entity.Term;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddStudent extends AppCompatActivity {

    int studentID;

    String studentFirst;
    String studentLast;
    String studentPhone;
    String studentEmail;
    EditText addStudentFirst;
    EditText addStudentLast;
    EditText addStudentPhone;
    EditText addStudentEmail;
    FloatingActionButton floatButton;
    Repository repository;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        setTitle("Add New Student");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Assign studentID to -1 for creation and initialize views
        studentID = -1;
        addStudentFirst = findViewById(R.id.addStudentFirst);
        addStudentLast = findViewById(R.id.addStudentLast);
        addStudentPhone = findViewById(R.id.addStudentPhone);
        addStudentEmail = findViewById(R.id.addStudentEmail);
        floatButton = findViewById(R.id.addStudentFAB); //adds a term

        // Initialize the repository
        repository = new Repository(getApplication());


        addStudentFirst.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addStudentLast.requestFocus();
                return true;
            }
            return false;
        });
        // Last Name details
        addStudentLast.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addStudentPhone.requestFocus();
                return true;
            }
            return false;
        });
        // Phone Details
        addStudentPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        addStudentPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        addStudentPhone.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addStudentEmail.requestFocus();
                return true;
            }
            return false;
        });
        // Email Details
        addStudentEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(addStudentEmail.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        // Setup FAB
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentFirst = addStudentFirst.getText().toString();
                studentLast = addStudentLast.getText().toString();
                studentPhone = addStudentPhone.getText().toString();
                studentEmail = addStudentEmail.getText().toString();

                if (studentFirst.isEmpty() || studentLast.isEmpty() || studentPhone.isEmpty() ||
                        studentEmail.isEmpty()) {
                    Toast.makeText(AddStudent.this, "Please add new student details to save before adding term.", Toast.LENGTH_LONG).show();
                    return;
                }

                Student student;
                if(studentID == -1) {
                    if(repository.getmAllStudents().size() == 0) {
                        studentID = 1;
                    }
                    else {
                        studentID = repository.getmAllStudents().get(repository.getmAllStudents().size() - 1).getStudentID() + 1;
                    }
                    student = new Student(studentID, studentFirst, studentLast, studentPhone, studentEmail);
                    repository.insert(student);
                    Toast.makeText(AddStudent.this, "Student has been saved. View in Student List if you cancel.", Toast.LENGTH_LONG).show();
                }
                else {
                    student = new Student(studentID, studentFirst, studentLast, studentPhone, studentEmail);
                    repository.update(student);
                    Toast.makeText(AddStudent.this, "Student has been updated. View in Student List if you cancel.", Toast.LENGTH_LONG).show();

                }

                Intent intent = new Intent(AddStudent.this, AddTerm.class);
                intent.putExtra("studentID", studentID);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_student, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Assure data is preserved in previous screen
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.addStudentCancel) {
            setResult(RESULT_CANCELED);
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.addStudentSave) {
            // set string values
            studentFirst = addStudentFirst.getText().toString();
            studentLast = addStudentLast.getText().toString();
            studentPhone = addStudentPhone.getText().toString();
            studentEmail = addStudentEmail.getText().toString();
            // validations
            if (studentFirst.isEmpty() || studentLast.isEmpty() || studentPhone.isEmpty()
                    || studentEmail.isEmpty()) {
                Toast.makeText(AddStudent.this, "Please add new student details to save.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            if (studentID == -1) {
                if (repository.getmAllStudents().size() == 0) {
                    studentID = 1;
                } else {
                    studentID = repository.getmAllStudents().get(repository.getmAllStudents().size() - 1).getStudentID() + 1;
                }
            }
            studentFirst = addStudentFirst.getText().toString();
            studentLast = addStudentLast.getText().toString();
            studentPhone = addStudentPhone.getText().toString();
            studentEmail = addStudentEmail.getText().toString();
            Student student = new Student(studentID, studentFirst, studentLast, studentPhone, studentEmail);
            repository.insert(student);

            Intent intent = new Intent(AddStudent.this, StudentList.class);
            intent.putExtra("studentID", studentID);
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
