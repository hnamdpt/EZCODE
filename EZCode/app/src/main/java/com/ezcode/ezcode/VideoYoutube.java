package com.ezcode.ezcode;

public class VideoYoutube {
    private String title;
    private String Thumbnail;
    private String IdVideo;

    public VideoYoutube() {
    }

    public VideoYoutube(String title, String thumbnail, String idVideo) {
        this.title = title;
        Thumbnail = thumbnail;
        IdVideo = idVideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getIdVideo() {
        return IdVideo;
    }

    public void setIdVideo(String idVideo) {
        IdVideo = idVideo;
    }
}
