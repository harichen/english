package com.happy.english.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 关卡
 * 
 * @author lc
 * 
 */
public class Mission implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int level; // 关卡数
    private Question[] subject;// 题目
    private Tip tips;// 提示

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public Question[] getSubject()
    {
        return subject;
    }

    public void setSubject(Question[] subject)
    {
        this.subject = subject;
    }

    public Tip getTips()
    {
        return tips;
    }

    public void setTips(Tip tips)
    {
        this.tips = tips;
    }
}

class Tip
{
    String title;
    String comment;
}