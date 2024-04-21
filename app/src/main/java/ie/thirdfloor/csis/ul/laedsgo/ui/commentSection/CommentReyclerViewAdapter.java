package ie.thirdfloor.csis.ul.laedsgo.ui.commentSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.R;

public class CommentReyclerViewAdapter extends RecyclerView.Adapter<CommentReyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<CommentModel> commentModels;
    public CommentReyclerViewAdapter(Context context , ArrayList<CommentModel> commentModels) {
        this.context = context;
        this.commentModels = commentModels;
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
        holder.imageView.setImageResource(commentModels.get(position).getImage());
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
