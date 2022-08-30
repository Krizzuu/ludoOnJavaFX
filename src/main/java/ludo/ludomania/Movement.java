package ludo.ludomania;

public class Movement {

    public Movement() {

    }

    public int getDiceNumber() {
        return (int) ((Math.random() * (6)) + 1);
    }
    public int getDiceNumberMod() {
        return (int) ((Math.random() * (3)) + 1);
    }

    public int getRandomPosition() {
        return (int)(Math.random() * (47)) + 1;
    }
}
