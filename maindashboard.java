package com.example.spinbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class maindashboard extends AppCompatActivity {

    private EditText expenseNameEditText;
    private EditText expenseCostEditText;
    private EditText expname;
    private Button b1;
    private PieChart piechart; // Added PieChart member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindashboard2);

        piechart = findViewById(R.id.pieChart); // Initialize PieChart

        expenseNameEditText = findViewById(R.id.e2);
        expenseCostEditText = findViewById(R.id.e3);
        expname = findViewById(R.id.e1);
        TextView welcomeTextView = findViewById(R.id.t1);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        dbhandler db = new dbhandler(maindashboard.this);
        User user = db.getUserDetails(username);

        if (user != null) {
            String welcomeText = "Hello, " + user.getName() + " " + user.getSurname() + "!";
            welcomeTextView.setText(welcomeText);
        } else {
            Toast.makeText(this, "Error retrieving user details", Toast.LENGTH_SHORT).show();
        }
    }

    public void addExpense(View view) {
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        dbhandler db = new dbhandler(maindashboard.this);
        User user = db.getUserDetails(username);
        String name = expname.getText().toString().trim();
        String enteredName = expenseNameEditText.getText().toString().trim();
        String enteredCost = expenseCostEditText.getText().toString().trim();

        if (TextUtils.isEmpty(enteredName) || TextUtils.isEmpty(enteredCost)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        dashDb db1 = new dashDb(this);
        String res = db1.addExpense(enteredName, Double.parseDouble(enteredCost), name);

        Toast.makeText(this, res, Toast.LENGTH_LONG).show();

        // Update the PieChart after adding the expense
        updatePieChart();

        expname.setText("");
        expenseNameEditText.setText("");
        expenseCostEditText.setText("");
    }

    private void updatePieChart() {
        // Fetch expenses and update the PieChart
        dashDb db1 = new dashDb(this);
        List<ExpenseEntry> expenses = db1.getExpensesByName(expname.getText().toString().trim());

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (ExpenseEntry expense : expenses) {
            entries.add(new PieEntry((float) expense.getExpenseCost(), expense.getExpenseName()));
        }

        PieDataSet data = new PieDataSet(entries, "Subjects");
        data.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData piedata = new PieData(data);
z
        piechart.setData(piedata);
        piechart.getDescription().setEnabled(false);
        piechart.animateY(1000);
        piechart.invalidate();
    }
}
