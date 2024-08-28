package com.example.spinbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;
import java.util.List;
import java.util.ArrayList;

public class dashDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "mainexp.db";
    private static final int DB_VERSION = 1;

    public dashDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String expensesTableQuery = "CREATE TABLE dataexp (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "expense_name TEXT, expense_cost REAL, name TEXT)";
        db.execSQL(expensesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dataexp");
        onCreate(db);
    }

    public String addExpense(String expenseName, double expenseCost, String sname) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("expense_name", expenseName);
            cv.put("expense_cost", expenseCost);
            cv.put("name", sname);

            long res = db.insert("dataexp", null, cv);
            if (res == -1) {
                return "Failed to add expense. Try again later.";
            } else {
                return "Expense added successfully.";
            }
        } catch (Exception e) {
            Log.e("DashDb", "Error adding expense: " + e.getMessage());
            return "Error: Unable to add expense.";
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<ExpenseEntry> getExpensesByName(String sname) {
        List<ExpenseEntry> expenses = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String[] columns = {"expense_name", "expense_cost"};
            String selection = "name=?";
            String[] selectionArgs = {sname};

            cursor = db.query("dataexp", columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String expenseName = cursor.getString(cursor.getColumnIndex("expense_name"));
                    double expenseCost = cursor.getDouble(cursor.getColumnIndex("expense_cost"));

                    ExpenseEntry expense = new ExpenseEntry(expenseName, expenseCost);
                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DashDb", "Error fetching expenses: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return expenses;
    }
}
