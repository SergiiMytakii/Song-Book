package com.sergiimytakii.songbook;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class SongRepository {
    private SongDao songDao;
    private LiveData<List<Song>> allSongs;
    public static List<Song> songsFromServer = new ArrayList<>();

    public SongRepository(Application application) {
        SongDatabase database = SongDatabase.getInstance(application);
        songDao = database.songDao();
        allSongs = songDao.getAllSongs();
        songsFromServer = new SongLoader().load();
        loadSongsFromWeb(application.getApplicationContext());
    }

    public void insertAll(List<Song> songs) {
        new InsertAllSongAsyncTask(songDao).execute(songs);
    }
    public void insertSongWithStatus(Song song) {
        new insertSongWithStatusSongAsyncTask(songDao).execute(song);
    }
    public void update(Song song) {
        new UpdateAsyncTask(songDao).execute(song);
    }
    public void delete(Song song) {
        new DeleteSongAsyncTask(songDao).execute(song);
    }
    public void deleteAllSongs() {
        new DeleteAllSongsAsyncTask(songDao).execute();
    }
    public LiveData<List<Song>> getAllSongs() {
        return allSongs;
    }


    private void loadSongsFromWeb(final Context context) {


        final Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                int i = 0;
                while (i < 5000) {
                    try {
                        wait(1);
                        //Log.d("cs50", "waiting for data " + i);
                        i++;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (allSongs.getValue().size() == 0 && songsFromServer.size() != 0 ){
                    insertAll(songsFromServer);
                }
                else{
                    if (allSongs.getValue().size() < songsFromServer.size()) {
                        for (int j = 0; j < allSongs.getValue().size(); j++){
                            int favoriteStatus = allSongs.getValue().get(j).getFavStatus();
                            songsFromServer.get(j).setFavStatus(favoriteStatus);
                        }
                        insertAll(songsFromServer);
                   }else {
                        Log.d("cs50","local and web databases are equal");
                    }
                }
                if (allSongs.getValue().size() == 0 && songsFromServer.size() == 0){
                    Log.d("cs50", "Упс.. не можем загрузить песни с сервера... проверьте интернет соединение");
                   ToastUtils.show(context, "Упс.. не можем загрузить песни с сервера... проверьте интернет соединение");


                }
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
    }

    private static class InsertAllSongAsyncTask extends AsyncTask<List<Song>, Void, Void> {
        private SongDao songDao;

        private InsertAllSongAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        protected Void doInBackground(List<Song>... songs) {
            songDao.insertAll(songs[0]);
            Log.d("cs50", "inserted " + songs[0].get(0).getTitle() + "  " + songs[0].get(1).getTitle() + "   " + songs[0].get(2).getTitle());
            return null;
        }
    }
    private static class insertSongWithStatusSongAsyncTask extends AsyncTask<Song, Void, Void> {
        private SongDao songDao;

        private insertSongWithStatusSongAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }
        @Override
        protected Void doInBackground(Song... songs) {

            long i = songDao.insertSongWithStatus(songs[0]);
            Log.d("cs50",  i + " insertSongWithStatus " + songs[0].getTitle() + " " + songs[0].getFavStatus());
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Song, Void, Void> {
        private SongDao songDao;

        private UpdateAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            songDao.update(songs[0]);
            return null;
        }
    }
    private static class DeleteSongAsyncTask extends AsyncTask<Song, Void, Void> {
        private SongDao songDao;

        private DeleteSongAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            songDao.delete(songs[0]);
            return null;
        }
    }
    private static class DeleteAllSongsAsyncTask extends AsyncTask<Void, Void, Void> {
        private SongDao songDao;

        private DeleteAllSongsAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            songDao.deleteAllSongs();
            return null;
        }
    }

    public static class ToastUtils {
        static Toast toast = null;
        public static void show(Context context, String text) {
            try {
                if(toast!=null){
                    toast.setText(text);
                }else{
                    toast= Toast.makeText(context, text, Toast.LENGTH_LONG);
                }
                toast.show();
            } catch (Exception e) {

                Looper.prepare();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }

}