package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.R;
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

        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mContentView.setText(mValues.get(position).getContent());
        holder.mLocation.setText(mValues.get(position).getLocation());
        holder.mLikes.setText(mValues.get(position).getLikes());
        holder.mDislikes.setText(mValues.get(position).getDislikes());

        String[] dateTime = mValues.get(position).getTime().split(" ");

        holder.mDate.setText(dateTime[0]);
        holder.mTime.setText(dateTime[1]);

        holder.mCommentCount.setText(mValues.get(position).getCommentCount());
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        public final TextView mContentView;
        public final TextView mUsername;
        public final TextView mLikes;
        public final TextView mDislikes;
        public boolean isLiked;
        public boolean isDisliked;
        public final TextView mLocation;
        public final TextView mTime;
        public final TextView mCommentCount;
        public final TextView mDate;
        public DiscoveryPost mItem;
        public final ImageButton likeBtn;
        public final ImageButton dislikeBtn;
        public final ImageButton commentBtn;
        public final ImageView pfp;

        public ViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mLikes = binding.likeCount;
            mDislikes = binding.dislikeCount;
            mUsername = binding.username;
            mTime = binding.time;
            mLocation = binding.location;
            mCommentCount = binding.comments;
            mDate = binding.date;

            likeBtn = binding.likeButton;
            dislikeBtn = binding.dislikeButton;
            commentBtn = binding.commentsButton;
            pfp = binding.pfp;

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLikeButtonClick(v);
                }
            });

            dislikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDislikeButtonClick(v);
                }
            });

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openComments(v);
                }
            });

            pfp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfile(v);
                }
            });

            mUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfile(v);
                }
            });

        }

        public void onLikeButtonClick(View view) {
            Log.i(TAG, "Like Button Clicked");

            this.isLiked = !isLiked;

            if(isLiked && isDisliked){
                isDisliked = false;
                updateDislike();
            }


            updateLike();
        }

        public void onDislikeButtonClick(View view) {
            Log.i(TAG, "Dislike Button Clicked");

            this.isDisliked = !isDisliked;

            if(isLiked && isDisliked){
                isLiked = false;
                updateLike();
            }


            updateDislike();
        }

        public void updateLike(){
            if(isLiked){
                likeBtn.setBackgroundResource(R.drawable.colour_like);
            }else{
                likeBtn.setBackgroundResource(R.drawable.like);
            }
        }

        public void updateDislike(){
            if (isDisliked) {
                dislikeBtn.setBackgroundResource(R.drawable.colour_dislike);
            }else{
                dislikeBtn.setBackgroundResource(R.drawable.dislike);
            }
        }

        public void openComments(View view) {
            Log.i(TAG, "Opening Comments");
        }

        public void openProfile(View view) {
            Log.i(TAG, "Requesting to view profile");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}