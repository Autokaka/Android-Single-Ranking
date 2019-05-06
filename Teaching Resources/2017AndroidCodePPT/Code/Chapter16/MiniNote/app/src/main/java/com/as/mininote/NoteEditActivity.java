package com.as.mininote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteEditActivity extends Activity {


    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DiaryDbAdapter mDbHelper;
    private Context mcontext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.activity_note_edit);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        Button deletButton = (Button) findViewById(R.id.delet);

        mRowId = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(DiaryDbAdapter.KEY_TITLE);
            String body = extras.getString(DiaryDbAdapter.KEY_BODY);
            mRowId = extras.getLong(DiaryDbAdapter.KEY_ROWID);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String title = mTitleText.getText().toString().trim();
                String body = mBodyText.getText().toString().trim();
                if(!title.equals("")&&!body.equals("")){
                    if (mRowId != null) {
                        mDbHelper.updateDiary(mRowId, title, body);
                    } else
                        mDbHelper.createDiary(title, body);
                    AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
                    builder.setTitle("提示");
                    builder.setMessage("保存成功");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.show();

                    Intent mIntent = new Intent();
                    setResult(RESULT_OK, mIntent);

                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
                    builder.setTitle("提示");
                    builder.setMessage("标题与内容不能为空！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });

        deletButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mRowId != null) {
                    mDbHelper.deleteDiary(mRowId);
                    AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
                    builder.setTitle("提示");
                    builder.setMessage("删除成功！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.show();
                    Intent mIntent = new Intent();
                    setResult(RESULT_OK, mIntent);
                }
            }
        });
    }
}
