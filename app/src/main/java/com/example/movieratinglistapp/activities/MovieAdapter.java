package com.example.movieratinglistapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieratinglistapp.R;
import com.example.movieratinglistapp.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies;
    private final Context context;

    public MovieAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    public void updateMovies(List<Movie> newMovies) {
        movies.clear();
        movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.serialNumber.setText(String.valueOf(position + 1)); // Serial number starts from 1
        holder.title.setText(movie.getTitle());
        holder.studio.setText(movie.getStudio());
        holder.rating.setText(String.valueOf(movie.getRating()));

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).showUpdateMovieDialog(movie);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (context instanceof MainActivity) {
                showDeleteConfirmationDialog(movie);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private void showDeleteConfirmationDialog(Movie movie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Movie");
        builder.setMessage("Are you sure you want to delete this movie?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).deleteMovieFromFirestore(movie);
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView serialNumber, title, studio, rating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            title = itemView.findViewById(R.id.movieTitle);
            studio = itemView.findViewById(R.id.movieStudio);
            rating = itemView.findViewById(R.id.movieRating);
        }
    }
}
