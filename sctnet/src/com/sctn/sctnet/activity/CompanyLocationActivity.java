package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.sctn.sctnet.R;
/**
 * 用地图显示公司的位置
 * @author xueweiwei
 *
 */
public class CompanyLocationActivity extends MapActivity {
	
	private MapView mMapView = null;	// 地图View
	private MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	private BMapManager mapManager;
	private TextView desText;
	
	private ImageView image;
	
	private String city;//城市
	private String detailAddress;//详细地址
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.company_location_activity);

     	mapManager = new BMapManager(getApplication());
     	// init方法的第一个参数需填入申请的API Key
     	mapManager.init("C66C0501D0280744759A6957C42543AE38F5D540", null);
     // 如果使用地图SDK，请初始化地图Activity
     	super.initMapActivity(mapManager);
     	
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        city = bundle.getString("city");
        detailAddress = bundle.getString("detailAddress");
  
     	image = (ImageView) findViewById(R.id.point_image);
        mMapView = (MapView)findViewById(R.id.map_view);
        
        MapController mapController = mMapView.getController(); 
     	mapController.setZoom(18);// 设置缩放级别 
        mMapView.setBuiltInZoomControls(true);
        mMapView.setDrawOverlayWhenZooming(true);
        
        desText = (TextView) this.findViewById(R.id.map_bubbleText);
        desText.setText("正在加载中...");
        // 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(mapManager, new MKSearchListener() {
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
				//	String str = String.format("错误号：%d", error);
					desText.setVisibility(View.GONE);
					image.setVisibility(View.GONE);
					Toast.makeText(CompanyLocationActivity.this,"抱歉，未找到相关位置", Toast.LENGTH_LONG).show();
					return;
				}
				image.setVisibility(View.GONE);
				mMapView.getController().animateTo(res.geoPt);
					
//				String strInfo = String.format("纬度：%f 经度：%f\r\n", res.geoPt.getLatitudeE6()/1e6, 
//							res.geoPt.getLongitudeE6()/1e6);
				desText.setText(detailAddress);
			//	Toast.makeText(CompanyLocationActivity.this, strInfo, Toast.LENGTH_LONG).show();
				Drawable marker = getResources().getDrawable(R.drawable.point_start);  //得到需要标在地图上的资源
				marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
						.getIntrinsicHeight());   //为maker定义位置和边界
				
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(new OverItemT(marker, CompanyLocationActivity.this, res.geoPt, res.strAddr));
				

			}
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				if (error != 0 || res == null) {
					Toast.makeText(CompanyLocationActivity.this, "解析失败", Toast.LENGTH_LONG).show();
					return;
				}
				if (res != null && res.getCurrentNumPois() > 0) {
					GeoPoint ptGeo = res.getAllPoi().get(0).pt;
					// 移动地图到该点：
					mMapView.getController().animateTo(ptGeo);
					
					String strInfo = String.format("纬度：%f 经度：%f\r\n", ptGeo.getLatitudeE6()/1e6, 
							ptGeo.getLongitudeE6()/1e6);
					strInfo += "\r\n附近有：";
					for (int i = 0; i < res.getAllPoi().size(); i++) {
						strInfo += (res.getAllPoi().get(i).name + ";");
					}
					Toast.makeText(CompanyLocationActivity.this, strInfo, Toast.LENGTH_LONG).show();
				}
			}
			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				// TODO Auto-generated method stub
				
			}
            public void onGetRGCShareUrlResult(String arg0, int arg1) {
                // TODO Auto-generated method stub
                
            }

        });
        
  
  
        
        Thread mThread = new Thread(new Runnable() {// 启动新的线程，
			public void run() {
				requestData();
			}
		});
        mThread.start();
	}
	
	private void requestData() {
		Message msg = new Message();
		msg.what = 0;
		handler.sendMessage(msg);
	}
	// 处理线程发送的消息
		private Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					mSearch.geocode(detailAddress, city);
					break;
				}
			}
		};
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
		static class MyGeneralListener implements MKGeneralListener {
		
			public void onGetNetworkState(int iError) {
			//	Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), "网络连接异常处理", Toast.LENGTH_LONG).show();
				
			}

		
			public void onGetPermissionState(int iError) {
				Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
				if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
					// 授权Key错误：
			//		Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), "百度授权出错", Toast.LENGTH_LONG).show();
					
				}
			}
		}
	@Override
	protected void onPause() {
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}
	@Override
	protected void onResume() {
		if (mapManager != null) {
			mapManager.start();
		}
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	class OverItemT extends ItemizedOverlay<OverlayItem>{
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

		public OverItemT(Drawable marker, Context context, GeoPoint pt, String title) {
			super(boundCenterBottom(marker));
			
			mGeoList.add(new OverlayItem(pt, title, null));

			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		@Override
		public boolean onSnapToItem(int i, int j, Point point, MapView mapview) {
			Log.e("ItemizedOverlayDemo","enter onSnapToItem()!");
			return false;
		}
	}

}
