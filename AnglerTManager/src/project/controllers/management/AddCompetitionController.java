package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import project.dao.CompetitionDAO;
import project.dao.FishingMethodDAO;
import project.dao.LakeDAO;
import project.models.Competition;
import project.models.FishingMethod;
import project.models.Lake;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AddCompetitionController {

    @FXML private TextField nameField;
    @FXML private DatePicker datePicker;
    @FXML private TextField locationField;
    @FXML private ComboBox<FishingMethod> methodComboBox;
    @FXML private ComboBox<Lake> lakeComboBox;

    @FXML
    public void initialize() {

        List<Lake> list = LakeDAO.getAllLakes();
        AddTeamWithMembersController.fixDatePicker(datePicker);
        lakeComboBox.getItems().setAll(list);

        lakeComboBox.setConverter(new StringConverter<Lake>() {
            @Override
            public String toString(Lake lake) {
                return (lake != null) ? lake.getName() : "";
            }

            @Override
            public Lake fromString(String string) {
                return null;
            }
        });

        FishingMethodDAO methodDao = new FishingMethodDAO();
        methodComboBox.getItems().setAll(methodDao.getAllMethods());

        methodComboBox.setConverter(new StringConverter<FishingMethod>() {
            @Override public String toString(FishingMethod m) { return (m != null) ? m.getName() : ""; }
            @Override public FishingMethod fromString(String s) { return null; }
        });
    }

    @FXML
    void saveCompetition() {
        try {

            String name = nameField.getText();
            String location = locationField.getText();
            LocalDate localDate = datePicker.getValue();


            if (name.isEmpty() || localDate == null || methodComboBox.getValue() == null || lakeComboBox.getValue() == null) {
                showAlert("Greška", "Popunite sva polja!", Alert.AlertType.ERROR);
                return;
            }

            Date sqlDate = Date.valueOf(localDate);

            Competition newComp = new Competition(
                    0,
                    name,
                    sqlDate,
                    location,
                    methodComboBox.getValue().getId(),
                    lakeComboBox.getValue().getId()
            );

            CompetitionDAO dao = new CompetitionDAO();
            dao.insertCompetition(newComp);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}