package com.xz.wakeup2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;

public class NewDdlActivity extends AppCompatActivity{

    private int year, month, day, minute, hour;
    private int oyear,omonth,oday,ominute,ohour;
    private String oevent;
    private String event;
    private EditText datePicker;
    private EditText timePicker;
    private RatingBar ratingBar;
    private Switch editSwitch;
    private Calendar c = Calendar.getInstance();
    int rating=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ddl);
        final EditText editText = (EditText)findViewById(R.id.editText);
        timePicker=(EditText)findViewById(R.id.editText4);
        datePicker = (EditText)findViewById(R.id.editText3);
        ratingBar=findViewById(R.id.ratingBar);
        //Edit switch
        editSwitch=findViewById(R.id.switch1);
        final Button ok = (Button)findViewById(R.id.OkButton);
        final Button updateButton=(Button)findViewById(R.id.UpdateButton);
        final MyDatabase mdb=new MyDatabase(this);
        //删除事件的按钮
        final Button delete = (Button)findViewById(R.id.deleteButton);
        //点击事件没有写

        //识别是从哪里启动的这个activity
        int editable = getIntent().getExtras().getInt("editable");
        if(editable == 0)
        {
            ok.setVisibility(View.GONE);
            editSwitch.setChecked(false);
            editText.setEnabled(false);
            datePicker.setEnabled(false);
            timePicker.setEnabled(false);
            ratingBar.setEnabled(false);
            updateButton.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
            String[] a=getIntent().getExtras().getStringArray("original_event");
            oyear=Integer.parseInt(a[0]);
            omonth=Integer.parseInt(a[1]);
            oday=Integer.parseInt(a[2]);
            ohour=Integer.parseInt(a[3]);
            ominute=Integer.parseInt(a[4]);
            oevent=a[5];
            month=omonth;
            year=oyear;
            day=oday;
            minute=ominute;
            hour=ohour;
            timePicker.setText(a[0]+"/"+a[1]+"/"+a[2]);
            datePicker.setText(a[3]+": "+a[4]);
            editText.setText(a[5]);
        }
        else
        {
            updateButton.setVisibility(View.GONE);
            editSwitch.setVisibility(View.GONE);
        }
        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editText.setEnabled(true);
                    datePicker.setEnabled(true);
                    timePicker.setEnabled(true);
                    ratingBar.setEnabled(true);
                    delete.setVisibility(View.GONE);
                    updateButton.setVisibility(View.VISIBLE);
                    ok.setVisibility(View.GONE);
                }
                else{
                    editText.setEnabled(false);
                    datePicker.setEnabled(false);
                    timePicker.setEnabled(false);
                    ratingBar.setEnabled(false);
                    delete.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.GONE);
                    ok.setVisibility(View.GONE);
                }
            }
        });
        //ok button
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                event=editText.getText().toString();

                if(year>0&&month>0&&month<13&&day>0&&minute>=0&&hour>=0&&event!=null){
                    mdb.addData(year,month,day,hour,minute,event);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //delete button
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mdb.deleteData(oyear,omonth,oday,ohour,ominute,oevent);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //update button
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                event=editText.getText().toString();
                if(year>0&&month>0&&month<13&&day>0&&minute>=0&&hour>=0&&event!=null){
                    mdb.updateData(oyear,omonth,oday,ohour,ominute,oevent,year,month,day,hour,minute,event);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        //Date picker
        //datePicker.setInputType(InputType.TYPE_NULL);//不显示系统输入键盘
        datePicker.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });

        //Time picker
        timePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        timePicker.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showTimePickerDialog();
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
        //Rating bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating1, boolean fromUser) {
                rating=(int)ratingBar.getRating();
            }
        });


    }

    private void showDatePickerDialog() {

        new DatePickerDialog(NewDdlActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                datePicker.setText(year1+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                year=year1;
                month=monthOfYear+1;
                day=dayOfMonth;
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimePickerDialog(){
        new TimePickerDialog(NewDdlActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override

            public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {

                //同DatePickerDialog控件

                timePicker.setText(String.format("%02d",hourOfDay)+":"+String.format("%02d",minute));
                hour=hourOfDay;
                minute=minute1;
            }

        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();

        //将页面TextView的显示更新为最新时间
    }
    //按下返回键的时候也可以把“新”字转回来
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
