package exercise.android.reemh.todo_items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TodoItemsHolderImpl implements TodoItemsHolder {
  List<TodoItem> items;

  // empty constructor
  public TodoItemsHolderImpl(){
      this.items = new ArrayList<>(); //init with empty list
  }

  // copy constructor
  public TodoItemsHolderImpl(List<TodoItem> items_list){
    if(items_list == null){
      this.items = new ArrayList<>(); //init with empty list
    }
    else{
      this.items = new ArrayList<>(items_list); // copy the list when initializing
      this.sort_items();
    }

  }

  @Override
  public List<TodoItem> getCurrentItems() { return items; }

  @Override
  public TodoItem addNewInProgressItem(String description) {
    TodoItem item = new TodoItem(description, TodoItem.IN_PROGRESS);
    this.items.add(0, item);
    sort_items();
    return item;
  }

  @Override
  public TodoItem addNewInProgressItem(String description, Date creation_date) {
    TodoItem item = new TodoItem(description, TodoItem.IN_PROGRESS, creation_date);
    // last modified is set the same as creation date
    this.items.add(0, item);
    sort_items();
    return item;
  }

  @Override
  public TodoItem addNewInProgressItem(String description, Date creation_date, Date last_modified) {
    TodoItem item =new TodoItem(description, TodoItem.IN_PROGRESS, creation_date, last_modified);
    this.items.add(0, item);
    sort_items();
    return item;
  }

  @Override
  public TodoItem addNewDoneItem(String description) {
    TodoItem item =new TodoItem(description, TodoItem.DONE);
    this.items.add(item);
    sort_items();
    return item;
  }

  @Override
  public TodoItem addNewDoneItem(String description, Date creation_date) {
    TodoItem item = new TodoItem(description, TodoItem.DONE, creation_date);
    this.items.add(item);
    sort_items();
    return item;
  }

  @Override
  public TodoItem addNewDoneItem(String description, Date creation_date, Date last_modified) {
    TodoItem item = new TodoItem(description, TodoItem.DONE, creation_date, last_modified);
    this.items.add(item);
    sort_items();
    return item;
  }

  public void sort_items(){
    Collections.sort(this.items, new Comparator() {

      public int compare(Object o1, Object o2) {

        Integer checked1 = ((TodoItem) o1).status;
        Integer checked2 = ((TodoItem) o2).status;
        int sComp = checked1.compareTo(checked2);

        if (sComp != 0) {
          return sComp;
        }

        Date d1 = ((TodoItem) o1).creation_TimeStamp;
        Date d2 = ((TodoItem) o2).creation_TimeStamp;
        return d2.compareTo(d1);
      }});
  }

  @Override
  public TodoItem markItemDone(TodoItem item) {
    //check if item is inside list?
    if(this.items.contains(item)) {
      this.deleteItem(item); // delete item1
      //******   re-add item as checked ( this ensures order)
      item = this.addNewDoneItem(item.description, item.creation_TimeStamp, new Date());
    }
    return item;
  }

  @Override
  public TodoItem markItemInProgress(TodoItem item) {
    //check if item is inside list?
    if(this.items.contains(item)) {
      this.deleteItem(item); // delete item1
      //******   re-add item as InProgress ( this ensures order)
      item = this.addNewInProgressItem(item.description, item.creation_TimeStamp, new Date());
    }
    return item;
  }

  @Override
  public void deleteItem(TodoItem item) {
    this.items.remove(item); // returns true if item found and removed, false otherwise
  }

  @Override
  public void setText(TodoItem item, String new_text) {
    //check if item is inside list?
    if(this.items.contains(item)) {
      item.description = new_text;
      item.last_modified = new Date();

    }
  }

}
