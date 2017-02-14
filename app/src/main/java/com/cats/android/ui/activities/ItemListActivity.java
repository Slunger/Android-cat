package com.cats.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.cats.android.R;
import com.cats.android.data.CatContent;
import com.cats.android.model.Cat;
import com.cats.android.service.MyIntentService;
import com.cats.android.ui.fragments.ItemDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatAlertDialog.openCreateCatDialog(view.getContext(), new ResultReceiver(new Handler()) {
                    @Override
                    public void onReceiveResult(int resultCode, Bundle resultData) {
                        if (resultCode == RESULT_OK) {
                            Toast.makeText(getApplicationContext(), resultData.getString("response"), Toast.LENGTH_LONG).show();
                            updateRecyclerView();
                        }
                    }
                });
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        assert recyclerView != null;

        testDataCat();

        if (CatContent.getITEMS() == null) {
            updateRecyclerView();
        } else {
            setRecyclerView();
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.reload) {
            updateRecyclerView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateRecyclerView() {
        MyIntentService.getAll(this, new ResultReceiver(new Handler()) {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    List<Cat> cats = CatContent.getITEMS();
                    if (cats != null) {
                        setRecyclerView();
                    } else {
                        Toast.makeText(getApplicationContext(), resultData.getString("response"), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void testDataCat() {
        List<Cat> cats = new ArrayList<>();
        int age = 1, weight = 1;
        int id = 1;
        Cat cat = new Cat(id++, "Kitty", age++, "white", "Cyprus", weight++);
        cats.add(cat);
        cat = new Cat(id++, "Byte", age++, "lavender", "Sphynx", weight++);
        cats.add(cat);
        cat = new Cat(id++, "Yoda", age, "chocolate", "European Shorthair", weight);
        cats.add(cat);
        cat = new Cat(id++, "Picasso", age++, "tortoiseshell", "Siamese", weight++);
        cats.add(cat);
        cat = new Cat(id++, "Lancelot", age++, "black", "Exotic Shorthair", weight++);
        cats.add(cat);
        cat = new Cat(id++, "eEwok", age, "chocolate", "Asian", weight);
        cats.add(cat);
        CatContent.putItems(cats);
    }

    private void setRecyclerView() {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter());
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Cat> catList;

        public SimpleItemRecyclerViewAdapter() {
            catList = CatContent.getITEMS();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.cat = catList.get(position);
            holder.mIdView.setText(catList.get(position).getId().toString());
            holder.mContentView.setText(catList.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, holder.cat.getId());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.cat.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return catList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Cat cat;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
