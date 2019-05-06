package edu.dartmouth.cs.databasedemo;

import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestDatabaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Comment>> {
	private CommentsDataSource dataSource;

	private ArrayAdapter<Comment> mAdapter;

	private static final int ALL_COMMENTS_LOADER_ID = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if(getSupportActionBar() != null){
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_database);
		}

		dataSource = new CommentsDataSource(this);
		ListView mListView = findViewById(R.id.list);
		mAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1);
		mListView.setAdapter(mAdapter);

		// start loader in the background thread.
		LoaderManager mLoader = getSupportLoaderManager();
		mLoader.initLoader(ALL_COMMENTS_LOADER_ID, null, this).forceLoad();
		// note we do not implement onForceLoad but it will be called as part of
        // the loader we set up; AsyncTaskLoader.onForceLoad() to start the
        // AsyncTaskLoader loader.

	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		Comment comment;
		switch (view.getId()) {
		case R.id.add:
			String[] comments = new String[] { "England", "Dartmouth", "CS65",
					"is", "is", "the", "best", "coolest", "place", "in", "the",
					"universe!" };
			int nextInt = new Random().nextInt(9);
			// Save the new comment to the database
			comment = dataSource.createComment(comments[nextInt]);
			mAdapter.add(comment);
			break;
		case R.id.delete:
			if (mAdapter.getCount() > 0) {
				comment = mAdapter.getItem(0);
				dataSource.deleteComment(comment);
				mAdapter.remove(comment);
			}
			break;
		case R.id.deleteall:
			if (mAdapter.getCount() > 0) {
				dataSource.deleteAllComments();
				mAdapter.clear();
			}
			break;
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// Async task loader callback functions

	// this method is for creating different loaders.
	@NonNull
	@Override
	public Loader<List<Comment>> onCreateLoader(int id, @Nullable Bundle args) {
		if(id == ALL_COMMENTS_LOADER_ID){
			// create an instance of the loader in our case AsyncTaskLoader
			// which loads a List of Comments List<Comment>
			return new CommentsListLoader(TestDatabaseActivity.this);
		}
		return null;
	}
	// this method will be called when loader finishes its task.
	@Override
	public void onLoadFinished(@NonNull Loader<List<Comment>> loader, List<Comment> data) {
		if(loader.getId() == ALL_COMMENTS_LOADER_ID){
			// returns the List<Comment> from queried from the db
			// Use the UI with the adapter to show the elements in a ListView
			if(data.size() > 0){
				mAdapter.addAll(data);
				// force notification -- tell the adapter to display
				mAdapter.notifyDataSetChanged();
			}

		}

	}
	// this method will be called after invoking loader.restart()
	@Override
	public void onLoaderReset(@NonNull Loader<List<Comment>> loader) {
		if(loader.getId() == ALL_COMMENTS_LOADER_ID){
			mAdapter.clear();
			mAdapter.notifyDataSetChanged();
		}
	}

}