package com.avayahackathon.envisage.Pojos;

import java.io.Serializable;

/**
 * Created by sushant on 6/5/17.
 */

public class RepositoryInformation implements Serializable{
    String repo_name;
    String description;
    String homepage;
    String language;
    public RepositoryInformation(String repname, String decs, String hpage, String lang){
        repo_name= repname;
        description= decs;
        homepage= hpage;
        language= lang;
    }
}
