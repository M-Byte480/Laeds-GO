package ie.thirdfloor.csis.ul.laedsgo.ui.laedDeck;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLaedRecyclerViewAdapter extends RecyclerView.Adapter<MyLaedRecyclerViewAdapter.LaedViewHolder> {
    private List<IDocument> mValues;
    private static final String TAG = "MyLaedRecyclerViewAdapter";

    public MyLaedRecyclerViewAdapter(List<IDocument> items) {
        mValues = items;
    }

    @Override
    public LaedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_laed_deck_v2_row, parent, false);
        return new LaedViewHolder(view);

    }

    public void onBindViewHolder(final LaedViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        LeadsDeckDocument lad = (LeadsDeckDocument) mValues.get(position);
        holder.name.setText(lad.name);
        holder.description.setText(lad.description);

        byte[] decodedString = Base64.decode(lad.picture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.picture.setImageBitmap(decodedByte);


        holder.laedRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaedDeckStatsFragment.laedID = lad.leadId;


                NavHostFragment navHostFragment = (NavHostFragment) ((AppCompatActivity) v.getContext()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                navHostFragment.getNavController().navigate(R.id.statsFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setLaeds(List<IDocument> list) {
        this.mValues = list;
    }


    public class LaedViewHolder extends RecyclerView.ViewHolder {
        public final ImageView picture;
        public final TextView name;
        public final TextView description;
        public final GridLayout laedRow;

        public IDocument mItem;

        public LaedViewHolder(View laedView) {
            super(laedView);
            name = laedView.findViewById(R.id.laedName);
            description = laedView.findViewById(R.id.laedDescription);
            picture = laedView.findViewById(R.id.laedpfp);
            laedRow = laedView.findViewById(R.id.laedRow);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}