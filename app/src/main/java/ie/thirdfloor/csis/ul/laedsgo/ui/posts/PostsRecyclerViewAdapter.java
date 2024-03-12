package ie.thirdfloor.csis.ul.laedsgo.ui.posts;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.databinding.PostItemBinding;
import ie.thirdfloor.csis.ul.laedsgo.ui.posts.placeholder.PlaceholderContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderContent.PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderContent.PlaceholderItem> mValues;

    public PostsRecyclerViewAdapter(List<PlaceholderContent.PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public PlaceholderContent.PlaceholderItem mItem;

        public ViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}