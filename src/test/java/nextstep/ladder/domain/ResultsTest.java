package nextstep.ladder.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ResultsTest {
    @Test
    public void create() {
        final List<String> results = List.of("result1", "result2", "result3");
        assertThat(Results.of(results)).isEqualTo(Results.of(results));
    }

    @ParameterizedTest(name = "create failed : [{arguments}]")
    @NullAndEmptySource
    public void createFailed(List<String> results) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Results.of(results))
                .withMessageContaining("cannot be null or empty");
    }
}
