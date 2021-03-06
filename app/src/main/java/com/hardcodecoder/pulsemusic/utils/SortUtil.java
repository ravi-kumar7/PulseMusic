package com.hardcodecoder.pulsemusic.utils;

import com.hardcodecoder.pulsemusic.loaders.SortOrder;
import com.hardcodecoder.pulsemusic.loaders.SortOrder.ALBUMS;
import com.hardcodecoder.pulsemusic.loaders.SortOrder.ARTIST;
import com.hardcodecoder.pulsemusic.model.AlbumModel;
import com.hardcodecoder.pulsemusic.model.ArtistModel;
import com.hardcodecoder.pulsemusic.model.MusicModel;

import java.util.Collections;
import java.util.List;

public class SortUtil {

    public static List<MusicModel> sortLibraryList(List<MusicModel> list, SortOrder sortOrder) {
        switch (sortOrder) {
            case TITLE_ASC:
                Collections.sort(list, (o1, o2) -> o1.getTrackName().compareToIgnoreCase(o2.getTrackName()));
                break;
            case TITLE_DESC:
                Collections.sort(list, (o1, o2) -> o2.getTrackName().compareToIgnoreCase(o1.getTrackName()));
                break;
            case DURATION_ASC:
                Collections.sort(list, (o1, o2) -> Integer.compare(o1.getTrackDuration(), o2.getTrackDuration()));
                break;
            case DURATION_DESC:
                Collections.sort(list, (o1, o2) -> Integer.compare(o2.getTrackDuration(), o1.getTrackDuration()));
                break;
            case DATE_ADDED_ASC:
                Collections.sort(list, (o1, o2) -> Long.compare(o1.getDateAdded(), o2.getDateAdded()));
                break;
            case DATE_ADDED_DESC:
                Collections.sort(list, (o1, o2) -> Long.compare(o2.getDateAdded(), o1.getDateAdded()));
                break;
            case DATE_MODIFIED_ASC:
                Collections.sort(list, (o1, o2) -> Long.compare(o1.getDateModified(), o2.getDateModified()));
                break;
            case DATE_MODIFIED_DESC:
                Collections.sort(list, (o1, o2) -> Long.compare(o2.getDateModified(), o1.getDateModified()));
                break;
            case TRACK_NUMBER_ASC:
                Collections.sort(list, (o1, o2) -> Long.compare(o1.getTrackNumber(), o2.getTrackNumber()));
                break;
            case TRACK_NUMBER_DESC:
                Collections.sort(list, (o1, o2) -> Long.compare(o2.getTrackNumber(), o1.getTrackNumber()));
                break;
        }
        return list;
    }

    public static List<ArtistModel> sortArtistList(List<ArtistModel> list, ARTIST sortOrder) {
        if (sortOrder == ARTIST.TITLE_ASC)
            Collections.sort(list, (o1, o2) -> o1.getArtistName().compareToIgnoreCase(o2.getArtistName()));
        else if (sortOrder == ARTIST.TITLE_DESC) {
            Collections.sort(list, (o1, o2) -> o2.getArtistName().compareToIgnoreCase(o1.getArtistName()));
        } else if (sortOrder == ARTIST.NUM_OF_TRACKS_ASC) {
            Collections.sort(list, (o1, o2) -> o1.getNumOfTracks() - o2.getNumOfTracks());
        } else if (sortOrder == ARTIST.NUM_OF_TRACKS_DESC) {
            Collections.sort(list, (o1, o2) -> o2.getNumOfTracks() - o1.getNumOfTracks());
        }
        return list;
    }

    public static List<AlbumModel> sortAlbumList(List<AlbumModel> list, ALBUMS sortOrder) {
        switch (sortOrder) {
            case TITLE_DESC:
                Collections.sort(list, (o1, o2) -> o2.getAlbumName().compareToIgnoreCase(o1.getAlbumName()));
                break;
            case ARTIST_ASC:
                Collections.sort(list, (o1, o2) -> o1.getAlbumArtist().compareToIgnoreCase(o2.getAlbumArtist()));
                break;
            case ARTIST_DESC:
                Collections.sort(list, (o1, o2) -> o2.getAlbumArtist().compareToIgnoreCase(o1.getAlbumArtist()));
                break;
            case ALBUM_DATE_FIRST_YEAR_ASC:
                Collections.sort(list, (o1, o2) -> {
                    int diff = Integer.compare(o1.getFirstYear(), o2.getFirstYear());
                    // If album year is the same, sort alphabetically
                    if (diff == 0)
                        return o1.getAlbumName().compareToIgnoreCase(o2.getAlbumName());
                    return diff;
                });
                break;
            case ALBUM_DATE_FIRST_YEAR_DESC:
                Collections.sort(list, (o1, o2) -> {
                    int diff = Integer.compare(o2.getFirstYear(), o1.getFirstYear());
                    // If album year is the same, sort alphabetically
                    if (diff == 0)
                        return o1.getAlbumName().compareToIgnoreCase(o2.getAlbumName());
                    return diff;
                });
                break;
            case TITLE_ASC:
            default:
                Collections.sort(list, (o1, o2) -> o1.getAlbumName().compareToIgnoreCase(o2.getAlbumName()));
                break;
        }
        return list;
    }
}