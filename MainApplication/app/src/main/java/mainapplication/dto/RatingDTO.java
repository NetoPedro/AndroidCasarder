package mainapplication.dto;

import java.util.List;

/**
 * Created by Luis Gouveia on 06/12/2017.
 */

public class RatingDTO {

    private List<AtendeeRatingDTO> bookerRatings;
    private List<AtendeeRatingDTO> participantRatings;

    public RatingDTO() {
    }

    public List<AtendeeRatingDTO> getBookerRatings() {
        return bookerRatings;
    }

    public void setBookerRatings(List<AtendeeRatingDTO> bookerRatings) {
        this.bookerRatings = bookerRatings;
    }

    public List<AtendeeRatingDTO> getParticipantRatings() {
        return participantRatings;
    }

    public void setParticipantRatings(List<AtendeeRatingDTO> participantRatings) {
        this.participantRatings = participantRatings;
    }
}