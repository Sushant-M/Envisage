package com.avayahackathon.envisage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avayahackathon.envisage.Pojos.RepositoryInformation;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.service.CommitService;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<String> commit_list = new ArrayList<String>();
        //The usual drama, not using butterknife for now.
        TextView name = (TextView)findViewById(R.id.name_repo);
        TextView description = (TextView)findViewById(R.id.description_repo);
        TextView language = (TextView)findViewById(R.id.repo_language);

        Intent intent = getIntent();
        RepositoryInformation local_repo = (RepositoryInformation)intent.getSerializableExtra("details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name.setText(local_repo.getRepoName());
        description.setText(local_repo.getDescription());
        language.setText(local_repo.getLanguage());

        CommitService commitService = new CommitService();
        //commit_list = commitService.getCommits();
        CommitComment commitComment = new CommitComment();

    }

}
