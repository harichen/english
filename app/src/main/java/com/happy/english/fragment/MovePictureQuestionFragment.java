package com.happy.english.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.english.R;
import com.happy.english.bean.Answer;
import com.happy.english.bean.Options;
import com.happy.english.bean.Question;
import com.happy.english.constant.Const;
import com.happy.english.support.file.FileManager;
import com.happy.english.support.lib.MyRadioGroup;
import com.happy.english.support.lib.MyRadioGroup.OnCheckedChangeListener;
import com.happy.english.support.lib.RectangleProgressView;
import com.happy.english.support.utils.AppLogger;
import com.happy.english.support.utils.UIUtils;
import com.happy.english.widget.DragModule;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MovePictureQuestionFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MovePictureQuestionFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class MovePictureQuestionFragment extends BaseFragment
        implements OnClickListener
{
    private Question mQuestion;
    private DragModule mDragModule ;

    public static MovePictureQuestionFragment newInstance(
            Question question)
    {
        MovePictureQuestionFragment fragment = new MovePictureQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Const.QUESTION_MOVE_PICTURE, question);
        fragment.setArguments(args);
        return fragment;
    }

    public MovePictureQuestionFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mQuestion = (Question) getArguments()
                    .getSerializable(Const.QUESTION_MOVE_PICTURE);
            missionIndex =  getArguments().getInt(Const.MISSIONINDEX); 
        }
        else
        {
            AppLogger.e(Const.QUESTION_MOVE_PICTURE + "is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_question_two, container,
                false);
        if (mQuestion != null)
        {
        	mDragModule = new DragModule(getActivity(),view,missionIndex);
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
				mDragModule.getFourItemPosition();
			}
		},300);
    }

    private void bindView(View view, Question mQuestion)
    {
        Options[] options = mQuestion.getOptions();
        TextView type2_stemDescption = (TextView) view
                .findViewById(R.id.type2_stem_descption);
        TextView type2_stemText = (TextView) view
                .findViewById(R.id.type2_stem_text);
        
        mDragModule.iniDate(options);
        
//        type2_stemDescption.setText(mQuestion
//                .getStem().getDescption());
        type2_stemDescption.setText(Const.DESCPTION[1]);
        type2_stemText.setText(mQuestion.getStem()
                .getText());
    }

	@Override
	public void onClick(View v) {
	}

//    @Override
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//            case R.id.btn_prompt:
//                UIUtils.showShort(getActivity(),
//                        "this is prompt");
//                break;
//            case R.id.btn_submit:
//                LevelOneAnswer.showAnswer(getActivity(), mQuestion, Const.LEVEL_ONE_ANSWER_ID);
//                // 提交答案给服务器
//                break;
//            case R.id.btn_share:
//                UIUtils.showShort(getActivity(),
//                        "this is share");
//                break;
//            default:
//                break;
//        }
//    }
	
    public void help(String answer) {
    	mDragModule.help(answer);
    }
	
}
