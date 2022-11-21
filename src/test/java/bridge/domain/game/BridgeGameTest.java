package bridge.domain.game;

import bridge.domain.direction.Direction;
import bridge.domain.factory.BridgeFactory;
import bridge.domain.pedestrian.Pedestrian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BridgeGameTest {

    private BridgeGame game;
    private Pedestrian pedestrian;

    @BeforeEach
    void setUp() {
        int size = 5;
        BridgeFactory bridgeFactory = new BridgeFactory();
        pedestrian = new Pedestrian();
        game = BridgeGame.from(bridgeFactory.createBridge(size));
    }

    @DisplayName("move 메소드에 방향을 입력하였을 때 방향이 Pedestrian 객체의 필드에 기록되는지 확인")
    @ParameterizedTest
    @MethodSource("provideArgumentsForMoveTest")
    void move_test(List<String> inputDirections) {
        List<Direction> directions = createDirections(inputDirections);

        moveInManyDirections(game, directions);

        assertThat(pedestrian.readRecord()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())).isEqualTo(directions);
    }

    @DisplayName("retry 메소드에 Pedestrian 객체를 입력하였을 때 Pedestrian 객체의 기록을 지우는지 확인")
    @ParameterizedTest
    @MethodSource("provideArgumentsForMoveTest")
    void retry_test(List<String> inputDirections) {
        List<Direction> directions = createDirections(inputDirections);
        moveInManyDirections(game, directions);
        int expectedCount = 0;

        game.retry(pedestrian);

        assertThat(pedestrian.readRecord()
                .count()).isEqualTo(expectedCount);
    }


    private void moveInManyDirections(BridgeGame game, List<Direction> directions) {
        for (Direction direction : directions) {
            game.move(pedestrian, direction);
        }
    }

    private List<Direction> createDirections(List<String> userInputs) {
        return userInputs.stream()
                .map(Direction::new)
                .collect(Collectors.toList());
    }

    static Stream<Arguments> provideArgumentsForMoveTest() {
        return Stream.of(
                Arguments.of(List.of("U", "U", "D", "U", "D")),
                Arguments.of(List.of("U", "D", "D", "U", "U")),
                Arguments.of(List.of("U", "U", "U", "U", "U")),
                Arguments.of(List.of("D", "D", "D", "D", "D"))
        );
    }

    static Stream<Arguments> provideArgumentsForIsSuccessTest() {
        return Stream.of(
                Arguments.of(List.of("U"), false),
                Arguments.of(List.of("U", "D"), false),
                Arguments.of(List.of("U", "U", "U"), false),
                Arguments.of(List.of("U", "U", "U", "D"), false),
                Arguments.of(List.of("D", "D", "D", "D", "D"), true)
        );
    }
}