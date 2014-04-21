package com.bookworm.scanbook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.bookworm.bookshelf.LoginShelfActivity;
import com.bookworm.more.SettingTabActivity;
import com.bookworm.searchbook.OnTabActivityResultListener;
import com.bookworm.searchbook.SearchActivity;
import com.xielu.scanbook.R;
import com.xnote.activity.XNoteMainActivity;

public class MainActivity extends Activity {
    List<View> listViews;//视图列表

    Context context = null;

    LocalActivityManager manager = null;

    TabHost tabHost = null;

    private BigViewPager pager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        /*在一个Activity的一部分中显示其他Activity”要用到LocalActivityManagerity
        作用体现在manager获取View：manager.startActivity(String, Intent).getDecorView()*/

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.setup(manager);//实例化THost
        
        context=MainActivity.this;
        
        pager  = (BigViewPager) findViewById(R.id.viewpager);//ViewPager
        

        //加入3个子Activity
        Intent i1 = new Intent(context, SearchActivity.class); 
        Intent i3 = new Intent(context, XNoteMainActivity.class); 
        Intent i2 = new Intent(context, LoginShelfActivity.class); 
        Intent i4 = new Intent(context, SettingTabActivity.class);
        
        listViews = new ArrayList<View>();  //实例化listViews       
        listViews.add(manager.startActivity("T1", i1).getDecorView());
        listViews.add(manager.startActivity("T2", i2).getDecorView());
        listViews.add(manager.startActivity("T3", i3).getDecorView());
        listViews.add(manager.startActivity("T4", i4).getDecorView());
        RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabwidget, null);  
        //tabIndicator1从一个layout文件获取view（即单个选项卡）再在大布局里显示
        TextView tvTab1 = (TextView)tabIndicator1.findViewById(R.id.tv_title);
        ImageView icTab1= (ImageView)tabIndicator1.findViewById(R.id.iv_title);
        icTab1.setImageResource(R.drawable.tabs_home);
        //id是tabIndicator1的
        tvTab1.setText("Tab1");
        
       RelativeLayout tabIndicator2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabwidget,null);  
        TextView tvTab2 = (TextView)tabIndicator2.findViewById(R.id.tv_title);
        tvTab2.setText("Tab2");
        ImageView icTab2= (ImageView)tabIndicator2.findViewById(R.id.iv_title);
        icTab2.setImageResource(R.drawable.tabs_search);
       
        RelativeLayout tabIndicator3 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabwidget,null);  
        TextView tvTab3 = (TextView)tabIndicator3.findViewById(R.id.tv_title);
        tvTab3.setText("Tab3");
        ImageView icTab3= (ImageView)tabIndicator3.findViewById(R.id.iv_title);
        icTab3.setImageResource(R.drawable.tabs_sort);
        RelativeLayout tabIndicator4 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabwidget,null);  
        TextView tvTab4 = (TextView)tabIndicator3.findViewById(R.id.tv_title);
        tvTab4.setText("Tab4");
        ImageView icTab4= (ImageView)tabIndicator4.findViewById(R.id.iv_title);
        icTab4.setImageResource(R.drawable.tabs_more);
        
        tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator1).setContent(i1));
        //TabSpec的名字A，B，C才是各个tab的Id
        tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator2).setContent(i2));
        tabHost.addTab(tabHost.newTabSpec("C").setIndicator(tabIndicator3).setContent(i3));
        tabHost.addTab(tabHost.newTabSpec("D").setIndicator(tabIndicator4).setContent(i4));
        
        //为tabhost设置监听
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                 tabHost.setOnTabChangedListener(new OnTabChangeListener() {
                        @Override
                        public void onTabChanged(String tabId) {
                            if ("A".equals(tabId)) {
                                pager.setCurrentItem(0);//在tabhost的监听改变Viewpager
                            } 
                            if ("B".equals(tabId)) {
                                pager.setCurrentItem(1);
                            } 
                            if ("C".equals(tabId)) {
                                pager.setCurrentItem(2);
                            }
                            if ("D".equals(tabId)) {
                                pager.setCurrentItem(3);
                            } 
                        }
                    });
                
            }
        });

        //为ViewPager适配和设置监听
        pager.setAdapter(new MyPageAdapter(listViews));
        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setCurrentTab(position);//在Viewpager改变tabhost
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    
    
    @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN)
				&& (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
			quitDialog();
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 *退出提醒
	 */
	private void quitDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.alerm_title)
				.setIcon(null)
				.setCancelable(false)
				.setMessage(R.string.alert_quit_confirm)
				.setPositiveButton(R.string.alert_yes_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.this.finish();
							}
						})
				.setNegativeButton(R.string.alert_no_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}


private class MyPageAdapter extends PagerAdapter {
        
        private List<View> list;

        private MyPageAdapter(List<View> list) {
            this.list = list;
        }
        @Override
        public void destroyItem(ViewGroup view, int position, Object arg2) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.removeView(list.get(position));
        }
        @Override
        public void finishUpdate(View arg0) {
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.addView(list.get(position));
            return list.get(position);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }
        @Override
        public Parcelable saveState() {
            return null;
        }
        @Override
        public void startUpdate(View arg0) {
        }
    }



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 获取当前活动的Activity实例
		Activity subActivity = manager.getCurrentActivity();
		// 判断是否实现返回值接口
		if (subActivity instanceof OnTabActivityResultListener) {
			// 获取返回值接口实例
			OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
}
