package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 28/12/2017.
 */

public class CityDTO implements Serializable {
    private int id;
    private String name;
    private DistrictDTO district;

    public CityDTO() {
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

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }
}
