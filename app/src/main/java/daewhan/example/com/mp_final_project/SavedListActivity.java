package daewhan.example.com.mp_final_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedListActivity extends AppCompatActivity {

    //database 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // database 이름
    String tableName = "idListTable"; // table 이름
    int dbMode = Context.MODE_PRIVATE;

    //layout object
    EditText mEtName;
    EditText mEtNum;
    Button mBtInsert;
    Button mBtRead;
    Button mBtDelete;
    Button mBtSort;
    Button mBtUpdate;

    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Database 생성 및 열기
        db = openOrCreateDatabase(dbName, dbMode, null);
        //table 생성
        //removeTable(); //초기화 할려면 이전거 지우고 하기
        createTable();

        mEtName = (EditText) findViewById(R.id.et_text);
        mEtNum = (EditText)findViewById(R.id.dt_text);
        mBtInsert = (Button) findViewById(R.id.bt_insert);
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtDelete = (Button)findViewById(R.id.bt_delete);
        mBtSort = (Button)findViewById(R.id.bt_sort);
        mBtUpdate=(Button)findViewById(R.id.bt_update);
        ListView mList = (ListView) findViewById(R.id.list_view);


        // Insert 버튼
        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                insertData(name);
            }
        });

        //read 버튼
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged(); // 변경된 데이터가 리스트뷰에 적용이 된다.
            }
        });

        //delete 버튼
       mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(mEtNum.getText().toString());
                removeData(num);
            }
        });

        //sort 버튼
        mBtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                sortReverse();
               baseAdapter.notifyDataSetChanged();
            }
        });

        //update버튼
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(mEtNum.getText().toString());
                String name = mEtName.getText().toString();
                updateData(num,name);
            }
        });

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);
    }



    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "name text not null)";
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

    // Data 추가
    public void insertData(String name) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "');";
        db.execSQL(sql);
    }

    // Data 업데이트
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {

        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 읽기(꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
    }
    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name);
            results.moveToNext();
        }
        results.close();
    }

    static int cnt = 1;
    //Data index 반대로 출력하기
    public void sortReverse() {
        Cursor result;
        if(cnt == 0) {
            String sql = "select * from " + tableName + " order by id asc;";
            result = db.rawQuery(sql, null);
            result.moveToFirst();
            cnt = 1;
        }
        else
        {
            String sql = "select * from " + tableName + " order by id desc;";
            result = db.rawQuery(sql, null);
            result.moveToFirst();
            cnt = 0;
        }
        while (!result.isAfterLast()) {
            int id = result.getInt(0);
            String name = result.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name);
            result.moveToNext();
        }
        result.close();
    }
}
