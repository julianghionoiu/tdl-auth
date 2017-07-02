package tdl.auth.linkgenerator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkGeneratorRequest {
    private String username;
    private Integer validityDays;
    private List<String> challengeIds;
}
