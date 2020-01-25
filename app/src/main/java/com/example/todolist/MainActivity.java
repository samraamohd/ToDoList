package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter<String> mtask;
    ListView lst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper= new DbHelper(this);
        lst=findViewById(R.id.task);
        loadtask();
    }
    private void loadtask(){
        ArrayList<String> taskls=dbHelper.gettasklist();
        if (mtask==null){
            mtask=new ArrayAdapter<String>(this,R.layout.row,R.id.txt,taskls);
            lst.setAdapter(mtask);
        }
        else {
            mtask.clear();
            mtask.addAll(taskls);
            mtask.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.action_task:
                final EditText taskEdit=new EditText(this);
                AlertDialog d=new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("what do you want to do next?")
                        .setView(taskEdit)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task=String.valueOf(taskEdit.getText());
                                dbHelper.insertTask(task);
                                loadtask();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                d.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        Drawable icon=menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }
    public void deleteTask(View view){
        View parent=(View)view.getParent();
        TextView tasktxt=findViewById(R.id.txt);
        String task=String.valueOf(tasktxt.getText());
        dbHelper.deleteTask(task);
        loadtask();
    }

}
