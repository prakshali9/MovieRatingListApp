package com.example.movieratinglistapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieratinglistapp.models.Movie;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends ViewModel {

    private final MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MovieViewModel() {
        fetchMovies();
    }

    // Expose the list of movies as LiveData
    public LiveData<List<Movie>> getMovies() {
        return moviesLiveData;
    }

    // Fetch movies from Firestore
    public void fetchMovies() {
        db.collection("movies")
                .orderBy("rating", Query.Direction.DESCENDING) // Sort movies by rating
                .limit(20) // Limit the results to 20 movies
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        moviesLiveData.setValue(new ArrayList<>()); // Return an empty list if there's an error
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        List<Movie> movies = queryDocumentSnapshots.toObjects(Movie.class);
                        moviesLiveData.setValue(movies); // Update the LiveData
                    }
                });
    }

    // Add a movie to Firestore
    public void addMovie(Movie movie) {
        db.collection("movies")
                .add(movie)
                .addOnSuccessListener(documentReference -> fetchMovies()) // Refresh movies after adding
                .addOnFailureListener(e -> {
                    // Optionally handle failure
                });
    }

    // Update an existing movie in Firestore
    public void updateMovie(String docId, Movie movie) {
        db.collection("movies")
                .document(docId)
                .set(movie)
                .addOnSuccessListener(aVoid -> fetchMovies()) // Refresh movies after updating
                .addOnFailureListener(e -> {
                    // Optionally handle failure
                });
    }

    // Delete a movie from Firestore
    public void deleteMovie(String docId) {
        db.collection("movies")
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> fetchMovies()) // Refresh movies after deleting
                .addOnFailureListener(e -> {
                    // Optionally handle failure
                });
    }
}
