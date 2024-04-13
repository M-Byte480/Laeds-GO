package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;
import ie.thirdfloor.csis.ul.laedsgo.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyPostViewHolder> {

    Context context;
    private List<DiscoveryPostModel> postModels;

    private ProfileCollection profileCollection = new ProfileCollection();

    public PostRecyclerViewAdapter(Context contenxt, List<DiscoveryPostModel> items) {
        this.postModels = items;
        this.context = contenxt;
    }

    public void clearArray(){
        this.postModels = new ArrayList<>();
    }

    public void setArray(ArrayList<DiscoveryPostModel> array){
        this.postModels = array;
    }

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

        DiscoveryPostModel model = postModels.get(position);

        holder.tvName.setText(model.getUsername());
        holder.tvLocation.setText(model.getLocation());
        holder.tvLikeCount.setText(String.valueOf(model.getLikes()));
        holder.tvDislikeCount.setText(String.valueOf(model.getDislikes()));
        holder.tvContent.setText(model.getContent());

        String[] dateTime = model.getTime().split(" ");


        holder.tvTime.setText(dateTime[0]);
        holder.tvDate.setText(dateTime[1]);

        // Todo: setup the pfp
//        holder.profilePicture

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

        public MyPostViewHolder(View rowView) {
            super(rowView);

            profilePicture = rowView.findViewById(R.id.pfp);
            tvContent = rowView.findViewById(R.id.content);
            tvName = rowView.findViewById(R.id.username);
            tvLikeCount = rowView.findViewById(R.id.likeCount);
            tvDislikeCount = rowView.findViewById(R.id.dislikeCount);
            tvLocation = rowView.findViewById(R.id.location);
            tvTime = rowView.findViewById(R.id.time);
            tvDate = rowView.findViewById(R.id.date);
        }

//        public MyPostViewHolder(ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentPostRowBinding binding) {
//            super(binding.getRoot());
//            mIdView = binding.username;
//            mContentView = binding.content;
//        }

        @Override
        public String toString() {
            return super.toString() + " '" + "mContentView.getText()" + "'";
        }
    }
}