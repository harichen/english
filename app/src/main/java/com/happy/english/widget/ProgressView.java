package com.happy.english.widget;

import com.happy.english.R;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

public class ProgressView  {
	
	private ProgressBar pb[] = new ProgressBar[6];

	public ProgressView(View view){
		init(view);
	}
	private void init(View view) {
		pb[0]  = (ProgressBar) view.findViewById(R.id.progressBar1);
		pb[1]  = (ProgressBar) view.findViewById(R.id.progressBar2);
		pb[2]  = (ProgressBar) view.findViewById(R.id.progressBar3);
		pb[3]  = (ProgressBar) view.findViewById(R.id.progressBar4);
		pb[4]  = (ProgressBar) view.findViewById(R.id.progressBar5);
		pb[5]  = (ProgressBar) view.findViewById(R.id.progressBar6);
		for (int i = 0; i < pb.length; i++) {
			pb[i].setMax(100);
		}
	}
	public void setProgress(int progress){
		if(progress >= pb.length){
			return ;
		}
		for (int i = 0; i < progress; i++) {
			pb[i].setProgress(100);
		}
		setTheCurrentProgress(progress); 
	}

	private int mTemp ; 
	private void setTheCurrentProgress(final int progress) {
		pb[progress].postDelayed(new Runnable() {
			@Override
			public void run() {
				if( mTemp < 100){
					mTemp += 5 ;
					pb[progress].setProgress(mTemp);
					setTheCurrentProgress(progress);
				}else{
					pb[progress].setProgress(mTemp);
					mTemp = 0 ;
				}
			}
		}, 1);
	}

}
