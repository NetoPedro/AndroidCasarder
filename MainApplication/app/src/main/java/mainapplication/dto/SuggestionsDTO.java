package mainapplication.dto;

/**
 * Created by Luis Gouveia on 11/01/2018.
 */

public class SuggestionsDTO {
    private int numFound;
    private SuggestionDTO[] suggestions;

    public SuggestionsDTO() {
    }

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public SuggestionDTO[] getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(SuggestionDTO[] suggestions) {
        this.suggestions = suggestions;
    }
}
