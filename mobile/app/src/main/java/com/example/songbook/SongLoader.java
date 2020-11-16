package com.example.songbook;



import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

public class SongLoader  {
        public List<Song> songs = new ArrayList<>();
        private DatabaseReference mDataBase;
        private String USER_KEY = "Songs";
        private Song song;



    public SongLoader() {
        init();
           }


    private void init(){
               mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        }
        public List<Song> load(){
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (songs.size() > 0) songs.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        song = ds.getValue(Song.class);
                        int id = Integer.parseInt(ds.getKey());
                        song.setId(id);
                        Log.d("cs50", song.getId() + " " + song.getTitle());
                        assert song != null;
                        songs.add(song);
                     }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mDataBase.addValueEventListener(valueEventListener);
            return songs;
        }






}
