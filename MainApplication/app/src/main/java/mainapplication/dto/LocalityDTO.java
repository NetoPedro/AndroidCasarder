package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 04/12/2017.
 */

class LocalityDTO implements Serializable {
    private int id;
    private String name;
    private int townHallId;

    public LocalityDTO(int id, String name, int townHallId) {
        this.id = id;
        this.name = name;
        this.townHallId = townHallId;
    }

    public LocalityDTO() {
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

    public int getTownHallId() {
        return townHallId;
    }

    public void setTownHallId(int townHallId) {
        this.townHallId = townHallId;
    }
}
