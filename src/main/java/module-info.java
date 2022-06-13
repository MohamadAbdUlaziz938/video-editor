module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires javafx.swing;
    requires javacv;
    requires ffmpeg;


    opens com.example.demo3 to javafx.fxml;
    exports com.example.demo3;
}