package com.happy.english.bean;

import java.io.Serializable;

public class Stem implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String getDescption()
    {
        return descption;
    }

    public void setDescption(String descption)
    {
        this.descption = descption;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getRes()
    {
        return res;
    }

    public void setRes(String res)
    {
        this.res = res;
    }

    String descption;
    String text;
    String res;
}