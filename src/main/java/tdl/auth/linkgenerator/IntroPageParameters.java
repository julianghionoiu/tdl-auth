package tdl.auth.linkgenerator;

import lombok.*;

import java.util.Date;
import java.util.Optional;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntroPageParameters {
    @Builder.Default private String headerImageName = "accelerate.jpg";
    @Builder.Default private String mainChallengeTitle = "xMAIN_CHALLENGE_TITLE";
    @Builder.Default private String sponsorName = "xSPONSOR";
    @Builder.Default private String inspiredByLabel = "xINSPIRED_BY";
    @Builder.Default private String codingSessionDurationLabel = "xCODING_DURATION";
    @Builder.Default private String defaultLanguage = "Java";
    @Builder.Default private VideoRecordingOption videoRecordingOption = VideoRecordingOption.OPTIONAL;
    @Builder.Default private Boolean enableApplyPressure = true;
    @Builder.Default private Boolean enableReportSharing = true;
    @Builder.Default private String username = "xUSERNAME";
    @Builder.Default private String challenge = "xCHALLENGE";
    @Builder.Default private String token = "xTOKEN";
    @Builder.Default private Date expirationDate = new Date();
    @Builder.Default private String journeyId = "xJOURNEY";

    // Testing only
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Builder.Default private Optional<Date> fakeCurrentDate = Optional.empty();
}
