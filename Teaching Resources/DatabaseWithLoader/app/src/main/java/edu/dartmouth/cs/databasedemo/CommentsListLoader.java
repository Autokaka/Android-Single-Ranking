package edu.dartmouth.cs.databasedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class CommentsListLoader extends AsyncTaskLoader<List<Comment>> {

    private CommentsDataSource dataSource;
    CommentsListLoader(@NonNull Context context) {
        super(context);
        // create the database
        this.dataSource = new CommentsDataSource(context);
    }

    // worker thread loads the comments
    @Nullable
    @Override
    public List<Comment> loadInBackground() {
        return dataSource.getAllComments();
    }
}
