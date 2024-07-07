package android.scheduler.johnsalazar.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Helper.MyReceiver;
import android.scheduler.johnsalazar.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AssessmentDetails extends AppCompatActivity {

    // Class Variables
    private Course currentCourse;
    private Assessment currentAssessment;
    private int courseID;
    private int assessmentID;
    private String courseTitle;
    private String aTitle;
    private String aType;
    private String aStart;
    private String aEnd;
    private TextView courseIDView;
    private TextView assessmentIDView;
    private TextView courseTitleView;
    private TextView titleView;
    private TextView typeView;
    private TextView startView;
    private TextView endView;
    private Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        setTitle("Assessment Details");

        // Hide the system navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Views
        assessmentIDView = findViewById(R.id.assessmentID);
        courseIDView = findViewById(R.id.courseID);
        courseTitleView = findViewById(R.id.courseTitle);
        titleView = findViewById(R.id.assessmentTitle);
        typeView = findViewById(R.id.assessmentType);
        startView = findViewById(R.id.assessmentStart);
        endView = findViewById(R.id.assessmentEnd);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Get Values for AssessmentDetails UI from adapter and repository.
        courseID = getIntent().getIntExtra("courseID", 0);
        if (courseID <= 0) {
            courseTitle = "No Course Associated";
        }
        else {
            currentCourse = repository.getThisCourse(courseID);
            courseTitle = currentCourse.getCourseTitle();
        }
        assessmentID = getIntent().getIntExtra("assessmentID", 0);
        aTitle = getIntent().getStringExtra("assessmentTitle");
        aType = getIntent().getStringExtra("assessmentType");
        aStart = getIntent().getStringExtra("assessmentStart");
        aEnd = getIntent().getStringExtra("assessmentEnd");

        // Fill AssessmentDetails values
        courseIDView.setText(String.valueOf(courseID));
        courseTitleView.setText(courseTitle);
        assessmentIDView.setText(String.valueOf(assessmentID));
        titleView.setText(aTitle);
        typeView.setText(aType);
        startView.setText(aStart);
        endView.setText(aEnd);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_details, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.appHome) {
            Intent intent = new Intent(AssessmentDetails.this, StudentList.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.assessmentEdit) {
            Intent editIntent = new Intent(AssessmentDetails.this, EditAssessment.class);
            editIntent.putExtra("assessmentID", assessmentID);
            editIntent.putExtra("courseID", courseID);
            startActivity(editIntent);
            finish();
            return true;        }
        if (item.getItemId() == R.id.assessmentDelete) {
            currentAssessment = repository.getThisAssessment(assessmentID);
            if (currentAssessment != null) {
                Course resumeCourse = repository.getThisCourse(courseID);
                Intent intent = new Intent(AssessmentDetails.this, CourseDetails.class);
                intent.putExtra("courseID", resumeCourse.getCourseID());
                intent.putExtra("termID", resumeCourse.getTermID());
                intent.putExtra("courseTitle", resumeCourse.getCourseTitle());
                intent.putExtra("courseStart", resumeCourse.getCourseStart());
                intent.putExtra("courseEnd", resumeCourse.getCourseEnd());
                intent.putExtra("courseStatus", resumeCourse.getCourseStatus());
                intent.putExtra("courseNote", resumeCourse.getCourseNote());
                intent.putExtra("instructorID", resumeCourse.getInstructorID());
                repository.delete(currentAssessment);
                startActivity(intent);
                finish();
                Toast.makeText(AssessmentDetails.this, "Assessment " + aTitle + " deleted.",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            else {
                Toast.makeText(AssessmentDetails.this, "Error with assessment",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }
        if (item.getItemId() == R.id.aStartAlert) {
            String dateFromScreen = aStart;
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
                Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("key", "Your assessment \"" + aTitle + "\" has started.");
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (item.getItemId() == R.id.aEndAlert) {
            String dateFromScreen = aEnd;
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
                Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("key", "Your assessment \"" + aTitle + "\" has ended.");
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
