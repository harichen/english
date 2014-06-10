package com.happy.english.fragment;

import com.happy.english.R;
import com.happy.english.constant.Const;
import com.happy.english.manager.CoinManager;
import com.happy.english.widget.ProgressView;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link QuestionTitleFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link QuestionTitleFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class QuestionTitleFragment extends BaseFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String QuestionFragment = "questionfragment";
    // TODO: Rename and change types of parameters
    private String mFragment;
	private ProgressView mProgessbar;
	private TextView tvMoney;


    public void setProgessbar(int progress)
    {
    	mProgessbar.setProgress(progress);
    }

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     * 
     * @param param1
     *            Parameter 1.
     * @param param2
     *            Parameter 2.
     * @return A new instance of fragment QuestionTitleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionTitleFragment newInstance(
            String param1, String param2)
    {
        QuestionTitleFragment fragment = new QuestionTitleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(QuestionFragment, param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    public QuestionTitleFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mFragment = getArguments().getString(QuestionFragment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_title,container, false);
        mProgessbar = new ProgressView(view);
        tvMoney = (TextView) view.findViewById(R.id.tv_money);
        CoinManager.getInstance().setTv(tvMoney);
        return view;
    }

}
