package android.scheduler.johnsalazar.UI;

import static android.net.http.SslCertificate.restoreState;
import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.Helper.CourseAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.scheduler.johnsalazar.R;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddCourse extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_INSTRUCTOR = 420;
    int courseID;
    int termID;
    int instructorID;
    String titleString;
    String startString;
    String endString;
    String noteString;
    Button instructorButton;
    EditText courseTitle;
    Spinner courseStatusSpinner;
    String courseStatus;
    TextView courseStart;
    TextView courseEnd;
    EditText courseNote;
    CheckBox instructorAddedCheckBox;
    FloatingActionButton addCourseFAB;
    Repository repository;


    DatePickerDialog.OnDateSetListener startListener;
    final Calendar startCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endListener;
    final Calendar endCalendar = Calendar.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setTitle("Add New Course");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        repository = new Repository(getApplication());

        courseID = -1;
        termID = getIntent().getIntExtra("termID", -1);
        instructorID = getIntent().getIntExtra("instructorID", -1);
        instructorAddedCheckBox = findViewById(R.id.instructorAddedCheckBox);
        courseTitle = findViewById(R.id.addCourseTitle);
        courseStart = findViewById(R.id.addCourseStart);
        courseEnd = findViewById(R.id.addCourseEnd);
        courseNote = findViewById(R.id.addNoteText);

        // String info for date
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        String currentDate = sdf.format(new Date());
        // Start Details
        courseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = currentDate.toString();
                try {
                    startCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddCourse.this, startListener, startCalendar.get(Calendar.YEAR),
                        startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDate(courseStart);
            }
        };
        // End Details
        courseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = currentDate.toString();
                try {
                    endCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddCourse.this, endListener, endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDate(courseEnd);
            }
        };
        // Status Spinner
        courseStatusSpinner = findViewById(R.id.courseStatusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatusSpinner.setAdapter(adapter);

        // Instructor Button and details
        instructorButton = findViewById(R.id.instructorButton);
        instructorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this, AddInstructor.class);
                //intent.putExtra("test", "Information sent");
                startActivityForResult(intent, REQUEST_CODE_ADD_INSTRUCTOR);
            }
        });

        // pressing Enter leaves EditText
        courseTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(courseTitle.getWindowToken(), 0);
                    hideKeyboard();
                    courseTitle.clearFocus();
                    return true;
                }
                return false;
            }
        });
        courseNote.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(courseNote.getWindowToken(), 0);
                    hideKeyboard();
                    courseNote.clearFocus();
                    return true;
                }
                return false;
            }
        });
        // Scroll back to beginning of note
        courseNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    courseNote.scrollTo(0, 0);
                    hideKeyboard();
                }
            }
        });
        courseTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    courseTitle.scrollTo(0, 0);
                    hideKeyboard();
                }
            }
        });
        // Add Assessment Float Button
        addCourseFAB = findViewById(R.id.addCourseFAB);
        addCourseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveCourse() == true) {
                    Toast.makeText(AddCourse.this, "Course saved. View in Term Details or Course List if canceled.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddCourse.this, AddAssessment.class);
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AddCourse.this, "Please enter course details for the assessment to be assigned.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Assure data is preserved in previous screen
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.courseSave) {
            saveCourse();
        }
        if (item.getItemId() == R.id.courseCancel) {
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
    // Check the box when instructor is saved. Collect the Instructor ID.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_INSTRUCTOR && resultCode == RESULT_OK) {
            if (data != null) {
                instructorID = data.getIntExtra("instructorID", -1);
            }
            instructorAddedCheckBox.setChecked(true);
            Toast.makeText(this, "Instructor Added. View in Course Details", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST_CODE_ADD_INSTRUCTOR && resultCode == RESULT_CANCELED) {
            instructorAddedCheckBox.setChecked(false);
        }
    }
    private boolean saveCourse() {
        titleString = courseTitle.getText().toString();
        startString = courseStart.getText().toString();
        endString = courseEnd.getText().toString();
        courseStatus = courseStatusSpinner.getSelectedItem().toString();
        noteString = courseNote.getText().toString();
        if (titleString.isEmpty() || startString.isEmpty() || endString.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields and try again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (startCalendar.getTime().after(endCalendar.getTime())) {
            Toast.makeText(this, "Course cannot end before starting. Adjust dates and try" +
                    " again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (courseStatusSpinner.getSelectedItem().toString().equals("Choose Status")) {
            Toast.makeText(AddCourse.this, "Please select a course status.", Toast.LENGTH_SHORT).show();
            courseStatusSpinner.requestFocus();
            return false;
        }
        if (!instructorAddedCheckBox.isChecked()) {
            Toast.makeText(AddCourse.this, "Please select a course Instructor.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Course course;
            if (courseID == -1) {
                if (repository.getmAllCourses().size() == 0) {
                    courseID = 1;
                } else {
                    courseID = repository.getmAllCourses().get(repository.getmAllCourses().size() - 1).getCourseID() + 1;
                }
            }
            // Handling Selected details from TermAdd
            Term resumeTerm = repository.getThisTerm(termID);
            Intent intent = new Intent(AddCourse.this, TermDetails.class);
            intent.putExtra("termID", termID);
            intent.putExtra("termTitle", resumeTerm.getTermTitle());
            intent.putExtra("termStart", resumeTerm.getTermStart());
            intent.putExtra("termEnd", resumeTerm.getTermEnd());
            course = new Course(courseID, titleString, startString, endString,
                    courseStatus, noteString, termID, instructorID);
            repository.insert(course);
            startActivity(intent);
            finish();
            return true;
        }
    }
    private void updateDate(TextView view) {
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        if (view == courseStart) {
            courseStart.setText(sdf.format(startCalendar.getTime()));
        } else if (view == courseEnd) {
            courseEnd.setText(sdf.format(endCalendar.getTime()));
        }
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}