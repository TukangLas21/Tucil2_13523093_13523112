module com.tukanglas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.tukanglas to javafx.fxml;
    exports com.tukanglas;
}
