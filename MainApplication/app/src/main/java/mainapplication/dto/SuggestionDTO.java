package mainapplication.dto;

/**
 * Created by Luis Gouveia on 11/01/2018.
 */

public class SuggestionDTO {
    private String term;
    private int weight;
    private String payload;

    public SuggestionDTO() {
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
