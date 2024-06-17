package android.scheduler.johnsalazar.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.R;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTerm extends AppCompatActivity {

    // Class Variables
    private int termID;
    private String termTitle;
    private String termStart;
    private String termEnd;
    private EditText editTitle;
    private TextView editStart;
    private TextView editEnd;
    DatePickerDialog.OnDateSetListener startListener;
    final Calendar startCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endListener;
    final Calendar endCalendar = Calendar.getInstance();

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        setTitle("Edit Term");

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize Views
        editTitle = findViewById(R.id.editTermTitle);
        editStart = findViewById(R.id.editTermStart);
        editEnd = findViewById(R.id.editTermEnd);

        // Get and fill values from TermDetails
        termID = getIntent().getIntExtra("termID", -1);
        termTitle = getIntent().getStringExtra("termTitle");
        termStart = getIntent().getStringExtra("termStart");
        termEnd = getIntent().getStringExtra("termEnd");
        editTitle.setText(termTitle);
        editStart.setText(termStart);
        editEnd.setText(termEnd);
        // Setting hints for blank, data but this should never happen based on Save validations.
        if (termTitle.isEmpty()) {
            editTitle.setHint("Enter Term Title");
        }
        if (termStart.isEmpty()) {
            editStart.setHint("Select Start Date");
        }
        if (termEnd.isEmpty()) {
            editEnd.setHint("Select End Date");
        }

        // Initialize Repository
        repository = new Repository(getApplication());

        // Setup date formatting
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        String currentDate = sdf.format(new Date());

        // Create Listeners and assign them for Start and End dates
        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = currentDate.toString();
                try {
                    startCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(EditTerm.this, startListener, startCalendar.get(Calendar.YEAR),
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
//                Date date;
                String info = currentDate.toString();
                try {
                    endCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(EditTerm.this, endListener, endCalendar.get(Calendar.YEAR),
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

        //Other Methods for design and functionality
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                    editTitle.clearFocus();
                }
            }
        });
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_term, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Save the updated term
        if (item.getItemId() == R.id.editTermSave) {
            // Set String values
            termTitle = editTitle.getText().toString();
            termStart = editStart.getText().toString();
            termEnd = editEnd.getText().toString();
            // Parse and set startCalendar and endCalendar
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                startCalendar.setTime(sdf.parse(termStart));
                endCalendar.setTime(sdf.parse(termEnd));
            } catch (ParseException e) {
                Toast.makeText(this, "There was an error saving your term. Please try again.",
                                Toast.LENGTH_LONG).show();
                finish();
                return false;
            }
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
            // Catch a bad term. Should not have a -1 termID at this point.
            if(termID == -1) {
                Toast.makeText(this, "There was an error loading your term. Please go back to the term list" +
                               " and try again.", Toast.LENGTH_LONG).show();
                finish();
                return false;
            }
            // Clears all validation, update the table.
            else {
                term = new Term(termID, termTitle, termStart, termEnd);
                repository.update(term);
            }
            // Send back to TermDetails to see results
            Intent resultIntent = new Intent(EditTerm.this, TermDetails.class);
            resultIntent.putExtra("termID", termID);
            resultIntent.putExtra("termTitle", termTitle);
            resultIntent.putExtra("termStart", termStart);
            resultIntent.putExtra("termEnd", termEnd);
            startActivity(resultIntent);
            finish();
        }
        if (item.getItemId() == R.id.editTermCancel) {
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
        termTitle = editTitle.getText().toString();
        termStart = editStart.getText().toString();
        termEnd = editStart.getText().toString();
        outState.putInt("termID", termID);
        outState.putString("termTitle", termTitle);
        outState.putString("termStart", termStart);
        outState.putString("termEnd", termEnd);
    }
    // Update the temporarily saved inputs
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
    // Refresh dates on input
    private void updateDate(TextView view) {
        String formatDate = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        if (view == editStart) {
            editStart.setText(sdf.format(startCalendar.getTime()));
        } else if (view == editEnd) {
            editEnd.setText(sdf.format(endCalendar.getTime()));
        }
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