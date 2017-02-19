package com.xyz.view;

import java.util.HashMap;

import com.xyz.customwindow.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 *
 * @author ruin
 *
 *         2016年11月27日
 *
 */
public class CustomWindow {
	private static final String TAG = CustomWindow.class.getSimpleName();
	private static CustomWindow window;
	private HashMap<String, Dialog> dialogs;
	private HashMap<String, View> views;
	private HashMap<String, PopupWindow> popupWindows;
	
	// 初始化HashMap
	private CustomWindow() {
		dialogs = new HashMap();
		popupWindows = new HashMap();
		views = new HashMap();
	}

	// 单例模式获取实例对象
	public static CustomWindow getInstance() {
		if (window == null)
			synchronized (CustomWindow.class) {
				window = new CustomWindow();
			}

		return window;
	}

	// 显示并添加Dialog
	public void showDialog(Context context,String key) {
		if (dialogs.containsKey(key)) {
			if (dialogs.get(key).isShowing())
				return;
			dialogs.get(key).show();
		} else {
			Dialog dialog = new Dialog(context, R.style.progress_dialog);
			View view = View.inflate(context, R.layout.popwindow_progressbar, null);
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			addDialog(key, dialog);
			addView(key, view);
		}
	}
	
	public void setContent(String key,String content){
		if(views.containsKey(key)){
			TextView tvContent = (TextView) views.get(key).findViewById(R.id.tvContent);
			
			if(null == content){
				tvContent.setText("加载中……");
			}else{
				tvContent.setText(content);
			}
		}
	}

	public void showPop(String key, View view, int gravity, Integer width, Integer height, Integer anim) {
		if (popupWindows.containsKey(key)) {
			if (popupWindows.get(key).isShowing())
				return;
			popupWindows.get(key).showAtLocation(view, gravity, 0, 0);
		} else {
			PopupWindow popupWindow = null;
			popupWindow = new PopupWindow(view);
			popupWindow.setFocusable(true);

			if (width != null) {
				popupWindow.setWidth(width);
			} else {
				popupWindow.setWidth(LayoutParams.MATCH_PARENT);
			}

			if (height != null) {
				popupWindow.setHeight(height);
			} else {
				popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			}

			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			if (anim != null) {
				popupWindow.setAnimationStyle(anim);
			}
			if(gravity == Gravity.BOTTOM){
				popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
				popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			}
			popupWindow.showAtLocation(view, gravity, 0, 0);
			addPop(key, popupWindow);
		}
	}

	// 显示并添加PopupWindow
	public void showPop(String key, View view, int gravity) {
		showPop(key, view, gravity, null, null, null);
	}

	public void showPop(String key, View view, int gravity, Integer anim) {
		showPop(key, view, gravity, null, null, anim);
	}

	public void setPopDismissListener(String key, OnDismissListener onDismissListener) {
		if (!popupWindows.containsKey(key)) {
			return;
		}

		popupWindows.get(key).setOnDismissListener(onDismissListener);
	}

	// 隐藏Dialog
	public void hideDialog(String key) {
		setContent(key,null);
		
		if (dialogs.containsKey(key)) {
			if (dialogs.get(key).isShowing()){
				dialogs.get(key).dismiss();
			}
		}
	}

	// 隐藏PopupWindow
	public void hidePop(String key) {
		if (popupWindows.containsKey(key)) {
			if (popupWindows.get(key).isShowing())
				popupWindows.get(key).dismiss();
		}
	}

	// 移除Dialog
	public void removeDialog(String key) {
		if (dialogs.containsKey(key)){
			dialogs.remove(key);
		}
		
		if (views.containsKey(key)){
			views.remove(key);
		}
	}

	// 移除PopupWindow
	public void removePop(String key) {
		if (popupWindows.containsKey(key))
			popupWindows.remove(key);
	}

	// 添加Dialog
	private void addDialog(String key, Dialog dialog) {
		if (!dialogs.containsKey(key))
			dialogs.put(key, dialog);
	}
	
	// 添加View
	private void addView(String key, View view) {
		if (!views.containsKey(key))
			views.put(key, view);
	}

	// 添加PopupWindow
	private void addPop(String key, PopupWindow popWindow) {
		if (!popupWindows.containsKey(key))
			popupWindows.put(key, popWindow);
	}

	// 移除所有Dialog
	public void removeAllDialog() {
		dialogs.clear();
		dialogs = null;
		views.clear();
		views = null;
	}

	// 移除所有PopupWindow
	public void removeAllPop() {
		popupWindows.clear();
		popupWindows = null;
	}

}
