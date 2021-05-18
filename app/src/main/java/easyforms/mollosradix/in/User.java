package easyforms.mollosradix.in;

public class User {

    private final String user_id;
    private final String name;
    private final String email;
    private final String profile_photo_url;

    public User(String user_id, String name, String email, String profile_photo_url) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.profile_photo_url = profile_photo_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }
}
