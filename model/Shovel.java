package model;

/** The class Shovel allows players
 * to remove plants currently on the board.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.0
  */

public class Shovel {

    /**
     * Removes a plant at the specified row and column
     * if one exists there. Called by the controller when
     * the player clicks a tile while the shovel is selected.
     *
     * @param model the GameModel to access and modify the board
     * @param row the row of the plant to remove
     * @param col the column of the plant to remove
     */
    public void removePlant(GameModel model, int row, int col) {
        Tile tile = model.getTile(row, col);
        if (tile.getPlant() != null) {
            tile.setPlant(null);
            System.out.println("Shovel removed plant at (" + row + "," + col + ")");
        } else {
            System.out.println("No plant exists on tile at (" + row + "," + col + ")");
        }
    }
}
