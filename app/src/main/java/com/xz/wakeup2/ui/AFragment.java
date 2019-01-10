package com.xz.wakeup2.ui;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.xz.wakeup2.NewDdlActivity;
import com.xz.wakeup2.MyDatabase;
import com.xz.wakeup2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xz on 2018/12.
 */

public class AFragment extends android.support.v4.app.Fragment {
    private List<String> eventList= new ArrayList<>();
    private List<String[]> a;
    private TextView textView;
    private ImageView imageView;
    private ListView listView;
    private MyDatabase mdb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_a, null);
        textView = (TextView)view.findViewById(R.id.textView);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        listView = (ListView)view.findViewById(R.id.list_view);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        getEvents();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.activity_list_item, android.R.id.text1, eventList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] event = a.get(position);
                Intent intent_edit = new Intent(getContext(), NewDdlActivity.class);
                intent_edit.putExtra("editable",0);
                intent_edit.putExtra("original_event", event);
                startActivity(intent_edit);
                //后面是刷新数据库后刷新list列表的代码了
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                if(eventList.size() == 0){
                    listView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);

                }
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        getEvents();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.activity_list_item, android.R.id.text1, eventList);
        listView.setAdapter(adapter);
    }

    private void getEvents(){
        mdb = new MyDatabase(getContext());
        a =mdb.getData();
        eventList.clear();
        for(int i =0;i<a.size();i++)
            eventList.add(a.get(i)[0]+"/"+a.get(i)[1]+"/"+a.get(i)[2]+" "+a.get(i)[3]+":"+a.get(i)[4]+" "+a.get(i)[5]);

    }
}