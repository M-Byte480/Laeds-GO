package ie.thirdfloor.csis.ul.laedsgo.ui.commentSection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;

public class CommentReyclerViewAdapter extends RecyclerView.Adapter<CommentReyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<CommentModel> commentModels = new ArrayList<>();
    public CommentReyclerViewAdapter(Context context , ArrayList<CommentModel> commentModels) {
        this.context = context;
        this.commentModels = commentModels;
    }

    public void setArray(ArrayList<CommentModel> array){
        this.commentModels = array;
    }

    public void addToArray(CommentModel array){
        this.commentModels.add(array);
    }

    public void sortArray(){
        Collections.sort(this.commentModels);
    }

    public void clearArray(){
        this.commentModels.clear();
    }

    public void reverseArray(){
        Collections.reverse(this.commentModels);
    }


    @NonNull
    @Override
    public CommentReyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row , parent, false);
        return new CommentReyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentReyclerViewAdapter.MyViewHolder holder, int position) {

        holder.userName.setText(commentModels.get(position).getUserName());
        holder.commentTxt.setText(commentModels.get(position).getComment_user_text());

        byte[] decodedString = Base64.decode(commentModels.get(position).image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.imageView.setImageBitmap(decodedByte);
//        holder.imageView.setImageResource(commentModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userName, commentTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            userName = itemView.findViewById(R.id.username);
            commentTxt = itemView.findViewById(R.id.commentText);
        }


    }



}
