package com.xz.wakeup2.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xz.wakeup2.MyDatabase;
import com.xz.wakeup2.R;
import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class CFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private TextView tv1;
    private android.widget.RelativeLayout liveFishPond;
    private android.widget.RelativeLayout activityfish;

    int padding = 10;

    private int deadFish;//这里控制死鱼
    private int liveFish;//这里控制活鱼
    private android.widget.LinearLayout deadFishPond;

    private int deadMax = 0;
    private int liveMax = 0;
    private int xMax;
    private int yMax;
    private int fishWidth;

    private MyDatabase mdb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c, container,false);
        this.deadFishPond = (LinearLayout) view.findViewById(R.id.deadFishPond);
        this.activityfish = (RelativeLayout) view.findViewById(R.id.activity_fish);
        this.liveFishPond = (RelativeLayout) view.findViewById(R.id.liveFishPond);
        this.tv1 = (TextView) view.findViewById(R.id.tv_1);
        TextView tv_1= (TextView) view.findViewById(R.id.tv_1);

        mdb=new MyDatabase(getContext());
        Log.d("salttttttttt", mdb.getSaltyFish()+"");
        Log.d("aliveeeeeeee", mdb.getFishAlive()+"");


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("salttttttttt", mdb.getSaltyFish()+"");
        Log.d("aliveeeeeeee", mdb.getFishAlive()+"");
        activityfish.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = liveFishPond.getWidth();
                int height = liveFishPond.getHeight();

                fishWidth = dip2px(getContext(),40);

                xMax = width / fishWidth;
                deadMax = deadFishPond.getWidth()/fishWidth;
                yMax = height / fishWidth;
                liveMax = xMax * yMax;

                activityfish.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                addFishDead(mdb.getSaltyFish());
                addFishLive(mdb.getFishAlive());
            }
        });
    }
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void addFishLive(int n){

        int childCount = liveFishPond.getChildCount();
        int index = childCount - 1;
        while(index>=0){
            liveFishPond.removeViewAt(index);
            index--;
        }
        liveFish=0;
        for(int i =0;i<n;i++){
            int y = liveFish/xMax;
            int x = liveFish%xMax;
            if(liveFish <liveMax){

                int value = new Random().nextInt(12);

                int fishId = 0;
                if(value<3){
                    fishId = R.drawable.fish_rating_bar;
                }else if(value<6){
                    fishId = R.drawable.blue_fish;
                }else if(value<9){
                    fishId = R.drawable.fish_yellow;
                }else{
                    fishId = R.drawable.fish2;
                }


                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(fishId);
                RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(dip2px(getContext(),40),dip2px(getContext(),40));
                lp.leftMargin = x*fishWidth;
                lp.topMargin = y*fishWidth;
                imageView.setLayoutParams(lp);
                liveFishPond.addView(imageView);
                doRepeatAnim(imageView);
                imageView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"i am alive",Toast.LENGTH_SHORT).show();;
                    }
                });
            }

            liveFish +=1;
        }
    }
    @Override
    public void onClick(View v){

    }
    public void addFishDead(int n){
        int childCount = deadFishPond.getChildCount();
        int index = childCount - 1;
        while(index>=0)
        {
            deadFishPond.removeViewAt(index);
            index--;
        }
        deadFish=0;
        for(int i=0;i<n;i++){
            if(deadFish < deadMax){
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.dead_fish);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(dip2px(getContext(),40),dip2px(getContext(),40));
                deadFishPond.addView(imageView,lp);
                imageView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"i am dead",Toast.LENGTH_SHORT).show();;
                    }
                });
            }
            deadFish +=1;
        }
    }
    public void doRepeatAnim(ImageView target) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationY", -padding, padding, -padding);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setDuration(1000+new Random().nextInt(1000));
        animator.start();
    }

}
