import java.util.Random;

public class DungeonGenerator {
    private static final int WIDTH = 35;         // Taille de la matrice
    private static final int HEIGHT = 12;        // Taille de la matrice
    private static final int RANDOMITY = 5;     // Nombre de salles placées aléatoirement sur la avant toute vérification (Ne semble pas etre très utile à part pour ralentir le code).
                                                 // Ne dois pas dépaser WIDTH * HEIGHT - 2
    private static final boolean ARCHAIC = true; // Si true, les salles seront connectées
                                                 // si false, les salles non connectées seront supprimées du graph

    private static final int MIN_DISTANCE = 10;   // Distance minimale entre A et B (En distance de Manhattan)
                                                  // Peut être très long à placer si la distance est très grande voire impossible si un "Spot" apparait au milieu

    // Si on veut un donjon classique, simple et peu piegeux, il est conseillé de mettre MIN_DISTANCE et RANDOMITY assez bas.
    // Si on veut un donjon plus complexe, avec des salles plus nombreuses et plus éloignées, il est conseillé de mettre MIN_DISTANCE et RANDOMITY assez haut.

    // Le code peut prendre quelques secondes sur les grandes tailles de matrice, car il est de complexité de O(n et quelques).


    private static Room[][] matrix = new Room[HEIGHT][WIDTH];
    private Random random = new Random();

    public static void main(String[] args) {
        DungeonGenerator generator = new DungeonGenerator();
        System.out.println("Vérifications préalables");
        generator.check();
        System.out.println("Génération de la carte");
        generator.generate();
        System.out.println("Carte générée, amélioration de la lisibilité");
        generator.prettify();
        System.out.println("Génération Terminée :");
        printMatrix(matrix);
    }

    private void check() {
        // On vérifie que les valeurs de RANDOMITY et MIN_DISTANCE sont correctes
        if (RANDOMITY >= WIDTH * HEIGHT - 2) {
            throw new IllegalArgumentException("RANDOMITY doit être inférieur à WIDTH * HEIGHT - 2 >:(");
        }
        if (MIN_DISTANCE >= Math.max(WIDTH, HEIGHT)) {
            throw new IllegalArgumentException("MIN_DISTANCE doit être inférieur à la taille minimale de la matrice >:(");
        }

        if (WIDTH < 2 || HEIGHT < 2) {
            throw new IllegalArgumentException("La matrice doit être de taille minimale 2x2 >:(");
        }
    }

    private void prettify() {
        // Remove "alone" rooms
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (matrix[y][x] != null && !(matrix[y][x] instanceof Spot)) {
                    if (!matrix[y][x].hasBeenConnected) {
                        if (this.ARCHAIC) {
                            connectRoomToRoom(matrix[y][x], matrix[y][x].getNearestConnectedRoom());
                        } else {
                            matrix[y][x] = null;
                        }
                    }
                }
            }
        }

        // We are now adding - and | between rooms to make the map more readable.
        // This means adding new lines and columns to the matrix, between each line and column.
        Room[][] newMatrix = new Room[HEIGHT * 2 - 1][WIDTH * 2 - 1];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                newMatrix[y * 2][x * 2] = matrix[y][x];

                if (matrix[y][x] != null) {
                    if (y > 0 && matrix[y - 1][x] != null) {
                        newMatrix[y * 2 - 1][x * 2] = new Door(newMatrix, x * 2, y * 2 - 1, "|");
                    }
                    if (x > 0 && matrix[y][x - 1] != null) {
                        newMatrix[y * 2][x * 2 - 1] = new Door(newMatrix, x * 2 - 1, y * 2, "-");
                    }
                }
            }
        }
        matrix = newMatrix;
    }

    public void connectRoomToRoom(Room toConnect, Room connectedRoom) {
        // On ajoute des salles entre les salles pour qu'elles soient connectées

        int x = toConnect.x;
        int y = toConnect.y;
        int x2 = connectedRoom.x;
        int y2 = connectedRoom.y;

        while (x != x2 || y != y2) {
            if (x != x2) {
                if (x < x2) {
                    x++;
                } else {
                    x--;
                }
            } else {
                if (y < y2) {
                    y++;
                } else {
                    y--;
                }
            }
            if (matrix[y][x] == null) {
                new Room(matrix, x, y);
            }
        }

    }

    public void generate() {
        // Place randomly A and B
        int x_a = random.nextInt(WIDTH);
        int y_a = random.nextInt(HEIGHT);
        int x_b = x_a;
        int y_b = y_a;

        // In a While loop, so we make sure that A and B are not the same

        System.out.println("Placement des points A et B");
        while ( (x_b == x_a && y_b == y_a)
                || Math.abs(x_b - x_a) < MIN_DISTANCE
                || Math.abs(y_b - y_a) < MIN_DISTANCE
        ) {
            x_a = random.nextInt(WIDTH);
            y_a = random.nextInt(HEIGHT);

            x_b = random.nextInt(WIDTH);
            y_b = random.nextInt(HEIGHT);
        }

        // Place A and B in the matrix

        Spot A = new Spot(matrix, x_a, y_a, "A");
        Spot B = new Spot(matrix, x_b, y_b, "B");

        // Placing random rooms
        System.out.println("Placement des "+ RANDOMITY +" salles aléatoires");
        for (int i = 0; i < RANDOMITY; i++) {
            int x_r = x_a;
            int y_r = y_a;

            // In order to make sure that the room is not placed on an existing room
            while (!(matrix[y_r][x_r] == null)) {
                x_r = random.nextInt(WIDTH);
                y_r = random.nextInt(HEIGHT);
            }

            new Room(matrix, x_r, y_r);
        }

        System.out.println("Points A et B placés. Points aléatoires placés.");
        System.out.println("Début de la connexion des points A et B.");
        // While A and B are not connected, place rooms
        while (!A.isConnected()) {
            int x_r = x_a;
            int y_r = y_a;

            // In order to make sure that the room is not placed on an existing room
            while (!(matrix[y_r][x_r] == null)) {

                x_r = random.nextInt(WIDTH);
                y_r = random.nextInt(HEIGHT);
            }

            new Room(matrix, x_r, y_r);
        }
    }

    private static void printMatrix(Room[][] matrix) {
        for (Room[] row : matrix) {
            for (Room cell : row) {
                System.out.print(cell != null ? cell.toString() : " ");
            }
            System.out.println();
        }
    }
}