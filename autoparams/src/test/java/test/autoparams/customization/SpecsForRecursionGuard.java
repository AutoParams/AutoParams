package test.autoparams.customization;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.customization.RecursionGuard;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForRecursionGuard {

    public static class User {
        private final String username;
        private final Following[] followings;

        public User(String username, Following[] followings) {
            this.username = username;
            this.followings = followings;
        }

        public String getUsername() {
            return username;
        }

        public Following[] getFollowings() {
            return followings;
        }
    }

    public static class Following {
        private final User follower;

        public Following(User follower) {
            this.follower = follower;
        }

        public User getFollower() {
            return follower;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_guards_recursion_with_1_depth(ResolutionContext context) {
        final int recursionDepth = 1;
        context.customizeGenerator(new RecursionGuard(recursionDepth));

        final User actual = context.generate(User.class);

        assertThat(actual.getFollowings()[0].getFollower()).isNull();
        assertThat(actual.getFollowings()[1].getFollower()).isNull();
        assertThat(actual.getFollowings()[2].getFollower()).isNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_guards_recursion_with_2_depth(ResolutionContext context) {
        final int recursionDepth = 2;
        context.customizeGenerator(new RecursionGuard(recursionDepth));

        final User actual = context.generate(User.class);

        assertThat(actual.getFollowings()[0].getFollower()).isNotNull();
        assertThat(actual.getFollowings()[1].getFollower()).isNotNull();
        assertThat(actual.getFollowings()[2].getFollower()).isNotNull();
    }
}
