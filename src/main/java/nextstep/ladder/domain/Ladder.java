package nextstep.ladder.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import nextstep.ladder.domain.line.LineGenerateStrategy;

public class Ladder {
    private final List<Line> lines;

    private Ladder(final List<Line> lines) {
        this.lines = Collections.unmodifiableList(lines);
    }

    public static Ladder of(final PlayerCount playerCount, final Height height, LineGenerateStrategy strategy) {
        if (height == null) {
            throw new IllegalArgumentException("invalid height: cannot be null");
        }

        return new Ladder(Stream.generate(() -> Line.of(playerCount, strategy))
                .limit(height.toInt())
                .collect(Collectors.toList()));
    }

    public static Ladder of(final PlayerCount playerCount, final int height, LineGenerateStrategy strategy) {
        return Ladder.of(playerCount, Height.of(height), strategy);
    }

    public Stream<Line> stream() {
        return lines.stream();
    }

    public ResultOfGame result(Players players, Results results) {
        List<Integer> indexMap = IntStream.range(0, players.count().toInt())
                .boxed()
                .collect(Collectors.toList());

        // todo stream으론 못바꿀까?
        for (Line line : lines) {
            indexMap = line.nextPosition(indexMap);
        }

        return ResultOfGame.of(players, results, indexMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ladder ladder = (Ladder) o;
        return Objects.equals(lines, ladder.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
