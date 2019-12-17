package application.utils;

import java.util.Objects;

public class Notification implements Comparable {
    private String title;
    private String content;

    public Notification(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(content, that.content);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        Notification that = (Notification) o;
        if(Objects.equals(title, that.title) &&
                Objects.equals(content, that.content)) {
            return 0;
        }
        return 1;
    }
}
