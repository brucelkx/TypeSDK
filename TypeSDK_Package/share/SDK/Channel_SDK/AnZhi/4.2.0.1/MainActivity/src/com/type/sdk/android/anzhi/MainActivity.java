package com.type.sdk.android.anzhi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.type.sdk.android.TypeSDKDefine;
import com.type.sdk.android.TypeSDKEvent;
import com.type.sdk.android.TypeSDKEvent.EventType;
import com.type.sdk.android.TypeSDKEventManager;
import com.type.sdk.android.TypeSDKLogger;
import com.type.sdk.android.TypeSDKDefine.AttName;
import com.type.sdk.android.TypeSDKEventListener;
import com.type.sdk.android.TypeSDKUpdateManager;
import com.type.sdk.android.BaseMainActivity;

import android.content.Intent;
import android.os.Bundle;

import com.type.sdk.android.TypeSDKData;
import com.type.sdk.android.TypeSDKTool;
import com.type.utils.HttpUtil;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends BaseMainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		String result = "";
		result += "~"
				+ TypeSDKBonjour.Instance().isHasRequest(
						TypeSDKDefine.AttName.REQUEST_INIT_WITH_SEVER);
		result += "~"
				+ TypeSDKBonjour.Instance().isHasRequest(
						TypeSDKDefine.AttName.SUPPORT_PERSON_CENTER);
		result += "~"
				+ TypeSDKBonjour.Instance().isHasRequest(
						TypeSDKDefine.AttName.SUPPORT_SHARE);

		TypeSDKLogger.i("result " + result);
		super.onCreate(savedInstanceState);
		TypeSDKLogger.i("android on create finish");
		// CallInitSDK();
		TypeSDKUpdateManager update = new TypeSDKUpdateManager(this,
				TypeSDKBonjour.Instance().platform
						.GetData(AttName.CHANNEL_ID),
				TypeSDKBonjour.Instance().platform
						.GetData("check_update_url"));
		update.checkUpdateInfo();
		TypeSDKEventManager.Instance().AddEventListener(
				EventType.AND_EVENT_INIT_FINISH, initListener);
		// SharkSDK.Instance().initSDK(_in_context, "");

	}

	TypeSDKEventListener initListener = new TypeSDKEventListener() {
		@Override
		public Boolean NotifySDKEvent(TypeSDKEvent event) {
			RealOncreate();
			return null;
		}
	};

	private void RealOncreate() {
		// SharkSDK.Instance().ClientInit(_in_context, "");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		TypeSDKBonjour.Instance().onNewIntent(intent);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		TypeSDKBonjour.Instance().onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		TypeSDKBonjour.Instance().onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TypeSDKBonjour.Instance().onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		TypeSDKLogger.e("sdk do destory");
		TypeSDKBonjour.Instance().onDestroy();

	}

	@Override
	protected void onResume() {
		super.onResume();
		TypeSDKBonjour.Instance().onResume();
	}

	/**
	 * ���ⲿ call �� init����
	 * 
	 * @param _in_context
	 * @param _in_data
	 */
	public void CallInitSDK() {
		String _in_data = "";
		TypeSDKBonjour.Instance().initSDK(_in_context, _in_data);
	}

	/**
	 * ���ⲿ call�� login����
	 * 
	 * @param _in_context
	 * @param _in_data
	 */
	public void CallLogin(String _in_data) {
		TypeSDKLogger.i("call login:" + _in_data);
		TypeSDKBonjour.Instance().ShowLogin(_in_context, _in_data);
	}

	/**
	 * ���ⲿ call ��logout����
	 * 
	 * @param _in_context
	 */
	public void CallLogout() {
		TypeSDKBonjour.Instance().ShowLogout(_in_context);
	}

	/***
	 * 
	 * payData.SetData(U3DSharkAttName.REAL_PRICE,inputStr);
	 * payData.SetData(U3DSharkAttName.ITEM_NAME,"sk bi");
	 * payData.SetData(U3DSharkAttName.ITEM_DESC,"desc");
	 * payData.SetData(U3DSharkAttName.ITEM_COUNT,"1");
	 * payData.SetData(U3DSharkAttName.ITEM_SEVER_ID,"id");
	 * payData.SetData(U3DSharkAttName.SEVER_ID,"1");
	 * payData.SetData(U3DSharkAttName.EXTRA,"extra
	 * 
	 * 
	 * ���ⲿcall�Ķ���֧������(rmb�һ� ��Ϸ��)
	 * 
	 * @param _in_context
	 * @param _in_data
	 * @return
	 */
	public String CallPayItem(final String _in_data) {
		TypeSDKLogger.i("CallPayItem:" + _in_data);
		new Thread() {
			@Override
			public void run() {
				String payMessage;
				try {
					payMessage = HttpUtil.http_get(TypeSDKBonjour
							.Instance().platform
							.GetData(AttName.SWITCHCONFIG_URL));
					if (((payMessage.equals("") || payMessage.isEmpty()) && openPay)
							|| TypeSDKTool.openPay(TypeSDKBonjour
									.Instance().platform
									.GetData(AttName.SDK_NAME), payMessage)) {
						TypeSDKBonjour.Instance().PayItem(_in_context,
								_in_data);
					} else {
						TypeSDKNotify_anzhi notify = new TypeSDKNotify_anzhi();
						TypeSDKData.PayInfoData payResult = new TypeSDKData.PayInfoData();
						payResult.SetData(AttName.PAY_RESULT, "0");
						notify.Pay(payResult.DataToString());
						Handler dialogHandler = new Handler(
								Looper.getMainLooper());
						dialogHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								TypeSDKTool.showDialog("暂未开放充值！！！",
										_in_context);
							}
						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}.start();
		return "client pay function finished";
	}

	/***
	 * ���ⲿcall �� �Ƕ���֧������һ��ƶ�����Ʒ��
	 * 
	 * @param _in_context
	 * @param _in_data
	 * @return
	 */
	public String CallExchangeItem(String _in_data) {
		return TypeSDKBonjour.Instance().ExchangeItem(_in_context,
				_in_data);
	}

	/***
	 * ���ⲿ���õ� ��ʵ����������
	 * 
	 * @param _in_context
	 */
	public void CallToolBar() {
		TypeSDKBonjour.Instance().ShowToolBar(_in_context);
	}

	public void CallHideToolBar() {
		TypeSDKBonjour.Instance().HideToolBar(_in_context);
	}

	/***
	 * ���ⲿ���õ���ʵ�û����ĺ���
	 * 
	 * @param _in_context
	 */
	public void CallPersonCenter() {
		TypeSDKBonjour.Instance().ShowPersonCenter(_in_context);
	}

	public void CallHidePersonCenter() {
		TypeSDKBonjour.Instance().HidePersonCenter(_in_context);
	}

	public void CallShare(String _in_data) {
		TypeSDKBonjour.Instance().ShowShare(_in_context, _in_data);
	}

	public void CallSetPlayerInfo(String _in_data) {
		TypeSDKBonjour.Instance().SetPlayerInfo(_in_context, _in_data);
	}

	public void CallExitGame() {
		TypeSDKBonjour.Instance().ExitGame(_in_context);
	}

	public void CallDestory() {
		TypeSDKBonjour.Instance().onDestroy();
	}

	public int CallLoginState() {
		return TypeSDKBonjour.Instance().LoginState(_in_context);
	}

	public String CallUserData() {
		return TypeSDKBonjour.Instance().GetUserData();
	}

	public String CallPlatformData() {
		return TypeSDKBonjour.Instance().GetPlatformData();
	}

	public boolean CallIsHasRequest(String _in_data) {
		return TypeSDKBonjour.Instance().isHasRequest(_in_data);
	}

	public String CallAnyFunction(String FuncName, String _in_data) {
		Method[] me = TypeSDKBonjour.Instance().getClass().getMethods();
		for (int i = 0; i < me.length; ++i) {
			if (me[i].getName().equals(FuncName)) {
				try {
					return (String) me[i].invoke(
							TypeSDKBonjour.Instance(), _in_context,
							_in_data);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return "error";
	}

	public void AddLocalPush(String _in_data) {
		TypeSDKLogger.i(_in_data);
		TypeSDKBonjour.Instance().AddLocalPush(_in_context, _in_data);
	}

	public void RemoveLocalPush(String _in_data) {
		TypeSDKLogger.i(_in_data);
		TypeSDKBonjour.Instance()
				.RemoveLocalPush(_in_context, _in_data);
	}

	public void RemoveAllLocalPush() {

		TypeSDKBonjour.Instance().RemoveAllLocalPush(_in_context);
	}

}
