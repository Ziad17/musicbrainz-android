package org.metabrainz.mobile.presentation.features.collection;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.MusicBrainzActivity;
import org.metabrainz.mobile.data.CollectionUtils;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.presentation.features.UserPreferences;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends MusicBrainzActivity {

    private static CollectionViewModel viewModel;
    private RecyclerView recyclerView;
    private CollectionListAdapter adapter;
    private List<Collection> collections;

    private TextView noRes;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
        collections = new ArrayList<>();

        noRes = findViewById(R.id.no_result);
        noRes.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_spinner);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        adapter = new CollectionListAdapter(collections);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        viewModel.getCollectionData().observe(this, data -> {
            CollectionUtils.removeCollections(data);
            collections.clear();
            collections.addAll(data);
            adapter.notifyDataSetChanged();

            checkHasResults();
        });

        fetchCollections();
    }

    private void fetchCollections() {
        progressBar.setVisibility(View.VISIBLE);
        boolean getPrivateCollections =
                LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN
                        && UserPreferences.getPrivateCollectionsPreference();
        getPrivateCollections = true;
        viewModel.fetchCollections(LoginSharedPreferences.getUsername(), getPrivateCollections);
    }

    private void checkHasResults() {
        progressBar.setVisibility(View.GONE);
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            noRes.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noRes.setVisibility(View.GONE);
        }
    }
}