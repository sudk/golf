package com.jason.golf.adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.GScoreEditFragment;
import com.jason.golf.GScoreListFragment;
import com.jason.golf.R;
import com.jason.golf.classes.GScore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


public class GScoreAdapter extends BaseAdapter {

	private ArrayList<GScore> _scores; 
	private ActionBarActivity _fatherActivtiy;
	private LayoutInflater _inflater;
	private SwipeListView _swipeListView;

	public GScoreAdapter(ActionBarActivity act, ArrayList<GScore> goods, SwipeListView slv) {
		// TODO Auto-generated constructor stub
		_fatherActivtiy = act;
		_swipeListView = slv;
		_inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_scores = new ArrayList<GScore>();
		if(goods != null){
			_scores.addAll(goods);
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _scores.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _scores.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ViewHolder holder;
		if(convertView == null){
			v = _inflater.inflate(R.layout.fragment_score_list_item, null);
			holder = new ViewHolder();

			holder._courtName = (TextView) v.findViewById(R.id.courtname);
			holder._date = (TextView) v.findViewById(R.id.date);
			holder._teamMember = (TextView) v.findViewById(R.id.teammembers);
			
			holder._del = (Button) v.findViewById(R.id.delete);
			holder._edit = (Button) v.findViewById(R.id.edit);
			
			v.setTag(holder);
		}else{
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		
		final GScore score = _scores.get(position);
		
		holder._courtName.setText(score.getCourtName());
		holder._date.setText(score.getFeeTime());
		holder._teamMember.setText(score.getTeamMembers());
		
		holder._del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remove(position);
				
			}
		});
		
		holder._edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle params = new Bundle();
				params.putString(GScoreEditFragment.KEY_SCORE_ID, score.getId());
				
				FragmentTransaction transaction = _fatherActivtiy.getSupportFragmentManager().beginTransaction();
				Fragment editFragment = GScoreEditFragment.Instance();
				editFragment.setArguments(params);
				transaction.replace(R.id.container, editFragment);
				transaction.addToBackStack(null);
				// Commit the transaction
				transaction.commit();
				
			}
		});
		
		return v;
	}
	
	public void addData(ArrayList<GScore> data){
		_scores.addAll(_scores.size(), data);
		notifyDataSetChanged();
	}
	
	public void swapData(ArrayList<GScore> data){
		_scores.clear();
		_scores.addAll(data);
		notifyDataSetChanged();
	}
	
	private void remove(final int position){
		
		GScore bean = (GScore) getItem(position);

		JSONObject params = new JSONObject();

		try {

			params.put("cmd", "score/del");
			params.put("id", bean.getId());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(_fatherActivtiy, params, new HttpCallback() {

					@Override
					public void sucessData(String res) {
						
						_swipeListView.closeAnimate(position);
						_swipeListView.dismiss(position);
						_scores.remove(position);
						
						Intent intent = new Intent(GScoreListFragment.BROADCAST_REFRESH_ACTION);
						LocalBroadcastManager.getInstance(_fatherActivtiy).sendBroadcast(intent);
						
						super.sucessData(res);
					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub
						super.faildData(code, res);
					}

					@Override
					public void finalWork() {
						// TODO Auto-generated method stub
						super.finalWork();
					}
				});

		GThreadExecutor.execute(r);
		
	}
	
	private class ViewHolder{
		public TextView _courtName;
		public TextView _teamMember;
		public TextView _date;
		
		public Button _del;
		public Button _edit;
	}

}
