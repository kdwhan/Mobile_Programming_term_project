package daewhan.example.com.mp_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by daewhan on 2015-12-18.
 */
public class MainActivity extends AppCompatActivity{

    Button bt_map;
    Button bt_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
