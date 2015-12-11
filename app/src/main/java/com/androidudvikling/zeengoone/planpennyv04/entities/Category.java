package com.androidudvikling.zeengoone.planpennyv04.entities;

import java.util.ArrayList;

public class Category {
	private String categoryTitle;
    private ArrayList<Plan> planList;

    public Category(String categoryTitle) {
    	planList = new ArrayList<Plan>(); 
        this.categoryTitle = categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle){
    	this.categoryTitle=categoryTitle;
    }

    public String getCategoryTitle() {
    	return categoryTitle;
    }

    public ArrayList<Plan> getPlanList(){
    	return planList;
    }

    public Plan getPlan(Date startDate, Date endDate) {
    	for (Plan temp : planList) {
    		if (temp.getStartDate().equals(startDate) && temp.getEndDate().equals(endDate))
    			return temp;
    	}
    	return null;
    }

    public void addPlan(Plan p) {
    	planList.add(p);
    }

	public Date getStartDatePlan(int categoryIndex){
		Date temp = new Date(2017,1,1);
		for (Plan p : getPlanList()) {
			if(p.getEndDate().before(temp)){
				temp = p.getEndDate();
			}
		}

		return temp;
	}

	public Date getEndDatePlan(int categoryIndex){
		Date temp = new Date(2012,1,1);
		for (Plan p : getPlanList()) {
			if(p.getEndDate().after(temp)){
				temp = p.getEndDate();
			}
		}

		return temp;
	}

    
    public ArrayList<Date> getContainingDays() {
    	ArrayList<Date> dateList = new ArrayList<Date>();
    	for (Plan p : planList) {
    		for (Date d : p.getContainingDates()){
    			if (!dateList.contains(d))
    				dateList.add(d);
    		}
    	}
    	return dateList;
    }

	public String toString(Category c){
		return c.getCategoryTitle();
	}
}