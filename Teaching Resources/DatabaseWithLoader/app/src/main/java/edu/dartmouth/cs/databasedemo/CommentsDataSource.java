package edu.dartmouth.cs.databasedemo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommentsDataSource {

	// Database fields
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_COMMENT };

	private static final String TAG = "DBDEMO";

	CommentsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public Comment createComment(String comment) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
		long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Comment newComment = cursorToComment(cursor);

		// Log the comment stored
		Log.d(TAG, "comment = " + cursorToComment(cursor).toString()
				+ " insert ID = " + insertId);

		cursor.close();
		database.close();
		dbHelper.close();
		return newComment;
	}

	public void deleteComment(Comment comment) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		long id = comment.getId();
		Log.d(TAG, "delete comment = " + id);
		database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
		database.close();
		dbHelper.close();
	}
	
	public void deleteAllComments() {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		System.out.println("Comment deleted all");
		Log.d(TAG, "delete all = ");
		database.delete(MySQLiteHelper.TABLE_COMMENTS, null, null);
		database.close();
		dbHelper.close();
	}
	
	public List<Comment> getAllComments() {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		List<Comment> comments = new ArrayList<>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
				allColumns, null, null, null, null, null);

		while (cursor.moveToNext()) {
			Comment comment = cursorToComment(cursor);
			Log.d(TAG, "get comment = " + cursorToComment(cursor).toString());
			comments.add(comment);
		}
		// Make sure to close the cursor
		cursor.close();
		database.close();
		dbHelper.close();
		return comments;
	}

	private Comment cursorToComment(Cursor cursor) {
		Comment comment = new Comment();
		comment.setId(cursor.getLong(0));
		comment.setComment(cursor.getString(1));
		return comment;
	}
}