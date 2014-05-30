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
import com.jason.golf.classes.GOrder;
import com.jason.golf.classes.OrderAdapter;
import com.jason.golf.R;

import android.app.DownloadManager.Query;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GOrderListFragment extends Fragment implements OnItemClickListener {

	private ArrayList<GOrder> _orders;
	private PullToRefreshListView mOrders;
	private OrderAdapter mAdapter;

	private int _page = 0;

	public static GOrderListFragment Instance() {
		return new GOrderListFragment();
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
		_orders = new ArrayList<GOrder>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_order_list, null);
		mOrders = (PullToRefreshListView) v.findViewById(R.id.order_list);
		mOrders.getRefreshableView().setOnItemClickListener(this);
		mOrders.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryOrders(0, true);
			}
		});

		mOrders.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				queryOrders(_page, false);
			}
		});

		mAdapter = new OrderAdapter(getActivity(), _orders);
		mOrders.setAdapter(mAdapter);

		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryOrders(0, true);
	}

	private void queryOrders(int page, final boolean isRefresh) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "order/list");
			params.put("start_time", "");
			params.put("end_time", "");
			params.put("relation_name", "");
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
						mOrders.onRefreshComplete();
					}

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						/*
						 * {"status":0,"desc":"\u6210\u529f","_pg_":"0",
						 * "data":[ { "order_id":"20140411230750123456",
						 * "user_id":"1", "type":"0", "relation_id":
						 * "admin20140409220421", "relation_name":
						 * "\u5e7f\u5dde\u73e0\u6d77\u91d1\u6e7e\u9ad8\u5c14\u592b444"
						 * , "tee_time":"2014-04-12 09:00:00", "count":"2",
						 * "unitprice":"200", "amount":"400", "had_pay":"20",
						 * "pay_type":"0", "status":"1",
						 * "record_time":"2014-04-11 23:09:28",
						 * "desc":"\u9884\u5b9a\u597d\u4f4d\u7f6e",
						 * "agent_id":"1", "contact":"\u5f20\u4e09",
						 * "phone":"13312341234" } ]}
						 */
						super.sucessData(res);

						try {
							JSONArray data = new JSONArray(res);

							_orders.clear();

							for (int i = 0, length = data.length(); i < length; i++) {

								JSONObject item = data.getJSONObject(i);
								GOrder order = new GOrder();
								order.setOrderId(item.getString("order_id"));
								order.setType(item.getInt("type"));
								order.setRelationId(item
										.getString("relation_id"));
								order.setRelationName(item
										.getString("relation_name"));
								order.setTeeTime(item.getString("tee_time"));
								order.setCount(item.getInt("count"));
								order.setUnitprice(item.getInt("unitprice"));
								order.setAmount(item.getInt("amount"));
								order.setHadPay(item.getInt("had_pay"));
								order.setPayType(item.getString("pay_type"));
								order.setStatus(item.getString("status"));
								order.setRecordTime(item
										.getString("record_time"));
								order.setDesc(item.getString("desc"));
								order.setContact(item.getString("contact"));
								order.setPhone(item.getString("phone"));
								order.setAgentId(item.getString("agent_id"));

								_orders.add(order);
							}

							if (isRefresh) {
								mAdapter.swapData(_orders);
								_page = 0;
							} else {
								mAdapter.addData(_orders);
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
//		System.out.println(String.format("postion is %d", position));
		GOrder order = (GOrder) mAdapter.getItem(position - 1);

		Bundle params = new Bundle();
		params.putString(GOrderDetailsFragment.KEY_ORDER_ID, order.getOrderId());

		Fragment detailFragment = GOrderDetailsFragment.Instance();
		detailFragment.setArguments(params);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, detailFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}
}
