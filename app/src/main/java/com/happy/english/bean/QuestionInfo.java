package com.happy.english.bean;

import java.io.Serializable;

import android.os.Bundle;

public class QuestionInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final Class<?> clss;
    private final Bundle args;

    public QuestionInfo(Class<?> _class, Bundle _args)
    {
        clss = _class;
        args = _args;
    }

    public Class<?> getClss()
    {
        return clss;
    }

    public Bundle getArgs()
    {
        return args;
    }

}
