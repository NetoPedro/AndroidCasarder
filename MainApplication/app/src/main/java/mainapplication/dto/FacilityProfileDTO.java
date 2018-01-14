package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 04/12/2017.
 */

class FacilityProfileDTO implements Serializable{
    private int id;
    private String name;
    private int maxCapacity;
    private int facilityId;

    public FacilityProfileDTO() {
    }

    public FacilityProfileDTO(int id, String name, int maxCapacity, int facilityId) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
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

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
}
