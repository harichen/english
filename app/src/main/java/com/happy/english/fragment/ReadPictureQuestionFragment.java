package com.happy.english.fragment;

import com.happy.english.R;
import com.happy.english.R.id;
import com.happy.english.R.layout;
import com.happy.english.bean.Answer;
import com.happy.english.bean.Options;
import com.happy.english.bean.Question;
import com.happy.english.constant.Const;
import com.happy.english.support.file.FileManager;
import com.happy.english.support.utils.AppLogger;
import com.happy.english.support.utils.UIUtils;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ReadPictureQuestionFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link ReadPictureQuestionFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class ReadPictureQuestionFragment extends BaseFragment
        implements
        OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Question mQuestion;
	private int missionIndex;

    // private OnFragmentInteractionListener mListener;
    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     * 
     * @param param1
     *            Parameter 1.
     * @param param2
     *            Parameter 2.
     * @return A new instance of fragment QuestionThreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadPictureQuestionFragment newInstance(
            String param1, String param2)
    {
        ReadPictureQuestionFragment fragment = new ReadPictureQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ReadPictureQuestionFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mQuestion = (Question) getArguments()
                    .getSerializable(Const.QUESTION_READ_PICTURE);
            missionIndex =  getArguments().getInt(Const.MISSIONINDEX); 
        }
        else
        {
            AppLogger.e(Const.QUESTION_READ_PICTURE + " is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_seven,container,
                false);
        if (mQuestion != null)
        {
            bindView(view, mQuestion);
        }
        return view;
    }
    private void bindView(View view, Question mQuestion)
    {
        Options[] options = mQuestion.getOptions();
        TextView type7_stemDescption = (TextView) view
                .findViewById(R.id.type7_stem_descption);
        TextView type7_stemText = (TextView) view
                .findViewById(R.id.type7_stem_text);
        ImageView type7_stemRes = (ImageView) view
                .findViewById(R.id.type7_stem_res);
        type7_stemRes.setImageBitmap(FileManager.getBitmapByMission(missionIndex, mQuestion.getStem().getRes()));
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.type7_opt_rg);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group,
                    int checkedId)
            {
            	if(mDoneListener != null){
					mDoneListener.done(missionIndex, Const.ANSWERKEY[(checkedId - R.id.type7_opt_rb1)]);
				}
            }
        });
        RadioButton type7_opt_rb1 = (RadioButton) view
                .findViewById(R.id.type7_opt_rb1);
        RadioButton type7_opt_rb2 = (RadioButton) view
                .findViewById(R.id.type7_opt_rb2);
        RadioButton type7_opt_rb3 = (RadioButton) view
                .findViewById(R.id.type7_opt_rb3);
        RadioButton type7_opt_rb4 = (RadioButton) view
                .findViewById(R.id.type7_opt_rb4);
		item[0] = type7_opt_rb1 ;
		item[1] = type7_opt_rb2 ;
		item[2] = type7_opt_rb3 ;
		item[3] = type7_opt_rb4 ;
        type7_opt_rb1.setText(options[0].getOption());
        type7_opt_rb2.setText(options[1].getOption());
        type7_opt_rb3.setText(options[2].getOption());
        type7_opt_rb4.setText(options[3].getOption());
//        type7_stemDescption.setText(mQuestion
//        		.getStem().getDescption());
        type7_stemDescption.setText(Const.DESCPTION[5]);
        
        type7_stemText.setText(mQuestion.getStem()
                .getText());
    }



    @Override
    public void onClick(View v)
    {
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
    }
    
    public void help(String answer) {
    	removeWrongAnswer(answer);
    }
}
