package cn.dshitpie.learnlitepal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createTableBook = (Button) findViewById(R.id.create_table_book);
        createTableBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.getDatabase();
            }
        });
        Button insertIntoTableBook = (Button) findViewById(R.id.insert_into_table_book);
        insertIntoTableBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book("The Lost Symbol", "Dan Brown", 510, 19.95, "Unknow");
                book.save();
                book.setPrice(10.99);
                book.save();
            }
        });
    }
}
