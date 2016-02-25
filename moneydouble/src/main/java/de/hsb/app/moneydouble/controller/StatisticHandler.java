package de.hsb.app.moneydouble.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class StatisticHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init(){
		
	}
	
}
