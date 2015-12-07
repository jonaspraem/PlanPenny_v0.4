package com.androidudvikling.zeengoone.planpennyv04.entities;

import java.util.ArrayList;

public class ProjectDB {
    private ArrayList<Project> projectList;

    public ProjectDB() {
    	projectList = new ArrayList<Project>();
    }

    public Project getProject(String title) {
    	for (Project temp : projectList) {
    		if (temp.getTitle().equals(title))
    			return temp;
    	}
    	return null;
    }

    public void addProject(Project p) {
    	projectList.add(p);
    }

    public ArrayList<Project> getProjectList() {
    	return projectList;
    }
}