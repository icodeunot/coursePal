package android.scheduler.johnsalazar.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.Helper.CourseAdapter;
import android.scheduler.johnsalazar.R;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddTerm extends AppCompatActivity {

    // Class Variables
    int termID;
    int studentID;
    String termTitle;
    String termStart;
    String termEnd;
    EditText editTitle;
    TextView editStart;
    TextView editEnd;
    Repository repository;
    FloatingActionButton floatButton;
    DatePickerDialog.OnDateSetListener startListener;
    final Calendar startCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endListener;
    final Calendar endCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        setTitle("Add New Term");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Repository
        repository = new Repository(getApplication());

        // Assign termID to -1 for creation and initialize views
        termID = -1;
        studentID = getIntent().getIntExtra("studentID", -1);
        editTitle = findViewById(R.id.addTermTitle);
        editStart = findViewById(R.id.addTermStart);
        editEnd = findViewById(R.id.addTermEnd);
        floatButton = findViewById(R.id.addTermFAB);


        // Setup date format
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        String currentDate = sdf.format(new Date());

        // Create listeners and assign them for Start and End dates
        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = currentDate.toString();
                try {
                    startCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddTerm.this, startListener, startCalendar.get(Calendar.YEAR),
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

                updateDate(editStart);
            }
        };
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = currentDate.toString();
                try {
                    endCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddTerm.this, endListener, endCalendar.get(Calendar.YEAR),
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

                updateDate(editEnd);
            }
        };

        // Setup FAB
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termTitle = editTitle.getText().toString();
                termStart = editStart.getText().toString();
                termEnd = editEnd.getText().toString();

                if (termTitle.isEmpty() || termStart.isEmpty() || termEnd.isEmpty()) {
                    Toast.makeText(AddTerm.this, "Please add new term details to save before adding course.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (startCalendar.getTime().after(endCalendar.getTime())) {
                    Toast.makeText(AddTerm.this, "Term cannot end before starting. Adjust dates and try" +
                            " again.", Toast.LENGTH_LONG).show();
                    return;
                }

                Term term;
                if(termID == -1) {
                    if(repository.getmAllTerms().size() == 0) {
                        termID = 1;
                    }
                    else {
                        termID = repository.getmAllTerms().get(repository.getmAllTerms().size() - 1).getTermID() + 1;
                    }
                    term = new Term(termID, studentID, termTitle, termStart, termEnd);
                    repository.insert(term);
                    Toast.makeText(AddTerm.this, "Term has been saved. View in Term List if you cancel.", Toast.LENGTH_LONG).show();
                }
                else {
                    term = new Term(termID, studentID, termTitle, termStart, termEnd);
                    repository.update(term);
                    Toast.makeText(AddTerm.this, "Term has been updated. View in Term List if you cancel.", Toast.LENGTH_LONG).show();

                }

                Intent intent = new Intent(AddTerm.this, AddCourse.class);
                intent.putExtra("termID", termID);
                startActivity(intent);
            }
        });
        //Other Methods for design
        editTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTitle.getWindowToken(), 0);
                    hideKeyboard();
                    editTitle.clearFocus();
                    return true;
                }
                return false;
            }
        });
        editTitle.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard();
                editTitle.clearFocus();
                return true;
            }
            return false;
        });
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                    editTitle.clearFocus();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_term, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addTermSave) {
            // Set String values
            termTitle = editTitle.getText().toString();
            termStart = editStart.getText().toString();
            termEnd = editEnd.getText().toString();
            // Validations (empty fields, start/end)
            if (termTitle.isEmpty() || termStart.isEmpty() || termEnd.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields and try again.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (startCalendar.getTime().after(endCalendar.getTime())) {
                Toast.makeText(this, "Term cannot end before starting. Adjust dates and try" +
                                " again.", Toast.LENGTH_LONG).show();
                return false;
            }

            Term term;
            if(termID == -1) {
                if(repository.getmAllTerms().size() == 0) {
                    termID = 1;
                }
                else {
                    termID = repository.getmAllTerms().get(repository.getmAllTerms().size() - 1).getTermID() + 1;
                }
                term = new Term(termID, studentID, termTitle, termStart, termEnd);
                repository.insert(term);
                this.finish();
            }
            else {
                term = new Term(termID, studentID, termTitle, termStart, termEnd);
                repository.update(term);
                this.finish();
            }
        }
        if (item.getItemId() == R.id.addTermCancel) {
            this.finish();
            Intent intent = new Intent(AddTerm.this, TermList.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(AddTerm.this, TermList.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        termTitle = editTitle.getText().toString();
        termStart = editStart.getText().toString();
        termEnd = editStart.getText().toString();
        outState.putInt("termID", termID);
        outState.putString("termTitle", termTitle);
        outState.putString("termStart", termStart);
        outState.putString("termEnd", termEnd);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        termID = savedInstanceState.getInt("termID");
        termTitle = savedInstanceState.getString("termTitle");
        termStart = savedInstanceState.getString("termStart");
        termEnd = savedInstanceState.getString("termEnd");
        editTitle.setText(termTitle);
        editStart.setText(termStart);
        editEnd.setText(termEnd);
    }
    private void updateDate(TextView view) {
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        if (view == editStart) {
            editStart.setText(sdf.format(startCalendar.getTime()));
        } else if (view == editEnd) {
            editEnd.setText(sdf.format(endCalendar.getTime()));
        }
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
