package com.example.aldrac.pictionis;

/**
 * Created by AlDrac on 22/03/2018.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
        import android.util.DisplayMetrics;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
import android.widget.TextView;

import com.example.aldrac.pictionis.PaintView;

public class DrawPlace extends AppCompatActivity {

    private PaintView paintView;


    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent in;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    //return true;
                    in=new Intent(getBaseContext(),DrawPlace.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                    break;




                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paintview);
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        /*mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.drawmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.emboss:
                paintView.emboss();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
            case R.id.setSizeBrushSmall:
                paintView.setSizeBrush(20);
                return true;
            case R.id.setSizeBrushMedium:
                paintView.setSizeBrush(40);
                return true;
            case R.id.setSizeBrushLarge:
                paintView.setSizeBrush(60);
                return true;
            case R.id.setCurrentColorBlack:
                paintView.setCurrentColor(Color.BLACK);
                return true;
            case R.id.setCurrentColorGreen:
                paintView.setCurrentColor(Color.GREEN);
                return true;
            case R.id.setCurrentColorBlue:
                paintView.setCurrentColor(Color.BLUE);
                return true;
            case R.id.setCurrentColorRed:
                paintView.setCurrentColor(Color.RED);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}