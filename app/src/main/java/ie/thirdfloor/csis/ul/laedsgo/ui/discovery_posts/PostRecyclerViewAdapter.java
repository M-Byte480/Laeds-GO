package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DiscoveryPostModel}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyPostViewHolder> {

    Context context;
    private List<DiscoveryPostModel> postModels;

    private ProfileCollection profileCollection = new ProfileCollection();

    private int USER_ID;
    private MutableLiveData<IDocument> loggedInUser = new MutableLiveData<>();

    public PostRecyclerViewAdapter(Context contenxt, List<DiscoveryPostModel> items) {
        this.postModels = items;
        this.context = contenxt;
        this.USER_ID = profileCollection.getUserId();
        profileCollection.get(USER_ID, loggedInUser);
    }

    public void clearArray(){
        this.postModels = new ArrayList<>();
    }

    public void setArray(ArrayList<DiscoveryPostModel> array){
        this.postModels = array;
    }
    public static final String TAG = "PostRecyclerViewAdapter";

    @Override
    public MyPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate view: parse the XML and apply the binding.
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.fragment_post_row, parent, false);
//        return new ViewHolder(ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyPostViewHolder holder, int position) {
        // Function responsible for assigning values to each
        // view as they are coming onto the screen based on their index
        // IN THE RECYCLE VIEW

        // Model from the array
        DiscoveryPostModel model = postModels.get(position);

        // Simple binding
        holder.tvName.setText(model.getUsername());
        holder.tvLocation.setText(model.getLocation());
        holder.tvLikeCount.setText(String.valueOf(model.getLikes()));
        holder.tvDislikeCount.setText(String.valueOf(model.getDislikes()));
        holder.tvContent.setText(model.getContent());

        String[] dateTime = model.getTime().split(" ");


        holder.tvTime.setText(dateTime[0]);
        holder.tvDate.setText(dateTime[1]);

        // Button Setups
        if(model.isLiked()){
            holder.ibLike.setBackgroundResource(R.drawable.colour_like);
        }else{
            holder.ibLike.setBackgroundResource(R.drawable.like);
        }


        if(model.isDisliked()){
            holder.ibDislike.setBackgroundResource(R.drawable.colour_dislike);
        }else{
            holder.ibDislike.setBackgroundResource(R.drawable.dislike);
        }

        holder.ibComment.setBackgroundResource(R.drawable.chat);

        // button actions setup

        // Like
        holder.ibLike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "onClick: " + model.getId());
                model.setLiked(!model.isLiked());
                Log.i(TAG, "onClick: " + model.isLiked());

                if(model.checkIfPostIsLikedAndDisliked()){
                    model.setLiked();
                }

                if(model.isLiked()){
                    holder.ibLike.setBackgroundResource(R.drawable.colour_like);
                }else{
                    holder.ibLike.setBackgroundResource(R.drawable.like);
                }
                if(model.isDisliked()){
                    holder.ibDislike.setBackgroundResource(R.drawable.colour_dislike);
                }else{
                    holder.ibDislike.setBackgroundResource(R.drawable.dislike);
                }

                ProfileDocument accountState = (ProfileDocument) loggedInUser.getValue();
                if(accountState != null){
                    accountState.likedPosts.add(model.getId());
                    profileCollection.update(loggedInUser);
                }
            }
        });

        // Dislike
        holder.ibDislike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "onClick: " + model.getId());
                model.setDisliked(!model.isDisliked());
                Log.i(TAG, "onClick: " + model.isDisliked());

                if(model.checkIfPostIsLikedAndDisliked()){
                    model.setDisliked();
                }

                if(model.isDisliked()){
                    holder.ibDislike.setBackgroundResource(R.drawable.colour_dislike);
                }else{
                    holder.ibDislike.setBackgroundResource(R.drawable.dislike);
                }
                if(model.isLiked()){
                    holder.ibLike.setBackgroundResource(R.drawable.colour_like);
                }else{
                    holder.ibLike.setBackgroundResource(R.drawable.like);
                }
            }
        });

        // Comment
        holder.ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Comments");
            }
        });
    }

    @Override
    public int getItemCount() {
        // Recycler view needs to know how many posts we got
        // Kinda acts like a 2 parallel arrays
        return postModels.size();
    }

    public static class MyPostViewHolder extends RecyclerView.ViewHolder {
        // Grab the views from the fragment_post_row.xml layout file
        // assign it to each field. Basically onCreate

        ImageView profilePicture;
        TextView tvContent, tvLocation, tvName, tvTime, tvDate, tvLikeCount, tvDislikeCount;

        ImageButton ibLike, ibDislike, ibComment;

        public MyPostViewHolder(View rowView) {
            super(rowView);

            // ImageView
            profilePicture = rowView.findViewById(R.id.pfp);

            // TextViews
            tvContent = rowView.findViewById(R.id.content);
            tvName = rowView.findViewById(R.id.username);
            tvLikeCount = rowView.findViewById(R.id.likeCount);
            tvDislikeCount = rowView.findViewById(R.id.dislikeCount);
            tvLocation = rowView.findViewById(R.id.location);
            tvTime = rowView.findViewById(R.id.time);
            tvDate = rowView.findViewById(R.id.date);

            // Buttons
            ibLike = rowView.findViewById(R.id.likeButton);
            ibDislike = rowView.findViewById(R.id.dislikeButton);
            ibComment = rowView.findViewById(R.id.commentsButton);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + "mContentView.getText()" + "'";
        }
    }
}