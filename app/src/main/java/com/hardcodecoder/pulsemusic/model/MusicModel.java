package com.hardcodecoder.pulsemusic.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class MusicModel implements Serializable {

    private String mTrackName;
    private String mTrackPath;
    private String mAlbum;
    private String mArtist;
    private String mAlbumArtUrl;
    private long mDateAdded;
    private long mDateModified;
    private int mId;
    private int mAlbumId;
    private int mTrackNumber;
    private int mTrackDuration;

    public MusicModel(int mId,
                      @NonNull String trackName,
                      @NonNull String album,
                      int albumId,
                      @NonNull String artist,
                      @NonNull String trackPath,
                      @Nullable String albumArtUrl,
                      long dateAdded,
                      long dateModified,
                      int trackNumber,
                      int trackDuration) {
        this.mId = mId;
        this.mTrackName = trackName;
        this.mTrackPath = trackPath;
        this.mAlbum = album;
        this.mArtist = artist;
        this.mAlbumArtUrl = albumArtUrl;
        this.mAlbumId = albumId;
        this.mDateAdded = dateAdded;
        this.mDateModified = dateModified;
        this.mTrackNumber = trackNumber;
        this.mTrackDuration = trackDuration;
    }

    public int getId() {
        return mId;
    }

    public String getTrackName() {
        return mTrackName;
    }

    public String getTrackPath() {
        return mTrackPath;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getAlbumArtUrl() {
        return mAlbumArtUrl;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public int getTrackNumber() {
        return mTrackNumber;
    }

    public long getDateAdded() {
        return mDateAdded;
    }

    public long getDateModified() {
        return mDateModified;
    }

    public int getTrackDuration() {
        return mTrackDuration;
    }

    @Override
    public int hashCode() {
        return mTrackPath.hashCode();
    }
}