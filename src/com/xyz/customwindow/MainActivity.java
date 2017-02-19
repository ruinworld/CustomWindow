package com.xyz.customwindow;

import com.xyz.view.CustomWindow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		
		findViewById(R.id.btnShowBar).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomWindow.getInstance().showDialog(context, TAG);
				//模拟线程
				getDataFromServer();
			}
		});
		
		findViewById(R.id.btnShowPopup).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View view = LayoutInflater.from(context).inflate(R.layout.item_popwindow_pay_layout, null);
				view.findViewById(R.id.tvPayWeixin).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CustomWindow.getInstance().hidePop(TAG);
						backgroundAlpha(1.0f);
						Toast.makeText(context, "微信支付!", Toast.LENGTH_LONG).show();
					}
				});
				view.findViewById(R.id.tvPayAli).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CustomWindow.getInstance().hidePop(TAG);
						backgroundAlpha(1.0f);
						Toast.makeText(context, "支付宝支付!", Toast.LENGTH_LONG).show();
					}
				});
				CustomWindow.getInstance().showPop(TAG, view, Gravity.CENTER,windowXY(context)[0]*3/4,null,null);
				backgroundAlpha(0.5f);
			}
		});
	}
	
	private void getDataFromServer(){
		new Thread(){
			public void run() {
				try {
					CustomWindow.getInstance().setContent(TAG, "这个地方可以加上步骤的提示！");
					Thread.sleep(3*1000);
					runOnUiThread(new Runnable() {
						public void run() {
							CustomWindow.getInstance().hideDialog(TAG);
							Toast.makeText(context, "数据获取完毕!", Toast.LENGTH_LONG).show();
						}
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	private void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // 加上这个有效
	}
	
	// 程序第一次获取宽高后缓存在全局参数里，不用每次使用都要计算屏幕宽高，节省cpu，内存和电量
	public static int[] windowXY(Context context) {
		int[] wh = new int[2];
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		wh[0] = displayMetrics.widthPixels;
		wh[1] = displayMetrics.heightPixels;
		return wh;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CustomWindow.getInstance().removeDialog(TAG);
	}
	
}
