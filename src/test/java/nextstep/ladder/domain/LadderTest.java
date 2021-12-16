package nextstep.ladder.domain;

import java.util.Map;
import java.util.stream.Stream;

import nextstep.ladder.domain.line.LineGenerateStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static nextstep.ladder.domain.HeightTest.h;
import static nextstep.ladder.domain.PlayerCountTest.pc;
import static nextstep.ladder.domain.PlayerTest.p;
import static nextstep.ladder.domain.PlayersTest.ps;
import static nextstep.ladder.domain.ResultTest.r;
import static nextstep.ladder.domain.ResultsTest.rs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class LadderTest {
    @Test
    public void create() {
        final PlayerCount playerCount = pc(5);
        final Height height = h(5);
        final LineGenerateStrategy strategy = TestLineStrategy.NO_LINE_STRATEGY;

        assertThat(Ladder.of(playerCount, height, strategy)).isEqualTo(Ladder.of(playerCount, height, strategy));
    }

    static Stream<Arguments> parseInvalidLadder() {
        return Stream.of(
                Arguments.of(null, h(5), TestLineStrategy.NO_LINE_STRATEGY),
                Arguments.of(pc(5), null, TestLineStrategy.NO_LINE_STRATEGY),
                Arguments.of(pc(5), h(5), null)
        );
    }

    @ParameterizedTest
    @MethodSource("parseInvalidLadder")
    public void createFailed(PlayerCount playerCount, Height height, LineGenerateStrategy strategy) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Ladder.of(playerCount, height, strategy));
    }

    @Test
    public void stream() {
        final PlayerCount playerCount = pc(3);
        final int height = 5;
        final LineGenerateStrategy strategy = TestLineStrategy.NO_LINE_STRATEGY;
        assertThat(Ladder.of(playerCount, height, strategy).stream()).hasSize(height);
        assertThat(Ladder.of(playerCount, height, strategy).stream()).hasOnlyElementsOfType(Line.class);
    }

    static Stream<Arguments> parseLadderResult() {
        return Stream.of(
                Arguments.of(Ladder.of(pc(2), 2, TestLineStrategy.NO_LINE_STRATEGY),
                        ps("p1", "p2"), rs("r1", "r2"),
                        ResultOfGame.of(Map.of(p("p1"), r("r1"), p("p2"), r("r2")))),
                Arguments.of(Ladder.of(pc(2), 1, TestLineStrategy.ALL_LINE_STRATEGY),
                        ps("p1", "p2"), rs("r1", "r2"),
                        ResultOfGame.of(Map.of(p("p1"), r("r2"), p("p2"), r("r1")))),
                Arguments.of(Ladder.of(pc(2), 2, TestLineStrategy.ALL_LINE_STRATEGY),
                        ps("p1", "p2"), rs("r1", "r2"),
                        ResultOfGame.of(Map.of(p("p1"), r("r1"), p("p2"), r("r2"))))
        );
    }

    @ParameterizedTest(name = "result of ladder: {arguments}")
    @MethodSource("parseLadderResult")
    public void result(Ladder ladder, Players players, Results results, ResultOfGame expected) {
        assertThat(ladder.result(players, results)).isEqualTo(expected);
    }
}
