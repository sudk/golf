package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.R;
import com.jason.golf.adapters.GScoreDetailBeanAdapter;
import com.jason.golf.classes.GScore;
import com.jason.golf.classes.GScoreDetailBean;
import com.jason.golf.dialog.ScroseDetailNewDialog;
import com.jason.golf.dialog.WarnDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

public class GScoreDetailsFragment extends Fragment implements TabListener {
	
	public static final String BROADCAST_REFRESH_ACTION = "com.jason.golf.score.refresh";

	private static final int REQUESTCODE_CTIY = 1001;

	private static final String TAG = MainActivity.class.getSimpleName();

	public static final String KEY_SCORE_ID = "key_score_id";
	public static final String KEY_HOLE_TOTAL = "key_hole_total";

	private String _scoreId;
	private int _holeTotal;

	private SwipeListView mScoreDetailList;

	private GScoreDetailBeanAdapter mAdapter;

	private ArrayList<GScoreDetailBean> _beans;

	private GScore _scoreInfo;

	private LocalBroadcastManager mLocalBroadcastManager;
	
	public static GScoreDetailsFragment Instance() {
		return new GScoreDetailsFragment();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		_scoreId = getArguments().getString(KEY_SCORE_ID, "");
		_holeTotal = Integer.parseInt(getArguments().getString(KEY_HOLE_TOTAL, "0"));
		_beans = new ArrayList<GScoreDetailBean>();
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
		setHasOptionsMenu(true);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initialTabs();
		
		View v = inflater.inflate(R.layout.fragment_score_info, null);
		mScoreDetailList = (SwipeListView) v.findViewById(R.id.scoreList);
		mAdapter = new GScoreDetailBeanAdapter(getActivity(), _beans, mScoreDetailList);
		mScoreDetailList.setAdapter(mAdapter);
		mScoreDetailList
				.setSwipeListViewListener(new BaseSwipeListViewListener() {

					@Override
					public void onStartOpen(int position, int action,
							boolean right) {

						Log.d(TAG, "onStartOpen");
					}

					@Override
					public void onStartClose(int position, boolean right) {

						Log.d(TAG, "onStartClose");
					}

					@Override
					public void onClickFrontView(int position) {

						Log.d(TAG, "onClickFrontView");
					}

					@Override
					public void onClickBackView(int position) {

						Log.d(TAG, "onClickBackView");
					}

					@Override
					public void onDismiss(int[] reverseSortedPositions) {

					}

				});
		
		
		_reciver = new BroadcastReceiver (){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
				int position = intent.getIntExtra("Position", -1);
				if (intent.getBooleanExtra("Del", false)) {
					// if DELETE item， dismiss the item
					mAdapter.removeItem( position );
					mScoreDetailList.closeAnimate(position);
					mScoreDetailList.dismiss(position);
				} else {
					// if Not Delete item, just closing adnimation is OK.
					if(position > 0)
						mScoreDetailList.closeAnimate(position);
				}
				queryScoreDetails();
			}
			
		};

		// queryScoreInfo();
		queryScoreDetails();
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter(BROADCAST_REFRESH_ACTION);
		mLocalBroadcastManager.registerReceiver(_reciver, filter);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(_reciver);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.score_detail_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case R.id.action_add:

			int count = _beans.size();

			if (count == _holeTotal || _holeTotal == 0)

				break;

			int newHoleNum = 1;

			if (count > 0) {

				for (int i = 0, length = count; i < length; i++) {
					GScoreDetailBean bean = _beans.get(i);
					if (bean.getHoleNo() - 1 > i) {
						newHoleNum = i + 1;
						break;
					} else {
						newHoleNum++;
					}
				}

			}

			ScroseDetailNewDialog dialog = new ScroseDetailNewDialog(getActivity(), _scoreId, newHoleNum);
			dialog.show();

			break;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		RemoveTabs();
	}

	private void initialTabs() {
		ActionBar bar = ((ActionBarActivity) getActivity()).getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		if (bar.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}

		bar.addTab(bar.newTab().setText("前九洞").setTag("front")	.setTabListener(this));
		bar.addTab(bar.newTab().setText("后九洞").setTag("back").setTabListener(this));
		bar.setTitle(R.string.score_details);

	}

	private void RemoveTabs() {
		ActionBar bar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE,
				ActionBar.DISPLAY_SHOW_TITLE);

		bar.removeAllTabs();
	}

	private void queryScoreDetails() {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();

		try {

			params.put("cmd", "score/dlist");
			params.put("score_id", _scoreId);
			params.put("_pg_", "0");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(getActivity(), params,
				new HttpCallback() {

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						// WarnDialog dialog =
						// WarnDialog.newInstance(getActivity());
						// dialog.setTitle(R.string.flee_cancel_good).setMessage(R.string.flee_cancel_good_sucess)
						// .setPositiveBtn(R.string.confirm, new
						// DialogInterface.OnClickListener() {
						//
						// @Override
						// public void onClick(DialogInterface arg0, int arg1) {
						// // TODO Auto-generated method stub
						// ((ActionBarActivity)getActivity()).onSupportNavigateUp();
						// }
						// });
						//
						// dialog.show(getFragmentManager(), "FleeDelSuccess");

						try {
							JSONArray data = new JSONArray(res);

							_beans.clear();

							for (int i = 0, length = data.length(); i < length; i++) {

								JSONObject item = data.getJSONObject(i);
								GScoreDetailBean s = new GScoreDetailBean();
								s.initialize(item);
								_beans.add(s);

							}

							mAdapter.swapData(_beans);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.sucessData(res);
					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub
						WarnDialog dialog = WarnDialog
								.newInstance(getActivity());
						dialog.setTitle(R.string.flee_cancel_good)
								.setMessage(res)
								.setPositiveBtn(R.string.confirm,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												// TODO Auto-generated method
												// stub

											}
										});

						dialog.show(getFragmentManager(), "DListFaild");

						super.faildData(code, res);
					}

				});

		GThreadExecutor.execute(r);

	}

	private void queryScoreInfo() {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {

			params.put("cmd", "score/info");
			params.put("id", _scoreId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(getActivity(), params,
				new HttpCallback() {

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub

						try {
							JSONObject item = new JSONObject(res);
							_scoreInfo = new GScore();
							_scoreInfo.initialize(item);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.sucessData(res);
					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub
						super.faildData(code, res);
					}

				});

		GThreadExecutor.execute(r);

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction f) {
		// TODO Auto-generated method stub

		if (mAdapter != null) {

			if ("front".equals(tab.getTag()))
				mAdapter.setIsFront(true, _beans);

			if ("back".equals(tab.getTag()))
				mAdapter.setIsFront(false, _beans);
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
	private BroadcastReceiver _reciver ;

}
