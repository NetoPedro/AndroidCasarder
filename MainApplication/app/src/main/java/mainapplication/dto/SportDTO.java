package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 10/12/2017.
 */

public class SportDTO implements Serializable {
    private int id;
    private String name;

    public SportDTO() {
    }

    public SportDTO(int id, String name, String description) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
