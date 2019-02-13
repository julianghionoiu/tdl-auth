package tdl.auth.linkgenerator;

import lombok.*;

import java.util.Date;



@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IntroPageParameters {
    @Builder.Default String headerImageName = "accelerate.jpg";
    @Builder.Default String mainChallengeTitle = "Challenge";
    @Builder.Default String sponsorName = "Accelerate";
    @Builder.Default String codingSessionDurationLabel = "few hours";
    @Builder.Default Boolean allowNoVideoOption = true;
    @Builder.Default String username = "xUSERNAME";
    @Builder.Default String challenge = "xCHALLENGE";
    @Builder.Default String token = "xTOKEN";
    @Builder.Default Date expirationDate = new Date();
    @Builder.Default String journeyId = "xJOURNEY";
}
