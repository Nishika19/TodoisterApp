package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.Util.TaskRoomDatabase;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.data.*;

import java.util.List;

public class Repository {
    public final TaskDao taskDao;
    public final LiveData<List<Task>> allTasks;


    public Repository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTask();
    }

    public LiveData<List<Task>> getAllTasks() {return allTasks;}

    public void insert(Task task){TaskRoomDatabase.databaseWriteExecutor
            .execute(()->taskDao.insert(task));}

    public void delete(Task task){TaskRoomDatabase.databaseWriteExecutor
            .execute(()->taskDao.delete(task));}

    public void update(Task task){TaskRoomDatabase.databaseWriteExecutor
            .execute(()->taskDao.update(task));}

    public void deleteAll(){TaskRoomDatabase.databaseWriteExecutor
            .execute(()->taskDao.deleteAll());}

    public LiveData<Task> getTask(long id){return taskDao.get(id);}
}
