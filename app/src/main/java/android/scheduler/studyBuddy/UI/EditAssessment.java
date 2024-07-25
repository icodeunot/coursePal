package android.scheduler.studyBuddy.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.studyBuddy.DB.Repository;
import android.scheduler.studyBuddy.Entity.Assessment;
import android.scheduler.studyBuddy.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditAssessment extends AppCompatActivity {

    // Class Variables
    private Assessment thisAssessment;
    private int courseID;
    private int assessmentID;
    private String assessmentTitle;
    private String typeString;
    private String startString;
    private String endString;
    private EditText titleTextView;
    private Spinner typeSpinner;
    private TextView startView;
    private TextView endView;
    DatePickerDialog.OnDateSetListener startListener;
    final Calendar editStartCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endListener;
    final Calendar editEndCalendar = Calendar.getInstance();

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        setTitle("Edit Assessment");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Repository and get objects
        repository = new Repository(getApplication());
        courseID = getIntent().getIntExtra("courseID", -1);
        assessmentID = getIntent().getIntExtra("assessmentID", -1);
        if (courseID != -1 && assessmentID != -1) {
            thisAssessment = repository.getThisAssessment(assessmentID);
        }

        // Initialize Views
        titleTextView = findViewById(R.id.editAssessmentTitle);
        typeSpinner = findViewById(R.id.assessmentTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        startView = findViewById(R.id.editAssessmentStart);
        endView = findViewById(R.id.editAssessmentEnd);

        // Get Values
        if (assessmentID != -1) {
            assessmentTitle = thisAssessment.getAssessmentTitle();
            typeString = thisAssessment.getAssessmentType();
            startString = thisAssessment.getStartDate();
            endString = thisAssessment.getEndDate();
            // Set Values
            titleTextView.setText(assessmentTitle);
            if (typeString != null) {
                int spinnerPosition = adapter.getPosition(typeString);
                typeSpinner.setSelection(spinnerPosition);
            }
            startView.setText(startString);
            endView.setText(endString);

            // Setup date formatting
            String formatDate = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
            String currentDate = sdf.format(new Date());

            // Create Listeners and assign them for Start and End dates
            startView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String info = currentDate.toString();
                    try {
                        editStartCalendar.setTime(sdf.parse(info));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new DatePickerDialog(EditAssessment.this, startListener, editStartCalendar.get(Calendar.YEAR),
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

                    updateDate(startView);
                }
            };
            endView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Date date;
                    String info = currentDate.toString();
                    try {
                        editEndCalendar.setTime(sdf.parse(info));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new DatePickerDialog(EditAssessment.this, endListener, editEndCalendar.get(Calendar.YEAR),
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

                    updateDate(endView);
                }
            };
        } else {
            Toast.makeText(EditAssessment.this, "Error getting assessment details.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_assessment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.editAssessmentSave) {
            updateAssessment();
        }
        if (item.getItemId() == R.id.editAssessmentCancel) {
            this.finish();
            return true;
        }
        return true;
    }

    // Update the course
    public boolean updateAssessment() {
        assessmentTitle = titleTextView.getText().toString();
        typeString = typeSpinner.getSelectedItem().toString();
        startString = startView.getText().toString();
        endString = endView.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            editStartCalendar.setTime(sdf.parse(startString));
            editEndCalendar.setTime(sdf.parse(endString));
        } catch (ParseException e) {
            Toast.makeText(this, "There was an error saving your Assessment. Please try again.",
                    Toast.LENGTH_LONG).show();
            finish();
            return false;
        }
        if (assessmentTitle.isEmpty() || startString.isEmpty() || endString.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields and try again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editStartCalendar.getTime().after(editEndCalendar.getTime())) {
            Toast.makeText(this, "Assessment cannot end before starting. Adjust dates and try" +
                    " again.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (typeSpinner.getSelectedItem().toString().equals("Choose Type")) {
            Toast.makeText(EditAssessment.this, "Please select an assessment type.", Toast.LENGTH_SHORT).show();
            typeSpinner.requestFocus();
            return false;
        }
        else {
            Assessment updatedAssessment;
            updatedAssessment = new Assessment(assessmentID, assessmentTitle, typeString, startString, endString, courseID);
            repository.update(updatedAssessment);
            // Send back to AssessmentDetails
            Intent refreshIntent= new Intent(EditAssessment.this, AssessmentDetails.class);
            refreshIntent.putExtra("courseID", courseID);
            refreshIntent.putExtra("assessmentID", assessmentID);
            refreshIntent.putExtra("assessmentTitle", assessmentTitle);
            refreshIntent.putExtra("assessmentStart", startString);
            refreshIntent.putExtra("assessmentEnd", endString);
            refreshIntent.putExtra("assessmentType", typeString);
            Toast.makeText(EditAssessment.this, "Assessment Updated", Toast.LENGTH_SHORT).show();
            startActivity(refreshIntent);
            finish();
            return true;
        }
    }

    // Refresh dates on input
    private void updateDate(TextView view) {
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        if (view == startView) {
            startView.setText(sdf.format(editStartCalendar.getTime()));
        } else if (view == endView) {
            endView.setText(sdf.format(editEndCalendar.getTime()));
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
}
