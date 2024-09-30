package com.map.nguyenanhduc.bt1.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.map.nguyenanhduc.bt1.R;
import com.map.nguyenanhduc.bt1.dao.UserDAO;
import com.map.nguyenanhduc.bt1.model.User;

import java.util.ArrayList;

public class SearchAct extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userDAO = new UserDAO(this);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        userRecyclerView = findViewById(R.id.userRecyclerView);

        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, new ArrayList<>(), new UserAdapter.OnUserClickListener() {
            @Override
            public void onEditClick(User user) {
                Intent intent = new Intent(SearchAct.this, EditAct.class);
                intent.putExtra("USER_ID", user.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(User user) {
                showDeleteConfirmationDialog(user);
            }
        });
        userRecyclerView.setAdapter(userAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchEditText.getText().toString();
                ArrayList<User> searchResults = userDAO.search(keyword);
                userAdapter.updateUsers(searchResults);
            }
        });
    }

    private void showDeleteConfirmationDialog(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean success = userDAO.delete(user.getId());
                    if (success) {
                        Toast.makeText(SearchAct.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                        refreshUserList();
                    } else {
                        Toast.makeText(SearchAct.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUserList();
    }

    private void refreshUserList() {
        String keyword = searchEditText.getText().toString();
        ArrayList<User> searchResults = userDAO.search(keyword);
        userAdapter.updateUsers(searchResults);
    }
}