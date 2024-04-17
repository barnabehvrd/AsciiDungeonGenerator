import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Spot extends Room {
    private String letter;

    public Spot(Room[][] matrix, int x, int y, String letter) {
        super(matrix, x, y);
        this.letter = letter;
    }

    @Override
    public boolean isConnected() {

        Set<Room> checkedRooms = new HashSet<Room>();
        checkedRooms.add(this);

        List<Room> neighborsList = this.getNeighbors();

        // Tant qu'il reste des voisins à traiter
        while (!neighborsList.isEmpty()) {
            // On retire le premier élément de la liste
            Room neighbor = neighborsList.removeFirst();

            // Si le voisin est un Spot différent de lui-même, alors, les deux Spots sont connectés
            if (neighbor instanceof Spot && neighbor != this) {

                // On va considérer que tous les spots vérifiés sont connectés
                for (Room room : checkedRooms) {
                    room.setConnected();
                }
                return true;
            }
            // Sinon, on ajoute ses voisins à la liste des voisins à traiter
            else {
                checkedRooms.add(neighbor);
                // On ajoute les voisins du voisin à la liste des voisins à traiter, si ils n'ont pas déjà été traités
                for (Room neighborNeighbor : neighbor.getNeighbors()) {
                    if (!checkedRooms.contains(neighborNeighbor)) {
                        neighborsList.add(neighborNeighbor);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.letter;
    }
}