package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static exercise.android.reemh.todo_items.TaskStore_Application.readObjectFromFile;

public class MainActivity extends AppCompatActivity {
  private TextView tv;
  private RecyclerView rv;
  TaskStore_Application tasks_app;
  public MyAdapter adapter = null;
  BroadcastReceiver broadcastReceiverForchange;
  // for saving when app closes
  private SharedPreferences mPrefs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    tasks_app = ((TaskStore_Application) getApplicationContext());
//    mPrefs = this.getSharedPreferences("saved_text", MODE_PRIVATE);
    mPrefs = tasks_app.mprefs;

    // getting UI elements
    FloatingActionButton btn = findViewById(R.id.buttonCreateTodoItem);
    tv = findViewById(R.id.editTextInsertTask);
    rv = findViewById(R.id.recyclerTodoItemsList);

//    if (holder == null) {
//      holder = new TodoItemsHolderImpl();
//    }

    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String task_description = tv.getText().toString();
        if(!task_description.isEmpty()){
          tasks_app.tasks_info().addNewInProgressItem(task_description);
          tv.setText("");
          adapter.notify_adapter_data_changed();
        }
      }
    });

    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForchange = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("db_changed"))
          return;
        // db changed, refresh UI
        adapter.notify_adapter_data_changed();
        //save all the data to phone
        tasks_app.save_all(tv.getText().toString());

      }
    };
    this.registerReceiver(broadcastReceiverForchange, new IntentFilter("db_changed"));

    // set-up the adapter
    if(adapter == null) {
      adapter = new MyAdapter(this.getApplicationContext() ,rv, (TodoItemsHolder) new TodoItemsHolderImpl());
      rv.setItemAnimator(new DefaultItemAnimator());
      rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
      rv.setAdapter(adapter);
    }

  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);

    tasks_app.save_all(tv.getText().toString());
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    load_all();

  }

  @Override
  protected void onPause() {
    super.onPause();
    tasks_app.save_all(tv.getText().toString());
  }



  @Override
  protected void onResume() {
    super.onResume();
    load_all();
  }

  private void load_all(){
//    mPrefs = this.getPreferences(Context.MODE_PRIVATE);
    mPrefs = tasks_app.mprefs;
    String text = mPrefs.getString("input_string", "");
    tv.setText(text);
    // get the list
    ArrayList<TodoItem> items_loaded = (ArrayList<TodoItem>) readObjectFromFile(MainActivity.this,"list_saved" );
    // redefine holder (with the old list as input), set it to adapter
    TodoItemsHolder temp = new TodoItemsHolderImpl(items_loaded);
    adapter = new MyAdapter(this.getApplicationContext(), rv, (TodoItemsHolder)temp);
    rv.setAdapter(adapter);

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // remove ALL broadcast receivers we registered earlier in onCreate().
    //  to remove a registered receiver, call method `this.unregisterReceiver(<receiver-to-remove>)`
    this.unregisterReceiver(broadcastReceiverForchange);
  }
}
