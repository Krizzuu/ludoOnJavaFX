package ludo.ludomania;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;


public class Player {
    public Text label;
    public String name;
    public int pieceStart;
    public checkerState states[] = new checkerState[4];
    public int[] positions = new int[4];
    boolean won = false;
    public Integer piecesFinished = 0;
    public ImageView[] checkers = new ImageView[4];
    public boolean[] moving = new boolean[]{false, false, false, false};
    public Player(String name, int pieceStart) {
        this.name = name;
        this.pieceStart = pieceStart;
        for(int i = 0; i < 4; i++) {
            states[i] = checkerState.Dead;
        }
    }

    public boolean anyInGame() {
        boolean any = false;
        for(int i = 0; i < 4; i++) {
            if(states[i] == checkerState.Playing)
                return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Integer getPiecesFinished() {
        return piecesFinished;
    }
}
