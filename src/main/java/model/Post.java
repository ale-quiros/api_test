package model;

public class Post {


    public Post(String title, String content) {
        this.title = title;
        Content = content;
    }

    //Usado para pruebas negativas
    public Post(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    private String title;
    private String Content;


}
