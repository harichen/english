package com.happy.english.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 题型
 * 
 * @author lc
 * 
 */
public class Question implements Serializable 
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getNo()
    {
        return no;
    }

    public void setNo(int no)
    {
        this.no = no;
    }

    public String getAbility()
    {
        return ability;
    }

    public void setAbility(String ability)
    {
        this.ability = ability;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public Options[] getOptions()
    {
        return options;
    }

    public void setOptions(Options[] options)
    {
        this.options = options;
    }

    public Stem getStem()
    {
        return stem;
    }

    public void setStem(Stem stem)
    {
        this.stem = stem;
    }

    public String getAnswers()
    {
        return answers;
    }

    public void setAnswers(String answers)
    {
        this.answers = answers;
    }

    public String getSolution()
    {
        return solution;
    }

    public void setSolution(String solution)
    {
        this.solution = solution;
    }

    String id; // 题目id
    int no; // 第几题
    String ability; // 考察的能力项
    int type;// 题目类型
    Options[] options; // 选项
    Stem stem;// 题干
    String answers;
    String solution;
}
