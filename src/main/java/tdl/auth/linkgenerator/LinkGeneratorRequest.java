package tdl.auth.linkgenerator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkGeneratorRequest {
    private String headerImageName;
    private String mainChallengeTitle;
    private String sponsorName;
    private String codingDurationLabel;
    private Boolean allowNoVideoOption;
    private String username;
    private Integer validityDays;
    private List<String> warmupChallenges;
    private String officialChallenge;
}
