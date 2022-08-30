module ludo.ludomania {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens ludo.ludomania to javafx.fxml;
    exports ludo.ludomania;
}