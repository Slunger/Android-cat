package com.cats.android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.cats.android.R;
import com.cats.android.util.WebManager;
import com.cats.android.ui.fragments.ItemDetailFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.cats.android.util.Constants.RESPONSE;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        itemId = getIntent().getIntExtra(ItemDetailFragment.ARG_ITEM_ID, 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatAlertDialog.openUpdateCatDialog(view.getContext(), new ResultReceiver(new Handler()) {
                    @Override
                    public void onReceiveResult(int resultCode, Bundle resultData) {
                        if (resultCode == RESULT_OK) {
                            Toast.makeText(getApplicationContext(), resultData.getString(RESPONSE), Toast.LENGTH_LONG).show();
                            recordChangeItem();
                        }
                    }
                }, itemId);
            }
        });


        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                WebManager.delete(view.getContext(), new ResultReceiver(new Handler()) {
                    @Override
                    public void onReceiveResult(int resultCode, Bundle resultData) {
                        if (resultCode == RESULT_OK) {
                            Toast.makeText(getApplicationContext(), resultData.getString(RESPONSE), Toast.LENGTH_LONG).show();
                            recordDeletedItem();
                            finish();
                        }
                    }
                }, itemId);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, itemId);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, itemId);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void recordDeletedItem() {
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, itemId);
        mFirebaseAnalytics.logEvent("deleted_item", bundle);
    }

    private void recordChangeItem() {
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, itemId);
        mFirebaseAnalytics.logEvent("changed_item", bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
