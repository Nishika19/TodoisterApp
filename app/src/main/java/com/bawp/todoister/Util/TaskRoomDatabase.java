package com.bawp.todoister.Util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.data.*;
import com.bawp.todoister.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class},version = 1,exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    public static final int NUMBER_OF_THREADS = 4;

    private static volatile TaskRoomDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TaskRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
                synchronized (TaskRoomDatabase.class){
                    if(INSTANCE==null){
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskRoomDatabase
                        .class,"task_database")
                                .addCallback(sRoomDatabaseCallback)
                                .build();

                    }
                }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback=
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(()->{
                        TaskDao taskDao = INSTANCE.taskDao();
                        taskDao.deleteAll();
                    });

                }
            };
}
