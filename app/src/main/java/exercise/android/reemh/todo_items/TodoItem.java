package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.util.Date;

public class TodoItem implements Serializable {
    static int IN_PROGRESS = 0;
    static int DONE = 1;

    public int status ;
    String description;
    Date creation_TimeStamp;
    Date last_modified;

    public TodoItem(){
        /*
          STATUS by default will be set to IN_PROGRESS
         */
        this.status = IN_PROGRESS;
        this.description = "";
        this.creation_TimeStamp  = new Date();
        this.last_modified = new Date();

    }

    public TodoItem(String description){
        /*
          STATUS by default will be set to IN_PROGRESS
         */
        this.status = IN_PROGRESS;
        this.description = description;
        this.creation_TimeStamp  = new Date();
        this.last_modified = new Date();

    }

    public TodoItem(String description, int status){
        this.status = status;
        this.description = description;
        this.creation_TimeStamp  = new Date(); // renew the date
        this.last_modified = new Date();
    }


    public TodoItem(String description, int status, Date creation_date){
        this.status = status;
        this.description = description;
        this.creation_TimeStamp  = creation_date; // reuse the given timestamp
        this.last_modified = creation_date;

    }

    public TodoItem(String description, int status, Date creation_date, Date last_modified){
        this.status = status;
        this.description = description;
        this.creation_TimeStamp  = creation_date; // reuse the given timestamp
        this.last_modified = last_modified;

    }


    public void setStatus(int status) {
        if(status == IN_PROGRESS || status == DONE){
            this.status = status;
            this.last_modified = new Date();

        }
    }

    public boolean is_done(){
        return this.status == TodoItem.DONE;
    }
}
