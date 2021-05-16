package exercise.android.reemh.todo_items;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoItemsHolderImplTest {

  @Test
  public void when_addingTodoItem_then_callingListShouldHaveThisItem(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void when_initialized_with_no_input_list_ends_up_empty(){
    /* when TodoItemsHolderImpl is intialized through empty constructor, will have an empty list
     * by default*/
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    // test
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void when_adding_two_TodoItem_the_recent_is_on_top() throws InterruptedException {
    /* the list keeps the order described in the excersise*/
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    // test
    holderUnderTest.addNewInProgressItem("lower");
    Thread.sleep(300); // we wait alittle between creation of items to make a time difference
    holderUnderTest.addNewInProgressItem("upper");

    // verify
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());

    Assert.assertEquals("upper", holderUnderTest.getCurrentItems().get(0).description);
    Assert.assertEquals("lower", holderUnderTest.getCurrentItems().get(1).description);

    // switch insertion order -----------------------------

    // setup
    holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    // test
    holderUnderTest.addNewInProgressItem("upper");
    holderUnderTest.addNewInProgressItem("lower");

    // verify
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());

    Assert.assertEquals("upper", holderUnderTest.getCurrentItems().get(1).description);
    Assert.assertEquals("lower", holderUnderTest.getCurrentItems().get(0).description);

  }

  @Test
  public void checked_comes_after_unchecked_items() throws InterruptedException {
    /* checked items always come after*/
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    // test
    holderUnderTest.addNewInProgressItem("item0"); //unchecked
    Thread.sleep(300); // we wait alittle between creation of items to make a time difference
    holderUnderTest.addNewInProgressItem("item1"); //unchecked
    Thread.sleep(300); // we wait alittle between creation of items to make a time difference
    // if the following item was unchecked it would be on top
    holderUnderTest.addNewDoneItem("item2"); //checked

    // verify
    Assert.assertEquals(3, holderUnderTest.getCurrentItems().size());
    // make sure the checked element comes after unchecked
    Assert.assertEquals("item1", holderUnderTest.getCurrentItems().get(0).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(0).status); // unchecked

    Assert.assertEquals("item0", holderUnderTest.getCurrentItems().get(1).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(1).status); // unchecked

    Assert.assertEquals("item2", holderUnderTest.getCurrentItems().get(2).description);
    Assert.assertEquals(TodoItem.DONE, holderUnderTest.getCurrentItems().get(2).status); // unchecked
  }

  @Test
  public void check_then_uncheck() throws InterruptedException {
    /* checking item then un-checking after should re-create it back into its original place */
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    holderUnderTest.addNewInProgressItem("item0"); //unchecked
    Thread.sleep(300); // we wait alittle between creation of items to make a time difference
    holderUnderTest.addNewInProgressItem("item1"); //unchecked
    Thread.sleep(300); // we wait alittle between creation of items to make a time difference
    holderUnderTest.addNewInProgressItem("item2"); //unchecked

    Assert.assertEquals(3, holderUnderTest.getCurrentItems().size());
    // make sure the checked element comes after unchecked
    Assert.assertEquals("item2", holderUnderTest.getCurrentItems().get(0).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(0).status); // unchecked

    Assert.assertEquals("item1", holderUnderTest.getCurrentItems().get(1).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(1).status); // unchecked

    Assert.assertEquals("item0", holderUnderTest.getCurrentItems().get(2).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(2).status); // unchecked

    // test

    // 1) check 'item1' ( and remove and re-add)
    TodoItem item1 = holderUnderTest.getCurrentItems().get(1); // item1 is in index 1
    holderUnderTest.markItemDone(item1);

    // test item1' is checked and new position is at the end.
    Assert.assertEquals("item2", holderUnderTest.getCurrentItems().get(0).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(0).status); // unchecked

    Assert.assertEquals("item0", holderUnderTest.getCurrentItems().get(1).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(1).status); // unchecked

    Assert.assertEquals("item1", holderUnderTest.getCurrentItems().get(2).description);
    Assert.assertEquals(TodoItem.DONE, holderUnderTest.getCurrentItems().get(2).status); // checked

    // 2) uncheck item1 ( and remove and re-add)
    item1 = holderUnderTest.getCurrentItems().get(2); // item1 is in index 1
    holderUnderTest.markItemInProgress(item1);

    // test original position
    Assert.assertEquals("item2", holderUnderTest.getCurrentItems().get(0).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(0).status); // unchecked

    Assert.assertEquals("item1", holderUnderTest.getCurrentItems().get(1).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(1).status); // unchecked

    Assert.assertEquals("item0", holderUnderTest.getCurrentItems().get(2).description);
    Assert.assertEquals(TodoItem.IN_PROGRESS, holderUnderTest.getCurrentItems().get(2).status); // unchecked

  }

  @Test
  public void in_progress_created_recently_is_higher_up() throws InterruptedException {
    /* testing that items on top have been created more recently*/
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    // test
    holderUnderTest.addNewInProgressItem("before");
    Thread.sleep(300); // we wait a little between creation of items to make a time difference
    holderUnderTest.addNewInProgressItem("after");

    // verify
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());

    Date item0_creation_date = holderUnderTest.getCurrentItems().get(0).creation_TimeStamp;
    Assert.assertEquals("after",holderUnderTest.getCurrentItems().get(0).description);
    Date item1_creation_date = holderUnderTest.getCurrentItems().get(1).creation_TimeStamp;
    Assert.assertEquals("before",holderUnderTest.getCurrentItems().get(1).description);

    assert (item1_creation_date.before(item0_creation_date));
  }


  @Test
  public void test_remove_item() {
    /* testing that items on top have been created more recently*/
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    // test
    holderUnderTest.addNewInProgressItem("item0");
    holderUnderTest.addNewInProgressItem("item1");
    TodoItem item1 = holderUnderTest.getCurrentItems().get(0);
    TodoItem item0 = holderUnderTest.getCurrentItems().get(1);

    // verify
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());
    Assert.assertEquals("item0", item0.description); // we actually got item0
    Assert.assertEquals("item1", item1.description); // we actually got item1


    // remove item0
    holderUnderTest.deleteItem(item0);

    // test ( only item1 left)
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
    Assert.assertEquals(item1, holderUnderTest.getCurrentItems().get(0)); // we actually got item1
  }

  @Test
  public void test_items_are_added_unchecked() {
    /* testing that items on top have been created more recently*/
    // setup
    List<TodoItem> items_list = new ArrayList<>();
    items_list.add(new TodoItem("item0"));
    items_list.add(new TodoItem("item1"));

    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(items_list);
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());

    // test
    TodoItem item1 = holderUnderTest.getCurrentItems().get(0);
    TodoItem item0 = holderUnderTest.getCurrentItems().get(1);

    // verify
    Assert.assertEquals(TodoItem.IN_PROGRESS, item0.status);
    Assert.assertEquals(TodoItem.IN_PROGRESS, item1.status);
  }

  @Test
  public void test_list_constructor_works() {
    /* testing that items on top have been created more recently*/
    // setup
    List<TodoItem> items_list = new ArrayList<>();
    items_list.add(new TodoItem("item0"));
    items_list.add(new TodoItem("item1"));

    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(items_list);

    // test
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void test_make_item_in_progress() {
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    // test
    holderUnderTest.addNewInProgressItem("item0");
    holderUnderTest.addNewDoneItem("item1");
    holderUnderTest.addNewInProgressItem("item2");
    TodoItem item1 = holderUnderTest.getCurrentItems().get(2);
    TodoItem item0 = holderUnderTest.getCurrentItems().get(1);
    TodoItem item2 = holderUnderTest.getCurrentItems().get(0);

    // verify
    Assert.assertEquals(3, holderUnderTest.getCurrentItems().size());

    Assert.assertEquals("item0", item0.description); // we actually got item0

    Assert.assertEquals("item1", item1.description); // we actually got item1
    Assert.assertEquals(TodoItem.DONE, item1.status); // item1 status is DONE

    Assert.assertEquals("item2", item2.description); // we actually got item2


    // make item1 IN_PROGRESS
    holderUnderTest.markItemInProgress(item1);

    // test size remains the same
    Assert.assertEquals(3, holderUnderTest.getCurrentItems().size());

    // test item1 is now IN_PROGRESS
    boolean item1_found = false;
    for(TodoItem item:holderUnderTest.getCurrentItems()){
      // when item1 is found, check that its in-progress
      if(item.description.equals("item1")){
        item1_found = true;
        Assert.assertEquals(TodoItem.IN_PROGRESS, item.status); // item1's status is IN_PROGRESS
      }
    }
    // make sure item1 was found
    Assert.assertTrue(item1_found);
  }

  @Test
  public void test_make_item_done() {
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
    // test
    holderUnderTest.addNewInProgressItem("item0");
    holderUnderTest.addNewInProgressItem("item1");
    holderUnderTest.addNewInProgressItem("item2");
    TodoItem item0 = holderUnderTest.getCurrentItems().get(2);
    TodoItem item1 = holderUnderTest.getCurrentItems().get(1);
    TodoItem item2 = holderUnderTest.getCurrentItems().get(0);

    // verify
    Assert.assertEquals(3, holderUnderTest.getCurrentItems().size());

    Assert.assertEquals("item0", item0.description); // we actually got item0

    Assert.assertEquals("item1", item1.description); // we actually got item1
    Assert.assertEquals(TodoItem.IN_PROGRESS, item1.status); // item1's status is in-progress

    Assert.assertEquals("item2", item2.description); // we actually got item2


    // make item1 IN_PROGRESS
    holderUnderTest.markItemDone(item1);

    // test size remains the same
    Assert.assertEquals(3, holderUnderTest.getCurrentItems().size());

    // test item1 is now IN_PROGRESS
    boolean item1_found = false;
    for(TodoItem item:holderUnderTest.getCurrentItems()){
      // when item1 is found, check that its in-progress
      if(item.description.equals("item1")){
        item1_found = true;
        Assert.assertEquals(TodoItem.DONE, item.status); // item1's status is DONE
      }
    }
    // make sure item1 was found
    Assert.assertTrue(item1_found);
  }

}