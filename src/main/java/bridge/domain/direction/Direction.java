package bridge.domain.direction;

public class Direction {

    public static final String UPWARD_DIRECTION = "U";
    public static final String DOWNWARD_DIRECTION = "D";
    private final String direction;

    public Direction(String direction) {
        validateForm(direction);
        this.direction = direction;
    }

    private void validateForm(String direction) {
        if(!direction.matches("[UD]")) {
            throw new IllegalArgumentException("[ERROR] U 혹은 D만 입력할 수 있습니다.");
        }
    }
}
