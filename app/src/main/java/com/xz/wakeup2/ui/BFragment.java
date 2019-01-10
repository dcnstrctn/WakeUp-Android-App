package com.xz.wakeup2.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.xz.wakeup2.MyDatabase;
import com.xz.wakeup2.R;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jue on 2018/6/2.
 */

public class BFragment extends android.support.v4.app.Fragment implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener{
    private static boolean isMiUi = false;
    TextView mTextMonthDay;
    TextView mTextYear;
    TextView mTextLunar;
    TextView mTextCurrentDay;
    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    RecyclerView mRecyclerView;
    MyDatabase mdb;
    private List<String[]> a;
    private List<String[]> dayEvents;
    List<String> stringList;//holds elements for recycleview


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container,false);
        mdb = new MyDatabase(getContext());
        dayEvents = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initView(view);
        initData(dayEvents);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //dayEvents = new ArrayList<>();
        initData(dayEvents);
    }
    @SuppressLint("SetTextI18n")
    protected void initView(View v){
        //setStatusBarDarkMode();
        mTextMonthDay = (TextView) v.findViewById(R.id.tv_month_day);
        mTextYear = (TextView) v.findViewById(R.id.tv_year);
        mTextLunar = (TextView) v.findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) v.findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) v.findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) v.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        v.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = (CalendarLayout) v.findViewById(R.id.calendarLayout);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }
    protected void initData(List<String[]> dayEvents){
        List<Calendar> schemes = new ArrayList<>();
        a=mdb.getData();
        for(int i =0;i<a.size();i++){
            int year=Integer.parseInt(a.get(i)[0]);
            int month=Integer.parseInt(a.get(i)[1]);
            int day = Integer.parseInt(a.get(i)[2]);
            schemes.add(getSchemeCalendar(year, month, day, 0xFFe69138,"事"));
        }
        mCalendarView.setSchemeDate(schemes);

        if(dayEvents.size()==0){
            stringList=new ArrayList<>();
            stringList.add("下拉切换月视图");
            stringList.add("上划切换周视图");
        }
        else {
            stringList=new ArrayList<>();
            for (int i = 0; i < dayEvents.size(); i++) {
                stringList.add(dayEvents.get(i)[0] + "/" + dayEvents.get(i)[1] + "/" + dayEvents.get(i)[2]
                        + " " + dayEvents.get(i)[3] + ":" + dayEvents.get(i)[4] + " " + dayEvents.get(i)[5]);
            }
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MyAdapter(getContext(),stringList));
    }


    static {
        try {
            @SuppressLint("PrivateApi") Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            isMiUi = version.compareTo("V6") >= 0 && Build.VERSION.SDK_INT < 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800,"假");
        calendar.addScheme(0xFF008800,"节");
        return calendar;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        Log.d("选择的日子是",day+"");
        dayEvents = mdb.getDataByDate(year, month, day);
        initData(dayEvents);
    }


    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onClick(View v) {

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        Context context;
        List<String> stringList;

        public MyAdapter(Context context, List<String> stringList) {
            this.context = context;
            this.stringList = stringList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.item_string,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(stringList.get(position));
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public MyViewHolder(View itemView) {
                super(itemView);
                textView= (TextView) itemView.findViewById(R.id.string);
            }
        }
    }

}