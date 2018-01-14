package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 28/12/2017.
 */

public class DistrictDTO implements Serializable{
    private int id;
    private String name;
    private CountryDTO country;

    public DistrictDTO() {
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

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }
}
