package daewhan.example.com.mp_final_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by daewhan on 2015-12-18.
 */
public class MainActivity extends AppCompatActivity{

    Button bt_map;
    Button bt_history;

    //database 객체들
    SQLiteDatabase db;
    String dbName = "idList.db";
    String tableName = "idListTable";
    int dbMode = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Database 생성 및 열기
        db = openOrCreateDatabase(dbName, dbMode, null);
        //table 생성
        //removeTable(); //초기화 할려면 이전거 지우고 하기
        createTable();

        bt_map = (Button)findViewById(R.id.bt_save);
        bt_history = (Button)findViewById(R.id.bt_history);

        //위치보기 버튼
        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //history보기 버튼
        bt_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedListActivity.class);
                startActivity(intent);
            }
        });

    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "name text, lat text, lng text)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }
}