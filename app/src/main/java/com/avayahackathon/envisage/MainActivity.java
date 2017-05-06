package com.avayahackathon.envisage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.avayahackathon.envisage.Pojos.BasicUserInfo;
import com.avayahackathon.envisage.Pojos.RepositoryInformation;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GitHubClient client;
    ArrayList<String> local_arr = new ArrayList<String>();
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<RepositoryInformation> repo_array= new ArrayList<RepositoryInformation>();
    ArrayList<Repository> repository_array = new ArrayList<Repository>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = "1125a1f77a3d791ada97c67ca9ec844a84696652";
        client = new GitHubClient();
        client.setOAuth2Token(token);

        listView = (ListView)findViewById(R.id.repo_list);
        adapter = new ArrayAdapter<String>(this,
                R.layout.arrayelement,local_arr);
        listView.setAdapter(adapter);
        new FetchRepositoriesAsync().execute("Sushant-M");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepositoryInformation local_repo = repo_array.get(position);
                Intent launch_intent = new Intent(getApplicationContext(),DetailActivity.class);
                launch_intent.putExtra("details",local_repo);
                startActivity(launch_intent);
            }
        });


    }


    public class FetchRepositoriesAsync extends AsyncTask<String,Void,ArrayList<String>> {

        private String TAG = getClass().getSimpleName();
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> repo_list = new ArrayList<String>();
            RepositoryService service = new RepositoryService(client);
            try {
                for (Repository repo : service.getRepositories(params[0])) {
                    repo_list.add(repo.getName());
                   /* RepositoryInformation temp_info = new
                            RepositoryInformation(repo.getName(),
                            repo.getDescription(),
                            repo.getHomepage(),
                            repo.getLanguage(),
                            repo.getId());*/
                    repository_array.add(repo);


                    /*List<RepositoryCommit> comm_local;
                    CommitService commit_server = new CommitService(client);
                    IRepositoryIdProvider hell = repo;
                    comm_local = commit_server.getCommits(hell);
                    RepositoryCommit local = comm_local.get(0);
                    Log.d(TAG,local.toString());

                    Log.d(TAG,"Well something worked");*/
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return repo_list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            adapter.clear();
            adapter.addAll(strings);
        }
    }
}
