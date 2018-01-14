package mainapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 11/12/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplementalEquipmentDTO implements Serializable {

    private int id;
    private String name;
    private String description;
    private int typeId;
    private int facilityId;
    private float price;

    public SupplementalEquipmentDTO() {
    }

    public SupplementalEquipmentDTO(int id, String name, String description, int typeId, int facilityId, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.typeId = typeId;
        this.facilityId = facilityId;
        this.price = price;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
