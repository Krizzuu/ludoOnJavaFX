package ludo.ludomania;

import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;


public class Board extends LudoMain {
    public ImageView imageBoard;
    public AnchorPane windowMain;
        @FXML AnchorPane boardMain;
        @FXML AnchorPane sidebarMain;
        @FXML AnchorPane startMenu;
        @FXML AnchorPane endGameScreen;
    private int diceNumber = -1;
    private final Player[] players = new Player[4];
    private int currentPlayer = 0;
    // Movement
    public static int currentTurn = 0; // 0 red, 1 green, 2 blue, 3 yellow
    Movement movement = new Movement();
    private boolean losFocused = false;
    private boolean eventGenerated = false;
    private int focusedChecker;
    private int eventNumber = 1;
    private final int[] throwedWithoutSix = new int[4];

    @FXML private Text redName;
    @FXML private Text greenName;
    @FXML private Text yellowName;
    @FXML private Text blueName;

    // Board
    //@FXML private AnchorPane imageBoard;
    @FXML private Button buttonThrow;

    // Scoreboard
    @FXML private TableView<Player> scoreboard;
    @FXML private TableColumn<Player, String> playerName;
    @FXML private TableColumn<Player, Integer> pieceFinish;
    @FXML private ImageView diceImage;
    @FXML private Text currentPlayerText;
    @FXML private Text textErrorMsg;
    @FXML private Button buttonLos;
    private int []finishOrder = new int[]{-1,-1,-1,-1}; // 0 red, 1 yellow, 2 green, 3 blue
    private int currentPlace = 0;

    // Player Red
    @FXML private ImageView redLeader;
    @FXML private ImageView redSprinter;
    @FXML private ImageView redSoldier0;
    @FXML private ImageView redSoldier1;

    // Player Green
    @FXML private ImageView greenLeader;
    @FXML private ImageView greenSprinter;
    @FXML private ImageView greenSoldier0;
    @FXML private ImageView greenSoldier1;

    // Player Blue
    @FXML private ImageView blueLeader;
    @FXML private ImageView blueSprinter;
    @FXML private ImageView blueSoldier0;
    @FXML private ImageView blueSoldier1;

    // Player Yellow
    @FXML private ImageView yellowLeader;
    @FXML private ImageView yellowSprinter;
    @FXML private ImageView yellowSoldier0;
    @FXML private ImageView yellowSoldier1;

    @FXML private Button Button0;
    @FXML private Button Button1;
    @FXML private Button Button2;
    @FXML private Button Button3;


    //                           1,2,3,4 cols         1,2,3,4 rows
    static int[][] START_POS = {{102, 138, 426, 462}, {102, 138, 426, 462}};
    // beg at red beginning                              v                        v         v                        v                        v         v                        v                        v         v                        v
    static int[][] GAME_POS = {{66, 102, 138, 174, 210, 246, 246, 246, 246, 246, 246, 282, 318, 318, 318, 318, 318, 318, 354, 390, 426, 462, 498, 498, 498, 464, 426, 390, 354, 318, 318, 318, 318, 318, 318, 282, 246, 246, 246, 246, 246, 246, 210, 174, 138, 102, 66, 66},
            {246, 246, 246, 246, 246, 246, 210, 174, 138, 102, 66, 66, 66, 102, 138, 174, 210, 246, 246, 246, 246, 246, 246, 282, 318, 318, 318, 318, 318, 318, 354, 390, 426, 462, 498, 498, 498, 462, 426, 390, 354, 318, 318, 318, 318, 318, 318, 282}};
    //                                 ^                        ^       ^                       ^                        ^         ^                        ^                        ^         ^
    static int[][][] WON_POS = {
            { {210, 102, 138, 174},  {282, 282, 282, 282} },
            { {354, 462, 390, 426}, {282, 282, 282, 282} },
            { {282, 282, 282, 282}, {210, 102, 138, 174} },
            { {282, 282, 282, 282}, {354, 462, 390, 426} }
        };
    static String[] PLAYER_COLORS = {"Czerwony", "Zółty", "Zielony", "Niebieski"};


