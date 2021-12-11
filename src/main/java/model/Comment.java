package model;

public class Comment {

    private String name;

    public Comment(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    //Usado para pruebas negativas
    public Comment(String name) {
        this.name = name;
    }

    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }





}
