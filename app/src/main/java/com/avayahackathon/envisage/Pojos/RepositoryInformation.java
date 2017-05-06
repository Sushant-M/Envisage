package com.avayahackathon.envisage.Pojos;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushant on 6/5/17.
 */

public class RepositoryInformation implements Serializable{
    String repo_name;
    String description;
    String homepage;
    String language;
    long ID;
    Repository repository;

    public RepositoryInformation(String repname, String decs, String hpage, String lang, long id){
        repo_name= repname;
        description= decs;
        homepage= hpage;
        language= lang;
        ID = id;

    }

    public RepositoryInformation(Repository repo){
        repository = repo;
    }

    public Repository getRepoObject(){return repository;}

    public String getRepoName(){
        return repo_name;
    }

    public String getDescription(){
        return description;
    }

    public long getID(){
        return ID;
    }


    public String getHomepage(){
        return homepage;
    }

    public String getLanguage(){
        return language;
    }
}
