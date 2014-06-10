package com.happy.english.fragment;

import java.util.Random;

import com.happy.english.R;
import com.happy.english.constant.Const;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

public class BaseFragment extends Fragment
{
    protected Resources mResources;
    protected QuestionBottomFragment questionBottomFragment;
    protected DoneListener mDoneListener ;
	public int missionIndex;
	public void setmDoneListener(DoneListener mDoneListener) {
		this.mDoneListener = mDoneListener;
	}

    public View []  item = new View[4];
    
	public interface DoneListener {
    	void done(int index,String answer);
		void undo();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mResources = getActivity().getResources();
        FragmentActivity mFragmentActivity = (FragmentActivity) getActivity();
        questionBottomFragment = (QuestionBottomFragment) mFragmentActivity
                .getSupportFragmentManager()
                .findFragmentById(R.id.frag_ques_bottom);
    }
    
    /**随机得到一个不是答案的指引
     * @param answer
     * @return
     */
    public int getRadomWrongIndex(String answer){
    	int radom = new Random().nextInt(4);
    	if(!Const.ANSWERKEY[radom].toLowerCase().equals(answer.toLowerCase())){
    		return radom;
    	}else{
    		return getRadomWrongIndex(answer);
    	}
    }
    
    
    /**去掉一个错误答案
     * @param answer
     */
    public void removeWrongAnswer(String answer){
    	final int wrongindex = getRadomWrongIndex(answer);
    	if(item[wrongindex].isSelected() || (item[wrongindex] instanceof RadioButton && ((RadioButton)item[wrongindex]).isChecked())){
    		mDoneListener.undo();
    	}
    	AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(1000);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) { 
				item[wrongindex].setVisibility(View.INVISIBLE);
			}
		});
		item[wrongindex].startAnimation(animation);
    }
    
    public void help(String answer){
    }
    
}
