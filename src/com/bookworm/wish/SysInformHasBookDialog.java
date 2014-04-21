package com.bookworm.wish;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookworm.app.BookApplication;
import com.bookworm.utls.HttpUtil;
import com.xielu.scanbook.R;




	 
	public class SysInformHasBookDialog extends Dialog {
		
		
	    public SysInformHasBookDialog(Context context) {
	        super(context);
	        
	    }
	    public SysInformHasBookDialog(Context context, int theme) {
	        super(context, theme);
	    }
	 
	    /**
	     * Helper class for creating a custom dialog
	     */
	    public static class Builder {
	 
	        private Context context;
	        private String title;
	        private String message;
	        private String positiveButtonText;
	        private String negativeButtonText;
	        private View contentView;
	      private String responserId;
	      private String wishId;
		    private  EditText edit;
		    
	        private DialogInterface.OnClickListener 
	                        positiveButtonClickListener,
	                        negativeButtonClickListener;
	 
	        public Builder(Context context, String wishId,String responserId) {
	            this.context = context;
	           this.responserId = responserId;
	           this.wishId = wishId;
	        }
	 
	        /**
	         * Set the Dialog message from String
	         * @param title
	         * @return
	         */
	        public Builder setMessage(String message) {
	            this.message = message;
	            return this;
	        }
	 
	        /**
	         * Set the Dialog message from resource
	         * @param title
	         * @return
	         */
	        public Builder setMessage(int message) {
	            this.message = (String) context.getText(message);
	            return this;
	        }
	 
	        /**
	         * Set the Dialog title from resource
	         * @param title
	         * @return
	         */
	        public Builder setTitle(int title) {
	            this.title = (String) context.getText(title);
	            return this;
	        }
	 
	        /**
	         * Set the Dialog title from String
	         * @param title
	         * @return
	         */
	        public Builder setTitle(String title) {
	            this.title = title;
	            return this;
	        }
	 
	        /**
	         * Set a custom content view for the Dialog.
	         * If a message is set, the contentView is not
	         * added to the Dialog...
	         * @param v
	         * @return
	         */
	        public Builder setContentView(View v) {
	            this.contentView = v;
	            return this;
	        }
	 
	        /**
	         * Set the positive button resource and it's listener
	         * @param positiveButtonText
	         * @param listener
	         * @return
	         */
	        public Builder setPositiveButton(int positiveButtonText,
	                DialogInterface.OnClickListener listener) {
	            this.positiveButtonText = (String) context
	                    .getText(positiveButtonText);
	            this.positiveButtonClickListener = listener;
	            return this;
	        }
	 
	        /**
	         * Set the positive button text and it's listener
	         * @param positiveButtonText
	         * @param listener
	         * @return
	         */
	        public Builder setPositiveButton(String positiveButtonText,
	                DialogInterface.OnClickListener listener) {
	            this.positiveButtonText = positiveButtonText;
	            this.positiveButtonClickListener = listener;
	            return this;
	        }
	 
	        /**
	         * Set the negative button resource and it's listener
	         * @param negativeButtonText
	         * @param listener
	         * @return
	         */
	        public Builder setNegativeButton(int negativeButtonText,
	                DialogInterface.OnClickListener listener) {
	            this.negativeButtonText = (String) context
	                    .getText(negativeButtonText);
	            this.negativeButtonClickListener = listener;
	            return this;
	        }
	 
	        /**
	         * Set the negative button text and it's listener
	         * @param negativeButtonText
	         * @param listener
	         * @return
	         */
	        public Builder setNegativeButton(String negativeButtonText,
	                DialogInterface.OnClickListener listener) {
	            this.negativeButtonText = negativeButtonText;
	            this.negativeButtonClickListener = listener;
	            return this;
	        }
	 
	        /**
	         * Create the custom dialog
	         */
	        public WriteWishDialog create() {
	            LayoutInflater inflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            // instantiate the dialog with the custom Theme
	            final WriteWishDialog dialog = new WriteWishDialog(context, 
	            		R.style.dialog);
	            View layout = inflater.inflate(R.layout.guancang_dialog, null);
	            //dialog.addContentView(layout, new LayoutParams(  LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	            // set the dialog title
	            ((TextView) layout.findViewById(R.id.title_bookName)).setText(title);
	            edit = (EditText) layout.findViewById(R.id.editText_dialog);
	            edit.setVisibility(View.VISIBLE);
	            // set the confirm button
	            Button positive = (Button) layout.findViewById(R.id.positiveButton);
	            positive.setVisibility(View.VISIBLE);
	            if (positiveButtonText != null) {
	            	
	            	positive.setText(positiveButtonText);
	            	positive.setOnClickListener(new View.OnClickListener() {
	                                public void onClick(View v) {
	                                	if(!edit.getText().toString().equals("")){
	                                		Log.i("ok",BookApplication.userNum+" "+wishId+"  "+message);
	                                		AT at = new AT(wishId, BookApplication.userNum, edit.getText().toString());
	                                		at.start();
	                                		dialog.dismiss();
	                                	}
	                                }
	                            });   
	            }
	            if (negativeButtonText != null) {
	                ((Button) layout.findViewById(R.id.negativeButton))
	                        .setText(negativeButtonText);
	                if (negativeButtonClickListener != null) {
	                    ((Button) layout.findViewById(R.id.negativeButton))
	                            .setOnClickListener(new View.OnClickListener() {
	                                public void onClick(View v) {
	                                	AT at = new AT(wishId, BookApplication.userNum, edit.getText().toString());
                                		at.start();
                                		dialog.dismiss();
	                                }
	                            });
	                }
	            } else {
	                // if no confirm button just set the visibility to GONE
	                layout.findViewById(R.id.negativeButton).setVisibility(
	                        View.GONE);
	            }
	            
	            
	                // if no confirm button just set the visibility to GONE
	          
	            // set the cancel button
	       
	            // set the content message
	            if (message != null) {
	            	//int color = Resources.getSystem().getColor(R.color.bisque);
	                ((TextView) layout.findViewById(
	                		R.id.message)).setText(message);
	               
	              
	            } else if (contentView != null) {
	                // if no message set
	                // add the contentView to the dialog body
	                ((LinearLayout) layout.findViewById(R.id.content))
	                        .removeAllViews();
	                ((LinearLayout) layout.findViewById(R.id.content))
	                        .addView(contentView, 
	                                new LayoutParams(
	                                        LayoutParams.WRAP_CONTENT, 
	                                        LayoutParams.WRAP_CONTENT));
	            }
	            dialog.setContentView(layout);
	            return dialog;
	        }
	 
	    }
	    
	    
	  
	    private static class AT extends Thread{
	    	private String wishId ;
	    	private String userId;
	    	private String msg;
	    	public AT(String wishId, String userId, String msg){
	    		this.wishId=wishId;
	    		this.msg=msg;
	    		
	    		this.userId = userId;
	    	}
			@Override
			public void run() {
			
				HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_WISH_URL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("wishId", wishId));
				params.add(new BasicNameValuePair("userId", userId));
				params.add(new BasicNameValuePair("msg", msg));
				params.add(new BasicNameValuePair("what", "6"));	//servlet筛选对应功能是使用
				try {
					httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
					new DefaultHttpClient().execute(httpRequest);
					//Toast.makeText(context, "心愿已发动，请您耐心等待.....", 2).show();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	    	
	    	
	    }
	    
	    
	 
	}

