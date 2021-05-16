package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity {
  private TextView tv;
  private RecyclerView rv;
  public TodoItemsHolder holder = null;
  public MyAdapter adapter = null;
  // for saving when app closes
  private SharedPreferences mPrefs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mPrefs = this.getSharedPreferences("saved_text", MODE_PRIVATE);

    // getting UI elements
    FloatingActionButton btn = findViewById(R.id.buttonCreateTodoItem);
    tv = findViewById(R.id.editTextInsertTask);
    rv = findViewById(R.id.recyclerTodoItemsList);

    if (holder == null) {
      holder = new TodoItemsHolderImpl();
    }

    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String task_description = tv.getText().toString();
        if(!task_description.isEmpty()){
          holder.addNewInProgressItem(task_description);
          tv.setText("");
          adapter.notifyDataSetChanged();
//          Toast.makeText(MainActivity.this, " " +adapter.getItemCount() ,Toast.LENGTH_LONG).show();
        }
      }
    });

    adapter = new MyAdapter(rv, (TodoItemsHolder)holder);
    rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    rv.setItemAnimator(new DefaultItemAnimator());
    rv.setAdapter(adapter);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putString("input_string", tv.getText().toString());
    outState.putSerializable("list_of_items", (Serializable) holder.getCurrentItems());
    super.onSaveInstanceState(outState);

  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    tv.setText(savedInstanceState.getString("input_string"));
    // redefine holder (with the old list as input), set it to adapter
    holder = new TodoItemsHolderImpl((List<TodoItem>) savedInstanceState.getSerializable("list_of_items"));
    adapter = new MyAdapter(rv, (TodoItemsHolder)holder);
    rv.setAdapter(adapter);
  }

  @Override
  protected void onPause() {
    super.onPause();

    SharedPreferences.Editor ed = mPrefs.edit();
    ed.putString("input_string", tv.getText().toString());
    witeObjectToFile(MainActivity.this, holder.getCurrentItems(),"list_saved");
    // save the list
    ed.apply();
  }

  @Override
  protected void onResume() {
    super.onResume();

    mPrefs = this.getPreferences(Context.MODE_PRIVATE);
    String text = mPrefs.getString("input_string", "");
    tv.setText(text);
    // get the list
    ArrayList<TodoItem> items_loaded = (ArrayList<TodoItem>) readObjectFromFile(MainActivity.this,"list_saved" );
    // redefine holder (with the old list as input), set it to adapter
    holder = new TodoItemsHolderImpl(items_loaded);
    adapter = new MyAdapter(rv, (TodoItemsHolder)holder);
    rv.setAdapter(adapter);
  }


  /**
   *
   * @param context
   * @param object
   * @param filename
   */
  public static void witeObjectToFile(Context context, Object object, String filename) {

    ObjectOutputStream objectOut = null;
    try {

      FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
      objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(object);
      fileOut.getFD().sync();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (objectOut != null) {
        try {
          objectOut.close();
        } catch (IOException e) {
          // do nowt
        }
      }
    }
  }


  /**
   *
   * @param context
   * @param filename
   * @return
   */
  public static Object readObjectFromFile(Context context, String filename) {

    ObjectInputStream objectIn = null;
    Object object = null;
    try {

      FileInputStream fileIn = context.getApplicationContext().openFileInput(filename);
      objectIn = new ObjectInputStream(fileIn);
      object = objectIn.readObject();

    } catch (FileNotFoundException e) {
      // Do nothing
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      if (objectIn != null) {
        try {
          objectIn.close();
        } catch (IOException e) {
          // do nowt
        }
      }
    }

    return object;
  }
}
