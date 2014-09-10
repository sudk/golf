package com.jason.golf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jason.controller.GThreadExecutor;
import com.jason.controller.HttpCallback;
import com.jason.controller.HttpRequest;
import com.jason.golf.adapters.VipChoiceAdapter;
import com.jason.golf.classes.GAccount;
import com.jason.golf.classes.GAdver;
import com.jason.golf.classes.GVipChoiceBean;
import com.jason.golf.dialog.WarnDialog;
import com.jason.golf.R;
import com.unionpay.UPPayAssistEx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class GVipActivity extends ActionBarActivity implements OnClickListener,
		OnItemSelectedListener {

	private Button mPayVip;

	private TextView mVipRigths, mExpireDate;

	private Spinner mVipChoice;

	private VipChoiceAdapter mAdapter;

	private GVipChoiceBean _selsectedVipChoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_vip);

		mPayVip = (Button) findViewById(R.id.payForVip);
		mPayVip.setOnClickListener(this);

		mVipRigths = (TextView) findViewById(R.id.vip_rights);
		
		mExpireDate = (TextView) findViewById(R.id.vip_expire_date);
		setVipInfo();

		mVipChoice = (Spinner) findViewById(R.id.vip_choice);
		mVipChoice.setOnItemSelectedListener(this);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.vip);
		bar.setIcon(R.drawable.actionbar_icon);
		int change = bar.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP;
		bar.setDisplayOptions(change, ActionBar.DISPLAY_HOME_AS_UP);

		_selsectedVipChoice = null;

		queryVipInfo();

	}

	private void setVipInfo() {
		// TODO Auto-generated method stub
		
		GolfAppliaction app = (GolfAppliaction) getApplication();
		GAccount acc = app.getAccount();
		
//		VIP状态：-1、为已经过期，0、为非会员，1、为正常
		switch (acc.getVipStatus()) {
		case -1:
			mExpireDate.setText(String.format("您的VIP身份已经过期 "));
			mExpireDate.setVisibility(View.VISIBLE);
			break;
		case 0:
			mExpireDate.setVisibility(View.GONE);
			break;
		case 1:
			mExpireDate.setText(String.format("您的VIP身份将于 %s 到期 ",acc.getVipExpireDate()));
			mExpireDate.setVisibility(View.VISIBLE);
			break;
		default:
			mExpireDate.setVisibility(View.GONE);
		}
		
		
	}

	private void queryVipInfo() {
		// TODO Auto-generated method stub

		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "setting/list");
			params.put("agent_id", "1");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Runnable r = new HttpRequest(this, params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				HashMap<String, GVipChoiceBean> data = new HashMap<String, GVipChoiceBean>();

				try {
					JSONArray array = new JSONArray(res);

					for (int i = 0, length = array.length(); i < length; i++) {
						JSONObject item = array.getJSONObject(i);
						data.put(
								item.getString("id"),
								new GVipChoiceBean(item.getString("id"), item
										.getString("value"), item
										.getString("desc")));
					}

					mVipRigths.setText(data.get("vip_members_rights")
							.getValue());

					ArrayList<GVipChoiceBean> choices = new ArrayList<GVipChoiceBean>();
					choices.add(data.get("vip_one_year"));
					choices.add(data.get("vip_three_year"));

					mAdapter = new VipChoiceAdapter(GVipActivity.this, choices);

					mVipChoice.setAdapter(mAdapter);

					mVipChoice.setSelection(0);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {

					data.clear();
					data = null;
				}

			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
			}

		});

		GThreadExecutor.execute(r);

	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		finish();
		return super.onSupportNavigateUp();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.payForVip:

			if (_selsectedVipChoice == null) {

				System.out.println("no choice");

			} else {
				generanteVipOrder();
			}

			break;
		}
	}

	private void generanteVipOrder() {
		// TODO Auto-generated method stub

		GolfAppliaction app = (GolfAppliaction) getApplication();
		GAccount acc = app.getAccount();

		Date date = new Date();
		String pat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pat);

		int price = Integer.parseInt(_selsectedVipChoice.getValue());

		JSONObject params = new JSONObject();

		try {
			params.put("cmd", "order/create");
			params.put("type", "4");// 订单类型，0、订场；1、行程；2、赛事报名；3、充值；4、购买VIP
			params.put("relation_id", acc.getId());
			params.put("relation_name", "购买VIP");
			params.put("tee_time", sdf.format(date));
			params.put("count", String.format("%d", 1));
			params.put("unitprice", String.format("%d", price));
			params.put("amount", String.format("%d", price));
			params.put("pay_type", "1");// 支付方式：0为现付，1为全额预付，2为押金
			params.put("contact", acc.getPhone());
			params.put("phone", acc.getPhone());
			params.put("agent_id", "1");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Runnable r = new HttpRequest(this, params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);

				try {
					JSONArray data = new JSONArray(res);

					JSONObject obj = data.getJSONObject(0);
					String orderid = obj.getString("order_id");

					payOrder(orderid);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);

				WarnDialog dialogTimeout = new WarnDialog(GVipActivity.this);
				dialogTimeout
						.setTitle(R.string.account_recharge)
						.setMessage(res)
						.setPositiveBtn(R.string.confirm,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								});
				dialogTimeout.show(getSupportFragmentManager(),
						"CreateOrderFaild");
			}

		});

		GThreadExecutor.execute(r);

	}

	private void payOrder(String orderId) {

		int price = Integer.parseInt(_selsectedVipChoice.getValue());

		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		try {
			params.put("cmd", "order/pay");
			params.put("order_id", orderId);
			params.put("type", "2");
			params.put("amount", String.format("%d", price));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest r = new HttpRequest(this, params, new HttpCallback() {

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);

				try {
					JSONArray data = new JSONArray(res);
					JSONObject obj = data.getJSONObject(0);
					String tn = obj.getString("tn");

					int ret = UPPayAssistEx.startPay(GVipActivity.this, null,
							null, tn, "01");

					if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
						// 需要重新安装控件
						AlertDialog.Builder builder = new AlertDialog.Builder(
								GVipActivity.this);
						builder.setTitle("提示");
						builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

						builder.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										UPPayAssistEx
												.installUPPayPlugin(GVipActivity.this);
									}
								});

						builder.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//

			}

			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				WarnDialog dialog = new WarnDialog(GVipActivity.this);
				dialog.setTitle(R.string.order)
						.setMessage(res)
						.setPositiveBtn(R.string.confirm,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								});
				dialog.show(getSupportFragmentManager(), "PayOrderFaild");
			}

		});

		GThreadExecutor.execute(r);
	}
	
	private void queryUserInfo(){
		
		JSONObject p = new JSONObject();
		try {
			p.put("cmd",    "user/info");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		Runnable r = new HttpRequest(this, p, new HttpCallback() {
			
			@Override
			public void faildData(int code, String res) {
				// TODO Auto-generated method stub
				super.faildData(code, res);
				
			}

			@Override
			public void sucessData(String res) {
				// TODO Auto-generated method stub
				super.sucessData(res);
				
				JSONObject data;
				try {
					data = new JSONObject(res);
					GolfAppliaction app = (GolfAppliaction) getApplication();
					GAccount acc = app.getAccount();
					
					acc.setName(data.getString("user_name"));
					acc.setPhone(data.getString("phone"));
					acc.setVipCardNo(data.getString("card_no"));
					acc.setEamil(data.getString("email"));
					acc.setSex(data.getString("sex"));
					acc.setBalance(data.getString("balance"));
					acc.setPoint(data.getString("point"));
					
					// Vip信息
					acc.setVipExpireDate(data.getString("vip_expire_date"));
					acc.setVipStatus(data.getInt("vip_status"));
					
					setVipInfo();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void finalWork() {
				// TODO Auto-generated method stub
			}

			
		});
		
		GThreadExecutor.execute(r);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		_selsectedVipChoice = (GVipChoiceBean) mAdapter.getItem(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /************************************************* 
         * 
         *  步骤3：处理银联手机支付控件返回的支付结果 
         *  
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
        	queryUserInfo();
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
