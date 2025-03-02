package test.autoparams.i310;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Campaign {

    private final Long id;
    private final CampaignType type;
}
