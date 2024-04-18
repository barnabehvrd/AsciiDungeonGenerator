import java.util.ArrayList;
import java.util.List;

public class Room {
    protected Room[][] matrix;
    protected int x;
    protected int y;
    protected boolean checking;
    protected boolean hasBeenConnected;

    public Room(Room[][] matrix, int x, int y) {
        this.matrix = matrix;
        this.x = x;
        this.y = y;

        // Si la case est en cours de vérification de voisins
        this.checking = false;

        // Si la case a déjà été marquée comme "connectée" à un Spot
        this.hasBeenConnected = false;
        this.matrix[y][x] = this;
    }

    public List<Room> getNeighbors() {
        List<Room> neighbors = new ArrayList<>();

        // On récupère de manière (conan le) barbare les voisins
        if (this.x > 0) {
            neighbors.add(this.matrix[this.y][this.x - 1]);
        }
        if (this.x < this.matrix[0].length - 1) {
            neighbors.add(this.matrix[this.y][this.x + 1]);
        }
        if (this.y > 0) {
            neighbors.add(this.matrix[this.y - 1][this.x]);
        }
        if (this.y < this.matrix.length - 1) {
            neighbors.add(this.matrix[this.y + 1][this.x]);
        }

        // Et on supprime les vilains null
        List<Room> toReturn = new ArrayList<>();
        for (Room neighbor : neighbors) {
            if (neighbor != null ) {
                toReturn.add(neighbor);
            }
        }

        return toReturn;
    }

    public boolean isConnected() {

        // On récupère une liste des voisins en String.
        // Si il y a autre chose que des "o", c'est que la case est connectée à au moins un spot
        List<String> neighborsTypes = new ArrayList<>();
        for (Room neighbor : this.getNeighbors()) {
            neighborsTypes.add(neighbor.toString());
        }

        if (neighborsTypes.contains("o")) {
            return true;
        }

        else {
            this.checking = true;
            boolean connectedToSpot = false;
            for (Room neighbor : this.getNeighbors()) {
                if (!neighbor.isChecking()) {
                    neighbor.check();
                    connectedToSpot = neighbor.isConnected();
                    if (connectedToSpot) {
                        break;
                    }
                }
            }
            this.checking = false;
            return connectedToSpot;
        }
    }

    public boolean isChecking() {
        return false;
    }
    public void check() {
        this.checking = true;
    }
    public void setConnected(){
        this.hasBeenConnected = true;
    }

    public Room getNearestConnectedRoom () {
        Room nearest = null;
        int minDistance = Integer.MAX_VALUE;

        // On cherche, dans la matrice, la salle la plus proche de la salle actuelle
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if (matrix[y][x] != null && ((Room) matrix[y][x]).hasBeenConnected) {;
                    int distance = Math.abs(this.x - x) + Math.abs(this.y - y);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = matrix[y][x];
                    }
                }
            }
        }

        return nearest;
    }

    @Override
    public String toString() {
        return "o";
    }
}

