package test.autoparams.i310;

import autoparams.AutoSource;
import autoparams.Repeat;
import autoparams.generator.Factory;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CampaignTests {

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void testMethod(Factory<Campaign> factory) {
        factory.applyCustomizer(new CampaignTypeFieldFreezer(CampaignType.A));
        Campaign campaign = factory.get();
        assertThat(campaign.getType()).isEqualTo(CampaignType.A);
    }
}
