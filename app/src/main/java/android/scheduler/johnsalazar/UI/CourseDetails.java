package android.scheduler.johnsalazar.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Instructor;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.Helper.AssessmentAdapter;
import android.scheduler.johnsalazar.Helper.MyReceiver;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class CourseDetails extends AppCompatActivity {

    // Class Variables
    private Course currentCourse;
    private Term courseTerm;
    private Instructor courseInstructor;
    private int courseID;
    private int termID;
    private int instructorID;
    private String termTitle;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private String courseStatus;
    private String courseNote;
    private String instructorFirst;
    private String instructorLast;
    private String instructorPhone;
    private String instructorEmail;
    private TextView editTermTitle;
    private TextView editTermID;
    private TextView editCourseID;
    private TextView editInstructorFirst;
    private TextView editInstructorLast;
    private TextView editInstructorPhone;
    private TextView editInstructorEmail;
    private TextView editTitle;
    private TextView editStart;
    private TextView editEnd;
    private TextView editStatus;
    private TextView editNote;
    private RecyclerView assessmentRecycler;
    private AssessmentAdapter assessmentAdapter;
    private FloatingActionButton courseDetailsFAB;
    private Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        setTitle("Course Details");

        // Hide the system navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Views
        editCourseID = findViewById(R.id.courseID);
        editTermID = findViewById(R.id.termID);
        editTermTitle = findViewById(R.id.termTitle);
        editTitle = findViewById(R.id.courseTitle);
        editStart = findViewById(R.id.courseStart);
        editEnd = findViewById(R.id.courseEnd);
        editStatus = findViewById(R.id.courseStatus);
        editNote = findViewById(R.id.courseNote);
        editInstructorFirst = findViewById(R.id.instructorFirst);
        editInstructorLast = findViewById(R.id.instructorLast);
        editInstructorPhone = findViewById(R.id.instructorPhone);
        editInstructorEmail = findViewById(R.id.instructorEmail);
        assessmentRecycler = findViewById(R.id.courseAssessmentRecylcer);
        courseDetailsFAB = findViewById(R.id.courseDetailsFAB);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Get values for Course Details UI
        courseID = getIntent().getIntExtra("courseID", 0);
        termID = getIntent().getIntExtra("termID", 0);
        courseTerm = repository.getThisTerm(termID); // Get the term
        termTitle = courseTerm.getTermTitle();
        courseTitle = getIntent().getStringExtra("courseTitle");
        courseStart = getIntent().getStringExtra("courseStart");
        courseEnd = getIntent().getStringExtra("courseEnd");
        courseStatus = getIntent().getStringExtra("courseStatus");
        courseNote = getIntent().getStringExtra("courseNote");
        instructorID = getIntent().getIntExtra("instructorID", 0);
        if (instructorID <= 0) {
            instructorFirst = null;
            instructorLast = null;
            instructorPhone = null;
            instructorEmail = null;
        }
        else {
            courseInstructor = repository.getThisInstructor(instructorID); // Get the Instructor
            instructorFirst = courseInstructor.getInstructorFirstName();
            instructorLast = courseInstructor.getInstructorLastName();
            instructorPhone = courseInstructor.getInstructorPhone();
            instructorEmail = courseInstructor.getInstructorEmail();
        }

        // Fill CourseDetails values
        editCourseID.setText(String.valueOf(courseID));
        editTermID.setText(String.valueOf(termID));
        editTermTitle.setText(termTitle);
        editTitle.setText(courseTitle);
        editStart.setText(courseStart);
        editEnd.setText(courseEnd);
        editStatus.setText(courseStatus);
        editNote.setText(courseNote);
        editInstructorFirst.setText(instructorFirst);
        editInstructorLast.setText(instructorLast);
        editInstructorPhone.setText(instructorPhone);
        editInstructorEmail.setText(instructorEmail);

        // Setup RecyclerView and filter Assessments for current Course
        List<Assessment> filteredAssessments = new ArrayList<>();
        for (Assessment a : repository.getmAllAssessments()) {
            if (a.getCourseID() == courseID) filteredAssessments.add(a);
        }
        assessmentAdapter = new AssessmentAdapter(this);
        assessmentRecycler.setAdapter(assessmentAdapter);
        assessmentRecycler.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(filteredAssessments);

        // Course Details FAB - Used to add assessments through EditCourse
        courseDetailsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetails.this, AddAssessment.class);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.courseEdit) {
            Intent editIntent = new Intent(CourseDetails.this, EditCourse.class);
            editIntent.putExtra("courseID", courseID);
            startActivity(editIntent);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.courseDelete) {
            currentCourse = repository.getThisCourse(courseID);
            if (currentCourse != null) {
                Term resumeTerm = repository.getThisTerm(termID);
                Intent intent = new Intent(CourseDetails.this, TermDetails.class);
                intent.putExtra("termID", termID);
                intent.putExtra("termTitle", resumeTerm.getTermTitle());
                intent.putExtra("termStart", resumeTerm.getTermStart());
                intent.putExtra("termEnd", resumeTerm.getTermEnd());
                if (repository.getCourseAssessments(courseID) != null) {
                    repository.deleteCourseAssessments(courseID);
                    repository.delete(courseInstructor);
                    Toast.makeText(CourseDetails.this, courseTitle + ", and associated assessments and instructors " +
                                    "have been deleted.",
                            Toast.LENGTH_LONG).show();
                    repository.delete(currentCourse);

                }
                repository.delete(currentCourse);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                Toast.makeText(CourseDetails.this, "Error with course",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }
        if (item.getItemId() == R.id.courseNote) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId() == R.id.cStartAlert) {
            String dateFromScreen = courseStart;
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
                intent.putExtra("key", "Your course \"" + courseTitle + "\" has started."); // specific info about course and whether it is starting or ending
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (item.getItemId() == R.id.cEndAlert) {
            String dateFromScreen = courseEnd;
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
                intent.putExtra("key", "Your course \"" + courseTitle + "\" has ended.");
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (item.getItemId() == R.id.appHome) {
            Intent intent = new Intent(CourseDetails.this, TermList.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
