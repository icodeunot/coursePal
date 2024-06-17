package android.scheduler.johnsalazar.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Instructor;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.Helper.CourseAdapter;
import android.scheduler.johnsalazar.R;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditCourse extends AppCompatActivity {

    // Class Variables
    private Course thisCourse;
    private Instructor thisInstructor;
    private Term thisTerm;
    private int courseID;
    private int termID;
    private int instructorID;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private String courseStatus;
    private String instructorFirst;
    private String instructorLast;
    private String instructorPhone;
    private String instructorEmail;
    private String courseNote;
    private EditText editCourseTitle;
    private TextView viewCourseStart;
    private TextView viewCourseEnd;
    DatePickerDialog.OnDateSetListener startListener;
    final Calendar editStartCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endListener;
    final Calendar editEndCalendar = Calendar.getInstance();
    private Spinner courseStatusSpinner;
    private EditText editInstFirst;
    private EditText editInstLast;
    private EditText editInstPhone;
    private EditText editInstEmail;
    private EditText editCourseNote;

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        setTitle("Edit Course");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Repository and get objects
        repository = new Repository(getApplication());
        courseID = getIntent().getIntExtra("courseID", -1);
        if (courseID != -1) {
            thisCourse = repository.getThisCourse(courseID);
            thisInstructor = repository.getThisInstructor(thisCourse.getInstructorID());
            thisTerm = repository.getThisTerm(thisCourse.getTermID());
        }

        // Initialize views
        editCourseTitle = findViewById(R.id.editCourseTitle);
        viewCourseStart = findViewById(R.id.editCourseStart);
        viewCourseEnd = findViewById(R.id.editCourseEnd);
        courseStatusSpinner = findViewById(R.id.editCourseStatusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatusSpinner.setAdapter(adapter);
        editInstFirst = findViewById(R.id.editInstructorFirst);
        editInstLast = findViewById(R.id.editInstructorLast);
        editInstPhone = findViewById(R.id.editInstructorPhone);
        editInstEmail = findViewById(R.id.editInstructorEmail);
        editCourseNote = findViewById(R.id.editNoteText);

        // Get Values
        if (courseID != -1) {
            instructorID = thisCourse.getInstructorID();
            courseTitle = thisCourse.getCourseTitle();
            courseStart = thisCourse.getCourseStart();
            courseEnd = thisCourse.getCourseEnd();
            courseStatus = thisCourse.getCourseStatus();
            instructorFirst = thisInstructor.getInstructorFirstName();
            instructorLast = thisInstructor.getInstructorLastName();
            instructorPhone = thisInstructor.getInstructorPhone();
            instructorEmail = thisInstructor.getInstructorEmail();
            courseNote = thisCourse.getCourseNote();
            // Set Values
            editCourseTitle.setText(courseTitle);
            viewCourseStart.setText(courseStart);
            viewCourseEnd.setText(courseEnd);
            if (courseStatus != null) {
                int spinnerPosition = adapter.getPosition(courseStatus);
                courseStatusSpinner.setSelection(spinnerPosition);
            }
            editInstFirst.setText(instructorFirst);
            editInstLast.setText(instructorLast);
            editInstPhone.setText(instructorPhone);
            editInstEmail.setText(instructorEmail);
            editCourseNote.setText(courseNote);

            // Setup date formatting
            String formatDate = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
            String currentDate = sdf.format(new Date());
            // Create Listeners and assign them for Start and End dates
            viewCourseStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String info = currentDate.toString();
                    try {
                        editStartCalendar.setTime(sdf.parse(info));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new DatePickerDialog(EditCourse.this, startListener, editStartCalendar.get(Calendar.YEAR),
                            editStartCalendar.get(Calendar.MONTH), editStartCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            startListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    editStartCalendar.set(Calendar.YEAR, year);
                    editStartCalendar.set(Calendar.MONTH, monthOfYear);
                    editStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateDate(viewCourseStart);
                }
            };
            viewCourseEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Date date;
                    String info = currentDate.toString();
                    try {
                        editEndCalendar.setTime(sdf.parse(info));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new DatePickerDialog(EditCourse.this, endListener, editEndCalendar.get(Calendar.YEAR),
                            editEndCalendar.get(Calendar.MONTH), editEndCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            endListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    editEndCalendar.set(Calendar.YEAR, year);
                    editEndCalendar.set(Calendar.MONTH, monthOfYear);
                    editEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateDate(viewCourseEnd);
                }
            };

            // pressing Enter leaves EditText
            editCourseNote.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editCourseNote.getWindowToken(), 0);
                        hideKeyboard();
                        editCourseNote.clearFocus();
                        return true;
                    }
                    return false;
                }
            });
            // Scroll back to beginning of note
            editCourseNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        editCourseNote.scrollTo(0, 0);
                        hideKeyboard();
                    }
                }
            });
        } else {
            Toast.makeText(EditCourse.this, "Error getting course details.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_course, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.editCourseSave) {
            updateCourse();
        }
        if (item.getItemId() == R.id.editCourseCancel) {
            this.finish();
            return true;
        }
        return true;
    }
    public boolean updateCourse() {
        termID = thisCourse.getTermID();
        instructorID = thisCourse.getInstructorID();

        courseTitle = editCourseTitle.getText().toString();
        courseStart = viewCourseStart.getText().toString();
        courseEnd = viewCourseEnd.getText().toString();
        courseStatus = courseStatusSpinner.getSelectedItem().toString();
        instructorFirst = editInstFirst.getText().toString();
        instructorLast = editInstLast.getText().toString();
        instructorPhone = editInstPhone.getText().toString();
        instructorEmail = editInstEmail.getText().toString();
        courseNote = editCourseNote.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            editStartCalendar.setTime(sdf.parse(courseStart));
            editEndCalendar.setTime(sdf.parse(courseEnd));
        } catch (ParseException e) {
            Toast.makeText(this, "There was an error saving your Course. Please try again.",
                    Toast.LENGTH_LONG).show();
            finish();
            return false;
        }
        // Validations
        if (courseTitle.isEmpty() || courseStart.isEmpty() || courseEnd.isEmpty() || courseStatus.isEmpty()
            || instructorFirst.isEmpty() || instructorLast.isEmpty() || instructorPhone.isEmpty()
            || instructorEmail.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields and try again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editStartCalendar.getTime().after(editEndCalendar.getTime())) {
            Toast.makeText(this, "Course cannot end before starting. Adjust dates and try" +
                    " again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (courseStatusSpinner.getSelectedItem().toString().equals("Choose Status")) {
            Toast.makeText(EditCourse.this, "Please select a course status.", Toast.LENGTH_SHORT).show();
            courseStatusSpinner.requestFocus();
            return false;
        }
        else {
            Course editedCourse;
            editedCourse = new Course(courseID, courseTitle, courseStart, courseEnd, courseStatus,
                                      courseNote, termID, instructorID);
            thisInstructor = new Instructor(instructorID, instructorFirst, instructorLast, instructorPhone, instructorEmail);
            repository.update(thisInstructor);
            repository.update(editedCourse);
            // Send back to CourseDetails
            thisTerm = repository.getThisTerm(termID);
            Intent refreshIntent= new Intent(EditCourse.this, CourseDetails.class);
            refreshIntent.putExtra("courseID", courseID);
            refreshIntent.putExtra("termID", termID);
            refreshIntent.putExtra("courseTitle", courseTitle);
            refreshIntent.putExtra("courseStart", courseStart);
            refreshIntent.putExtra("courseEnd", courseEnd);
            refreshIntent.putExtra("courseStatus", courseStatus);
            refreshIntent.putExtra("courseID", courseID);
            refreshIntent.putExtra("courseNote", courseNote);
            refreshIntent.putExtra("instructorID", instructorID);
            Toast.makeText(EditCourse.this, "Course Updated", Toast.LENGTH_SHORT).show();
            startActivity(refreshIntent);
            finish();
            return true;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    // Refresh dates on input
    private void updateDate(TextView view) {
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        if (view == viewCourseStart) {
            viewCourseStart.setText(sdf.format(editStartCalendar.getTime()));
        } else if (view == viewCourseEnd) {
            viewCourseEnd.setText(sdf.format(editEndCalendar.getTime()));
        }
    }
    // Drop the keyboard when clicking out of fields
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
