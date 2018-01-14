package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 11/12/2017.
 */

public class BaseEquipmentDTO implements Serializable {

    private int id;
    private String name;
    private String description;
    private int typeId;
    private int facilityId;

    public BaseEquipmentDTO() {
    }

    public BaseEquipmentDTO(int id, String name, String description, int typeId, int facilityId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.typeId = typeId;
        this.facilityId = facilityId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
}
