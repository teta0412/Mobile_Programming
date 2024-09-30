package com.map.nguyenanhduc.bt1.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.map.nguyenanhduc.bt1.R;
import com.map.nguyenanhduc.bt1.dao.UserDAO;
import com.map.nguyenanhduc.bt1.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAct extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, nameEditText, emailEditText, telephoneNumEditText;
    private Spinner genderSpinner;
    private Button dobButton, addButton;
    private Date selectedDate;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        userDAO = new UserDAO(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        telephoneNumEditText = findViewById(R.id.telephoneNumEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        dobButton = findViewById(R.id.dobButton);
        addButton = findViewById(R.id.addButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        dobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, monthOfYear, dayOfMonth);
                        selectedDate = selectedCalendar.getTime();
                        updateDobButtonText();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateDobButtonText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dobButton.setText(sdf.format(selectedDate));
    }

    private void addUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String telephoneNum = telephoneNumEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || telephoneNum.isEmpty() || selectedDate == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(0, username, password, name, email, telephoneNum, gender, selectedDate);
        boolean success = userDAO.add(newUser);

        if (success) {
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show();
        }
    }
}