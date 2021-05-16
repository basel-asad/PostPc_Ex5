package exercise.android.reemh.todo_items;


import java.util.Date;
import java.util.List;


public interface TodoItemsHolder {

  /** Get a copy of the current items list */
  List<TodoItem> getCurrentItems();

  /**
   * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
   * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
   */
  void addNewInProgressItem(String description);

  /** a new 'InProgress' constructor that also sets the timestamo of a TodoItem*/
  void addNewInProgressItem(String description, Date creation_date);

  /**
   * Creates a new TodoItem and adds it to the end of the list, with the @param description and status=DONE
   * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list (end)
   */
  void addNewDoneItem(String description);

  /** a new 'DONE' constructor that also sets the timestamo of a TodoItem*/
  void addNewDoneItem(String description, Date creation_date);


  void markItemDone(TodoItem item);

  void markItemInProgress(TodoItem item);

  /** delete the @param item */
  void deleteItem(TodoItem item);
}
