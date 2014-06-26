package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.adapters.GScoreAdapter;
import com.jason.golf.classes.GScore;
import com.jason.golf.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class GScoreListFragment extends Fragment {

	private static final int REQUESTCODE = 1001;

	public static final String BROADCAST_REFRESH_ACTION = "com.jason.golf.score.refresh";

	private ArrayList<GScore> _scores;
	private SwipeListView mScores;
	private GScoreAdapter mAdapter;
	
	private MenuItem mSearchItem;

	private String _cityId;
	private int _page = 0;
	private String _title;
	
	private boolean isQueryLoading ;
	private boolean hasMoreDate;
	
	private LinearLayout mFooter;

	public static GScoreListFragment Instance() {
		return new GScoreListFragment();
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
		setHasOptionsMenu(true);
		_scores = new ArrayList<GScore>();

		android.app.ActionBar bar = getActivity().getActionBar();
		bar.setTitle(R.string.score);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActivity().getActionBar().setTitle(R.string.scorelist);
		
		View v = inflater.inflate(R.layout.fragment_score_list, null);
		mScores = (SwipeListView) v.findViewById(R.id.score_list);
		mFooter = (LinearLayout) inflater.inflate(R.layout.footer, null);
		mFooter.setVisibility(View.GONE);  
		mFooter.setPadding(0, -600, 0, 0);  
		mScores.addFooterView(mFooter);
	
		mAdapter = new GScoreAdapter((ActionBarActivity) getActivity(), _scores, mScores);
		mScores.setAdapter(mAdapter);
		
		hasMoreDate = true;
		queryScores(0, true, "");
		
		mScores.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) { }
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
				if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount == visibleItemCount){
					
					if( hasMoreDate ) {
						System.out.println("已滑到底部，查询数据去喽！！！！");
						queryScores( _page , false, "");
					}
					
				}
				
			}
		});
		
		mScores.setSwipeListViewListener(new BaseSwipeListViewListener(){

			@Override
			public void onClickFrontView(int position) {
				// TODO Auto-generated method stub
				super.onClickFrontView(position);

				GScore s = (GScore) mAdapter.getItem(position);
				
				GScoreDetailsFragment detailFragment = GScoreDetailsFragment.Instance();
				Bundle params = new Bundle();
				params.putString(GScoreDetailsFragment.KEY_SCORE_ID, s.getId());
				params.putString(GScoreDetailsFragment.KEY_HOLE_TOTAL, s.getHoles());
				detailFragment.setArguments(params);
				
				FragmentTransaction ft = ((ActionBarActivity)getActivity()).getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, detailFragment);
				ft.addToBackStack(null);
				// Commit the transaction
				ft.commit();
				
			}
			
		});

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.score_list_menu, menu);
		mSearchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setOnQueryTextListener(mOnQueryTextListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			
			@Override
			public boolean onClose() {
				// TODO Auto-generated method stub
				queryScores(0, true, "");
				return false;
			}
		});
        searchView.setQueryHint("输入球场名称");
        
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        search_text.setTextColor(Color.WHITE);
        
        ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchButton.setImageResource(R.drawable.ic_search);
        ImageView searchCloseButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseButton.setImageResource(R.drawable.ic_close);
		
		super.onCreateOptionsMenu(menu, inflater);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_create:
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			Fragment newFragment = GScoreNewFragment.Instance();
			transaction.replace(R.id.container, newFragment);
			transaction.addToBackStack(null);
			// Commit the transaction
			transaction.commit();
			
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private final SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextChange(String newText) {
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			queryScores(0, true, query);
			return true;
		}
	};

	private void queryScores(int page, final boolean isRefresh, String courtName) {
		// TODO Auto-generated method stub
		
		if( isQueryLoading ) 
			return;
		
		isQueryLoading = true; 
		
		JSONObject params = new JSONObject();

		try {

			params.put("cmd", "score/list");
			params.put("_pg_", String.format("%d", page));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(getActivity(), params,
				new HttpCallback() {

					@Override
					public void finalWork() {
						// TODO Auto-generated method stub
						super.finalWork();
						isQueryLoading = false;
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub

						try {
							JSONArray data = new JSONArray(res);

							_scores.clear();

							for (int i = 0, length = data.length(); i < length; i++) {
								JSONObject item = data.getJSONObject(i);
								GScore good = new GScore();
								good.initialize(item);
								_scores.add(good);
							}
							if (isRefresh) {
								mAdapter.swapData(_scores);
								_page = 0;
							} else {
								mAdapter.addData(_scores);
							}
							_page++;
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							super.sucessData(res);
						}

					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub

						if (code == 4) {
							// 没有数据
							hasMoreDate = false;
							mFooter.setVisibility(View.VISIBLE);  
							mFooter.setPadding(0, 0, 0, 0); 
						}
						
						super.faildData(code, res);
					}

				});

		GThreadExecutor.execute(r);

	}
	
}
