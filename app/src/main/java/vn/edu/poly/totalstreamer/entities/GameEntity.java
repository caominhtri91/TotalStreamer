package vn.edu.poly.totalstreamer.entities;

/**
 * Created by nix on 11/10/16.
 */

public class GameEntity {
    private String gameBitmap;
    private String name;

    public GameEntity(String gameBitmap, String name) {
        this.gameBitmap = gameBitmap;
        this.name = name;
    }

    public String getGameBitmap() {
        return gameBitmap;
    }

    public void setGameBitmap(String gameBitmap) {
        this.gameBitmap = gameBitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
