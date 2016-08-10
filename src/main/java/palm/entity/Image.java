package palm.entity;

/**
 * Created by sukai on 8/9/16.
 */
public class Image {

    private int id;
    private int age;
    private String url;

    public Image(int id, int age, String url) {
        this.id = id;
        this.age = age;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
