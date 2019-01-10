package com.xz.wakeup2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.next.easynavigation.view.EasyNavigationBar;
import com.xz.wakeup2.R;
import com.xz.wakeup2.ui.AFragment;
import com.xz.wakeup2.ui.BFragment;
import com.xz.wakeup2.ui.CFragment;
import com.xz.wakeup2.ui.DFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EasyNavigationBar navigationBar;

    private String[] tabText = {"事件", "日历", "鱼塘", "关于"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.index, R.mipmap.calendar, R.mipmap.pool,R.mipmap.me};
    //选中时icon
    private int[] selectIcon = {R.mipmap.index1, R.mipmap.calendar1, R.mipmap.pool1, R.mipmap.me1};

    private List<android.support.v4.app.Fragment> fragments = new ArrayList<>();
    private Handler mHandler = new Handler();
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyDatabase mdb=new MyDatabase(this);
        navigationBar = findViewById(R.id.navigationBar);

        fragments.add(new AFragment());
        fragments.add(new BFragment());
        fragments.add(new CFragment());
        fragments.add(new DFragment());


        View view = LayoutInflater.from(this).inflate(R.layout.custom_add_view, null);

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .canScroll(true)
                .addAsFragment(false)
                .mode(EasyNavigationBar.MODE_ADD_VIEW)
                .addCustomView(view)
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        Log.e("Tap->Position", position + "");
                        if (position == 2) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //＋ 旋转动画
                                    if (flag) {
                                        navigationBar.getCustomAddView().animate().rotation(180).setDuration(400);
                                        Intent intent_new = new Intent(MainActivity.this, NewDdlActivity.class);
                                        intent_new.putExtra("editable", 1);
                                        startActivityForResult(intent_new, 1);
                                    } else {
                                        navigationBar.getCustomAddView().animate().rotation(0).setDuration(400);
                                    }
                                    flag = !flag;
                                }
                            });
                        }
                        return false;
                    }
                })
                .build();

    }

    public EasyNavigationBar getNavigationBar() {
        return navigationBar;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    navigationBar.getCustomAddView().animate().rotation(0).setDuration(400);
                    flag=true;
                }
                break;
            default:

        }

    }

}