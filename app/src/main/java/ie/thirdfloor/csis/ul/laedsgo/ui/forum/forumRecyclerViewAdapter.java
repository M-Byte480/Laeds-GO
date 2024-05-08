package ie.thirdfloor.csis.ul.laedsgo.ui.forum;

import static ie.thirdfloor.csis.ul.laedsgo.ui.Cache.profilePhotosCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.forums.ForumsDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;
import ie.thirdfloor.csis.ul.laedsgo.entities.ForumPostModel;
import ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts.PostRecyclerViewAdapter;

public class forumRecyclerViewAdapter extends RecyclerView.Adapter<forumRecyclerViewAdapter.MyPostViewHolder> {

    Context context;
    private List<ForumPostModel> forumsModels;
    private final MutableLiveData<IDocument> loggedInUser;
    public int itemCount = 0;

    private final ProfileCollection profileCollection = new ProfileCollection();

    public forumRecyclerViewAdapter(Context context, List<ForumPostModel> items, MutableLiveData<IDocument> loggedInUser) {
        this.forumsModels = items;
        this.context = context;
        this.loggedInUser = loggedInUser;
    }

    public void setArray(ArrayList<ForumPostModel> array){
        this.forumsModels = array;
        itemCount = array.size();
    }

        @NonNull
        @Override
        public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate view: parse the XML and apply the binding.
            LayoutInflater inflater = LayoutInflater.from(this.context);
            View view = inflater.inflate(R.layout.fragment_post_row, parent, false);
            return new forumRecyclerViewAdapter.MyPostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
            if(loggedInUser.getValue() == null){
                return;
            }
            ForumPostModel model = forumsModels.get(position);

            // Model from the array
            holder.tvContent.setText(model.getContent());

            String timestamp = model.getTime();

            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MMM/yyyy", Locale.US);

            try{
                Date date = inputFormat.parse(timestamp);

                String[] dateTime = outputFormat.format(date).split(" ");

                holder.tvTime.setText(dateTime[0]);
                holder.tvDate.setText(dateTime[1].replace("/", " "));

            }catch (ParseException e){
                holder.tvTime.setText(R.string.error);
                holder.tvDate.setText(R.string.error);
            }

            // Profile Picture
            Integer userId = Integer.parseInt(model.getUserId());

            if(profilePhotosCache.containsKey(userId)) {
                byte[] decodedString = Base64.decode(profilePhotosCache.get(userId), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.profilePicture.setImageBitmap(decodedByte);
            }else{
                MutableLiveData<IDocument> pfp = new MutableLiveData<>();

                pfp.observe((AppCompatActivity) context, document -> {
                    ProfileDocument profileDocument = (ProfileDocument) document;
                    byte[] decodedString = Base64.decode(profileDocument.profilePhoto, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.profilePicture.setImageBitmap(decodedByte);
                    profilePhotosCache.put(userId, profileDocument.profilePhoto);
                });

                profileCollection.getUserById(Integer.parseInt(model.getUserId()), pfp);
            }


        }

        @Override
        public int getItemCount() {
            return forumsModels.size();
        }



        public static class MyPostViewHolder extends RecyclerView.ViewHolder {
        // Grab the views from the fragment_post_row.xml layout file
        // assign it to each field. Basically onCreate

        ImageView profilePicture;
        TextView tvContent, tvName, tvTime, tvDate;

        public MyPostViewHolder(View rowView) {
            super(rowView);

            profilePicture = rowView.findViewById(R.id.pfp);
            tvContent = rowView.findViewById(R.id.content);
            tvName = rowView.findViewById(R.id.username);
            tvTime = rowView.findViewById(R.id.time);
            tvDate = rowView.findViewById(R.id.date);

        }


        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + "mContentView.getText()" + "'";
        }
    }
}
