module AnglerTManager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens project.main to javafx.fxml;
    exports project.main;

    opens project.models to javafx.fxml;
    exports project.models;

    exports project.controllers.management;
    opens project.controllers.management to javafx.fxml;

    exports project.controllers.view;
    opens project.controllers.view to javafx.fxml;
}