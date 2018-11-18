package com.example.mkruz.receiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mkruz.receiver.TodoTask;
import com.example.mkruz.receiver.R;

public class ListActivity extends AppCompatActivity {

    long id;
    EditText item, qty, price;
    String itemValue = "";
    String qtyValue = "";
    String priceValue = "";
    TodoDbAdapter db;
    TodoTask editedItem;
    private final int MEMORY_ACCESS = 5;
    private final long NON_EXISTING_ID = -1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){}
        else{
            ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MEMORY_ACCESS);
        }
        try {
            db = new TodoDbAdapter(getApplicationContext());
            db.open();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        item = findViewById(R.id.item);
        qty = findViewById(R.id.qty);
        price = findViewById(R.id.price);
        id = getIntent().getLongExtra("id", NON_EXISTING_ID);
        if (id != NON_EXISTING_ID && editedItem == null){
            editedItem = db.getTodo(id);
            Log.i("onCreate", "6");
            Toast.makeText(getApplicationContext(), editedItem.toString(), Toast.LENGTH_LONG).show();
            Log.i("onCreate", "item = " + editedItem.getDescription());
            item.setText(editedItem.getDescription());
            Log.i("onCreate", "7");
            qty.setText(String.valueOf(editedItem.getQty()));
            Log.i("onCreate", "8");
            price.setText(String.valueOf(editedItem.getPrice()));
            Log.i("onCreate", "9");
            FloatingActionButton fab = findViewById(R.id.floatingActionButton1);
            Log.i("onCreate", "10");
            fab.setVisibility(View.VISIBLE);
        }else{
            item.setText(itemValue);
            qty.setText(qtyValue);
            price.setText(priceValue);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case(MEMORY_ACCESS):
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else{

                    Toast.makeText(getApplicationContext(), "Memory write permission missing!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onPause() {
        itemValue = item.getText().toString();
        qtyValue = qty.getText().toString();
        priceValue = price.getText().toString();
        super.onPause();
    }

    public void onSaveBtn(View view) {
        itemValue = item.getText().toString();
        qtyValue = qty.getText().toString();
        priceValue = price.getText().toString();
        if (!itemValue.equals("")) {
            if (qtyValue.equals("")) {
                qtyValue = "1";
            }
            if (priceValue.equals("")) {
                priceValue = "0";
            }
            int qtyInt = Integer.parseInt(qtyValue);
            float priceFloat = Float.parseFloat(priceValue);
            if (id == NON_EXISTING_ID){
                db.insertTodo(itemValue, qtyInt, priceFloat);
                Intent intent1 = new Intent();
                intent1.setAction("pl.edu.pja.smb.MY_BROADCAST_TEST");
                intent1.putExtra("id", id);
                sendBroadcast(intent1, "pl.edu.pja.smb.MY_PERMISSION");
                finish();
            }else{
                db.updateTodo(id, itemValue, qtyInt, priceFloat, false);
                finish();
            }
        }else{
            Snackbar.make(view, "Invalid data!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void onDeleteButton(View view){
        db.deleteTodo(id);
        finish();
    }
}
