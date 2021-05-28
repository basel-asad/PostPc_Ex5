package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    TaskStore_Application tasks_app;
//    TodoItemsHolderImpl holder_TodoItems;
    RecyclerView adapter_owner_RecyclerView;

    // wrapper class that shows individual view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        CheckBox status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            description =(TextView) itemView.findViewById(R.id.description_TV);
            status =(CheckBox) itemView.findViewById(R.id.status);
        }

        public TextView getDescription(){
            return this.description;
        }

        public CheckBox getStatus(){
            return this.status;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    // empty constructor
    public MyAdapter(Context context, RecyclerView rv){
        adapter_owner_RecyclerView = rv;
//        this.holder_TodoItems = new TodoItemsHolderImpl();
//        this.tasks_app = new TaskStore_Application();
        this.tasks_app = (TaskStore_Application) context;
        this.tasks_app.set_tasks_info(new TodoItemsHolderImpl());
    }

    // constructor with TodoItemsHolderImpl
    public MyAdapter(Context context, RecyclerView rv, TodoItemsHolder holder){
        adapter_owner_RecyclerView = rv;
//        this.holder_TodoItems = (TodoItemsHolderImpl) holder;
//        this.tasks_app = new TaskStore_Application();
        this.tasks_app = (TaskStore_Application) context;
        this.tasks_app.set_tasks_info((TodoItemsHolderImpl) holder);

    }

    // constructor with List<TodoItem>
    public MyAdapter(Context context, RecyclerView rv, List<TodoItem> TodoItems){
        adapter_owner_RecyclerView = rv;
//        this.holder_TodoItems = new TodoItemsHolderImpl(TodoItems);
//        this.tasks_app = new TaskStore_Application();
        this.tasks_app = (TaskStore_Application) context;
        this.tasks_app.set_tasks_info(new TodoItemsHolderImpl(TodoItems));

    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_todo_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

//        List<TodoItem> items = holder_TodoItems.getCurrentItems();
        List<TodoItem> items = tasks_app.tasks_info().tasks_list.getCurrentItems();
        TodoItem item = items.get(position);
        CheckBox holder_status = holder.getStatus();
        TextView holder_description = holder.getDescription();

        // set viewHolder parameters ((un)-checked, text (un)striked-through ..)
        holder_description.setText(item.description);
        holder_status.setChecked(item.is_done()); // if DONE then button is checked
        if(holder_status.isChecked()) {
            // STRIKE THROUGH THE TEXT
            holder_description.setPaintFlags(holder_description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            // change background to light red
        }
        else{
            // un-STRIKE THROUGH THE TEXT
            holder_description.setPaintFlags(holder_description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            // change background to normal white

        }


        holder_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.is_done()) {
                    // this item was done, should be moved to in-progress
                    tasks_app.tasks_info().deleteItem(item);
                    tasks_app.tasks_info().addNewInProgressItem(item.description, item.creation_TimeStamp, new Date());
                }
                else {
                    tasks_app.tasks_info().deleteItem(item);
                    tasks_app.tasks_info().addNewDoneItem(item.description, item.creation_TimeStamp, new Date());
                }

                MyAdapter.this.notify_adapter_data_changed();
            }
        });

        holder_description.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // remove
                tasks_app.tasks_info().deleteItem(item);
//                holder_TodoItems.deleteItem(item);
                MyAdapter.this.notify_adapter_data_changed();
                return false;
            }
        });

        holder_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_editing_for_view(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks_app.tasks_info().getCurrentItems().size();
//        return holder_TodoItems.getCurrentItems().size();
    }


    private void open_editing_for_view(TodoItem item){
        Intent intent = new Intent(this.adapter_owner_RecyclerView.getContext(), EditingActivity.class);
        //send view data along with intent
        intent.putExtra("task_text", item.description);
        intent.putExtra("status", item.status);
        intent.putExtra("creation_date", item.creation_TimeStamp.getTime());
        intent.putExtra("last_modified_date", item.last_modified.getTime());
        tasks_app.item_being_edited = item;
        this.adapter_owner_RecyclerView.getContext().startActivity(intent);
    }

    public void notify_adapter_data_changed(){
        //todo: save data

        // notify adapter that data has changed
        MyAdapter.this.notifyDataSetChanged();
    }

}
