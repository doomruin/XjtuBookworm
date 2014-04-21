package com.bookworm.bookshelf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.app.BookApplication;
import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.util.UserLendBookDao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xielu.scanbook.R;

public class ShowUserDetail extends Activity {
	
	private SharedPreferences sp;

	private Button userdetail_goMap, userdetail_setuser;
	private ImageView userdetail_image, share_image;
	private TextView userdetail_name, userdetail_role, userdetail_score,
			userdetail_grade;
	private ProgressBar userdetail_misssion1, userdetail_misssion2,
			userdetail_misssion3, userdetail_misssion4, userdetail_misssion5;
	
	private RelativeLayout layout_myreadhistory,layout_booksiwant,layout_booksyoumaylike;

	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	public static String IMAGE_FILE_NAME = "";
	// 控制图像显示
	DisplayImageOptions options;

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdetail);
		
		//获取可能存在的密码
				sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE); 

		/*--配置图像显示工具--*/
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.head1)
				.showImageForEmptyUri(R.drawable.head1)
				.showImageOnFail(R.drawable.head1)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// 获取当前用户信息
		IMAGE_FILE_NAME = BookApplication.userNum + "_avator.jpg";

		userdetail_goMap = (Button) findViewById(R.id.userdetail_goMap);
		userdetail_image = (ImageView) findViewById(R.id.userdetail_image);
		share_image = (ImageView) findViewById(R.id.share_image);
		userdetail_name = (TextView) findViewById(R.id.textview_username);
		userdetail_role = (TextView) findViewById(R.id.textview_usernum);
		userdetail_score = (TextView) findViewById(R.id.textview_userdegree);
		userdetail_name.setText("昵称："+BookApplication.userName);
		userdetail_score.setText("等级: 小书童");
		userdetail_role.setText("学工号:"+BookApplication.userNum);
		userdetail_name.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showEditNameDialog();
				
			}
		}
		);
		
		layout_myreadhistory = (RelativeLayout) findViewById(R.id.layout_myreadhistory);
		layout_booksiwant = (RelativeLayout) findViewById(R.id.layout_booksiwant);
		layout_booksyoumaylike = (RelativeLayout) findViewById(R.id.layout_booksyoumaylike);
		
		layout_booksyoumaylike.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//查数据----改成访问用户兴趣
				UserLendBookDao.openDB(ShowUserDetail.this);
				List<BookBean> books = UserLendBookDao.findAllReadBook(BookApplication.userNum);
				UserLendBookDao.closeDB();
				
				if(books != null && books.size() > 0)
				{
					//跳转到列表页
					Intent intent=new Intent(ShowUserDetail.this,ShowBookModelList.class);
					intent.putExtra("title","猜你可能喜欢");
					intent.putParcelableArrayListExtra("bookList", (ArrayList<? extends Parcelable>) books);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(ShowUserDetail.this, "还有发现你的兴趣啊，快去设置界面设置一下！", Toast.LENGTH_SHORT).show();
				}
				
				
			}
			
		});
		layout_booksiwant.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//查数据
				UserLendBookDao.openDB(ShowUserDetail.this);
				List<BookBean> books = UserLendBookDao.findAllReadBook(BookApplication.userNum);
				UserLendBookDao.closeDB();
				
				if(books != null && books.size() > 0)
				{
					//跳转到列表页
					Intent intent=new Intent(ShowUserDetail.this,ShowBookModelList.class);
					intent.putExtra("title","我想看这些书");
					intent.putParcelableArrayListExtra("bookList", (ArrayList<? extends Parcelable>) books);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(ShowUserDetail.this, "多看看书吧！你还没有任何想看的书！", Toast.LENGTH_SHORT).show();
				}
				
				
			}
			
		});
		
		layout_myreadhistory.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//查数据
				UserLendBookDao.openDB(ShowUserDetail.this);
				List<BookBean> books = UserLendBookDao.findAllReadBook(BookApplication.userNum);
				UserLendBookDao.closeDB();
				
				if(books != null && books.size() > 0)
				{
					//跳转到列表页
					Intent intent=new Intent(ShowUserDetail.this,ShowBookModelList.class);
					intent.putExtra("title","我看的那些书");
					intent.putParcelableArrayListExtra("bookList", (ArrayList<? extends Parcelable>) books);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(ShowUserDetail.this, "懒家伙！你还没有任何阅读记录！", Toast.LENGTH_SHORT).show();
				}
				
				
			}
			
		});

		userdetail_image.setOnClickListener(listener);
		File avator = null;
		if(BookApplication.userAvator != null && !BookApplication.userAvator.equals(""))
		{
			avator = new File(Environment.getExternalStorageDirectory(),
				BookApplication.userAvator);
			if(avator.exists())
			{
				ImageLoader.getInstance().displayImage(Uri.fromFile(avator).toString(), userdetail_image,
						options);
				
				
			}
		}

		share_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String temp = "我在使用交大书虫，你呢";
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享你的成果");
				intent.putExtra(Intent.EXTRA_TEXT, temp);
				startActivity(Intent.createChooser(intent, "分享你的成果"));

			}

		});

		userdetail_goMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// LoginShelfActivity activity =
				// (LoginShelfActivity)getParent();
				// activity.showMarkView();
				finish();
			}

		});

	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog();
		}
	};
	
	private void showEditNameDialog()
	{
		final EditText edit = new EditText(this);
		edit.setSingleLine(true);
		
		new AlertDialog.Builder(this).setTitle("修改昵称").setView(edit).setPositiveButton(
				"确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BookApplication.userName = edit.getText().toString();
						userdetail_name.setText("昵称："+BookApplication.userName);
						Toast.makeText(ShowUserDetail.this, "更改成功", Toast.LENGTH_SHORT).show();
						//放到数据库中
						sp.edit().putString("username", BookApplication.userName).commit();
					}
				}).
		setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	public boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(ShowUserDetail.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			userdetail_image.setImageDrawable(drawable);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str = sdf.format(new Date());
			String name = BookApplication.userNum + "_" + str + ".jpg";
			
			//使用此流读取
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        //第二个参数影响的是图片的质量，但是图片的宽度与高度是不会受影响滴
			photo.compress(Bitmap.CompressFormat.JPEG, 80, baos);
	        //这个函数能够设定图片的宽度与高度
	        //Bitmap map = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
	        //把数据转为为字节数组
	        byte[] byteArray = baos.toByteArray();
			
			boolean flag = Tools.putByteArrayToFile(byteArray, name);
			if(flag)
			{
				BookApplication.userAvator = name;
				Toast.makeText(ShowUserDetail.this, "更改成功", Toast.LENGTH_SHORT).show();
				//放到数据库中
				sp.edit().putString("avator", name).commit();
			}
			else
			{
				Toast.makeText(ShowUserDetail.this, "更改失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
