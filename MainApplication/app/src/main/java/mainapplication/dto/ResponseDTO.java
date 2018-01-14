package mainapplication.dto;

/**
 * Created by Luis Gouveia on 10/01/2018.
 */

public class ResponseDTO {
    private int numFound;
    private int start;
    private SolrFacilityDTO[] docs;

    public ResponseDTO() {
    }

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public SolrFacilityDTO[] getDocs() {
        return docs;
    }

    public void setDocs(SolrFacilityDTO[] docs) {
        this.docs = docs;
    }
}
