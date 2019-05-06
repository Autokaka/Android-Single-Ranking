package edu.dartmouth.cs.databasedemo;

import java.util.List;
import java.util.Random;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestDatabaseActivity extends AppCompatActivity {
	private CommentsDataSource datasource;

	private ArrayAdapter<Comment> mAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if(getSupportActionBar() != null){
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_database);
		}


		ListView mListView = findViewById(R.id.list);
		datasource = new CommentsDataSource(this);
		datasource.open();

		List<Comment> values = datasource.getAllComments();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		mAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, values);
		mListView.setAdapter(mAdapter);
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
			comment = datasource.createComment(comments[nextInt]);
			mAdapter.add(comment);
			break;
		case R.id.delete:
			if (mAdapter.getCount() > 0) {
				comment = mAdapter.getItem(0);
				datasource.deleteComment(comment);
				mAdapter.remove(comment);
			}
			break;
		case R.id.deleteall:
			if (mAdapter.getCount() > 0) {
				datasource.deleteAllComments();
				mAdapter.clear();
			}
			break;
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

}