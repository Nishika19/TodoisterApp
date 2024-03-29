package com.bawp.todoister.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");
        return simpleDateFormat.format(date);
    }

    public static void hideSoftKeyboard(View view){
        InputMethodManager hideKeyboard = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        hideKeyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int setPriorityColor(Task task){
        int color = 0;
        if(task.getPriority() == Priority.HIGH){
            color = Color.argb(200,201,21,23);
        }else if(task.getPriority() == Priority.MEDIUM){
            color = Color.argb(200,155,179,0);
        }else if (task.getPriority()==Priority.LOW){
            color = Color.argb(200,51,181,129);
        }
        return color;
    }
}

