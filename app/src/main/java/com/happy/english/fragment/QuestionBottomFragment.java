package com.happy.english.fragment;

import com.happy.english.R;
import com.happy.english.R.layout;
import com.happy.english.support.utils.UIUtils;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link QuestionBottomFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link QuestionBottomFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class QuestionBottomFragment extends Fragment implements OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Button mPrompt;
    private Button mSubmit;
    private Button mShare;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     * 
     * @param param1
     *            Parameter 1.
     * @param param2
     *            Parameter 2.
     * @return A new instance of fragment QuestionBottomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionBottomFragment newInstance(
            String param1, String param2)
    {
        QuestionBottomFragment fragment = new QuestionBottomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionBottomFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_question_bottom,
                container, false);
        mPrompt = (Button) view
                .findViewById(R.id.btn_prompt);
        mSubmit = (Button) view
                .findViewById(R.id.btn_submit);
        mShare = (Button) view.findViewById(R.id.btn_share);
     
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    // public void onButtonPressed(Uri uri)
    // {
    // if (mListener != null)
    // {
    // mListener.onFragmentInteraction(uri);
    // }
    // }
    //
    // @Override
    // public void onAttach(Activity activity)
    // {
    // super.onAttach(activity);
    // try
    // {
    // mListener = (OnFragmentInteractionListener) activity;
    // }
    // catch (ClassCastException e)
    // {
    // throw new ClassCastException(
    // activity.toString()
    // + " must implement OnFragmentInteractionListener");
    // }
    // }
    //
    // @Override
    // public void onDetach()
    // {
    // super.onDetach();
    // mListener = null;
    // }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public Button getmPrompt()
    {
        return mPrompt;
    }

    public void setmPrompt(Button mPrompt)
    {
        this.mPrompt = mPrompt;
    }

    public Button getmSubmit()
    {
        return mSubmit;
    }

    public void setmSubmit(Button mSubmit)
    {
        this.mSubmit = mSubmit;
    }

    public Button getmShare()
    {
        return mShare;
    }

    public void setmShare(Button mShare)
    {
        this.mShare = mShare;
    }

    @Override
    public void onClick(View v)
    {
//        switch (v.getId())
//        {
//            case R.id.btn_prompt:
//                UIUtils.showShort(getActivity(), "this is prompt");
//                break;
//            case R.id.btn_submit:
//                UIUtils.showShort(getActivity(), "this is submit");
//                break;
//            case R.id.btn_share:
//                UIUtils.showShort(getActivity(), "this is share");
//                break;
//            default:
//                break;
//        }
    }
}
