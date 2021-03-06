package com.sctn.sctnet.view;

import com.sctn.sctnet.R;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.DashPathEffect;  
import android.graphics.Paint;  
import android.graphics.Path;  
import android.graphics.PathEffect;  
import android.util.AttributeSet;  
import android.view.View;  
 
public class DashedLineBlueView extends View { 
    
    public DashedLineBlueView(Context context, AttributeSet attrs) {  
        super(context, attrs);                      
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub  
        super.onDraw(canvas);          
        Paint paint = new Paint();  
        paint.setStyle(Paint.Style.STROKE);  
        paint.setColor(getResources().getColor(R.color.blue));  
        Path path = new Path();       
        path.moveTo(0, 10);  
        path.lineTo(this.getWidth(),10);        
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
        paint.setPathEffect(effects);  
        canvas.drawPath(path, paint);  
    } 

}
