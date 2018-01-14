package mainapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by Luis Gouveia on 10/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHeaderDTO {
    private int status;
    private int qtime;
    private HashMap<String, Object> params;

    public ResponseHeaderDTO() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQtime() {
        return qtime;
    }

    public void setQtime(int qtime) {
        this.qtime = qtime;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
}
