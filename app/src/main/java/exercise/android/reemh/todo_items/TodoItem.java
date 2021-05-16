package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.util.Date;

public class TodoItem implements Serializable {
    static int IN_PROGRESS = 0;
    static int DONE = 1;

    public int status ;
    String description;
    Date creation_TimeStamp;

    public TodoItem(){
        /*
          STATUS by default will be set to IN_PROGRESS
         */
        this.status = IN_PROGRESS;
        this.description = "";
        this.creation_TimeStamp  = new Date();

    }

    public TodoItem(String description){
        /*
          STATUS by default will be set to IN_PROGRESS
         */
        this.status = IN_PROGRESS;
        this.description = description;
        this.creation_TimeStamp  = new Date();

    }

    public TodoItem(String description, int status){
        this.status = status;
        this.description = description;
        this.creation_TimeStamp  = new Date(); // renew the date
    }

    public TodoItem(String description, int status, Date creation_date){
        this.status = status;
        this.description = description;
        this.creation_TimeStamp  = creation_date; // reuse the given timestamp

    }

    public void setStatus(int status) {
        if(status == IN_PROGRESS || status == DONE){
            this.status = status;
        }
    }

    public boolean is_done(){
        return this.status == TodoItem.DONE;
    }
}
