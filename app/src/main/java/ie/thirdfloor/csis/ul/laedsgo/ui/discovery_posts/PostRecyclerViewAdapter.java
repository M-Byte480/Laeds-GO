package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments.CommentConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments.CommentDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;
import ie.thirdfloor.csis.ul.laedsgo.ui.commentSection.CommentFragment;
import ie.thirdfloor.csis.ul.laedsgo.ui.commentSection.CommentModel;
import ie.thirdfloor.csis.ul.laedsgo.ui.commentSection.CommentReyclerViewAdapter;
import ie.thirdfloor.csis.ul.laedsgo.ui.view_profile.ViewProfileFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DiscoveryPostModel}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyPostViewHolder> {

    Context context;
    private List<DiscoveryPostModel> postModels;

    private ProfileCollection profileCollection = new ProfileCollection();

    private MutableLiveData<IDocument> loggedInUser = new MutableLiveData<>();
    private static TOLPostCollection postCollection = new TOLPostCollection();

    private static CommentConnection commentCollection = new CommentConnection();

    public PostRecyclerViewAdapter(Context contenxt, List<DiscoveryPostModel> items, MutableLiveData<IDocument> loggedInUser) {
        this.postModels = items;
        this.context = contenxt;
        this.loggedInUser = loggedInUser;

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

        //Check if user has previously liked the post:
        ArrayList<Integer> likedPosts = ((ProfileDocument) loggedInUser.getValue()).likedPosts;
        ArrayList<Integer> dislikedPosts = ((ProfileDocument) loggedInUser.getValue()).dislikedPosts;

        if(likedPosts.contains(model.getId())){
            model.setLiked();
        } else if (dislikedPosts.contains(model.getId())) {
            model.setDisliked();
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

        holder.ibComment.setBackgroundResource(R.drawable.chat);

        // button actions setup

        // Like
        holder.ibLike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ProfileDocument accountState = (ProfileDocument) loggedInUser.getValue();

                Log.i(TAG, "onClick: " + model.getId());
                model.setLiked(!model.isLiked());
                Log.i(TAG, "onClick: " + model.isLiked());

                if(model.checkIfPostIsLikedAndDisliked()){
                    model.setLiked();

                    accountState.dislikedPosts.remove(model.getId());
                    postCollection.incrementDislike(model.getId(), -1);
                    model.decrementDislikes();
                }

                if(model.isLiked()){
                    holder.ibLike.setBackgroundResource(R.drawable.colour_like);
                    postCollection.incrementLike(model.getId(), 1);
                    model.incrementLikes();
                }else{
                    holder.ibLike.setBackgroundResource(R.drawable.like);
                    postCollection.incrementLike(model.getId(), -1);
                    model.decrementLikes();
                }
                if(model.isDisliked()){
                    holder.ibDislike.setBackgroundResource(R.drawable.colour_dislike);
                }else{
                    holder.ibDislike.setBackgroundResource(R.drawable.dislike);
                }

                if(accountState != null){
                    if(accountState.likedPosts.contains(model.getId())){
                        accountState.likedPosts.remove(model.getId());
                    }else{
                        accountState.likedPosts.add(model.getId());

                    }
                    profileCollection.update(loggedInUser);
                }

                // Update Counters
                holder.tvLikeCount.setText(String.valueOf(model.getLikes()));
                holder.tvDislikeCount.setText(String.valueOf(model.getDislikes()));
            }
        });

        // Dislike
        holder.ibDislike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ProfileDocument accountState = (ProfileDocument) loggedInUser.getValue();

                Log.i(TAG, "onClick: " + model.getId());
                model.setDisliked(!model.isDisliked());
                Log.i(TAG, "onClick: " + model.isDisliked());

                if(model.checkIfPostIsLikedAndDisliked()){
                    model.setDisliked();

                    accountState.likedPosts.remove(model.getId());
                    postCollection.incrementLike(model.getId(), -1);

                    model.decrementLikes();
                }

                if(model.isDisliked()){
                    holder.ibDislike.setBackgroundResource(R.drawable.colour_dislike);
                    postCollection.incrementDislike(model.getId(), 1);
                    model.incremenetDislikes();


                }else{
                    holder.ibDislike.setBackgroundResource(R.drawable.dislike);
                    postCollection.incrementDislike(model.getId(), -1);
                    model.decrementDislikes();

                }
                if(model.isLiked()){
                    holder.ibLike.setBackgroundResource(R.drawable.colour_like);
                }else{
                    holder.ibLike.setBackgroundResource(R.drawable.like);
                }

                if(accountState != null){
                    if(accountState.dislikedPosts.contains(model.getId())){
                        accountState.dislikedPosts.remove(model.getId());
                    }else{
                        accountState.dislikedPosts.add(model.getId());
                    }
                    profileCollection.update(loggedInUser);
                }


                // Update Counters
                holder.tvLikeCount.setText(String.valueOf(model.getLikes()));
                holder.tvDislikeCount.setText(String.valueOf(model.getDislikes()));
            }
        });

        // Comment
        holder.ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Comments");
                int postId = model.getId();
                MutableLiveData<ArrayList<IDocument>> comments = new MutableLiveData<>();
                 commentCollection.getAllCommentsForPost(comments, postId);

                // todo: Inject the info into the comments fragment


//                comments.observe(getViewLifecycleOwner(), new Observer<ArrayList<IDocument>>() {
//                    @Override
//                    public void onChanged(ArrayList<IDocument> iDocuments) {
//                        ArrayList<CommentModel> commentModels = new ArrayList<>();
//                        Log.i(TAG, "onChanged: ");
//                        for (IDocument iDocument : iDocuments) {
//                            CommentDocument comment = (CommentDocument) iDocument;
//
//                            commentModels.add(
//                                    createComment(comment)
//                            );
//                        }
//
//                        commentModels.sort(Collections.reverseOrder());
//
//                        RecyclerView recyclerView = v.findViewById(R.id.commentRecycleView);
//                        CommentReyclerViewAdapter adapter = new CommentReyclerViewAdapter(context, commentModels);
//
//                        recyclerView.setAdapter(adapter);
////                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//                        recyclerView.getAdapter().notifyDataSetChanged();
//
//                        Navigation.findNavController(v).navigate(R.id.commentFragment);
//                    }
//                });

//                Bundle args = new Bundle();
//                args.putParcelableArrayList("comments", comments);
////                AppCompatActivity activity = (AppCompatActivity) v.getContext();
////                FragmentManager fragmentManager = activity.getSupportFragmentManager();
////                FragmentTransaction transaction = fragmentManager.beginTransaction();
////
////
////                transaction.replace(R.id.discoveryPostsRootFragment, commentsFragment);
////                transaction.addToBackStack("viewCommentsForPost");
////                transaction.commit();
//                Navigation.findNavController(v).navigate(R.id.commentFragment);
            }
        });

        // Profile Picture click
        holder.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ProfilePicture");

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();


                transaction.replace(R.id.discoveryPostsRootFragment, ViewProfileFragment.newInstance());
                transaction.addToBackStack("viewProfileTransaction");
                transaction.commit();

            }
        });
    }

//    private CommentModel createComment(CommentDocument comment) {
//        CommentModel model = new CommentModel();
//
//        String comment_user_text = "";
//
//        model.setId(comment.id);
//        model.setUserId(comment.userId);
//        model.setComment_user_text(comment.message);
//
//
//    }


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