package ludo.ludomania;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LudoMain extends Application {
    static Image[] dices;
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LudoMain.class.getResource("ludomania.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);

            dices = new Image[7];
            dices[0] = new Image("file:src\\main\\resources\\ludo\\ludomania\\images\\dices\\base.png");
            dices[1] = new Image("file:src\\main\\resources\\ludo\\ludomania\\images\\dices\\dice1.png");
            dices[2] = new Image("file:src\\main\\resources\\ludo\\ludomania\\images\\dices\\dice2.png");
            dices[3] = new Image("file:src\\main\\resources\\ludo\\ludomania\\images\\dices\\dice3.png");
            dices[4] = new Image("file:src\\main\\resources\\ludo\\ludomania\\images\\dices\\dice4.png");
            dices[5] = new Image("file:src\\main\\resources\\ludo\\ludomania\\images\\dices\\dice5.png");
            dices[6] = new Image("file:src\\main\\resources\\ludo\\ludomania\\images\\dices\\dice6.png");

            System.out.println("Working Directory = " + System.getProperty("user.dir"));

            stage.setTitle("Ludomania!");
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}