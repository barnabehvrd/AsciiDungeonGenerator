public class Door extends Room{
    // This class is only used to represent the doors between rooms
    // On the map, they are represented by "-" or "|"
    String direction;

    public Door(Room[][] matrix, int x, int y, String direction) {
        super(matrix, x, y);

        this.direction = direction;
    }

    @Override
    public String toString() {
        return this.direction;
    }
}
