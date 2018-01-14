package mainapplication.dto;

/**
 * Created by Luis Gouveia on 10/01/2018.
 */

public class SolrSearchResponseDTO {
    private ResponseHeaderDTO responseHeader;
    private ResponseDTO response;

    public SolrSearchResponseDTO() {
    }

    public ResponseDTO getResponse() {
        return response;
    }

    public void setResponse(ResponseDTO response) {
        this.response = response;
    }

    public ResponseHeaderDTO getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeaderDTO responseHeader) {
        this.responseHeader = responseHeader;
    }
}
