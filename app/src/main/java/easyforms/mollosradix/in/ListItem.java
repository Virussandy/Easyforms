package easyforms.mollosradix.in;

public class ListItem {
    private String id;
    private String sl_no;

    public ListItem(String id, String sl_no) {
        this.id = id;
        this.sl_no = sl_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getsl_no() {
        return sl_no;
    }

    public void setsl_no(String sl_no) {
        this.sl_no = sl_no;
    }
}
