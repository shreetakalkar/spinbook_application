package com.example.spinbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbhandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "expense.db";

    public dbhandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String registerTableQuery = "CREATE TABLE register_tbl (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50), surname VARCHAR(50), username VARCHAR(50), password TEXT)";
        db.execSQL(registerTableQuery);

//        String expensesTableQuery = "CREATE TABLE expenses (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "expense_name TEXT, expense_cost REAL)";
//        db.execSQL(expensesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS register_tbl");
//        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }

    public String addrecord(String name, String surname, String username, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("surname", surname);
            cv.put("username", username);
            cv.put("password", password);

            long res = db.insert("register_tbl", null, cv);
            if (res == -1) {
                return "Failed to register. Try again later.";
            } else {
                return "Registered successfully.";
            }
        } catch (Exception e) {
            Log.e("DbHandler", "Error adding record: " + e.getMessage());
            return "Error: Unable to register.";
        }
    }
//    public String addexpense(String expenseName, double expenseCost) {
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues cv = new ContentValues();
//            cv.put("expense_name", expenseName);
//            cv.put("expense_cost", expenseCost);
//
//            long res = db.insert("expenses", null, cv);
//            if (res == -1) {
//                return "Failed to add expense. Try again later.";
//            } else {
//                return "Expense added successfully.";
//            }
//        } catch (Exception e) {
//            Log.e("DbHandler", "Error adding expense: " + e.getMessage());
//            return "Error: Unable to add expense.";
//        }
//    }

    public boolean checkRecord(String username, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] columns = {"id"};
            String selection = "username=? AND password=?";
            String[] selectionArgs = {username, password};
            Cursor cursor = db.query("register_tbl", columns, selection, selectionArgs, null, null, null);

            int count = cursor.getCount();
            cursor.close();

            return count > 0;
        } catch (Exception e) {
            Log.e("DbHandler", "Error checking record: " + e.getMessage());
            return false;
        }
    }



    public User getUserDetails(String username) {
        User user = null;

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] columns = {"name", "surname"};
            String selection = "username=?";
            String[] selectionArgs = {username};
            Cursor cursor = db.query("register_tbl", columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("name");
                int surnameIndex = cursor.getColumnIndex("surname");

                String name = cursor.isNull(nameIndex) ? "" : cursor.getString(nameIndex);
                String surname = cursor.isNull(surnameIndex) ? "" : cursor.getString(surnameIndex);

                user = new User(name, surname);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e("DbHandler", "Error getting user details: " + e.getMessage());
        }

        return user;
    }
}
