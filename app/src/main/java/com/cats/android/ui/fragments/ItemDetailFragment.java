package com.cats.android.ui.fragments;

import android.app.Activity;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cats.android.R;
import com.cats.android.repository.CatRepository;
import com.cats.android.model.Cat;
import com.cats.android.ui.activities.ItemDetailActivity;
import com.cats.android.ui.activities.ItemListActivity;
import com.cats.android.util.WebManager;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Cat cat;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            cat = CatRepository.getItemMap().get(getArguments().getInt(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(cat.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (cat != null) {
            WebManager.get(rootView.getContext(), new ResultReceiver(new Handler()) {
                @Override
                public void onReceiveResult(int resultCode, Bundle resultData) {
                    if (resultCode == RESULT_OK) {
                        ((TextView) rootView.findViewById(R.id.item_detail)).setText(CatRepository.getCAT().toString());
                    }
                }
            }, cat.getId());
        }

        return rootView;
    }
}
