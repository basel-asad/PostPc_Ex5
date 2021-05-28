package exercise.android.reemh.todo_items;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskStore_Application extends Application {

    private TasksInfoStore tasks_info;
    public TodoItem item_being_edited = null;
    public SharedPreferences mprefs;

    @Override
    public void onCreate() {
        super.onCreate();

        mprefs = this.getSharedPreferences("saved_text", MODE_PRIVATE);

        tasks_info = new TasksInfoStore(this);

    }

    public TasksInfoStore tasks_info() {
        return this.tasks_info;
    }

    public void set_tasks_info(TodoItemsHolderImpl tasks) {
//        if(this.tasks_info)
        this.tasks_info.tasks_list.items.clear(); // empty
        this.tasks_info.add_bulk_Items((ArrayList<TodoItem>) tasks.items);
    }

    public void add_tasks_info(TodoItemsHolderImpl tasks) {
        this.tasks_info.add_bulk_Items((ArrayList<TodoItem>) tasks.items);
    }

    public void save_all(String newTaskString) {
        SharedPreferences.Editor ed = mprefs.edit();
        ed.putString("input_string", newTaskString); // the editiable string in main screen
        witeObjectToFile(this.getApplicationContext(), this.tasks_info().getCurrentItems(), "list_saved");
        // save the list
        ed.apply();

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