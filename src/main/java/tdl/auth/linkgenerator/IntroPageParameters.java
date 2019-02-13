package tdl.auth.linkgenerator;

import lombok.*;

import java.util.Date;
import java.util.Optional;


@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IntroPageParameters {
    @Builder.Default private String headerImageName = "accelerate.jpg";
    @Builder.Default private String mainChallengeTitle = "Challenge";
    @Builder.Default private String sponsorName = "Accelerate";
    @Builder.Default private String codingSessionDurationLabel = "few hours";
    @Builder.Default private Boolean allowNoVideoOption = true;
    @Builder.Default private String username = "xUSERNAME";
    @Builder.Default private String challenge = "xCHALLENGE";
    @Builder.Default private String token = "xTOKEN";
    @Builder.Default private Date expirationDate = new Date();
    @Builder.Default private String journeyId = "xJOURNEY";

    // Testing only
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Builder.Default private Optional<Date> fakeCurrentDate = Optional.empty();
}
