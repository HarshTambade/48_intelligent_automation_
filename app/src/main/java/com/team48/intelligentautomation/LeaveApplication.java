package com.team48.intelligentautomation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class LeaveApplication extends AppCompatActivity {

    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private EditText editTextReason;
    private EditText editTextLeaveType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);

        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        editTextReason = findViewById(R.id.editTextReason);
        editTextLeaveType = findViewById(R.id.editTextLeaveType);
        Button btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        Button btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        btnSelectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateCalendar, textViewStartDate);
            }
        });

        btnSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateCalendar, textViewEndDate);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String startDate = formatDate(startDateCalendar);
                String endDate = formatDate(endDateCalendar);
                String reason = editTextReason.getText().toString().trim();
                String leaveType = editTextLeaveType.getText().toString().trim();

                // Check if any of the fields are empty
                if (startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty() || leaveType.isEmpty()) {
                    Toast.makeText(LeaveApplication.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userMail = auth.getCurrentUser().getEmail().toString();
//                String userMail1 = userMail.replace(".", "");
                // Extract only digits after "_" and before "@" in userMail
                int underscoreIndex = userMail.indexOf("_");
                int atIndex = userMail.indexOf("@");
                if (underscoreIndex != -1 && atIndex != -1 && underscoreIndex < atIndex) {
                    // Extract the substring between "_" and "@"
                    userMail = userMail.substring(underscoreIndex + 1, atIndex);
                } else {
                    // Handle the case where "_" or "@" is not found
                    // You can either set userMail to an empty string or handle it as per your requirement
                    userMail = "404";
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LeaveApplication");
                LeaveHelper lh = new LeaveHelper(userMail,startDate,endDate,leaveType,reason);
                ref.child(userMail).setValue(lh);
                // Here, you can handle the submission of leave request
                // For demonstration, just displaying selected dates, reason, and leave type
                String message = "Start Date: " + startDate + ", End Date: " + endDate +
                        "\nReason: " + reason + "\nLeave Type: " + leaveType;
                Toast.makeText(LeaveApplication.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDatePickerDialog(final Calendar calendar, final TextView textView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplication.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        textView.setText(formatDate(calendar));
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private String formatDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);
    }
}
