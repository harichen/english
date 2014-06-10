package com.happy.english.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.happy.english.R;
import com.happy.english.bean.Mission;
import com.happy.english.bean.MissionReader;
import com.happy.english.bean.Question;
import com.happy.english.constant.Const;
import com.happy.english.fragment.BaseFragment;
import com.happy.english.fragment.BaseFragment.DoneListener;
import com.happy.english.fragment.QuestionBottomFragment;
import com.happy.english.fragment.TranslateQuestionFragment;
import com.happy.english.fragment.SelectPictureQuestionFragment;
import com.happy.english.fragment.ReadPictureQuestionFragment;
import com.happy.english.fragment.QuestionTitleFragment;
import com.happy.english.fragment.ListenQuestionFragment;
import com.happy.english.fragment.MovePictureQuestionFragment;
import com.happy.english.manager.CoinManager;
import com.happy.english.support.utils.ImageUtils;
import com.happy.english.support.utils.SoundUtil;
import com.happy.english.support.utils.UIUtils;
import com.happy.english.ui.Global;
import com.happy.english.widget.AnswerBanner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 试题页
 * 
 * @author lc
 * 
 */
/**
 * @author Administrator
 * 
 */
public class ExamActivity extends BaseFragmentActivity
        implements
        OnClickListener, DoneListener
{
    private static final int MISSIONREADER_SUCCESS = 0;
    private static final int MISSIONREADER_FAILD = 1;
    private String missionIndex; // 关卡数字
    private BaseFragment mCurrentfragment;
    private int questionIndex = 0;// 当前第几题
    private String answer;
    private int[] answerRate = new int[6]; // 记录六题的对错
    private boolean[] isHelp = new boolean[6];// 记录六题是否得到过帮助
    // private QuestionAdapter questionAdapter;
    private Button mBacktomap;
    private Question[] questions;
    private Button mPrompt;
    private Button mSubmit;
    private Button mShare;
    private Bundle bundle;
    private AnswerBanner mBanner;
    private boolean isDone;
    private static final String IMAGE_NAME = "question.jpg";
    public static String Question_IMAGE = Const.SHARE_IMAGE_FILE
            + IMAGE_NAME;
    private Bitmap screenshot;

    public Question[] getQuestions()
    {
        return questions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        bundle = new Bundle();
        mSoundUtil = new SoundUtil(this);
        initView();
        missionIndex = getIntent().getStringExtra(
                Const.MISSIONINDEX);
        getDate(missionIndex);
    }

    /**
     * 读取关卡数据
     * 
     * @param missionIndex
     */
    private void getDate(final String missionIndex)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Mission mission = MissionReader
                        .parseMission(Const.MISSION_FILE
                                + missionIndex
                                + File.separator
                                + Const.EAXMTXT);
                if (mission != null)
                {
                    Message msg = handler.obtainMessage();
                    msg.obj = mission;
                    msg.what = MISSIONREADER_SUCCESS;
                    handler.sendMessage(msg);
                }
                else
                {
                    handler.sendEmptyMessage(MISSIONREADER_FAILD);
                }
            }
        }).start();
    }

    private Mission mission;
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case MISSIONREADER_FAILD:
                    UIUtils.showToast(ExamActivity.this,
                            "MISSIONREADER_FAILD");
                    break;
                case MISSIONREADER_SUCCESS:
                    mission = (Mission) msg.obj;
                    inflateExam(mission);
                    // 开始第一题
                    showFragment(questionIndex);
                    break;
            }
        }
    };
    private SoundUtil mSoundUtil;

    protected void onDestroy()
    {
        super.onDestroy();
        if (mSoundUtil != null)
        {
            mSoundUtil.clean();
        }
    }

    /**
     * 填充试卷
     * 
     * @param mission
     */
    private void inflateExam(Mission mission)
    {
        questions = mission.getSubject();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            showquiteDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_back_to_map:
                showquiteDialog();
                break;
            case R.id.btn_prompt:
                if (CoinManager.getInstance().getMoney() >= (-CoinManager.HELP_CONSUME))
                {
                    if (isHelp[questionIndex])
                    {
                        UIUtils.showToast(this,
                                "一题只能用一次帮助哦！");
                    }
                    else
                    {
                        showHelpDialog();
                    }
                }
                else
                {
                    UIUtils.showToast(this, "不够金币咯~一次需要"
                            + (-CoinManager.HELP_CONSUME)
                            + "金币");
                }
                break;
            case R.id.btn_submit:
                if (!mBanner.isShowing())
                {
                    isDone = true;
                    int rate = checkAnswer();
                    answerRate[questionIndex] = rate;
                    mBanner.show(rate,
                            questions[questionIndex]
                                    .getSolution());
                    mSubmit.setText(getString(R.string.btn_submit_continue));
                    mSubmit.setSelected(true);
                    if (rate == Const.RIGHT)
                    {
                        mSoundUtil
                                .playSound(R.raw.right_answer);
                    }
                    else
                    {
                        mSoundUtil
                                .playSound(R.raw.wrong_answer);
                    }
                    findViewById(R.id.question_framelayout)
                            .setEnabled(false);
                    updateTitlebar(questionIndex);
                }
                else
                {
                    isDone = false;
                    questionIndex++;
                    mBanner.dismiss();
                    findViewById(R.id.question_framelayout)
                            .setEnabled(true);
                    showFragment(questionIndex);
                }
                break;
            case R.id.btn_share:
                if (questionIndex == 2)
                {
                    // 此处分享一个url
                }
                else
                {
                    screenshot = getSceenShot(mCurrentfragment
                            .getView());
                    if (screenshot != null)
                    {
                        new Thread()
                        {
                            public void run()
                            {
                                ImageUtils
                                        .createShareImage(
                                                Question_IMAGE,
                                                screenshot);
                            }
                        }.start();
                    }
                    if (screenshot != null)
                    {
                        Global.showShare(
                                this, false, null,
                                Question_IMAGE);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 退出会议
     */
    protected void showquiteDialog()
    {
        Dialog dialog = new Dialog(this,
                R.style.Theme_dialog);
        View v = getQuiteD(dialog);
        dialog.setContentView(v);
        dialog.getWindow().setWindowAnimations(
                R.style.anim_dialog);
        dialog.show();
    }

    /**
	 */
    private View getQuiteD(final Dialog dialog)
    {
        View dialogview = LayoutInflater.from(this)
                .inflate(
                        R.layout.newv_dialog_finish, null);
        OnClickListener listener = new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_not:
                        break;
                    case R.id.btn_ok:
                        finish();
                        break;
                }
                dialog.dismiss();
            }
        };
        dialogview.findViewById(R.id.btn_ok)
                .setOnClickListener(listener);
        dialogview.findViewById(R.id.btn_not)
                .setOnClickListener(listener);
        return dialogview;
    }

    /**
	 *  
	 */
    protected void showHelpDialog()
    {
        Dialog dialog = new Dialog(this,
                R.style.Theme_dialog);
        View v = getDialog(dialog);
        dialog.setContentView(v);
        dialog.getWindow().setWindowAnimations(
                R.style.anim_dialog);
        dialog.show();
    }

    /**
	 */
    private View getDialog(final Dialog dialog)
    {
        View dialogview = LayoutInflater.from(this)
                .inflate(
                        R.layout.newv_dialog_quite, null);
        OnClickListener listener = new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_not:
                        break;
                    case R.id.btn_ok:
                        // 消耗金币
                        updateMoney(
                                CoinManager.HELP_CONSUME,
                                CoinManager.TYPE_HELP);
                        CoinManager
                                .getInstance()
                                .earnMoney(
                                        CoinManager.HELP_CONSUME);
                        isHelp[questionIndex] = true;
                        mCurrentfragment
                                .help(questions[questionIndex]
                                        .getAnswers());
                        break;
                }
                dialog.dismiss();
            }
        };
        TextView tvTip = (TextView) dialogview
                .findViewById(R.id.tv_tip);
        tvTip.setText("确定要花" + (-CoinManager.HELP_CONSUME)
                + "个金币去掉一个错误选项吗？");
        dialogview.findViewById(R.id.btn_ok)
                .setOnClickListener(listener);
        dialogview.findViewById(R.id.btn_not)
                .setOnClickListener(listener);
        return dialogview;
    }

    private int checkAnswer()
    {
        if (answer.toLowerCase().equals(
                questions[questionIndex].getAnswers()
                        .toLowerCase()))
        {
            return Const.RIGHT;
        }
        else
        {
            return Const.WRONG;
        }
    }

    protected void initView()
    {
        mBanner = new AnswerBanner(this);
        mBacktomap = (Button) findViewById(R.id.btn_back_to_map);
        mPrompt = (Button) findViewById(R.id.btn_prompt);
        mSubmit = (Button) findViewById(R.id.btn_submit);
        mShare = (Button) findViewById(R.id.btn_share);
        mBacktomap.setOnClickListener(this);
        mPrompt.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mShare.setOnClickListener(this);
    }

    public void showFragment(int index)
    {
        mSubmit.setEnabled(false);
        mSubmit.setSelected(false);
        mSubmit.setText(getString(R.string.btn_submit_check));
        bundle.putInt(Const.MISSIONINDEX,
                Integer.parseInt(missionIndex));
        switch (index)
        {
            case 0:
                // 加载第一题
                bundle.putSerializable(
                        Const.QUESTION_SELECT_PICTURE,
                        questions[0]);
                SelectPictureQuestionFragment questionOneFragment = new SelectPictureQuestionFragment();
                questionOneFragment.setArguments(bundle);
                questionOneFragment.setmDoneListener(this);
                mCurrentfragment = questionOneFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.push_right_in,
                                R.anim.push_left_out);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.question_framelayout,
                                questionOneFragment)
                        .commitAllowingStateLoss();
                break;
            case 1:
                bundle.putSerializable(
                        Const.QUESTION_MOVE_PICTURE,
                        questions[1]);
                MovePictureQuestionFragment questionTwoFragment = new MovePictureQuestionFragment();
                questionTwoFragment.setArguments(bundle);
                questionTwoFragment.setmDoneListener(this);
                mCurrentfragment = questionTwoFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.push_right_in,
                                R.anim.push_left_out);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.question_framelayout,
                                questionTwoFragment)
                        .commitAllowingStateLoss();
                break;
            case 2:
                bundle.putSerializable(
                        Const.QUESTION_LISTEN, questions[2]);
                ListenQuestionFragment questionThreeFragment = new ListenQuestionFragment();
                questionThreeFragment.setArguments(bundle);
                questionThreeFragment
                        .setmDoneListener(this);
                mCurrentfragment = questionThreeFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.push_right_in,
                                R.anim.push_left_out);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.question_framelayout,
                                questionThreeFragment)
                        .commitAllowingStateLoss();
                break;
            case 3:
                bundle.putSerializable(
                        Const.QUESTION_TRANSLATE,
                        questions[3]);
                TranslateQuestionFragment questionFourFragment = new TranslateQuestionFragment();
                questionFourFragment.setArguments(bundle);
                questionFourFragment.setmDoneListener(this);
                mCurrentfragment = questionFourFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.push_right_in,
                                R.anim.push_left_out);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.question_framelayout,
                                questionFourFragment)
                        .commitAllowingStateLoss();
                break;
            case 4:
                bundle.putSerializable(
                        Const.QUESTION_TRANSLATE,
                        questions[4]);
                bundle.putBoolean("isO", true);
                TranslateQuestionFragment questionFiveFragment = new TranslateQuestionFragment();
                questionFiveFragment.setArguments(bundle);
                questionFiveFragment.setmDoneListener(this);
                mCurrentfragment = questionFiveFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.push_right_in,
                                R.anim.push_left_out);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.question_framelayout,
                                questionFiveFragment)
                        .commitAllowingStateLoss();
                break;
            case 5:
                bundle.putSerializable(
                        Const.QUESTION_READ_PICTURE,
                        questions[5]);
                ReadPictureQuestionFragment questionSixFragment = new ReadPictureQuestionFragment();
                questionSixFragment.setArguments(bundle);
                questionSixFragment.setmDoneListener(this);
                mCurrentfragment = questionSixFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.push_right_in,
                                R.anim.push_left_out);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.question_framelayout,
                                questionSixFragment)
                        .commitAllowingStateLoss();
                break;
            case 6:
                updateTitlebar(index);
                Intent intent = new Intent(this,
                        ReultActivity.class);
                intent.putExtra(Const.ANSWERRATE,
                        answerRate);
                intent.putExtra(Const.MISSIONINDEX,
                        Integer.parseInt(missionIndex));
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void updateTitlebar(int position)
    {
        QuestionTitleFragment mFragment = (QuestionTitleFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_ques_title);
        mFragment.setProgessbar(position);
    }

    // 每道题答完,能够提交时的回调
    @Override
    public void done(int questionindex, String answer)
    {
        mSubmit.setEnabled(true);
        this.answer = answer;
    }

    @Override
    public void undo()
    {
        if (!isDone)
        {
            mSubmit.setEnabled(false);
            mSubmit.setText(getString(R.string.btn_submit_check));
            mSubmit.setSelected(false);
        }
    }
}
