package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments.CommentConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;
import ie.thirdfloor.csis.ul.laedsgo.ui.view_profile.ViewProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DiscoveryPostModel}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyPostViewHolder> {

    Context context;
    private List<DiscoveryPostModel> postModels;

    private ProfileCollection profileCollection = new ProfileCollection();

    private MutableLiveData<IDocument> loggedInUser = new MutableLiveData<>();
    private HashMap<Integer, String> profilePhotos = new HashMap<>();
    private HashMap<Integer, String> profileNames = new HashMap<>();
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


        if(loggedInUser.getValue() == null){
            // We need to return here because we can't do anything without a logged in user
            return;
        }

        // Function responsible for assigning values to each
        // view as they are coming onto the screen based on their index
        // IN THE RECYCLE VIEW

        // Model from the array
        DiscoveryPostModel model = postModels.get(position);

        // Simple binding
        holder.tvLocation.setText(model.getLocation());
        holder.tvLikeCount.setText(String.valueOf(model.getLikes()));
        holder.tvDislikeCount.setText(String.valueOf(model.getDislikes()));
        holder.tvContent.setText(model.getContent());

        String timestamp = model.getTime();

        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US);

        Log.i(TAG, "onBindViewHolder: " + timestamp);
        try{
            Date date = inputFormat.parse(timestamp);

            String[] dateTime = outputFormat.format(date).split(" ");

            holder.tvTime.setText(dateTime[0]);
            holder.tvDate.setText(dateTime[1]);

            Log.i(TAG, "onBindViewHolder: " + dateTime[0] + " " + dateTime[1]);
        }catch (ParseException e){
            holder.tvTime.setText("Error");
            holder.tvDate.setText("Error");
        }

        // Button Setups


        //Check if user has previously liked the post:
        ArrayList<Integer> likedPosts = ((ProfileDocument) loggedInUser.getValue()).likedPosts;
        ArrayList<Integer> dislikedPosts = ((ProfileDocument) loggedInUser.getValue()).dislikedPosts;

        Integer userId = Integer.parseInt(model.getUserId());
        // Profile Picture

        if(profilePhotos.containsKey(userId)) {
            byte[] decodedString = Base64.decode(profilePhotos.get(userId), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.profilePicture.setImageBitmap(decodedByte);
        }else{
            MutableLiveData<IDocument> pfp = new MutableLiveData<>();

            pfp.observe((AppCompatActivity) context, document -> {
                ProfileDocument profileDocument = (ProfileDocument) document;
                byte[] decodedString = Base64.decode(profileDocument.profilePhoto, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.profilePicture.setImageBitmap(decodedByte);
                profilePhotos.put(userId, profileDocument.profilePhoto);
            });

            profileCollection.getUserById(Integer.parseInt(model.getUserId()), pfp);
        }

        // Set name
        if(profileNames.containsKey(userId)) {
            holder.tvName.setText(profileNames.get(userId));
        }else{
            MutableLiveData<IDocument> name = new MutableLiveData<>();

            name.observe((AppCompatActivity) context, document -> {
                ProfileDocument profileDocument = (ProfileDocument) document;
                holder.tvName.setText(profileDocument.name);
                profileNames.put(userId, profileDocument.name);
            });

            profileCollection.getUserById(Integer.parseInt(model.getUserId()), name);
        }


        // Like Buttons
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
                // commentCollection.getAllCommentsByParentId(postId, comments);

                // todo: Inject the info into the comments fragment
                //TemporaryCommentsFragment commentsFragment = TemporaryCommentsFragment.newInstance(comments, v);
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

                ViewProfileFragment profileFragment = ViewProfileFragment.newInstance(model.getUserId());

                transaction.replace(R.id.discoveryPostsRootFragment, profileFragment);
                transaction.addToBackStack("viewProfileTransaction");
                transaction.commit();

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