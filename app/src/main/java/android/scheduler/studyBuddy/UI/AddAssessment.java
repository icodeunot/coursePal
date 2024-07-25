package android.scheduler.studyBuddy.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.studyBuddy.DB.Repository;
import android.scheduler.studyBuddy.Entity.Assessment;
import android.scheduler.studyBuddy.Entity.Course;
import android.scheduler.studyBuddy.Entity.Term;
import android.scheduler.studyBuddy.R;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAssessment extends AppCompatActivity {

    int courseID;
    int asmntID;
    int termID;
    int instructorID;
    String asmntTitle;
    String asmntStart;
    String asmntEnd;
    String asmntType;
    EditText editATitle;
    TextView editAStart;
    TextView editAEnd;
    Spinner asmntTypeSpinner;
    DatePickerDialog.OnDateSetListener aStartListener;
    final Calendar aStartCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener aEndListener;
    final Calendar aEndCalendar = Calendar.getInstance();
    Repository repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        setTitle("Add New Assessment");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        repository = new Repository(getApplication());

        asmntID = -1; // TODO: THIS TOO
        courseID = getIntent().getIntExtra("courseID", -1);
        editATitle = findViewById(R.id.addAssessmentTitle);
        editAStart = findViewById(R.id.addAssessmentStart);
        editAEnd = findViewById(R.id.addAssessmentEnd);
        // Type Spinner
        asmntTypeSpinner = findViewById(R.id.assessmentTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asmntTypeSpinner.setAdapter(adapter);
        // String info for date methods
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        String currentDate = sdf.format(new Date());
        // Start Details
        editAStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = currentDate.toString();
                try {
                    aStartCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddAssessment.this, aStartListener, aStartCalendar.get(Calendar.YEAR),
                                     aStartCalendar.get(Calendar.MONTH), aStartCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        aStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                aStartCalendar.set(Calendar.YEAR, year);
                aStartCalendar.set(Calendar.MONTH, month);
                aStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDate(editAStart);
            }
        };
        // End Details
        editAEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = currentDate.toString();
                try {
                    aEndCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddAssessment.this, aEndListener, aEndCalendar.get(Calendar.YEAR),
                        aEndCalendar.get(Calendar.MONTH), aEndCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        aEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                aEndCalendar.set(Calendar.YEAR, year);
                aEndCalendar.set(Calendar.MONTH, month);
                aEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDate(editAEnd);
            }
        };

        // pressing enter leaves EditText
        editATitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    editATitle.scrollTo(0, 0);
                    hideKeyboard();
                }
            }
        });
        editATitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editATitle.getWindowToken(), 0);
                    hideKeyboard();
                    editATitle.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_assessment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Assure data is preserved in previous screen
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.assessmentSave) {
            assessmentSave();
            return true;
        }

        if (item.getItemId() == R.id.assessmentCancel) {
            this.finish();
            return true;
        }
        return true;
    }
    private void updateDate(TextView view) {
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        if (view == editAStart) {
            editAStart.setText(sdf.format(aStartCalendar.getTime()));
        } else if (view == editAEnd) {
            editAEnd.setText(sdf.format(aEndCalendar.getTime()));
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
    private boolean assessmentSave() {
        asmntTitle = editATitle.getText().toString();
        asmntStart = editAStart.getText().toString();
        asmntEnd = editAEnd.getText().toString();
        asmntType = asmntTypeSpinner.getSelectedItem().toString();
        if (asmntTitle.isEmpty() || asmntStart.isEmpty() || asmntEnd.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields and try again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (aStartCalendar.getTime().after(aEndCalendar.getTime())) {
            Toast.makeText(this, "Assessment cannot end before starting. Adjust dates and try" +
                    " again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (asmntTypeSpinner.getSelectedItem().toString().equals("Choose Type")) {
            Toast.makeText(AddAssessment.this, "Please select an assessment type.", Toast.LENGTH_SHORT).show();
            asmntTypeSpinner.requestFocus();
            return false;
        }
        else {
            Assessment assessment;
            if (asmntID == -1) {
                if (repository.getmAllAssessments().size() == 0) {
                    asmntID = 1;
                } else {
                    asmntID = repository.getmAllAssessments().get(repository.getmAllAssessments().size() - 1).getAssessmentID() + 1;
                }
            }
            // Handling Selected details from CourseAdd
            Course resumeCourse = repository.getThisCourse(courseID);
            termID = resumeCourse.getTermID();
            instructorID = resumeCourse.getInstructorID();
            Term forTermTitle = repository.getThisTerm(termID);
            assessment = new Assessment(asmntID, asmntTitle, asmntType, asmntStart, asmntEnd, courseID);
            repository.insert(assessment);
            Intent intent = new Intent(AddAssessment.this, CourseDetails.class);
            intent.putExtra("courseID", courseID);
            intent.putExtra("termID", termID);
            intent.putExtra("instructorID", instructorID);
//            intent.putExtra("termTitle", forTermTitle.getTermTitle());     // Taken out, CourseDetails gets this on its own.
            intent.putExtra("courseTitle", resumeCourse.getCourseTitle());
            intent.putExtra("courseStart", resumeCourse.getCourseStart());
            intent.putExtra("courseEnd", resumeCourse.getCourseEnd());
            intent.putExtra("courseStatus", resumeCourse.getCourseStatus());
            intent.putExtra("courseNote", resumeCourse.getCourseNote());
            startActivity(intent);
            finish();
            return true;
        }
    }
}
