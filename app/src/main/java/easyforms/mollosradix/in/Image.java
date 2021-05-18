package easyforms.mollosradix.in;

public class Image {

    private final String id_card;
    private final String photo;
    private final String form;

    public Image(String id_card, String photo, String form) {
        this.id_card = id_card;
        this.photo = photo;
        this.form = form;

    }

    public String getId_card() {
        return id_card;
    }

    public String getPhoto() {
        return photo;
    }

    public String getForm() {
        return form;
    }
}
