package com.bookworm.searchbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookworm.dbmodel.BookBean;
import com.xielu.scanbook.R;

public class XnoteLendBooksAdapter extends BaseExpandableListAdapter{
	
	private ArrayList<String> group = null;  
	private ArrayList<List<BookBean>> child = null; 
	private Context c =null;
	private LayoutInflater inflator;
	/*
	 * 由于大项只有一个，因此只需要一个groupName即可
	 */
	public XnoteLendBooksAdapter(Context c , ArrayList<List<BookBean>> currentLendBookList , String groupName){
		this.child = currentLendBookList;
		this.c = c;
		this.group = new ArrayList<String>();
		this.group.add(groupName);
		this.inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	//-----------------Child----------------//
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return child.get(groupPosition).size();
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View v, ViewGroup parent) {
		String dueDate = child.get(groupPosition).get(childPosition).getmDueDate();
		String string = child.get(groupPosition).get(childPosition).getmTitle()+"|"+dueDate;
		v = inflator.inflate(R.layout.lendbooklistview_childitem, null);
		TextView msg = (TextView)v.findViewById(R.id.Textview_lendBook_childmsg);
		msg.setText(string);
		
		ImageView  iv=(ImageView)v.findViewById(R.id.imageView_lendbook_progress);
		iv.setBackgroundResource(R.drawable.progressbar_icon_red);
		float textviewWidth = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		Log.i("textviewWidth",""+textviewWidth);
		
		float percent = getProgressImageWidthPecent(dueDate.split(" ")[1]);
		LayoutParams para= iv.getLayoutParams();
        para.height = 30;
        para.width = (int)(textviewWidth*percent);
        
        if(percent < 0.3){
        	iv.setBackgroundResource(R.drawable.progressbars_icon_blue);
        }else if( percent >=0.3 && percent <0.6){
        	iv.setBackgroundResource(R.drawable.progressbars_icon_green);
        }else if(percent >=0.6 && percent <0.8){
        	iv.setBackgroundResource(R.drawable.progressbars_icon_yellow);
        }
        
        iv.setLayoutParams(para);
		return v;
	}
	
	//----------------Group----------------//
	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}				

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}	
	
	@Override
	public int getGroupCount() {
		return group.size();
	}	
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View v, ViewGroup parent) {
		//String string = group.get(groupPosition);  
		v = inflator.inflate(R.layout.lendbooklistview_groupitem, null);
		TextView groupName = (TextView)v.findViewById(R.id.textView_lendbook_group);
		groupName.setText(group.get(groupPosition));
		groupName.setTextColor(Color.BLUE);
		return v;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}		

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	private float getProgressImageWidthPecent(String dueDate){
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String nowDate = sft.format(date);
        Long time = getQuot(dueDate, nowDate);
        return 1-(float) time / 60l;
        
	}
	private long getQuot(String time1, String time2){
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
        Date date1 = ft.parse( "20"+time1 );
        Date date2 = ft.parse( time2 );
        //System.out.println(time1 +"   "+time2);
        quot = date1.getTime() - date2.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
    } catch (ParseException e) {
        e.printStackTrace();
    }
        System.out.println( "距离"+time1+" 还有 "+quot+" 天" );
        return quot;
    }
	
}

