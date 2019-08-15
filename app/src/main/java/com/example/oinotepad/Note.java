package com.example.oinotepad;

import java.io.Serializable;
import java.lang.reflect.Array;

public class Note implements Serializable {
    private String id;
    private String title;
    private String notes;
    private String imageUrl;
    private String audioUrl;

    public Note( String title, String notes, String imageUrl, String audioUrl) {
        this.setId(id);
        this.setTitle(title);
        this.setNotes(notes);
        this.setImageUrl(imageUrl);
        this.setAudioUrl(audioUrl);
    }
    public Note(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

//    public Array getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(Array imageUrl) {
//        this.imageUrl = imageUrl;
    //}
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
