package mainapplication.dto;

import java.util.List;

/**
 * Created by Luis Gouveia on 26/12/2017.
 */

public class FacilitySearchDTO {
    private int resultsFound;
    private FacilityDTO[] facilitiesDTO;

    public FacilitySearchDTO() {
    }

    public int getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(int resultsFound) {
        this.resultsFound = resultsFound;
    }

    public FacilityDTO[] getFacilitiesDTO() {
        return facilitiesDTO;
    }

    public void setFacilitiesDTO(FacilityDTO[] facilitiesDTO) {
        this.facilitiesDTO = facilitiesDTO;
    }
}
