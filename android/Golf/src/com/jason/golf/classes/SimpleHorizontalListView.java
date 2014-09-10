package com.jason.golf.classes;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class SimpleHorizontalListView extends HorizontalScrollView {

	private LinearLayout mContainer;

	private int mCount;

	private BaseAdapter mAdapter;

	private DataSetObserver mObserver;
	
	private OnItemLongClickListener _onItemLongClickListener;
	
	private OnItemClickListener _onItemClickListener;

	public SimpleHorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SimpleHorizontalListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SimpleHorizontalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		initialize();
	}
	
	public void setOnItemClickListener(OnItemClickListener l){
		_onItemClickListener = l;
	}
	
	public void setOnItemLongClickListener(OnItemLongClickListener l){
		_onItemLongClickListener = l;
	}

	private void initialize() {

		mObserver = new DataSetObserver() {

			@Override
			public void onChanged() {
				// TODO Auto-generated method stub
				super.onChanged();

				layoutChildren();
			}

		};

		mContainer = new LinearLayout(getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mContainer.setLayoutParams(params);
		mContainer.setOrientation(LinearLayout.HORIZONTAL);

		this.addView(mContainer);
	}

	// public void addImage(ImageView img){
	// mContainer.addView(img);
	// }
	//
	// public void removeImage(int index){
	// mContainer.removeViewAt(index);
	// }

	public void setAdapter(BaseAdapter adapter) {
		if (mAdapter != null && mObserver != null) {
			mAdapter.unregisterDataSetObserver(mObserver);
		}

		mAdapter = adapter;

		if (mAdapter == null)
			return;

		mAdapter.registerDataSetObserver(mObserver);

		layoutChildren();

	}

	private void layoutChildren() {

		mContainer.removeAllViews();

		if (mAdapter == null)
			return;

		int count = mAdapter.getCount();

		if (count > 0) {

			for (int i = 0; i < count; i++) {

				View v = mAdapter.getView(i, null, null);
				
				v.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("Image is on click");
						
						if(_onItemClickListener != null)
							_onItemClickListener.onItemClick(null, v, (Integer) v.getTag(), 0);
					}
				});
				
				v.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("Image is on longclick");
						
						
						if(_onItemLongClickListener != null)
							_onItemLongClickListener.onItemLongClick(null, v, (Integer) v.getTag(), 0);
						
						return true;
					}
				});

				mContainer.addView(v);

			}
		}

	}

}
