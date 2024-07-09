package android.scheduler.johnsalazar.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.scheduler.johnsalazar.DB.Repository;
import android.scheduler.johnsalazar.Entity.Instructor;
import android.scheduler.johnsalazar.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String loginUN;
    private String loginPW;
    private EditText etUN;
    private EditText etPW;
    public static int numAlert;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        etUN = findViewById(R.id.loginUN);
        etPW = findViewById(R.id.loginPW);
        Button button=findViewById(R.id.button);

        // HIDE SYS NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);



        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // this deleted unassigned instructors.
//                repository = new Repository(getApplication());
//                for (Instructor i : repository.getmAllInstructors()) {
//                    repository.delete(i);
//                }
                loginUN = etUN.getText().toString();
                loginPW = etPW.getText().toString();
                if (loginUN.equals("admin") && loginPW.equals("Admin1")) {
                    Intent intent = new Intent(MainActivity.this, StudentList.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Incorrect login information. Please try again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}