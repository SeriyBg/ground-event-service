package com.explorer.groundevent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.random.RandomDataGenerator;

@NoArgsConstructor
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroundEvent {

    private static final String ALIEN_TYPE = "alien";
    private static final String STORM_TYPE = "storm";

    private String type;
    private Integer distance;

    GroundEvent enrich(Long playerScore) {
        long random = new RandomDataGenerator().nextLong(0, 1000) + playerScore;
        type = random > 600 ? STORM_TYPE : ALIEN_TYPE;
        return this;
    }
}
