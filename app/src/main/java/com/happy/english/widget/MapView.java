package com.happy.english.widget;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.LinearGradient;  
import android.graphics.Paint;  
import android.graphics.Path;  
import android.graphics.Shader;  
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;  
  
public class MapView extends View {  
  
    public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	public static int MAX  = 100;
    @Override  
    protected void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub   
        super.onDraw(canvas);  
        /*设置背景为白色*/  
        canvas.drawColor(Color.TRANSPARENT);  
        Paint paint=new Paint();  
        /*去锯齿*/  
        paint.setAntiAlias(true);  
        /*设置paint的颜色*/  
        paint.setColor(Color.GRAY);  
        /*设置paint的　style　为STROKE：實心*/  
        paint.setStyle(Paint.Style.STROKE);  
        /*设置paint的外框宽度*/  
        paint.setStrokeWidth(1);  
        /*得到蜘蛛背景图*/  
        Path path=new Path();  
        for (int i = 0; i < 5; i++) {
        	path.reset();
			path = getFiveTanglePath(path, i* MAX/5) ;
			canvas.drawPath(path, paint);  
		}
        canvas.drawLine(MAX*sin(72), MAX, 0 , (float)(MAX*(1 - sin(18))), paint);
        canvas.drawLine(MAX*sin(72), MAX,(float)(MAX*(sin(72) - sin(36)) )  ,  (float)(MAX * (1 +  sin(54) ) ), paint);
        canvas.drawLine(MAX*sin(72), MAX,  (float)(MAX*(sin(72) +  sin(36)) ) , (float)(MAX * (1 +  sin(54) )) , paint);
        canvas.drawLine(MAX*sin(72), MAX,(float)(2 * MAX* sin(72)),(float)(MAX*( 1 - sin(18)) ) , paint);
        canvas.drawLine(MAX*sin(72), MAX,  (float)(MAX* sin(72) ),0, paint);
        
        /*设置paint　的style为　FILL：实心*/  
        paint.setStyle(Paint.Style.FILL);  
        /*设置paint的颜色*/  
        paint.setColor(0x8800ff00);  
        path.reset();
		path = getMyPath(path, 100,80,90, 90,100);
        canvas.drawPath(path, paint);  
          
        }  
    
    /**得到蜘蛛背景图
     * @param path
     * @param b
     * @return
     */
    public Path getFiveTanglePath(Path path ,int b){
    	float  degree = (float)((float) b / 100) ;
    	path.moveTo(  0 +   degree * (MAX*sin(72)) ,               (float)(MAX*( 1 - sin(18)))  +    degree * (MAX*sin(18))    );  
    	path.lineTo(  (float)(MAX*(sin(72) -  sin(36)) ) + degree* (MAX*sin(36)) ,                (float)(MAX * (1 +  sin(54) ) ) -  degree* (MAX*sin(54))  );  
    	path.lineTo(  (float)(MAX*(sin(72) +  sin(36)) ) -  degree* (MAX*sin(36)),                 (float)(MAX * (1 +  sin(54) ))  - degree * (MAX*sin(54)) );  
    	path.lineTo(  (float)(2 * MAX* sin(72)) -  degree* (MAX*sin(72))  ,                  (float)(MAX*( 1 - sin(18)) )   +    degree* (MAX*sin(18))    );  
    	path.lineTo(  (float)(MAX* sin(72) ) ,     0 +  degree*MAX     );  
    	path.close();  
    	return path; 
    }
    
    
    /**得到个人能力图
     * @param path
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @return
     */
    public Path getMyPath(Path path ,int a,int b,int c, int d,int e){
    	float  degree1 = (float)((float)(100-a) / 100) ;
    	float  degree2 = (float)((float)(100-b) / 100) ;
    	float  degree3 = (float)((float)(100-c) / 100) ;
    	float  degree4 = (float)((float)(100-d) / 100) ;
    	float  degree5 = (float)((float)(100-e) / 100) ;
    	path.moveTo(  0 +   degree1 * (MAX*sin(72)) ,               (float)(MAX*( 1 - sin(18)))  +    degree1 * (MAX*sin(18))    );  
		path.lineTo(  (float)(MAX*(sin(72) -  sin(36)) ) + degree2* (MAX*sin(36)) ,                (float)(MAX * (1 +  sin(54) ) ) -  degree2* (MAX*sin(54))  );  
		path.lineTo(  (float)(MAX*(sin(72) +  sin(36)) ) -  degree3* (MAX*sin(36)),                 (float)(MAX * (1 +  sin(54) ))  - degree3 * (MAX*sin(54)) );  
		path.lineTo(  (float)(2 * MAX* sin(72)) -  degree4* (MAX*sin(72))  ,                  (float)(MAX*( 1 - sin(18)) )   +    degree4* (MAX*sin(18))    );  
		path.lineTo(  (float)(MAX* sin(72) ) ,     0 +  degree5*MAX     );  
		path.close();  
		return path; 
    }
  
    public float sin(int a){
    	return(float)Math.sin(a*Math.PI/180) ;
    }
      
}  

