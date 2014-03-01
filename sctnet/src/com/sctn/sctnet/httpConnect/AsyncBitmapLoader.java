package com.sctn.sctnet.httpConnect;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sctn.sctnet.Utils.PhoneUtil;
import com.sctn.sctnet.Utils.SDCardUtil;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


public class AsyncBitmapLoader {
	/**
	 * 内存图片软引用缓冲
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache;
	private static HttpClient httpClient;
	public static HttpPost post;

	// private int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	public AsyncBitmapLoader() {
		// imageCache = new HashMap<String, SoftReference<Bitmap>>();
		initImageCache();
	}

	/**
	 * 静态单例 保证全局唯一
	 * */
	private HashMap<String, SoftReference<Bitmap>> initImageCache() {
		if (imageCache == null) {
			imageCache = new HashMap<String, SoftReference<Bitmap>>();
		}
		return imageCache;
	}

	/**
	 * 获取HttpClient静态对象，用于和服务器端通信
	 * 
	 * @return httpClient
	 */
	private static HttpClient createHttpClient() {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 500000);
			HttpConnectionParams.setSoTimeout(params, 1800000);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);
		}
		return httpClient;

	}


	/***
	 * 
	 * */
	public Bitmap loadBitmap(final ImageView imageView, final String imageName, final String image, final boolean zoomFlag, final int width, final int height, final ImageCallBack imageCallBack) {

		final int outputWidth = PhoneUtil.getPhoneScreenWidth();
		final int outputHight = 0; // 后台 图片的比例是 5:4
		// final int outputWidth = 100;
		// final int outputHight = 80;

		// 在内存缓存中，则返回Bitmap对象
		if (imageCache.containsKey(imageName)) {
			SoftReference<Bitmap> reference = imageCache.get(imageName);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				if (zoomFlag) {
					bitmap = zoomImg(bitmap, width, height);// 如果zoomFlag
					return bitmap;
				} else {
					return bitmap;
				}
			}
		} else {
			// 加上一个对本地缓存的查找
			String bitmapName = imageName.substring(imageName.lastIndexOf("/") + 1);
			// 如果内存卡能用
			if (SDCardUtil.IsSDCardExist()) {
				File cacheDir = new File(Constant.SYS_IMAGE_DATA_STORE);
				File[] cacheFiles = cacheDir.listFiles();
				int i = 0;
				if (null != cacheFiles) {
					for (; i < cacheFiles.length; i++) {
						if (bitmapName.equals(cacheFiles[i].getName()) && cacheFiles[i].length() > 0) {
							break;
						}
					}
					if (i < cacheFiles.length) {
                        
						/*BitmapFactory.Options options=new BitmapFactory.Options();
						options.inSampleSize = 10;
						Bitmap bitmap= BitmapFactory.decodeFile(cacheFiles[i].toString(),options); 
			               */
						Bitmap bitmap = BitmapFactory.decodeFile(cacheFiles[i].toString());
						if (zoomFlag) {
							bitmap = zoomImg(bitmap, width, height);
							return bitmap;
						}
						return bitmap;
					}
				}
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				Bitmap zoomBitmap = null;
				if (zoomFlag) {
					zoomBitmap = zoomImg((Bitmap) msg.obj, width, height);// 在回调之前进行压缩
				}
				imageCallBack.imageLoad(imageView, zoomBitmap);
			}
		};
		final Bitmap bitmapImage[] = new Bitmap[1];
		// 如果不在内存缓存中，也不在本地（被jvm回收掉），则开启线程下载图片
		Thread thread = new Thread(new Runnable() {
			public void run() {
				HttpClient client = createHttpClient();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("image", image);
				map.put("outputWidth", outputWidth);
				map.put("outputHeight", outputHight);
				map.put("cutType", "w");
				map.put("proportion", "true");
				String parameter = new JSONObject(map).toString();// 构造参数

				String serverUrl = Constant.ServerImageURL;
				HttpPost post = new HttpPost(serverUrl);
				post.addHeader("Content-Type", "image/jpg");
				byte[] result = null;
				FileOutputStream fos = null;

				try {
					String encoderJson = URLEncoder.encode(parameter, HTTP.UTF_8);
					StringEntity resEntity = new StringEntity(encoderJson, "UTF-8");
					post.setEntity(resEntity);
					// 获取响应的结果
					HttpResponse response = client.execute(post);
					// 获取HttpEntity
					HttpEntity respEntity = response.getEntity();
					// 获取响应的结果信息
					result = EntityUtils.toByteArray(respEntity);
					if (!StringUtil.isBlank(result.toString())) {
						InputStream bitmapIs = new ByteArrayInputStream(result);
						bitmapImage[0] = BitmapFactory.decodeStream(bitmapIs);
						Bitmap bitmap = bitmapImage[0];
						if (bitmap != null) {
							Message msg = handler.obtainMessage(0, bitmap);
							handler.sendMessage(msg);
							// 将其保存到 软内存中去
							imageCache.put(imageName, new SoftReference<Bitmap>(bitmap));
							// 将其存到手机里去

							if (SDCardUtil.IsSDCardExist()) {
								// 目录是否存在 ->否则新建
								File dirImageStore = new File(Constant.SYS_IMAGE_DATA_STORE);
								if (!dirImageStore.exists()) {
									dirImageStore.mkdirs();
								}
								File imageFile = new File(Constant.SYS_IMAGE_DATA_STORE + imageName.substring(imageName.lastIndexOf("/") + 1));
								if (!imageFile.exists()) {
									imageFile.createNewFile();
									fos = new FileOutputStream(imageFile);
									bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
									fos.close();
								}
							} else {

								// Toast.makeText(BeautyApp.getInstance().getApplicationContext(),
								// "请插入SD卡", Toast.LENGTH_LONG).show();

							}
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		});
		thread.start();
		return bitmapImage[0];
	}

	
	// 回调函数
	public interface ImageCallBack {
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}

	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024;
		return (int) sdFreeMB;
	}

	public void delete() {
		File cacheDir = new File(Constant.SYS_IMAGE_DATA_STORE);
		File[] cacheFiles = cacheDir.listFiles();
		int i = 0;
		if (null != cacheFiles) {
			for (; i < cacheFiles.length; i++) {
				cacheFiles[i].delete();
			}
		}
	}

	public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		if (newHeight == 0) {
			scaleHeight = scaleWidth;
		}// 取得想要缩放的matrix参数
		if (newWidth == 0) {
			scaleWidth = scaleHeight;
		}// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}
}
