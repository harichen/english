package com.happy.english.bean;

import java.io.Serializable;

public class Options implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String getOptionCode() {
		return optionCode;
	}
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	String optionCode ;
	String option ;
	String res ;
 }