package com.happy.english.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link SelectPictureQuestionFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link SelectPictureQuestionFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class SelectPictureQuestionFragment extends BaseFragment
        implements
        OnClickListener
{
    private Question mQuestion;
    private ImageView type1_optRes1;
    private ImageView type1_optRes2;
    private ImageView type1_optRes3;
    private ImageView type1_optRes4;
    private RadioButton []  rb = new RadioButton[4];

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     * 
     * @param param1
     *            Parameter 1.
     * @param param2
     *            Parameter 2.
     * @return A new instance of fragment QuestionOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectPictureQuestionFragment newInstance(
            Question question)
    {
        SelectPictureQuestionFragment fragment = new SelectPictureQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Const.QUESTION_SELECT_PICTURE, question);
        fragment.setArguments(args);
        return fragment;
    }

    public SelectPictureQuestionFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() == null)
        {
            AppLogger.e(Const.QUESTION_SELECT_PICTURE + " is null");
        }
        else
        {
            mQuestion = (Question) getArguments()
                    .getSerializable(Const.QUESTION_SELECT_PICTURE);
            missionIndex =  getArguments().getInt(Const.MISSIONINDEX);  
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_question_select_picture, container,
                false);
        if (mQuestion != null){
            bindView(view, mQuestion);
        }
        return view;
    }

    private void bindView(View view, Question mQuestion)
    {
        Options[] options = mQuestion.getOptions();
        TextView type1_stemDescption = (TextView) view
                .findViewById(R.id.type1_stem_descption);
        TextView type1_stemText = (TextView) view
                .findViewById(R.id.type1_stem_text);
        item[0] =  view.findViewById(R.id.type1_opt_item1);
        item[1] =  view.findViewById(R.id.type1_opt_item2);
        item[2] =  view.findViewById(R.id.type1_opt_item3);
        item[3] =  view.findViewById(R.id.type1_opt_item4);
        
        type1_optRes1 = (ImageView) view
                .findViewById(R.id.type1_opt_res1);
        type1_optRes2 = (ImageView) view
                .findViewById(R.id.type1_opt_res2);
        type1_optRes3 = (ImageView) view
                .findViewById(R.id.type1_opt_res3);
        type1_optRes4 = (ImageView) view
                .findViewById(R.id.type1_opt_res4);
        rb[0] = (RadioButton) view
                .findViewById(R.id.rb1);
        rb[1] = (RadioButton) view
                .findViewById(R.id.rb2);
        rb[2] = (RadioButton) view
                .findViewById(R.id.rb3);
        rb[3] = (RadioButton) view
                .findViewById(R.id.rb4);
        item[0].setOnClickListener(this);
        item[1].setOnClickListener(this);
        item[2].setOnClickListener(this);
        item[3].setOnClickListener(this);
        rb[0].setOnClickListener(this);
        rb[1].setOnClickListener(this);
        rb[2].setOnClickListener(this);
        rb[3].setOnClickListener(this);
        type1_optRes1
                .setImageBitmap(FileManager
                        .getBitmapByMission(missionIndex, options[0]
                                .getRes()));
        type1_optRes2
                .setImageBitmap(FileManager
                        .getBitmapByMission(missionIndex, options[1]
                                .getRes()));
        type1_optRes3
                .setImageBitmap(FileManager
                        .getBitmapByMission(missionIndex, options[2]
                                .getRes()));
        type1_optRes4
                .setImageBitmap(FileManager
                        .getBitmapByMission(missionIndex, options[3]
                                .getRes()));
//        type1_stemDescption.setText(mQuestion
//        		.getStem().getDescption());
        type1_stemDescption.setText(Const.DESCPTION[0]);
        type1_stemText.setText(mQuestion.getStem()
                .getText());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.type1_opt_item1:
            case R.id.rb1:
                ChangedSelected(0);
                break;
            case R.id.type1_opt_item2:
            case R.id.rb2:
                ChangedSelected(1);
                break;
            case R.id.type1_opt_item3:
            case R.id.rb3:
                ChangedSelected(2);
                break;
            case R.id.type1_opt_item4:
            case R.id.rb4:
                ChangedSelected(3);
                break;
//            case R.id.btn_prompt:
//                UIUtils.showShort(getActivity(),
//                        "this is prompt");
//                break;
//            case R.id.btn_submit:
//                break;
//            case R.id.btn_share:
//                UIUtils.showShort(getActivity(),
//                        "this is share");
//                break;
        }
    }
    void setCheckBtn(int index){
    	for (int i = 0; i < rb.length; i++) {
    		if(index == i){
    			rb[i].setChecked(true);
    			item[i].setSelected(true);
    		}else{
    			rb[i].setChecked(false);
    			item[i].setSelected(false);
    		}
		}
    	
    }

    public void ChangedSelected(int selected)
    {
    	setCheckBtn(selected);
    	if(mDoneListener != null){
    		mDoneListener.done(0, Const.ANSWERKEY[selected]);
    	}
    }
    
    public void help(String answer) {
    	removeWrongAnswer(answer);
    }
}
