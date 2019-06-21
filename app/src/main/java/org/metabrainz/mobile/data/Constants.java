package org.metabrainz.mobile.data;

public class Constants {
    public static final String LOOKUP_ARTIST_PARAMS =
            "url-rels+releases+ratings+tags";
    public static final String LOOKUP_RELEASE_PARAMS =
            "recordings+url-rels+artist-credits";
    public static final String LOOKUP_LABEL_PARAMS =
            "releases+url-rels";
    public static final String LOOKUP_RECORDING_PARAMS =
            "releases+media+artists+artist-credits";
    public static final String LOOKUP_RELEASE_GROUP_PARAMS =
            "releases+artist-credits+url-rels+release-rels+media";

    public static final String LIMIT = "100";
    public static final String OFFSET = "0";
}