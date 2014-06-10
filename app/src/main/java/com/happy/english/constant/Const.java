package com.happy.english.constant;

import com.happy.english.support.file.FileManager;

import android.os.Environment;

public class Const {

	public static final int WRONG = 0;
	public static final int RIGHT = 1;

	public static final String TAG = "lcds";
	public static final String TAG_ACTION = "作答";
	public static final String MISSIONINDEX = "MISSIONINDEX";
	public static final String EAXMTXT = "exam.txt";
	public static final String RESOURCE = "resource";
	public static final String QUESTION_SELECT_PICTURE = "question_select_picture";
	public static final String QUESTION_MOVE_PICTURE = "question_move_picture";
	public static final String QUESTION_THREE = "question_three";
	public static final String QUESTION_LISTEN = "question_listen";
	public static final String QUESTION_TRANSLATE = "question_translate";
	public static final String QUESTION_SIX = "question_six";
	public static final String QUESTION_READ_PICTURE = "question_read_picture";
	public static final String QUESTION_EIGHT = "question_eight";
	public static final String ANSWER_TEXT = "answer_text";
	public static final String QUESTION_ID = "question_id";
	public static final String ANSWER_FRAGMENT = "answer_fragment";
	public static final String ANSWERRATE = "ANSWERRATE";
	
	
	public static final String ACTION_DONE = "action_done";
	// 存储题目目录
	public static final String MISSION_FILE = FileManager.getMissionFilePath();
    public static final String SHARE_IMAGE_FILE = FileManager.getShareImagePath();
    
	public static final String[] ANSWERKEY = { "A", "B", "C", "D", "E", "F" };
	
	public static final String[] DESCPTION = { "Picture", "Match", "Music", "Translation", "Oral", "Culture" };
	// 题目总数
	public static int TOTAL_QUESTIONNUM = 6;

	/**根据关数，得到下载地址(暂时先本地拿地址)
	 * @param missionIndex
	 * @return
	 */
	public static String getDownloadUrl(int missionIndex) {

		return "http://121.192.190.128:8080/HappyEnglish/static/data/world_1/"
				+ missionIndex + ".zip";
	}
}
