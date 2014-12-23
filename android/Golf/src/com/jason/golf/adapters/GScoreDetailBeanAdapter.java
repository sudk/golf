package com.jason.golf.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.GScoreDetailsFragment;
import com.jason.golf.R;
import com.jason.golf.classes.GComment;
import com.jason.golf.classes.GScoreDetailBean;
import com.jason.golf.classes.SearchCourtBean;
import com.jason.golf.dialog.ScroseDetailEditDialog;
import com.jason.golf.dialog.WarnDialog;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GScoreDetailBeanAdapter extends BaseAdapter {
	
	private int[] TeeColor = new int[]{
			R.color.black,
			R.color.gold,
			R.color.blue,
			R.color.white,
			R.color.red
	};

	private ArrayList<GScoreDetailBean> _scores;
	private Context _context;
	private LayoutInflater _inflater;
	
	private SwipeListView _scoreDetailList;

	private boolean _isFront;

	public GScoreDetailBeanAdapter(Context ctx,	ArrayList<GScoreDetailBean> beans, SwipeListView mScoreDetailList) {
		// TODO Auto-generated constructor stub
		_context = ctx;
		_scoreDetailList = mScoreDetailList;
		_inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_scores = new ArrayList<GScoreDetailBean>();
		if (beans != null) {

			for (GScoreDetailBean b : beans) {

				if (_isFront) {
					if (b.getHoleNo() - 1 <= 9) {
						_scores.add(b);
					}
				} else {
					if (b.getHoleNo() - 1 > 9) {
						_scores.add(b);
					}
				}
			}

			Collections.sort(_scores, _comparator);
		}

		_isFront = true;

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
		if (convertView == null) {
			v = _inflater.inflate(R.layout.fragment_score_detail_item, null);
			holder = new ViewHolder();

			holder._hole = (TextView) v.findViewById(R.id.holeNo);
			holder._tee = (TextView) v.findViewById(R.id.tee);
			holder._long = (TextView) v.findViewById(R.id.longout);
			holder._std = (TextView) v.findViewById(R.id.standard);
			holder._push = (TextView) v.findViewById(R.id.pushout);
			holder._delete = (Button) v.findViewById(R.id.delete);
			holder._edit = (Button) v.findViewById(R.id.edit);
			holder._total = (TextView) v.findViewById(R.id.total);

			v.setTag(holder);
		} else {
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}

		final GScoreDetailBean bean = _scores.get(position);
		holder._hole.setText(String.format("球洞 %d号", bean.getHoleNo()));
		holder._tee.setText(GScoreDetailBean.GetTeeDesc(bean.getTee()));
		holder._long.setText(String.format("长杆:%s", bean.getLangBar()));
		holder._push.setText(String.format("推杆:%s", bean.getPushBar()));
		holder._std.setText(String.format("标准杆:%s", bean.getStandardBar()));
		holder._total.setText(String.format("杆数:%d", bean.getTotal()));
		
		holder._delete.setOnClickListener(new OnClickListener() {

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
				ScroseDetailEditDialog editDialog = new ScroseDetailEditDialog((FragmentActivity) _context, bean, position);
				editDialog.show();
			}
		});
		
		return v;
	}

	public void addData(ArrayList<GScoreDetailBean> data) {
		_scores.addAll(_scores.size(), data);
		notifyDataSetChanged();
	}
	
	private void remove(final int postion) {
		// TODO Auto-generated method stub
		
		GScoreDetailBean bean = (GScoreDetailBean) getItem(postion);

		JSONObject params = new JSONObject();

		try {

			params.put("cmd", "score/ddel");
			params.put("id", bean.getId());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest((FragmentActivity) this._context, params, new HttpCallback() {

					@Override
					public void sucessData(String res) {
						
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
						
						Intent intent = new Intent(GScoreDetailsFragment.BROADCAST_REFRESH_ACTION);
						intent.putExtra("Position",postion);
						intent.putExtra("Del", true);
						
						LocalBroadcastManager.getInstance(_context).sendBroadcast(intent);
					}
					
					

				});

		GThreadExecutor.execute(r);
		
	}

	public void swapData(ArrayList<GScoreDetailBean> data) {
		_scores.clear();

		for (GScoreDetailBean b : data) {

			if (_isFront) {
				if (b.getHoleNo() <= 9) {
					_scores.add(b);
				}
			} else {
				if (b.getHoleNo() > 9) {
					_scores.add(b);
				}
			}
		}

		Collections.sort(_scores, _comparator);

		notifyDataSetChanged();
	}

	public void setIsFront(boolean mark, ArrayList<GScoreDetailBean> details) {
		_isFront = mark;
		swapData(details);
		notifyDataSetChanged();
	}

	private class ViewHolder {
		public TextView _hole;
		public TextView _tee;
		public TextView _push;
		public TextView _long;
		public TextView _std;
		public TextView _total;
		
		public Button _delete;
		public Button _edit;
	}

	private Comparator<GScoreDetailBean> _comparator = new Comparator<GScoreDetailBean>() {

		@Override
		public int compare(GScoreDetailBean lhs, GScoreDetailBean rhs) {
			// TODO Auto-generated method stub
			return lhs.getHoleNo() - rhs.getHoleNo();
		}

	};

	public void removeItem(int position) {
		// TODO Auto-generated method stub
		_scores.remove(position);
	}

}
