/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.happy.english.widget;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.happy.english.R;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This class has
 * all of the logic for adding, subtracting, and rendering Droidflakes.
 */
public class FlakeView extends View {

    Bitmap droid;       // The bitmap that all flakes use
    int res[] = {R.drawable.ball_1,R.drawable.ball_2,R.drawable.ball_3,R.drawable.ball_4,R.drawable.ball_5};
    int numFlakes = 0;  // Current number of flakes
    ArrayList<Flake> flakes = new ArrayList<Flake>(); // List of current flakes
    ArrayList<Point> locationList = new ArrayList<Point>(); // List of current flakes

    // Animator used to drive all separate flake animations. Rather than have potentially
    // hundreds of separate animators, we just use one and then update all flakes for each
    // frame of that single animation.
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime; // Used to track elapsed time for animations and fps
    int frames = 0;     // Used to track frames per second
    Paint textPaint;    // Used for rendering fps text
    float fps = 0;      // frames per second
    Matrix m = new Matrix(); // Matrix used to translate/rotate each flake during rendering
    String fpsString = "";
    String numFlakesString = "";
    int num ;
    /**
     * Constructor. Create objects used throughout the life of the View: the Paint and
     * the animator
     */
    public FlakeView(Context context) {
        super(context);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);

        // This listener is where the action is for the flak animations. Every frame of the
        // animation, we calculate the elapsed time and update every flake's position and rotation
        // according to its speed.
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (float)(nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlakes; ++i) {
                    Flake flake = flakes.get(i);
                    flake.y -= (flake.speed * secs);
                    if (flake.y < - flake.height) {
                        // If a flake falls off the bottom, send it back to the top
                        flake.y = getHeight() +  flake.height;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
                }
                // Force a redraw to see the flakes in their new positions and orientations
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
        
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		if(dm.density == 1){
			num = 3 ;
		}else if(dm.density == 1.5){
			num = 8 ;
		}else if(dm.density == 2){
			num = 10 ;
		}else if(dm.density == 3){
			num = 12 ;
		}else{
			num = 6 ;
		}
		droid = BitmapFactory.decodeResource(getResources(),res[0]);
        locationList = getLocationList(num , dm.widthPixels,dm.heightPixels,droid.getWidth(),droid.getHeight());
    }
    
    public static ArrayList<Point> getLocationList(int num ,int screenW,int screenH ,int picW,int picH){
		   ArrayList<Point> list = new ArrayList<Point>();
		   Random random = new Random() ;
		   for (int i = 0; i < num; i++) {
			   Point p = getPoint(random,list,screenW,screenH, picW, picH);
			   list.add(p);
		}
		   return list; 
	   }
    private static int time ;
   	private static Point getPoint(Random random, ArrayList<Point> list, int screenW, int screenH,
   			int picW, int picH) {
   		Point p = new Point() ;
   		p.x = random.nextInt(screenW - picW) ;
   		p.y =  random.nextInt(screenH - picH) ;
   		for (int i = 0; i < list.size(); i++) {
   			if( p.x >= (list.get(i).x - picW ) && p.x <= (list.get(i).x + picW) && 
   			    p.y >= (list.get(i).y - picH ) && p.y <= (list.get(i).y + picH)){
   				time ++ ;
   				if(time > 20){
   					time  = 0 ;
   					return p ;
   				}
   				return getPoint(random, list, screenW, screenH, picW, picH);
   			}
   		}
   		time  = 0 ;
   		return p;
   	}
   	
   	
    int getNumFlakes() {
        return numFlakes;
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
        numFlakesString = "numFlakes: " + numFlakes;
    }

    /**
     * Add the specified number of droidflakes.
     */
    void addFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            droid = BitmapFactory.decodeResource(getResources(),res[i % res.length]);
            flakes.add(Flake.createFlake(i,getWidth(),droid,locationList));
        }
        setNumFlakes(numFlakes + quantity);
    }

    /**
     * Subtract the specified number of droidflakes. We just take them off the end of the
     * list, leaving the others unchanged.
     */
    void subtractFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
           int index = numFlakes - i - 1;
            flakes.remove(index); 
        }
        setNumFlakes(numFlakes - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Reset list of droidflakes, then restart it with 8 flakes
        flakes.clear();
        numFlakes = 0;
        addFlakes(num);
        // Cancel animator in case it was already running
        animator.cancel();
        // Set up fps tracking and start the animation
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each flake: back-translate by half its size (this allows it to rotate around its center),
        // rotate by its current rotation, translate by its location, then draw its bitmap
        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width/2, -flake.height/2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width/2 + flake.x, flake.height/2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }
        // fps counter: count how many frames we draw and once a second calculate the
        // frames per second
        ++frames;
        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            float secs = (float) deltaTime / 1000f;
            fps = (float) frames / secs;
            fpsString = "fps: " + fps;
            startTime = nowTime;
            frames = 0;
        }
//        canvas.drawText(numFlakesString, getWidth() - 200, getHeight() - 50, textPaint);
//        canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80, textPaint);
    }

    public void pause() {
        // Make sure the animator's not spinning in the background when the activity is paused.
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

}
