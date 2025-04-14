package com.example.movieratinglistapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieratinglistapp.R;
import com.example.movieratinglistapp.viewmodels.MovieViewModel;
import com.example.movieratinglistapp.models.Movie;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button addMovieButton = findViewById(R.id.addMovieButton);
        addMovieButton.setOnClickListener(v -> showAddMovieDialog());

        fetchMovies();

        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getMovies().observe(this, movies -> {
            if (movies != null) {
                adapter.updateMovies(movies); // Update the RecyclerView adapter
            }
        });
    }



    private void showAddMovieDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Movie");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_movie, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.movieTitleInput);
        EditText studioInput = dialogView.findViewById(R.id.movieStudioInput);
        EditText ratingInput = dialogView.findViewById(R.id.movieRatingInput);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = titleInput.getText().toString();
            String studio = studioInput.getText().toString();
            double rating;

            try {
                rating = Double.parseDouble(ratingInput.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid rating input", Toast.LENGTH_SHORT).show();
                return;
            }

            addMovieToFirestore(title, studio, rating);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addMovieToFirestore(String title, String studio, double rating) {

        Movie movie = new Movie(title, studio, rating);

        db.collection("movies")
                .add(movie)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Movie added successfully!", Toast.LENGTH_SHORT).show();
                    fetchMovies(); // Refresh RecyclerView
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void showUpdateMovieDialog(Movie movie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Movie");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_movie, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.movieTitleInput);
        EditText studioInput = dialogView.findViewById(R.id.movieStudioInput);
        EditText ratingInput = dialogView.findViewById(R.id.movieRatingInput);

        // Prefill current movie details
        titleInput.setText(movie.getTitle());
        studioInput.setText(movie.getStudio());
        ratingInput.setText(String.valueOf(movie.getRating()));

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedTitle = titleInput.getText().toString();
            String updatedStudio = studioInput.getText().toString();
            double updatedRating;

            try {
                updatedRating = Double.parseDouble(ratingInput.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid rating input", Toast.LENGTH_SHORT).show();
                return;
            }

            updateMovieInFirestore(movie, updatedTitle, updatedStudio, updatedRating);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void updateMovieInFirestore(Movie movie, String title, String studio, double rating) {
        db.collection("movies")
                .whereEqualTo("title", movie.getTitle())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String docId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        db.collection("movies").document(docId)
                                .update("title", title, "studio", studio, "rating", rating)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Movie updated successfully!", Toast.LENGTH_SHORT).show();
                                    fetchMovies();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    public void deleteMovieFromFirestore(Movie movie) {
        db.collection("movies")
                .whereEqualTo("title", movie.getTitle())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String docId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        db.collection("movies").document(docId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Movie deleted successfully!", Toast.LENGTH_SHORT).show();
                                    fetchMovies();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to delete movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    private void fetchMovies() {
        db.collection("movies")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("MainActivity", "Error listening to updates", e);
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        List<Movie> movies = new ArrayList<>(queryDocumentSnapshots.toObjects(Movie.class));
                        if (adapter == null) {
                            adapter = new MovieAdapter(movies, this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.updateMovies(movies);
                        }
                    }
                });
    }
}
