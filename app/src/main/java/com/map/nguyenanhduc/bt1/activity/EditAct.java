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

public class EditAct extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, nameEditText, emailEditText, telephoneNumEditText;
    private Spinner genderSpinner;
    private Button dobButton, updateButton;
    private Date selectedDate;
    private UserDAO userDAO;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        userDAO = new UserDAO(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        telephoneNumEditText = findViewById(R.id.telephoneNumEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        dobButton = findViewById(R.id.dobButton);
        updateButton = findViewById(R.id.updateButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            currentUser = userDAO.getUserById(userId);
            if (currentUser != null) {
                populateUserData();
            }
        }

        dobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void populateUserData() {
        usernameEditText.setText(currentUser.getUsername());
        passwordEditText.setText(currentUser.getPassword());
        nameEditText.setText(currentUser.getName());
        emailEditText.setText(currentUser.getEmail());
        telephoneNumEditText.setText(currentUser.getTelephoneNum());

        int genderPosition = ((ArrayAdapter) genderSpinner.getAdapter()).getPosition(currentUser.getGender());
        genderSpinner.setSelection(genderPosition);

        selectedDate = currentUser.getDob();
        updateDobButtonText();
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        c.setTime(selectedDate != null ? selectedDate : new Date());
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

    private void updateUser() {
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

        currentUser.setUsername(username);
        currentUser.setPassword(password);
        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setTelephoneNum(telephoneNum);
        currentUser.setGender(gender);
        currentUser.setDob(selectedDate);

        boolean success = userDAO.edit(currentUser);

        if (success) {
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show();
        }
    }
}

