package mainapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Luis Gouveia on 11/01/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggesterDTO {
    private SuggestDTO suggest;

    public SuggestDTO getSuggest() {
        return suggest;
    }

    public void setSuggest(SuggestDTO suggest) {
        this.suggest = suggest;
    }

    public SuggesterDTO() {

    }
}
