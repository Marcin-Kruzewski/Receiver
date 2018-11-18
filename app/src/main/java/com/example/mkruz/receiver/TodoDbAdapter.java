package com.example.mkruz.receiver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mkruz.receiver.TodoTask;

public class TodoDbAdapter {
    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static final String DEBUG_TAG = "SqLiteTodoManager";

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "database.db";
    private static final String DB_TODO_TABLE = "todo";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_DESCRIPTION = "description";
    public static final String DESCRIPTION_OPTIONS = "TEXT NOT NULL";
    public static final int DESCRIPTION_COLUMN = 1;
    public static final String KEY_COMPLETED = "completed";
    public static final String COMPLETED_OPTIONS = "INTEGER DEFAULT 0";
    public static final int COMPLETED_COLUMN = 3;
    public static final String KEY_QTY = "qty";
    public static final String QTY_OPTIONS = "INTEGER DEFAULT 0";
    public static final int QTY_COLUMN = 2;
    public static final String KEY_PRICE = "price";
    public static final String PRICE_OPTIONS = "DECIMAL DEFAULT 0";
    public static final int PRICE_COLUMN = 3;

    private static final String DB_CREATE_TODO_TABLE =
            "CREATE TABLE " + DB_TODO_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
                    KEY_COMPLETED + " " + COMPLETED_OPTIONS + ", " +
                    KEY_QTY + " " + QTY_OPTIONS + ", " +
                    KEY_PRICE + " " + PRICE_OPTIONS +
                    ");";
    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + DB_TODO_TABLE;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    public TodoDbAdapter(Context context) {
        this.context = context;
    }

    public TodoDbAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertTodo(String description, int qty, float price) {
        ContentValues newTodoValues = new ContentValues();
        newTodoValues.put(KEY_DESCRIPTION, description);
        newTodoValues.put(KEY_QTY, qty);
        newTodoValues.put(KEY_PRICE, price);
        return db.insert(DB_TODO_TABLE, null, newTodoValues);
    }

    public boolean updateTodo(TodoTask task) {
        long id = task.getId();
        String description = task.getDescription();
        int qty = task.getQty();
        float price = task.getPrice();
        boolean completed = task.isCompleted();
        return updateTodo(id, description, qty, price, completed);
    }

    public boolean updateTodo(long id, String description, int qty, float price, boolean completed) {
        String where = KEY_ID + "=" + id;
        int completedTask = completed ? 1 : 0;
        ContentValues updateTodoValues = new ContentValues();
        updateTodoValues.put(KEY_DESCRIPTION, description);
        updateTodoValues.put(KEY_QTY, qty);
        updateTodoValues.put(KEY_PRICE, price);
        updateTodoValues.put(KEY_COMPLETED, completedTask);
        return db.update(DB_TODO_TABLE, updateTodoValues, where, null) > 0;
    }

    public boolean deleteTodo(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_TODO_TABLE, where, null) > 0;
    }

    public Cursor getAllTodos() {
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_QTY, KEY_PRICE, KEY_COMPLETED};
        return db.query(DB_TODO_TABLE, columns, null, null, null, null, null);
    }

    public TodoTask getTodo(long id) {
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_QTY, KEY_PRICE, KEY_COMPLETED};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_TODO_TABLE, columns, where, null, null, null, null);
        TodoTask task = null;
        Log.i("getTodo", "id = " + id);
        if(cursor != null && cursor.moveToFirst()) {
            Log.i("getTodo", "not null");
            String description = cursor.getString(DESCRIPTION_COLUMN);
            boolean completed = cursor.getInt(COMPLETED_COLUMN) > 0 ? true : false;
            int qty = cursor.getInt(QTY_COLUMN);
            float price = cursor.getFloat(PRICE_COLUMN);
            task = new TodoTask(id, description, qty, price, completed);
        }
        return task;
    }
}
