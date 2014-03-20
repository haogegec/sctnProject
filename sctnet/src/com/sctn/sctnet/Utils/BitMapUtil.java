package com.sctn.sctnet.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class BitMapUtil {

	// 将图变成二进制字节
	private static byte[] compressBitmap(Bitmap bitmap) {
		return bmp2ByteArray(bitmap, 50); // 压缩到50K 以内
	}

	// 转成Base64 位编码 的字符串
	public static String bitMap2Base64Encode(Bitmap bitmap) {
		return Base64.encodeToString(compressBitmap(bitmap), Base64.DEFAULT);
	}

	// 根据高度和宽度 设置图片
	public static Bitmap zoomBitmap(Bitmap bm, int newWidth, int newHeight) {
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
	/**
	 * Drawable转BitMap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable == null)
			return null;
		BitmapDrawable bd = (BitmapDrawable) drawable;
		return bd.getBitmap();

	}
	/**
	 * BitMap TO 二进制数组
	 * 
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmp2ByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static byte[] bmp2ByteArray(Bitmap bitmap, int size) {
		ByteArrayOutputStream baArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baArrayOutputStream);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baArrayOutputStream.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baArrayOutputStream.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baArrayOutputStream);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 50;// 每次都减少10
		}
		try {
			return baArrayOutputStream.toByteArray();
		} finally {
			try {
				baArrayOutputStream.flush();
				baArrayOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/**
	 * 
	 * @param bitmap
	 * @param size
	 * @return
	 */
	public static Bitmap bmp2bmpByCompress(Bitmap bitmap, int size) {
		byte[] byteBitmap = bmp2ByteArray(bitmap, size);
		if (byteBitmap.length != 0) {
			return BitmapFactory.decodeByteArray(byteBitmap, 0, byteBitmap.length);
		} else {
			return null;
		}
	}
}
