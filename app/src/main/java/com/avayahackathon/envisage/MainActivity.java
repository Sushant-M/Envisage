package com.avayahackathon.envisage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.avayahackathon.envisage.Pojos.BasicUserInfo;
import com.avayahackathon.envisage.Pojos.RepositoryInformation;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> local_arr = new ArrayList<String>();
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<RepositoryInformation> repo_array= new ArrayList<RepositoryInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> repo_list = new ArrayList<String>();
            RepositoryService service = new RepositoryService();
            try {
                for (Repository repo : service.getRepositories(params[0])) {
                    repo_list.add(repo.getName());
                    RepositoryInformation temp_info = new
                            RepositoryInformation(repo.getName(),
                            repo.getDescription(),
                            repo.getHomepage(),
                            repo.getLanguage());
                    repo_array.add(temp_info);
                    //System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
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
