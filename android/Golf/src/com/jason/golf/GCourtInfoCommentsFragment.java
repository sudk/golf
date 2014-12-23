package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpPageRequest;
import com.jason.golf.R;
import com.jason.golf.adapters.CommentAdapter;
import com.jason.golf.classes.GComment;
import com.jason.golf.classes.GCourt;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class GCourtInfoCommentsFragment extends Fragment {

	public static final String KEY_COURT_ID = "key_court_id";

	private PullToRefreshListView mComments;
	private ArrayList<GComment> _comment;
	private CommentAdapter mAdapter;

	private TextView mSecvice, mDesign, mFacilities, mLawn;
	
	private String _courtId;
	
	private int _page = 0;

	public static Fragment Instance() {
		// TODO Auto-generated method stub

		return new GCourtInfoCommentsFragment();
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
		hasOptionsMenu();
		_comment = new ArrayList<GComment>();
		mAdapter = new CommentAdapter(getActivity(), _comment);
		_courtId = getArguments().getString(KEY_COURT_ID);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActivity().getActionBar().setTitle(R.string.court_comment);

		GCourtInfoActivity a = (GCourtInfoActivity) getActivity();
		GCourt court = a.getCourt();
		
		View v = inflater.inflate(R.layout.fragment_court_info_comments, null);
		
		TextView courtName = (TextView) v.findViewById(R.id.courtname);
		courtName.setText(court.getName());

		mComments = (PullToRefreshListView) v.findViewById(R.id.comment_list);
		mComments.setAdapter(mAdapter);
		
		mComments.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryComments(0, true);
			}
		});

		mComments.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				queryComments(_page, false);
			}
		});
		

		// ActionBarActivity activity = (ActionBarActivity) getActivity();
		// ActionBar bar = activity.getSupportActionBar();
		// bar.setTitle(R.string.court_info);
		
		mSecvice = (TextView) v.findViewById(R.id.service);
		mDesign = (TextView) v.findViewById(R.id.design);
		mFacilities = (TextView) v.findViewById(R.id.facilitie);
		mLawn = (TextView) v.findViewById(R.id.lawn);
		
		queryComments(0, true);

		return v;
	}

	private void queryComments(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		try {

			params.put("cmd", "court/comment");
			params.put("court_id", _courtId);
			params.put("_pg_", String.format("%d", page));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpPageRequest r = new HttpPageRequest(getActivity(), params,	new HttpCallback() {
			
					@Override
					public void finalWork() {
						// TODO Auto-generated method stub
						super.finalWork();
						mComments.onRefreshComplete();
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						super.sucessData(res);

						try {
							JSONObject obj = new JSONObject(res);

							JSONObject header = obj.getJSONObject("header");
							
							String score = header.getString("service_total");
							mSecvice.setText(String.format("服务：%.2f 分", Float.parseFloat(score)));
							
							score = header.getString("design_total");
							mDesign.setText(String.format("设计：%.2f 分",  Float.parseFloat(score)));
							
							score = header.getString("facilitie_total");
							mFacilities.setText(String.format("设施：%.2f 分",  Float.parseFloat(score)));
							
							score = header.getString("lawn_total");
							mLawn.setText(String.format("草场：%.2f 分",  Float.parseFloat(score)));
							
							JSONArray array = obj.getJSONArray("content");
							
							_comment.clear();
							
							for (int i = 0, length = array.length(); i < length; i++) {
								GComment c = new GComment();
								c.initialize(array.getJSONObject(i));
								_comment.add(c);
							}
							
//							mAdapter.swapData(_comment);
							

							if (isRefresh) {
								mAdapter.swapData(_comment);
								_page = 0;
							} else {
								mAdapter.addData(_comment);
							}
							_page++;
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

		GThreadExecutor.execute(r);
	}

}
