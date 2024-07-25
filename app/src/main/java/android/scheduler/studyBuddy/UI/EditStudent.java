package android.scheduler.studyBuddy.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.studyBuddy.DB.Repository;
import android.scheduler.studyBuddy.Entity.Student;
import android.scheduler.studyBuddy.R;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditStudent extends AppCompatActivity {

    // Class Variables
    private int studentID;
    private String studentFirst;
    private String studentLast;
    private String studentPhone;
    private String studentEmail;
    private EditText etStudentFirst;
    private EditText etStudentLast;
    private EditText etStudentPhone;
    private EditText etStudentEmail;
    private Student currentStudent;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        setTitle("Edit Student");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Initialize Views
        etStudentFirst = findViewById(R.id.editStudentFirst);
        etStudentLast = findViewById(R.id.editStudentLast);
        etStudentPhone = findViewById(R.id.editStudentPhone);
        etStudentEmail = findViewById(R.id.editStudentEmail);

        // Get and fill values from TermDetails
        studentID = getIntent().getIntExtra("studentID", -1);
        currentStudent = repository.getThisStudent(studentID);
        studentFirst = currentStudent.getStudentFirst();
        studentLast = currentStudent.getStudentLast();
        studentPhone = currentStudent.getStudentPhone();
        studentEmail = currentStudent.getStudentEmail();
        etStudentFirst.setText(studentFirst);
        etStudentLast.setText(studentLast);
        etStudentPhone.setText(studentPhone);
        etStudentEmail.setText(studentEmail);
        // Setting hints for blank, data but this should never happen based on Save validations.
        if (studentFirst.isEmpty()) {
            etStudentFirst.setHint("Student First Name");
        }
        if (studentLast.isEmpty()) {
            etStudentLast.setHint("Student Last Name");
        }
        if (studentPhone.isEmpty()) {
            etStudentPhone.setHint("xxx-xxx-xxxx");
        }
        if (studentEmail.isEmpty()) {
            etStudentEmail.setHint("Student Email");
        }

        // First name details
        etStudentFirst.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etStudentLast.requestFocus();
                return true;
            }
            return false;
        });
        // Last Name details
        etStudentLast.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etStudentPhone.requestFocus();
                return true;
            }
            return false;
        });
        // Phone Details
        etStudentPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etStudentPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        etStudentPhone.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etStudentEmail.requestFocus();
                return true;
            }
            return false;
        });
        // Email Details
        etStudentEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etStudentEmail.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_student, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Save the updated term
        if (item.getItemId() == R.id.editStudentSave) {
            // Set String values
            studentFirst = etStudentFirst.getText().toString();
            studentLast = etStudentLast.getText().toString();
            studentPhone = etStudentPhone.getText().toString();
            studentEmail = etStudentEmail.getText().toString();
            // Validations (empty fields, start/end)
            if (studentFirst.isEmpty() || studentLast.isEmpty() ||
                    studentPhone.isEmpty() || studentEmail.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields and try again.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            Student student;
            // Catch a bad term. Should not have a -1 termID at this point.
            if(studentID == -1) {
                Toast.makeText(this, "There was an error loading your student. " +
                        "Please go back to the student list" +
                        " and try again.", Toast.LENGTH_LONG).show();
                finish();
                return false;
            }
            // Clears all validation, update the table.
            else {
                student = new Student(studentID, studentFirst, studentLast,
                        studentPhone, studentEmail);
                repository.update(student);
            }
            // Send back to StudentDetails to see results
            Intent resultIntent = new Intent(EditStudent.this, StudentDetails.class);
            resultIntent.putExtra("studentID", studentID);
            resultIntent.putExtra("studentFirst", studentFirst);
            resultIntent.putExtra("studentLast", studentLast);
            resultIntent.putExtra("studentPhone", studentPhone);
            resultIntent.putExtra("studentEmail", studentEmail);
            startActivity(resultIntent);
            finish();
        }
        if (item.getItemId() == R.id.editStudentCancel) {
            finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
//            Intent intent = new Intent(EditTerm.this, TermList.class);
//            startActivity(intent);
            finish();
            return true;
        }
        finish();
        return true;
    }
    // Save any details that were input in case screen is interrupted
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        studentFirst = etStudentFirst.getText().toString();
        studentLast = etStudentLast.getText().toString();
        studentPhone = etStudentPhone.getText().toString();
        studentEmail = etStudentEmail.getText().toString();
        outState.putInt("studentID", studentID);
        outState.putString("studentFirst", studentFirst);
        outState.putString("studentLast", studentLast);
        outState.putString("studentPhone", studentPhone);
        outState.putString("studentEmail", studentEmail);
    }
    // Update the temporarily saved inputs
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        studentID = savedInstanceState.getInt("studentID");
        studentFirst = savedInstanceState.getString("studentFirst");
        studentLast = savedInstanceState.getString("studentLast");
        studentPhone = savedInstanceState.getString("studentPhone");
        studentEmail = savedInstanceState.getString("studentEmail");
        etStudentFirst.setText(studentFirst);
        etStudentLast.setText(studentLast);
        etStudentPhone.setText(studentPhone);
        etStudentEmail.setText(studentEmail);
    }
    // Drop the keyboard when clicking out of editTitle
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}