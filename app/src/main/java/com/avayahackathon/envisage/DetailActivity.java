package com.avayahackathon.envisage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avayahackathon.envisage.Pojos.RepositoryInformation;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.CommitService;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    Repository global_repository_object;
    CommitService commitService;
    ListView our_list;
    ArrayAdapter<String> adapter;
    ArrayList<String> commit_array = new ArrayList<String>();
    ArrayList<String> to_apply;

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


        our_list = (ListView)findViewById(R.id.commit_list);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.arrayelement,commit_array);
        our_list.setAdapter(adapter);

        Intent intent = getIntent();
        RepositoryInformation local_repo = (RepositoryInformation)intent.getSerializableExtra("details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name.setText(local_repo.getRepoObject().getName());
        description.setText(local_repo.getRepoObject().getDescription());
        language.setText(local_repo.getRepoObject().getLanguage());

        global_repository_object = local_repo.getRepoObject();

        commitService = new CommitService();
        //commit_list = commitService.getCommits();
        new getCommitInfo().execute();

        our_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                String nametosend= to_apply.get(position);
                intent.putExtra("name",nametosend);
                startActivity(intent);
            }
        });
    }


    public class getCommitInfo extends AsyncTask<String,Void,List<RepositoryCommit>>{
        @Override
        protected List<RepositoryCommit> doInBackground(String... params) {
            try {
                List<RepositoryCommit> temporary = commitService.getCommits(global_repository_object);
                return temporary;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RepositoryCommit> commits) {
            super.onPostExecute(commits);
            if(commits != null){

                adapter.clear();
                to_apply = new ArrayList<String>();
                for(RepositoryCommit comm : commits){
                    String msg = comm.getCommit().getMessage();
                    to_apply.add(msg);
                }
                adapter.addAll(to_apply);
            }
        }
    }

}
