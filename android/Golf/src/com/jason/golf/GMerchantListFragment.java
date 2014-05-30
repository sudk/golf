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
import com.jason.golf.classes.GMerchant;
import com.jason.golf.classes.MerchantAdapter;
import com.jason.golf.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GMerchantListFragment extends Fragment implements OnItemClickListener {
	
	public static final String key_court_name = "key_court_name";
	public static final String key_facilitie_name = "key_facilitie_name";
	public static final String key_city = "key_city";
	public static final String key_type = "key_type";
	public static final String key_court_id = "key_court_id";
	
	private PullToRefreshListView mMerchants;
	private MerchantAdapter mAdapter;
	private ArrayList<GMerchant> _merchants;
	
	private MenuItem mSearchItem;
	
	private int _page = 0;
	
	private String _courtName, _facilitieName, _city, _type, _courtId;

	public static GMerchantListFragment Instance() {
		return new GMerchantListFragment();
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
		_merchants = new ArrayList<GMerchant>();
		
		Bundle params = getArguments();
		
		_courtName = params.getString(key_court_name,"");
		_facilitieName = params.getString(key_facilitie_name,"");
		_city = params.getString(key_city,"");
		_type = params.getString(key_type,"");
		_courtId = params.getString(key_court_id,"");
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_merchant_list, null);
		
		mAdapter = new MerchantAdapter(getActivity(), _merchants);
		
		mMerchants = (PullToRefreshListView) v.findViewById(R.id.merchant_list);
		
		mMerchants.setAdapter(mAdapter);
		
		mMerchants.setOnItemClickListener(this);
		
		mMerchants.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				queryMerchants(0, true, "");
			}

		});
		
		mMerchants.setOnLastItemVisibleListener(new PullToRefreshListView.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				
			}
		});
		
		queryMerchants(0, true, "");
		
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		
        inflater.inflate(R.menu.merchant_actions,  menu);
        mSearchItem = menu.findItem(R.id.action_search);
        
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setOnQueryTextListener(mOnQueryTextListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			
			@Override
			public boolean onClose() {
				// TODO Auto-generated method stub
				queryMerchants(0, true, "");
				return false;
			}
		});
        searchView.setQueryHint("输入球场名称");
        
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        search_text.setTextColor(Color.WHITE);
        
        ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchButton.setImageResource(R.drawable.ic_search);
//        ImageView searchMagButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
//        searchMagButton.setImageResource(R.drawable.ic_search);
        ImageView searchCloseButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseButton.setImageResource(R.drawable.ic_close);
        
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	 // The following callbacks are called for the SearchView.OnQueryChangeListener
    // For more about using SearchView, see src/.../view/SearchView1.java and SearchView2.java
    private final SearchView.OnQueryTextListener mOnQueryTextListener =
            new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String newText) {
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
        	queryMerchants(0, true, query);
            return true;
        }
    };

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	private void queryMerchants(int page, final boolean isRefresh, String courtName) {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "facilities/list");
			params.put("court_name", courtName);
			params.put("facilitie_name", "");
			params.put("city", "");
			params.put("type", "");
			params.put("court_id", "");
			params.put("_pg_", String.format("%d",page));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpRequest r = new HttpRequest(getActivity(), params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
				try {
					JSONArray data = new JSONArray(res);
					
					_merchants.clear();

					for (int i = 0, length = data.length(); i < length; i++) {

						JSONObject item = data.getJSONObject(i);
						
						GMerchant merchant = new GMerchant();
						
						merchant.initialize(item);
						
						_merchants.add(merchant);
					}
					
					if (isRefresh) {
						mAdapter.swapData(_merchants);
						_page = 0;
					} else {
						mAdapter.appendData(_merchants);
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
					mAdapter.swapData(new ArrayList<GMerchant>());
					Toast.makeText(getActivity(), "没有搜索到可用信息。", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void finalWork() {
				// TODO Auto-generated method stub
				super.finalWork();
				mMerchants.onRefreshComplete();
			}

			
		});
		
		GThreadExecutor.execute(r);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// TODO Auto-generated method stub
		
//		System.out.println(String.format("postion is %d", position));
		GMerchant m = (GMerchant) mAdapter.getItem(position - 1);
		
		Bundle params = new Bundle();
		params.putString(GMerchantInfoFragment.KEY_MERCHANT_ID, m.getId());
		
		Fragment infoFragment = GMerchantInfoFragment.Instance();
		infoFragment.setArguments(params);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, infoFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
		
	}
	
}
