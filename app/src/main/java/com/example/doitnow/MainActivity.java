package com.example.doitnow;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Main dashboard of the app showing the task list.
 * Handles task creation, deletion, and navigation to other sections.
 */
public class MainActivity extends AppCompatActivity {
    private List<Task> taskList = new ArrayList<>();
    private TaskAdapter adapter;
    private TextView tvSubtitle;
    private int selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize sample tasks if the list is empty
        if (taskList.isEmpty()) {
            taskList.add(new Task("Check current tasks", 9, 0, "Personal", true));
            taskList.add(new Task("Future planning", 12, 0, "Work", false));
        }

        tvSubtitle = findViewById(R.id.tvSubtitle);
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter();
        rv.setAdapter(adapter);
        
        refreshData();

        // Setup button click listeners
        findViewById(R.id.tvClear).setOnClickListener(v -> clearFinishedTasks());
        findViewById(R.id.fab).setOnClickListener(v -> showAddTaskDialog());

        // Setup bottom navigation
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_tasks);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, AccountActivity.class));
            } else if (id == R.id.nav_info) {
                startActivity(new Intent(this, InfoActivity.class));
            }
            return true;
        });
    }

    /**
     * Updates the subtitle showing remaining tasks.
     */
    private void updateSubtitle() {
        int count = 0;
        for (Task t : taskList) if (!t.isDone()) count++;
        tvSubtitle.setText("You have " + count + " tasks remaining for today.");
    }

    /**
     * Removes all tasks that are marked as done.
     */
    private void clearFinishedTasks() {
        Iterator<Task> it = taskList.iterator();
        while (it.hasNext()) if (it.next().isDone()) it.remove();
        refreshData();
    }
    
    /**
     * Refreshes the UI by updating the adapter and the subtitle.
     */
    public void refreshData() {
        adapter.setItems(taskList);
        updateSubtitle();
    }

    /**
     * Displays a dialog to add a new task with time and category selection.
     */
    private void showAddTaskDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();

        EditText etTask = v.findViewById(R.id.etNewTask);
        EditText etTime = v.findViewById(R.id.etTime);
        Spinner spinner = v.findViewById(R.id.spinnerCategory);
        RadioButton rbFocus = v.findViewById(R.id.rbFocus);

        // Initialize time to current system time
        Calendar c = Calendar.getInstance();
        selectedHour = c.get(Calendar.HOUR_OF_DAY);
        selectedMinute = c.get(Calendar.MINUTE);
        etTime.setText(formatTime(selectedHour, selectedMinute));

        // Show TimePickerDialog on time field click
        etTime.setOnClickListener(view -> new TimePickerDialog(this, (tp, h, m) -> {
            selectedHour = h; selectedMinute = m;
            etTime.setText(formatTime(h, m));
        }, selectedHour, selectedMinute, false).show());

        // OK button handles task creation
        v.findViewById(R.id.btnOk).setOnClickListener(view -> {
            String name = etTask.getText().toString().trim();
            if (!name.isEmpty()) {
                taskList.add(new Task(name, selectedHour, selectedMinute, 
                    spinner.getSelectedItem().toString(), rbFocus.isChecked()));
                refreshData();
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    /**
     * Helper to format time for display in the dialog.
     */
    private String formatTime(int h, int m) {
        int hour = h % 12; if (hour == 0) hour = 12;
        return String.format(Locale.getDefault(), "%d:%02d %s", hour, m, h < 12 ? "AM" : "PM");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
        // Ensure the correct bottom navigation item is selected
        ((BottomNavigationView)findViewById(R.id.bottomNav)).setSelectedItemId(R.id.nav_tasks);
    }
}