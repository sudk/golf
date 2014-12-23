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
import com.jason.golf.adapters.CardsAdapter;
import com.jason.golf.classes.GCardBean;
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

public class GCardListFragment extends Fragment implements OnItemClickListener {

	private ArrayList<GCardBean> _cards;
	private PullToRefreshListView mCards;
	private CardsAdapter mAdapter;
	private TextView mNoCard;

	private int _page = 0;

	public static GCardListFragment Instance() {
		return new GCardListFragment();
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
		_cards = new ArrayList<GCardBean>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_card_list, null);
		mNoCard = (TextView) v.findViewById(R.id.no_data);
		mCards = (PullToRefreshListView) v.findViewById(R.id.cards_list);
		mCards.getRefreshableView().setOnItemClickListener(this);
		mCards.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryCards(0, true);
			}
		});

		mCards.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
//				queryCards(_page, false);
			}
		});

		mAdapter = new CardsAdapter(getActivity(), _cards);
		mCards.setAdapter(mAdapter);

		queryCards(0, true);

		return v;
	}

	private void queryCards(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {

			params.put("cmd", "card/list");

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
						mCards.onRefreshComplete();
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub

						super.sucessData(res);

						try {
							JSONArray data = new JSONArray(res);

							_cards.clear();

							for (int i = 0, length = data.length(); i < length; i++) {

								JSONObject item = data.getJSONObject(i);
								GCardBean card = new GCardBean();
								card.initialize(item);
								_cards.add(card);

							}
							
							if (isRefresh) {
								mAdapter.swapData(_cards);
								_page = 0;
							} else {
								mAdapter.addData(_cards);
							}
							
							if( mAdapter.getCount() == 0 ){
								mCards.setVisibility(View.GONE);
								mNoCard.setVisibility(View.VISIBLE);
							}else{
								mCards.setVisibility(View.VISIBLE);
								mNoCard.setVisibility(View.GONE);
							}

							_page++;

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

						}

					}

				});

		GThreadExecutor.execute(r);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		 System.out.println(String.format("postion is %d", position));
		 GCardBean card = (GCardBean) mAdapter.getItem(position - 1);
		
		 Bundle params = new Bundle();
		 params.putString(GCardInfoFragment.KEY_CARD_ID, card.getId());
		
		 Fragment detailFragment = GCardInfoFragment.Instance();
		 detailFragment.setArguments(params);
		
		 FragmentTransaction transaction = getFragmentManager().beginTransaction();
		 transaction.replace(R.id.container, detailFragment);
		 transaction.addToBackStack(null);
		
		 // Commit the transaction
		 transaction.commit();
	}
}