    @FXML Text gameEndText;
    private final BooleanProperty gameEnded = new SimpleBooleanProperty();
    public void initialize() {
        gameEnded.addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                showEndScreen();
            }
        });

        playerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        pieceFinish.setCellValueFactory(new PropertyValueFactory<>("piecesFinished"));

        redName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        redName.setFill(Color.valueOf("#d13b3b"));

        greenName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        greenName.setFill(Color.valueOf("#1b7a16"));

        blueName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        blueName.setFill(Color.valueOf("#234799"));

        yellowName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        yellowName.setFill(Color.valueOf("#e69a20"));

    }

    @FXML Button gameRestartButton;
    @FXML
    private void handleRestartButton() {
        resetGame();
    }

    private void showEndScreen() {
        boardMain.setVisible(false);
        sidebarMain.setVisible(false);
        endGameScreen.setVisible(true);

        printWinners();
    }

    private void printWinners() {
        // TODO: wypelnienie Text na podstawie finishedOrder[], gdzie finishedOrder[0] oznacza 1 miejsce i ID koloru gracza itd.\

        // centerowanie
        gameEndText.setX(boardMain.getMaxWidth()/2);
        gameEndText.setY(boardMain.getMaxWidth()/2);
        gameEndText.minWidth(600);
        gameEndText.prefWidth(600);
        gameEndText.minHeight(600);
        gameEndText.prefHeight(600);
        gameEndText.setStyle("-fx-font: 15 verdana;");

        String winnerText = getWinnerText();
        gameEndText.setText(winnerText);
    }

    private String getWinnerText() {
        String res = "";
        res += "1.Miejsce: " + getPlayerName(finishOrder[0]);
        res += "\n";

        res += "2.Miejsce: " + getPlayerName(finishOrder[1]);
        res += "\n";

        if(finishOrder[2] != -1) {
            res += "3.Miejsce: " + getPlayerName(finishOrder[2]);
            res += "\n";
        }
        if(finishOrder[3] != -1) {
            res += "4.Miejsce: " + getPlayerName(finishOrder[3]);
            res += "\n";
        }

        return res;
    }

    private String getPlayerName(int color) {
        if(color == 0)
            return players[0].name;
        if(color == 1)
            return players[1].name;
        if(color == 2)
            return players[2].name;
        return players[3].name;
    }

    private void resetGame() {
        for(int i = 0;i<4;i++) {
            players[i] = null;
            finishOrder[i] = -1;
        }
        currentPlayer = 0;
        diceNumber = -1;
        currentPlace = 0;

        scoreboard.getItems().clear();
        clearAllTextFields();
//        clearAllCheckBoxes();

        endGameScreen.setVisible(false);
        startMenu.setVisible(true);
    }

    private void clearAllCheckBoxes() {
        yellowCheck.setSelected(false);
        blueCheck.setSelected(false);
        greenCheck.setSelected(false);
        redCheck.setSelected(false);
    }

    private void clearAllTextFields() {
        redName.setText("");
        blueName.setText("");
        greenName.setText("");
        yellowName.setText("");

//        yellowText.clear();
//        redText.clear();
//        greenText.clear();
//        blueText.clear();
    }

    @FXML Button start;

    @FXML CheckBox redCheck;
    @FXML CheckBox greenCheck;
    @FXML CheckBox yellowCheck;
    @FXML CheckBox blueCheck;

    @FXML TextField redText;
    @FXML TextField greenText;
    @FXML TextField yellowText;
    @FXML TextField blueText;


    @FXML
    private void handleGameStartButton(ActionEvent event) {
        int flag = 0;
        flag = getFlag(flag);


//      pieceStart: red 0, green 12, yellow 24, blue 36
        if(flag == 0) {
            if (redCheck.isSelected()) {
                players[0] = new Player(redText.getText(), 0);
                scoreboard.getItems().addAll(players[0]);
                redName.setText(players[0].name);
                players[0].label = redName;
                players[0].checkers[0] = redLeader;
                players[0].checkers[1] = redSprinter;
                players[0].checkers[2] = redSoldier0;
                players[0].checkers[3] = redSoldier1;
            }
            if (greenCheck.isSelected()) {
                players[2] = new Player(greenText.getText(), 12);
                scoreboard.getItems().addAll(players[2]);
                greenName.setText(players[2].name);
                players[2].label = greenName;
                players[2].checkers[0] = greenLeader;
                players[2].checkers[1] = greenSprinter;
                players[2].checkers[2] = greenSoldier0;
                players[2].checkers[3] = greenSoldier1;
            }
            if (blueCheck.isSelected()) {
                players[3] = new Player(blueText.getText(), 36);
                scoreboard.getItems().addAll(players[3]);
                blueName.setText(players[3].name);
                players[3].label = blueName;
                players[3].checkers[0] = blueLeader;
                players[3].checkers[1] = blueSprinter;
                players[3].checkers[2] = blueSoldier0;
                players[3].checkers[3] = blueSoldier1;
            }
            if (yellowCheck.isSelected()) {
                players[1] = new Player(yellowText.getText(), 24);
                scoreboard.getItems().addAll(players[1]);
                yellowName.setText(players[1].name);
                players[1].label = yellowName;
                players[1].checkers[0] = yellowLeader;
                players[1].checkers[1] = yellowSprinter;
                players[1].checkers[2] = yellowSoldier0;
                players[1].checkers[3] = yellowSoldier1;
            }

            startMenu.setVisible(false);
            boardMain.setVisible(true);
            sidebarMain.setVisible(true);
        }
    }

    private int getFlag(int flag) {
        Border border = new Border(new BorderStroke(Color.valueOf("#ff0000"), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(2,2,2,2,false,false,false,false)));

        redText.setBorder(null);
        yellowText.setBorder(null);
        greenText.setBorder(null);
        blueText.setBorder(null);
        textErrorMsg.setVisible(false);

        int howManySelected = 0;
        howManySelected += redCheck.isSelected() ? 1 : 0;
        howManySelected += yellowCheck.isSelected() ? 1 : 0;
        howManySelected += greenCheck.isSelected() ? 1 : 0;
        howManySelected += blueCheck.isSelected() ? 1 : 0;
        if(howManySelected < 2) {
            textErrorMsg.setVisible(true);
            textErrorMsg.setText("Za mało graczy !");
            return 1;
        }


        if(redCheck.isSelected() && redText.getText().isEmpty()) {
            redText.setBorder(border);
            flag = 1;
        }
        if(yellowCheck.isSelected() && yellowText.getText().isEmpty()) {
            yellowText.setBorder(border);
            flag = 1;
        }
        if(greenCheck.isSelected() && greenText.getText().isEmpty()) {
            greenText.setBorder(border);
            flag = 1;
        }
        if(blueCheck.isSelected() && blueText.getText().isEmpty()) {
            blueText.setBorder(border);
            flag = 1;
        }

        return flag;
    }


    @FXML
    private void handleDiceRollButton(ActionEvent event) {
        checkForWin();

//        for(int i = 0;i<4;i++) {
//            players[0].states[i] = checkerState.Finished;
//        }
//        for(int i = 0;i<4;i++) {
//            players[2].states[i] = checkerState.Finished;
//        }

        if(diceNumber != -1) {
            return;
        }
        diceNumber = busy ? movement.getDiceNumberMod() : movement.getDiceNumber();
        if(!busy) {
            if(diceNumber == 6) {
                throwedWithoutSix[currentPlayer] = 0;
            }
            else {
                if(throwedWithoutSix[currentPlayer] >= 4) {
                    diceNumber = 6;
                    throwedWithoutSix[currentPlayer] = 0;
                }
                else {
                    throwedWithoutSix[currentPlayer] += 1;
                }
            }
        }

        System.out.println("Dice roll button clicked " + diceNumber);
        diceImage.setImage(LudoMain.dices[diceNumber]);
        // check if current player has something to do
        if((players[currentPlayer].won || !players[currentPlayer].anyInGame() && diceNumber != 6)) {
            changeCurrentPlayer();
            diceNumber = -1;
        }
    }

    private boolean busy = false; // blokowanie pionkow innych niz Sprinter
    @FXML
    private void moveChecker(ActionEvent event) {
        System.out.println("Action from Button no. " + ((Button)event.getSource()).getText() + " CurrPlayer: " + currentPlayer);
        if (diceNumber == -1) {
            return;
        }

        int checkerId = Integer.parseInt(((Button)event.getSource()).getText());
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(1000));

        if(busy && checkerId != 1) {
            return;
        }
        if(players[currentPlayer].states[checkerId] != checkerState.Playing && diceNumber != 6) {
            return;
        }

        if(players[currentPlayer].states[checkerId] == checkerState.Dead && diceNumber == 6) {
            despawnChecker(currentPlayer, checkerId);
        }
        else if(players[currentPlayer].states[checkerId] == checkerState.Playing) {
            players[currentPlayer].positions[checkerId] += diceNumber;
            int pos = players[currentPlayer].positions[checkerId];
            if (pos < 48) {
                pos += players[currentPlayer].pieceStart;
                pos %= 48;
                checkCollision(currentPlayer, checkerId);
                int tempPos = (players[currentPlayer].positions[checkerId] + players[currentPlayer].pieceStart) % 48;
                players[currentPlayer].checkers[checkerId].relocate(GAME_POS[0][tempPos],GAME_POS[1][tempPos]);
                if(pos == tempPos) {
                    if(isOnSpecialField(pos)) {
                        losFocus(checkerId);
                        return;
                    }
                }
            }
            else {
                players[currentPlayer].states[checkerId] = checkerState.Finished;
                moveToFinished(currentPlayer, checkerId);
                players[currentPlayer].piecesFinished += 1;
                scoreboard.refresh();
                diceNumber = -1; // koniec metody
                diceImage.setImage(LudoMain.dices[0]);
                changeCurrentPlayer();
                return;
            }
            if(checkerId == 1) // Sprinter
            {
                busy = !busy;
                diceNumber = -1;
                if(busy) {
                    if(players[currentPlayer].positions[checkerId] > 48) {
                        busy = false;
                    }
                    else {
                        updateButtons();
                        diceImage.setImage(LudoMain.dices[0]);
                        return;
                    }
                }
            }
        }

        diceNumber = -1; // koniec metody
        diceImage.setImage(LudoMain.dices[0]);
        changeCurrentPlayer();
    }

    private void moveSpecificChecker(int playerId, int checkerId, int count) {
        Player temp = players[playerId];
        if(temp.states[checkerId] == checkerState.Playing) {
            temp.positions[checkerId] += count;
            int pos = temp.positions[checkerId];
            if(pos < 48) {
                pos += temp.pieceStart;
                pos %= 48;
                temp.checkers[checkerId].relocate(GAME_POS[0][pos], GAME_POS[1][pos]);
                checkCollision(playerId, checkerId);
            }
            else {
                players[playerId].states[checkerId] = checkerState.Finished;
                players[playerId].piecesFinished += 1;
                scoreboard.refresh();
                moveToFinished(playerId, checkerId);
            }
        }
        temp.moving[checkerId] = false;
    }

    private void swapPlayers() {
        int nextPlayer = currentPlayer;
        for(int i = 0; i < 3; i++) {
            if(players[(i + nextPlayer + 1) % 4] != null) {
                nextPlayer = (i + nextPlayer + 1) % 4;
                break;
            }
        }
        String temp = players[currentPlayer].name;
        players[currentPlayer].name = players[nextPlayer].name;
        players[nextPlayer].name = temp;

        players[nextPlayer].label.setText(players[nextPlayer].name);
        players[currentPlayer].label.setText(players[currentPlayer].name);
    }

    String events[] = {"Zamiana pionkami z następnym graczem", "Cofasz się o 2", "Robisz 2 kroki dalej", "Nic sie nie dzieje", "Cofasz sie na start", "Teleportacja w losowe miejsce"};

    private void performEvent() {
        switch (eventNumber) {
            case 1:
                swapPlayers();
                scoreboard.refresh();
                changeCurrentPlayer();
                break;
            case 2:
                moveSpecificChecker(currentPlayer, focusedChecker, -2);
                break;
            case 3:
                moveSpecificChecker(currentPlayer, focusedChecker, 2);
                break;
            case 4:
                break;
            case 5:
                despawnChecker(currentPlayer, focusedChecker);
                break;
            case 6:
                int newPosition = movement.getRandomPosition();
                System.out.println("TELEPORTACJA : " + newPosition);
                players[currentPlayer].positions[focusedChecker] = newPosition;
                int pos = newPosition + players[currentPlayer].pieceStart;
                pos %= 48;
                players[currentPlayer].checkers[focusedChecker].relocate(GAME_POS[0][pos], GAME_POS[1][pos]);
                checkCollision(currentPlayer, focusedChecker);
                break;
        }
    }

    @FXML
    private void handleButtonLos() {
        if(!losFocused) {
            return;
        }
        if(!eventGenerated) {
            eventNumber = movement.getDiceNumber();
            buttonLos.setText(events[eventNumber - 1]);
            eventGenerated = true;
        }
        else {
            buttonLos.setText("Los");
            losUndoFocus();
            performEvent();
            eventGenerated = false;
            diceNumber = -1; // koniec metody
            diceImage.setImage(LudoMain.dices[0]);
            changeCurrentPlayer();
            busy = false;
        }
    }
    private boolean isOnSpecialField(int pos) {
        return pos == 5 || pos == 17 || pos == 29 || pos == 41;
    }

    private void losFocus(int checkedId){
        focusedChecker = checkedId;
        Button0.setVisible(false);
        Button1.setVisible(false);
        Button2.setVisible(false);
        Button3.setVisible(false);
        buttonThrow.setVisible(false);
        losFocused = true;
    }

    private void losUndoFocus(){
        Button0.setVisible(true);
        Button1.setVisible(true);
        Button2.setVisible(true);
        Button3.setVisible(true);
        buttonThrow.setVisible(true);
        losFocused = false;
        eventGenerated = false;
    }

    private void changeCurrentPlayer() {
        for(int i = 0; i < 3; i++) {
            if(players[(i + currentPlayer + 1) % 4] != null) {
                currentPlayer = (i + currentPlayer + 1) % 4;
                break;
            }
        }
        Player temp = players[currentPlayer];
        Button0.relocate(temp.checkers[0].getLayoutX(), temp.checkers[0].getLayoutY());
        Button1.relocate(temp.checkers[1].getLayoutX(), temp.checkers[1].getLayoutY());
        Button2.relocate(temp.checkers[2].getLayoutX(), temp.checkers[2].getLayoutY());
        Button3.relocate(temp.checkers[3].getLayoutX(), temp.checkers[3].getLayoutY());
//        System.out.println("Button 0: " + temp.checkers[0].getLayoutX() + " " + temp.checkers[0].getLayoutY());
        System.out.println("Now it's time for " + currentPlayer);
        currentPlayerText.setText(PLAYER_COLORS[currentPlayer]);
    }

    private void updateButtons(){
        Player temp = players[currentPlayer];
        Button0.relocate(temp.checkers[0].getLayoutX(), temp.checkers[0].getLayoutY());
        Button1.relocate(temp.checkers[1].getLayoutX(), temp.checkers[1].getLayoutY());
        Button2.relocate(temp.checkers[2].getLayoutX(), temp.checkers[2].getLayoutY());
        Button3.relocate(temp.checkers[3].getLayoutX(), temp.checkers[3].getLayoutY());
        System.out.println("Updated current player: " + currentPlayer);
    }

    private void moveToFinished(int playerId, int checkerId) {
        Player temp = players[playerId];
        temp.checkers[checkerId].relocate(WON_POS[playerId][0][checkerId], WON_POS[playerId][1][checkerId]);
    }

    private void checkCollision(int playerId, int checkerId) {
        Player temp = players[playerId];
        temp.moving[checkerId] = true;
        // check friendly
        int otherId = -1;
        for (int i = 0; i < 4; i++) {
            if(i != checkerId && temp.states[i] == checkerState.Playing) {
                if(temp.positions[i] == temp.positions[checkerId]) {
                    otherId = i;
                    break;
                }
            }
        }
        if(otherId != -1) {
            moveSpecificChecker(playerId, checkerId, 1);
            return;
        }
        int currentCheckerPos = (temp.pieceStart + temp.positions[checkerId]) % 48;
        for(int i = 0; i < 4; i++) {
            if(players[i] != null && temp != players[i]) {
                for(int j = 0; j < 4; j++) {
                    int pos = (players[i].pieceStart + players[i].positions[j]) % 48;
                    if (pos == currentCheckerPos && players[i].states[j] == checkerState.Playing) {
                        fight(playerId, i, checkerId, j);
                        temp.moving[checkerId] = false;
                        return;
                    }
                }
            }
        }
        temp.moving[checkerId] = false;
    }

    private void despawnChecker(int playerId, int checkerId) {
        Player temp = players[playerId];
        int start = temp.pieceStart;

        temp.checkers[checkerId].relocate(GAME_POS[0][start], GAME_POS[1][start]);
        temp.states[checkerId] = checkerState.Playing;
        temp.positions[checkerId] = 0;
        checkCollision(playerId, checkerId);
    }
    private void killChecker(int playerId, int checkerId) {
        Player temp = players[playerId];

        int row = playerId == 0 || playerId == 2 ? 0 : 2;
        int col = playerId == 0 || playerId == 3 ? 0 : 2;
        int subRow = checkerId == 0 || checkerId == 1 ? 0 : 1;
        int subCol = checkerId == 0 || checkerId == 2 ? 0 : 1;
        temp.checkers[checkerId].relocate(START_POS[0][col + subCol], START_POS[1][row + subRow]);
        temp.states[checkerId] = checkerState.Dead;
        temp.positions[checkerId] = 0;
    }
    private int closestAllysPos(int playerId, int checkerId) {
        Player temp = players[playerId];
        int closestAlly = 0;
        int pos = temp.positions[checkerId];
        for(int i = 0; i < 4; i++) {
            if(i != checkerId && temp.states[i] == checkerState.Playing) {
                if(temp.positions[i] < pos && temp.positions[i] > closestAlly) {
                    closestAlly = temp.positions[i];
                }
            }
        }
        return closestAlly;
    }

    private int countPower(int playerId, int checkerId) {
        Player temp = players[playerId];
        int power = temp.positions[checkerId];
        int closestAlly = closestAllysPos(playerId, checkerId);
        if(closestAlly < power) {
            power = closestAlly;
        }

        return power;
    }

    private void fight(int playerId1, int playerId2, int checker1, int checker2) {
        if(playerId1 == playerId2) {
            return;
        }
        System.out.println("A FIGHT OCCURED ! " + playerId1 + "(" + checker1 +") vs " + playerId2 + "(" + checker2 + ")");
        if(players[playerId1].positions[checker1] == 0) {
            if(currentPlayer == playerId1) {
                despawnChecker(playerId2, checker2);
                return;
            }
            if(checker1 != 0) {
                killChecker(playerId1, checker1);
                return;
            }
        }else if(players[playerId2].positions[checker2] == 0) {
            if(checker2 != 0) {
                killChecker(playerId2, checker2);
                return;
            }
        }


        if(checker1 == 0) {
            if(checker2 != 0) {
                if (players[playerId2].positions[checker2] == 0) {
                    despawnChecker(playerId1, checker1);
                } else {
                    despawnChecker(playerId2, checker2);
                }
            }
            else {
                if(countPower(playerId1, checker1) <= countPower(playerId2, checker2)) {
                    despawnChecker(playerId2, checker2);
                }
                else {
                    despawnChecker(playerId1, checker1);
                }
            }
        } else if (checker1 == 1) {
            switch (checker2) {
                case 0, 2, 3 -> {
                    if(players[playerId1].positions[checker1] == 0) {
                        despawnChecker(playerId2, checker2);
                    } else {
                        despawnChecker(playerId1, checker1);
                    }
                }
                case 1 -> {
                    despawnChecker(playerId1, checker1);
                    despawnChecker(playerId2, checker2);
                }
                default -> {
                }
            }
        } else {
            switch (checker2) {
                case 0:
                    despawnChecker(playerId1, checker1);
                    break;
                case 1:
                    despawnChecker(playerId2, checker2);
                    break;
                case 2:
                case 3:
                    if(players[playerId1].moving[checker1]) {
                        despawnChecker(playerId2, checker2);
                    }
                    else {
                        despawnChecker(playerId1, checker1);
                    }
                    break;
                default:
                    break;
            }
        }
    }




    public BooleanProperty gameEndedProperty() {
        return gameEnded;
    }

    public final boolean isGameEnded() {
        return gameEndedProperty().get();
    }

    public final void setGameEnded(boolean gameEnded) {
        gameEndedProperty().set(gameEnded);
    }

    private int playerCount() {
        int res = 0;
        for(int i = 0;i<4;i++) {
            if(players[i]!= null)
                res++;
        }
        return res;
    }

    private boolean playerFinished(int color) { // 0 red, 1 yellow, 2 green, 3 blue
        if(players[color] == null)
            return false;
        for(int i = 0;i<4;i++) {
            if(players[color].states[i] != checkerState.Finished)
                return false;
        }
        return true;
    }

    private boolean playerInLeaderboard(int color) {
        if(players[color] == null)
            return false;
        for(int i = 0;i<4;i++) {
            if(finishOrder[i] == color)
                return true;
        }
        return false;
    }

    private void checkForWin() {
        int pNum = playerCount();
        int pFinishedCounter = 0;

        for(int i = 0;i<4;i++) {
            if(playerInLeaderboard(i)) {
                pFinishedCounter++;
                continue;
            }
            if(pFinishedCounter == pNum - 1) {
                for(int j = 0;j<4;j++) {
                    if(players[j] == null)
                        continue;
                    if(!playerInLeaderboard(j)) {
                        finishOrder[currentPlace] = j;
                        currentPlace++;
                    }
                }
                setGameEnded(true);
                return;
            }

            if(playerFinished(i) && !playerInLeaderboard(i)) {
                finishOrder[currentPlace] = i;
                currentPlace++;
                pFinishedCounter++;
            }

        }

        if(pFinishedCounter == pNum - 1)
            setGameEnded(true);
    }

}
