package com.bawp.todoister;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.bawp.todoister.adapter.OnTaskClickListener;
import com.bawp.todoister.adapter.RecyclerViewAdapter;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskClickListener {
    public TaskViewModel taskViewModel;
    public RecyclerView recyclerView;
    public RecyclerViewAdapter recyclerViewAdapter;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                recyclerViewAdapter = new RecyclerViewAdapter(tasks,MainActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Task task = new Task("Todo", Priority.HIGH, Calendar.getInstance().getTime()
//                        ,Calendar.getInstance().getTime(),false);
//                TaskViewModel.insert(task);
//                Log.d("TASK", "onClick: "+ taskViewModel.getTask(1));
            showBottomSheetDialog();

        });
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(),bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCLick(int adapterPosition, Task task) {
        sharedViewModel.setSelectedItem(task);
        sharedViewModel.setIsEdit(true);
        showBottomSheetDialog();

    }

    @Override
    public void onRadioClick(int adapterPosition) {
        Task task = taskViewModel.getAllTasks().getValue().get(adapterPosition);
        TaskViewModel.delete(task);
        Snackbar.make(recyclerView,R.string.deleted,Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskViewModel.getAllTasks().getValue().add(adapterPosition,task);
                        TaskViewModel.insert(task);
                    }
                }).show();
        recyclerViewAdapter.notifyDataSetChanged();
    }
}