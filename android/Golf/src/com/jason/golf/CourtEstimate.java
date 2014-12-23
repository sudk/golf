package com.jason.golf;

import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class CourtEstimate extends ActionBarActivity {

	public static final String KEY_COURT_ID = "key_court_id";
	public static final String KEY_COURT_NAME = "key_court_name";

	private String _courtId;
	private String _courtName;

	private RatingBar mService, mDesign, mFacilities, mGrass;

	private Button mSubmit;
	
	private EditText mCourtComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.court_estimate);

		_courtId = getIntent().getExtras().getString(KEY_COURT_ID, "");
		_courtName = getIntent().getExtras().getString(KEY_COURT_NAME, "");

		if (!TextUtils.isEmpty(_courtName)) {
			getActionBar().setTitle(_courtName);
		}

		mService = (RatingBar) findViewById(R.id.ratingBar1);
		mDesign = (RatingBar) findViewById(R.id.ratingBar2);
		mFacilities = (RatingBar) findViewById(R.id.ratingBar3);
		mGrass = (RatingBar) findViewById(R.id.ratingBar4);

		mCourtComment = (EditText) findViewById(R.id.court_comment);
		
		mSubmit = (Button) findViewById(R.id.submit_estimate);
		mSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				submitEstimate();
				
			}

			
		});
		
	}
	
	private void submitEstimate() {
		// TODO Auto-generated method stub
				JSONObject params = new JSONObject();
				try {
					
//					System.out.println(String.format("%f", mService.getRating()));
					
					int grad_service = (int) (20 * mService.getRating());
					int grad_design = (int) (20 * mDesign.getRating());
					int grad_grass = (int) (20 * mGrass.getRating());
					int grad_facilities = (int) (20 * mFacilities.getRating());
					
					params.put("cmd", "comment/create");
					
					params.put("court_id", _courtId);
					params.put("desc", mCourtComment.getText());
					params.put("service", String.format("%d", grad_service));
					params.put("design", String.format("%d", grad_design));
					params.put("facilitie", String.format("%d", grad_grass));
					params.put("lawn", String.format("%d", grad_facilities));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				HttpRequest r = new HttpRequest(this, params,new HttpCallback() {

					@Override
					public void sucessData(String res) {
						// TODO Auto-generated method stub
						super.sucessData(res);
						
						WarnDialog dialog = new WarnDialog(CourtEstimate.this);
						dialog.setTitle(R.string.court).setMessage(R.string.estimat_court_successful)
						.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								finish();
							}
						});
						dialog.show(getSupportFragmentManager(), "EstimateSuccess");
						
					}

					@Override
					public void faildData(int code, String res) {
						// TODO Auto-generated method stub
						super.faildData(code, res);
						WarnDialog dialog = new WarnDialog(CourtEstimate.this);
						dialog.setTitle(R.string.order).setMessage(res)
						.setPositiveBtn(R.string.confirm, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						});
						dialog.show(getSupportFragmentManager(), "PayOrderFaild");
					}
					
				});
				
				GThreadExecutor.execute(r);
	}

}
