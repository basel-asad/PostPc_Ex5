package exercise.android.reemh.todo_items;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    TodoItemsHolderImpl holder_TodoItems;
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
    public MyAdapter(RecyclerView rv){
        this.holder_TodoItems = new TodoItemsHolderImpl();
        adapter_owner_RecyclerView = rv;
    }

    // constructor with TodoItemsHolderImpl
    public MyAdapter(RecyclerView rv, TodoItemsHolder holder){
        adapter_owner_RecyclerView = rv;
        this.holder_TodoItems = (TodoItemsHolderImpl) holder;

    }

    // constructor with List<TodoItem>
    public MyAdapter(RecyclerView rv, List<TodoItem> TodoItems){
        adapter_owner_RecyclerView = rv;
        this.holder_TodoItems = new TodoItemsHolderImpl(TodoItems);
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

        List<TodoItem> items = holder_TodoItems.getCurrentItems();
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
                //todo: should the original item be recycled?
                if(item.is_done()) {
                    // this item was done, should be moved to in-progress
                    holder_TodoItems.deleteItem(item);
                    holder_TodoItems.addNewInProgressItem(item.description, item.creation_TimeStamp);
                }
                else {
                    holder_TodoItems.deleteItem(item);
                    holder_TodoItems.addNewDoneItem(item.description, item.creation_TimeStamp); // add a new item with the same description
                }

                MyAdapter.this.notifyDataSetChanged();
            }
        });

        holder_description.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // remove
                holder_TodoItems.deleteItem(item);
                MyAdapter.this.notifyDataSetChanged();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return holder_TodoItems.getCurrentItems().size();
    }

}
