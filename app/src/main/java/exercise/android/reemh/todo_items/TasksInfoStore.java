package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TasksInfoStore implements TodoItemsHolder {
    private final Context _context;
    public static String SP_INPUT_STRING = "input_string";
    public static String SP_TASKS = "tasks";
    String input_string;
    TodoItemsHolderImpl tasks_list;

    public TodoItemsHolderImpl getTasks_list() {
        return tasks_list;
    }


    public TasksInfoStore(Context context) {
        this._context = context;
        this.tasks_list = new TodoItemsHolderImpl();
    }

    public TasksInfoStore(Context context, ArrayList<TodoItem> list) {
        this._context = context;
        this.tasks_list = new TodoItemsHolderImpl(list);
    }

    public TasksInfoStore(Context context, ArrayList<TodoItem> list, String input_string) {
        this._context = context;
        this.tasks_list = new TodoItemsHolderImpl(list);
        this.input_string = input_string;

    }

    public void add_bulk_Items(ArrayList<TodoItem> list) {
        for (TodoItem item : list) {
            if (item.status == TodoItem.IN_PROGRESS) {
                tasks_list.addNewInProgressItem(item.description, item.creation_TimeStamp, item.last_modified);
            } else {
                tasks_list.addNewDoneItem(item.description, item.creation_TimeStamp, item.last_modified);
            }
        }
        // broadcast
        send_db_changed_broadcast();
    }

    @Override
    public List<TodoItem> getCurrentItems() {
        return tasks_list.getCurrentItems();
    }

    @Override
    public TodoItem addNewInProgressItem(String description) {
        TodoItem new_item = tasks_list.addNewInProgressItem(description);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public TodoItem addNewInProgressItem(String description, Date creation_date) {
        TodoItem new_item = tasks_list.addNewInProgressItem(description, creation_date);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public TodoItem addNewInProgressItem(String description, Date creation_date, Date last_modified) {
        TodoItem new_item = tasks_list.addNewInProgressItem(description, creation_date, last_modified);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public TodoItem addNewDoneItem(String description) {
        TodoItem new_item = tasks_list.addNewDoneItem(description);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public TodoItem addNewDoneItem(String description, Date creation_date) {
        TodoItem new_item = tasks_list.addNewDoneItem(description, creation_date);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public TodoItem addNewDoneItem(String description, Date creation_date, Date last_modified) {
        TodoItem new_item = tasks_list.addNewDoneItem(description, creation_date, last_modified);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public TodoItem markItemDone(TodoItem item) {
        TodoItem new_item = tasks_list.markItemDone(item);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public TodoItem markItemInProgress(TodoItem item) {
        TodoItem new_item = tasks_list.markItemInProgress(item);
        send_db_changed_broadcast();
        return new_item;
    }

    @Override
    public void deleteItem(TodoItem item) {
        tasks_list.deleteItem(item);
        send_db_changed_broadcast();
    }

    @Override
    public void setText(TodoItem item, String new_text) {
        tasks_list.setText(item, new_text);
        send_db_changed_broadcast();
    }

    private void send_db_changed_broadcast() {
        Intent it = new Intent("db_changed");
        it.putExtra(SP_INPUT_STRING, this.input_string);
        this._context.sendBroadcast(it);

    }
}
