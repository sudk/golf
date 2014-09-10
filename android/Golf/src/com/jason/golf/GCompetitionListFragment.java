package com.jason.golf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.adapters.CompetitionAdapter;
import com.jason.golf.classes.GCompetition;
import com.jason.golf.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class GCompetitionListFragment extends Fragment implements OnItemClickListener {
	
	private PullToRefreshListView mCompetitions;
	private CompetitionAdapter mAdapter;
	private ArrayList<GCompetition> _competitions;
	private TextView mNoData;
	
	private int _page = 0;

	public static GCompetitionListFragment Instance() {
		return new GCompetitionListFragment();
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
		_competitions = new ArrayList<GCompetition>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_competition_list, null);
		
		mNoData = (TextView) v.findViewById(R.id.no_data);
		
		mAdapter = new CompetitionAdapter(getActivity(), _competitions);
		
		mCompetitions = (PullToRefreshListView) v.findViewById(R.id.competition_list);
		
		mCompetitions.setAdapter(mAdapter);
		
		mCompetitions.setOnItemClickListener(this);
		
		mCompetitions.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryCompetitions(0, true);
			}

		});
		
		mCompetitions.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				
			}
		});
		
		queryCompetitions(0, true);
		
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void queryCompetitions(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "competition/list");
			params.put("name", "");
			params.put("_pg_", String.format("%d", page));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
//				  "id": "guest_one20140424174843", 
//		            "agent_id": "1", 
//		            "court_id": "admin20140409220421", 
//		            "name": "aaaa", 
//		            "desc": "11111", 
//		            "fee": "11111100", 
//		            "start_date": "2014-04-24", 
//		            "end_date": "2014-04-24", 
//		            "plan": "111", 
//		            "fee_include": "111", 
//		            "fee_not_include": "111", 
//		            "record_time": "2014-04-24 17:48:43", 
//		            "fee_type": "0", 
//		            "creatorid": "guest_one", 
//		            "court_name": "广州珠海金湾高尔夫444", 
//		            "agent_name": "高尔夫代理商001"
				
				
				try {
					JSONArray data = new JSONArray(res);
					
					_competitions.clear();

					for (int i = 0, length = data.length(); i < length; i++) {

						JSONObject item = data.getJSONObject(i);
						
						GCompetition competition = new GCompetition();
						
						competition.init(item);
						
//						competition.setId(item.getString("id"));
//						competition.setAgentId(item.getString("agent_id"));
//						competition.setAgentName(item.getString("agent_name"));
//						competition.setCourtId(item.getString("court_id"));
//						competition.setCourtName(item.getString("court_name"));
//						competition.setName(item.getString("name"));
//						competition.setDesc(item.getString("desc"));
//						competition.setFee(item.getString("fee"));
//						competition.setStartDate(item.getString("start_date"));
//						competition.setEndDate(item.getString("end_date"));
//						competition.setPlan(item.getString("plan"));
//						competition.setFeeInclude(item.getString("fee_include"));
//						competition.setFeeNotInclude(item.getString("fee_not_include"));
//						competition.setFeeType(item.getString("fee_type"));
//						competition.setImg(item.getString("img"));
						
//						System.out.println(competition.toString());
						
						_competitions.add(competition);
					}
					
					if (isRefresh) {
						mAdapter.swapData(_competitions);
						_page = 0;
					} else {
						mAdapter.appendData(_competitions);
					}
					_page++;
					
//					
					if(mAdapter.getCount() == 0){
						mNoData.setVisibility(View.VISIBLE);
//						mCompetitions.setVisibility(View.GONE);
					}else{
						mNoData.setVisibility(View.GONE);
//						mCompetitions.setVisibility(View.VISIBLE);
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				
				if (code == 4) {
					// 没有数据
					mNoData.setVisibility(View.VISIBLE);
//					mCompetitions.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void finalWork() {
				// TODO Auto-generated method stub
				super.finalWork();
				mCompetitions.onRefreshComplete();
			}

			
		});
		
		GThreadExecutor.execute(r);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// TODO Auto-generated method stub
		
//		System.out.println(String.format("postion is %d", position));
		GCompetition order = (GCompetition) mAdapter.getItem(position - 1);

		Bundle params = new Bundle();
		params.putString(GCompetitionDetailFragment.KEY_COMPETITION_ID, order.getId());

		Fragment detailFragment = GCompetitionDetailFragment.Instance();
		detailFragment.setArguments(params);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, detailFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
		
	}
}
