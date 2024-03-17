package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.databinding.PostItemBinding;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPost;
import ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts.placeholder.PlaceholderContent;

import java.util.List;


/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderContent.PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {

    private final List<DiscoveryPost> mValues;

    public PostsRecyclerViewAdapter(List<DiscoveryPost> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).getId());
        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mContentView.setText(mValues.get(position).getContent());
        holder.mLocation.setText(mValues.get(position).getLocation());
        holder.mLikes.setText(mValues.get(position).getLikes());
        holder.mDislikes.setText(mValues.get(position).getDislikes());
        holder.mTime.setText(mValues.get(position).getTime());

//        holder.mContentView.setText(mValues.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mUsername;
        public final TextView mLikes;
        public final TextView mDislikes;
        public boolean mIsLiked;
        public boolean mIsDisliked;
        public final TextView mLocation;
        public final TextView mTime;
        public DiscoveryPost mItem;

        public ViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mLikes = binding.likeCount;
            mDislikes = binding.dislikeCount;
            mUsername = binding.username;
            mTime = binding.time;
            mLocation = binding.location;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}