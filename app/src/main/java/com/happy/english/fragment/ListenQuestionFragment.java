package com.happy.english.fragment;

import java.io.File;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.happy.english.R;
import com.happy.english.bean.Options;
import com.happy.english.bean.Question;
import com.happy.english.constant.Const;
import com.happy.english.support.file.FileManager;
import com.happy.english.support.utils.AppLogger;
import com.happy.english.support.utils.UIUtils;
import com.happy.english.widget.MusicDragModule;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ListenQuestionFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link ListenQuestionFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class ListenQuestionFragment extends BaseFragment implements
		OnClickListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private Question mQuestion;
	private ImageView type4_btn_play_icon;
	private MediaPlayer mediaPlayer;
	Runnable iconChangeRunnable = new Runnable() {
		@Override
		public void run() {
			type4_btn_play_icon
					.setImageResource(R.drawable.sound_play1
							+ iconIndex % 2);
			iconIndex++;
			starIconPlay();
		}
	};
	private int iconIndex;
	private Uri uri;
	private MusicDragModule mDragModule;
	private SeekBar mSeekBar;
	private boolean isPlaying = true ;

	private void starIconPlay() {
		type4_btn_play_icon.postDelayed(iconChangeRunnable, 300);
	}

	public static ListenQuestionFragment newInstance(String param1,
			String param2) {
		ListenQuestionFragment fragment = new ListenQuestionFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ListenQuestionFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mQuestion = (Question) getArguments().getSerializable(
					Const.QUESTION_LISTEN);
            missionIndex =  getArguments().getInt(Const.MISSIONINDEX); 
		} else {
			AppLogger.e(Const.QUESTION_THREE + " is null");
		}
		
		String srcDir = Const.MISSION_FILE + missionIndex + File.separator
				+ Const.RESOURCE + File.separator +mQuestion.getStem().getRes();
		File file = new File(srcDir);
		if (file.exists()) {
			String mp3Name = file.toString();
			uri = Uri.parse("file://" + mp3Name);
			mediaPlayer = MediaPlayer.create(getActivity(), uri);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					type4_btn_play_icon.removeCallbacks(iconChangeRunnable);
					type4_btn_play_icon
					.setImageResource(R.drawable.sound_play1);
				}
			});
		}else{
			UIUtils.showToast(getActivity(), "mp3文件有误", 1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_question_four,
				container, false);
		if (mQuestion != null) {
			mDragModule = new MusicDragModule(getActivity(), view);
			mDragModule.setDoneListener(mDoneListener);
			bindView(view, mQuestion);
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mDragModule.getThreeItemPosition();
			}
		}, 300);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		isPlaying  = false ;
		if(mediaPlayer.isPlaying()){
			handler.removeCallbacks(delaythread);
			type4_btn_play_icon.removeCallbacks(iconChangeRunnable);
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null ;
		}
		super.onStop();
	}

	private void bindView(View view, Question mQuestion) {
		Options[] options = mQuestion.getOptions();
		TextView type4_stem_Descption = (TextView) view
				.findViewById(R.id.type4_stem_descption);
		TextView type4_stem_Text = (TextView) view
				.findViewById(R.id.type4_stem_text);
		type4_btn_play_icon = (ImageView) view
				.findViewById(R.id.type4_btn_play_icon);
		view.findViewById(R.id.type4_btn_play).
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					type4_btn_play_icon.removeCallbacks(iconChangeRunnable);
					type4_btn_play_icon.setImageResource(R.drawable.sound_play1);
					iconIndex = 0;
					isPlaying = false ;
				} else {
					isPlaying = true ;
					mediaPlayer.start();
					starIconPlay();
					new Thread(delaythread).start();
				}
			}
		});
//		type4_stem_Descption.setText(mQuestion.getStem().getDescption());
		type4_stem_Descption.setText(Const.DESCPTION[2]);
		
		type4_stem_Text.setText(mQuestion.getStem().getText());

		TextView tv_time = (TextView) view
				.findViewById(R.id.tv_time);
		tv_time.setText(mediaPlayer.getDuration()/1000+"s");
		
		mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
		synSeekBarAndPlay();
		mDragModule.iniDate(options);
	}

	private void synSeekBarAndPlay() {
		mSeekBar.setMax(mediaPlayer.getDuration());
		mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
	}

	class SeekBarChangeEvent implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// 开始拖动进度条
		}
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			if(mediaPlayer.isPlaying()){
				handler.removeCallbacks(delaythread);
				mediaPlayer.seekTo(arg0.getProgress());
				// 将media进度设置为当前seekbar的进度
			}else{
				isPlaying = true ;
				mediaPlayer.start();
				starIconPlay();
				mediaPlayer.seekTo(arg0.getProgress());
				new Thread(delaythread).start();
			}
			
			
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(mediaPlayer != null ){
				mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
			}
		}
	};// 实现消息传递
	class DelayThread implements Runnable{
		int milliseconds;
		public DelayThread(int i) {
			milliseconds = i;
		}
		public void run() {
			while (isPlaying) {
				try {
					handler.sendEmptyMessage(0);
					SystemClock.sleep(milliseconds);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	
	}
	DelayThread delaythread = new DelayThread(500);

	@Override
	public void onClick(View v) {
		// switch (v.getId())
		// {
		// case R.id.btn_prompt:
		// UIUtils.showShort(getActivity(),
		// "this is prompt");
		// break;
		// case R.id.btn_submit:
		// break;
		// case R.id.btn_share:
		// UIUtils.showShort(getActivity(),
		// "this is share");
		// break;
		// default:
		// break;
		// }
	}
	public void help(String answer){
		mDragModule.help(answer);
	}
}
