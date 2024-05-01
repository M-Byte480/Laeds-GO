package ie.thirdfloor.csis.ul.laedsgo.ui.laedDeck;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckDocument;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentLaedDeckV2RowBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLaedRecyclerViewAdapter extends RecyclerView.Adapter<MyLaedRecyclerViewAdapter.LaedViewHolder> {
    private List<IDocument> mValues;

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
        LeadsDeckDocument lad = (LeadsDeckDocument) mValues.get(position) ;
        holder.mIdView.setText(lad.description);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setLaeds(List<IDocument> list ){
        this.mValues = list;
    }


    public class LaedViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public IDocument mItem;

        public LaedViewHolder(View laedView) {
            super(laedView);
            mIdView = laedView.findViewById(R.id.ladId);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}