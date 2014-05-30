package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpPageRequest;
import com.jason.controller.HttpRequest;
import com.jason.golf.R;
import com.jason.golf.classes.CommentAdapter;
import com.jason.golf.classes.GComment;
import com.jason.golf.classes.GGood;

import android.app.DownloadManager.Query;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GCourtInfoCommentsFragment extends Fragment {

	public static final String KEY_COURT_ID = "key_court_id";

	private PullToRefreshListView mComments;
	private ArrayList<GComment> _comment;
	private CommentAdapter mAdapter;

	private TextView mSecvice, mDesign, mFacilities, mLawn;
	
	private String _courtId;

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
		View v = inflater.inflate(R.layout.fragment_court_info_comments, null);

		mComments = (PullToRefreshListView) v.findViewById(R.id.comment_list);
		mComments.setAdapter(mAdapter);

		// ActionBarActivity activity = (ActionBarActivity) getActivity();
		// ActionBar bar = activity.getSupportActionBar();
		// bar.setTitle(R.string.court_info);
		
		mSecvice = (TextView) v.findViewById(R.id.service);
		mDesign = (TextView) v.findViewById(R.id.design);
		mFacilities = (TextView) v.findViewById(R.id.facilitie);
		mLawn = (TextView) v.findViewById(R.id.lawn);
		
		queryComments();

		return v;
	}

	private void queryComments() {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		try {

			params.put("cmd", "court/comment");
			params.put("court_id", _courtId);
			params.put("_pg_", String.format("%d", 0));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpPageRequest r = new HttpPageRequest(getActivity(), params,	new HttpCallback() {

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						super.sucessData(res);

						try {
							JSONObject obj = new JSONObject(res);

							JSONObject header = obj.getJSONObject("header");
							mSecvice.setText(String.format("%s 分", header.getString("service_total")));
							mDesign.setText(String.format("%s 分", header.getString("design_total")));
							mFacilities.setText(String.format("%s 分", header.getString("facilitie_total")));
							mLawn.setText(String.format("%s 分", header.getString("lawn_total")));
							
							JSONArray array = obj.getJSONArray("content");
							
							_comment.clear();
							
							for (int i = 0, length = array.length(); i < length; i++) {
								GComment c = new GComment();
								c.initialize(array.getJSONObject(i));
								_comment.add(c);
							}
							
							mAdapter.swapData(_comment);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

		GThreadExecutor.execute(r);
	}

}
