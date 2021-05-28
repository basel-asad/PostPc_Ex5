package exercise.android.reemh.todo_items;


import java.util.Date;
import java.util.List;


public interface TodoItemsHolder {

  /** Get a copy of the current items list */
  List<TodoItem> getCurrentItems();

  /**
   * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
   * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
   * @return
   */
  TodoItem addNewInProgressItem(String description);

  /** a new 'InProgress' constructor that also sets the timestamo of a TodoItem
   * @return*/
  TodoItem addNewInProgressItem(String description, Date creation_date);

  TodoItem addNewInProgressItem(String description, Date creation_date, Date last_modified);

  /**
   * Creates a new TodoItem and adds it to the end of the list, with the @param description and status=DONE
   * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list (end)
   * @return
   */
  TodoItem addNewDoneItem(String description);

  /** a new 'DONE' constructor that also sets the timestamo of a TodoItem
   * @return*/
  TodoItem addNewDoneItem(String description, Date creation_date);


    TodoItem addNewDoneItem(String description, Date creation_date, Date last_modified);

    TodoItem markItemDone(TodoItem item);

  TodoItem markItemInProgress(TodoItem item);

  /** delete the @param item */
  void deleteItem(TodoItem item);

    void setText(TodoItem item, String new_text);
}
