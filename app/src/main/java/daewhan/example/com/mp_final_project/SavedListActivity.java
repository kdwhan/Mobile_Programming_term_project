package daewhan.example.com.mp_final_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedListActivity extends AppCompatActivity {

    //database 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // database 이름
    String tableName = "idListTable"; // table 이름
    int dbMode = Context.MODE_PRIVATE;

    //layout object
    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;
    Button bt_clean;
    Button bt_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Database 열기
        db = openOrCreateDatabase(dbName, dbMode, null);

        ListView mList = (ListView) findViewById(R.id.list_view);
        bt_clean = (Button)findViewById(R.id.bt_clean);
        bt_all = (Button)findViewById(R.id.bt_all);

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);

        // 저장된 자료들 보이기
         nameList.clear();
        selectAll();
        baseAdapter.notifyDataSetChanged(); // 변경된 데이터가 리스트뷰에 적용이 된다.


        //ListView의 아이템 하나가 클릭되는 것을 감지하는 Listener객체 생성 (Button의 OnClickListener와 같은 역할)
        AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {

            //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
            //첫번째 파라미터 : 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
            //두번째 파라미터 : 클릭된 아이템 뷰
            //세번째 파라미터 : 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3.....)
            //네번재 파리미터 : 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라이터인 position과 같은 값)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //저장된 곳으로 가며 position을 넘겨준다 (DB에서 그 position에 저장된 데이터를 사용하기 위해.)
                Intent intent = new Intent(SavedListActivity.this, SavedMapsActivity.class);
                intent.putExtra("position", position);//index 넘기기
                intent.putExtra("text", nameList.get(position));//내용 넘기기
                startActivity(intent);


                //클릭된 아이템의 위치를 이용하여 데이터인 문자열을 Toast로 출력
                Toast.makeText(SavedListActivity.this, nameList.get(position), Toast.LENGTH_SHORT).show();

            }
        };

        //gogo
        mList.setOnItemClickListener(listener);

        bt_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTable();
                createTable();
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });

        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedListActivity.this, AllMapsActivity.class);
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


}
