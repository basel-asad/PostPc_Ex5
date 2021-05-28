package exercise.android.reemh.todo_items;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

//import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EditingActivity extends AppCompatActivity {
    private Intent intent_created_me = null;
    private TaskStore_Application app = null;
    private TodoItem item_being_edited = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        TaskStore_Application app =  ((TaskStore_Application) getApplicationContext());
        this.item_being_edited = app.item_being_edited;
        intent_created_me = this.getIntent();

        // find views
        TextView creation_date_view = findViewById(R.id.creationDate);
        TextView last_modified_view = findViewById(R.id.LastModified);
        TextView status_text_view = findViewById(R.id.status_TextView);
        CheckBox box = findViewById(R.id.status_checkbox);
        TextView task_text_view = findViewById(R.id.Edit_Task);

        // set up the views
        //
        // creation date set
        long create_date_milli = intent_created_me.getLongExtra("creation_date", -1);
        update_last_modified_in_edit_window(creation_date_view, create_date_milli);

        // modification date
        long modified_date_milli = intent_created_me.getLongExtra("last_modified_date", -1);
        update_last_modified_in_edit_window(last_modified_view, modified_date_milli);

        // status
        int status_int = intent_created_me.getIntExtra("status", -1);
        box.setText("Status: ");
        if(status_int == -1){
            // error
            status_text_view.setText("error!!!");
            box.setChecked(true);
        }
        else{
            if(status_int == TodoItem.IN_PROGRESS){
                // in progress
                status_text_view.setText("IN PROGRESS!");
                box.setChecked(false);
            }
            else{
                // done
                status_text_view.setText("DONE!");
                box.setChecked(true);
            }
        }

        // task text
        String task_text = intent_created_me.getStringExtra("task_text");
        if(task_text == null){
            task_text_view.setText("error!");
        }
        else{
            task_text_view.setText(task_text);
        }


        // when box text is edited
        task_text_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //change last edited (in the edit activity)
                update_last_modified_in_edit_window(last_modified_view, new Date().getTime());
                // save changed text to item
                app.tasks_info().setText(item_being_edited, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // when status box is checked/unchecked
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //change last edited(in the edit activity)
                update_last_modified_in_edit_window(last_modified_view, new Date().getTime());
                //change text associated with it
                if(isChecked) {
                    item_being_edited = app.tasks_info().markItemDone(item_being_edited);
                    status_text_view.setText("Done!");
                }
                else{
                    item_being_edited = app.tasks_info().markItemInProgress(item_being_edited);
                    status_text_view.setText("IN PROGRESS!");
                }
            }
        });

    }

    private void update_last_modified_in_edit_window(TextView textview_to_edit, long modified_date_milli) {
        if (modified_date_milli == -1) {
            //error
            textview_to_edit.setText("error");
        }
        else {
            Date date_modified = new Date(modified_date_milli);

            textview_to_edit.setText(EditingActivity.get_proper_date_format(date_modified));
//            last_modified_view.setText(DateFormat.format("dd-M-yyyy <hh: mm: ss>", date_modified));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public String get_proper_date_format(Date time){
        try {
            TimeUnit.MILLISECONDS.sleep(10);// this sleep makes a small difference between the call time and 'now'
        } catch (InterruptedException e) {
            // supress
        }
        String ret = "--------";
        Instant now = Instant.now();
        boolean isWithinPrior1Hour =
                ( ! time.toInstant().isBefore( now.minus( 1 , ChronoUnit.HOURS) ) )
                        &&
                        ( time.toInstant().isBefore( now )
                        ) ;

        boolean isWithinPrior24Hours =
                ( ! time.toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS) ) )
                        &&
                        ( time.toInstant().isBefore( now )
                        ) ;

        if(isWithinPrior1Hour){
            long diff_milli = now.toEpochMilli() - time.toInstant().toEpochMilli();
            long minutes = (diff_milli / 1000) / 60;
            // if the last-modified time was less than a hour ago, the text should be "<minutes> minutes ago"
            ret = minutes + " minutes ago";
//            ret = DateFormat.format("mm 'minutes ago'", time).toString();
        }
        else if(isWithinPrior24Hours){
            // if the last-modified time was earlier than a hour, but today, the text should be "Today at <hour>"
            ret = DateFormat.format("'Today at' hh:mm", time).toString();
//            ret = DateFormat.format("'Today at' hh:mm", time).toString();
        }
        else{
            // if the last-modified time was yesterday or earlier, the text should be "<date> at <hour>"
            ret = DateFormat.format("dd-M-yyyy 'at' hh:mm", time).toString();
        }

        return ret;
    }
}