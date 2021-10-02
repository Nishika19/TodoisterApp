package com.bawp.todoister.adapter;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.Task;

public interface OnTaskClickListener {
    void onTaskCLick(int adapterPosition, Task task);
    void onRadioClick(int adapterPosition);
}
