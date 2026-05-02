package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import project.dao.TeamDAO;
import project.models.Competitor;
import java.util.ArrayList;
import java.util.List;

public class AddTeamWithMembersController {

    @FXML private TextField txtTeamName;
    @FXML private TextField txtAssociation;

    @FXML private TextField txtFirstName1;
    @FXML private TextField txtLastName1;
    @FXML private TextField txtFirstName2;
    @FXML private TextField txtLastName2;
    @FXML private TextField txtFirstName3;
    @FXML private TextField txtLastName3;

    @FXML private DatePicker dpBirthDate1;
    @FXML private DatePicker dpBirthDate2;
    @FXML private DatePicker dpBirthDate3;

    private int idTakmicenja;
    private TeamDAO teamDAO = new TeamDAO();

    public void setIdTakmicenja(int idTakmicenja) {
        this.idTakmicenja = idTakmicenja;
    }

    @FXML
    public void initialize() {
        fixDatePicker(dpBirthDate1);
        fixDatePicker(dpBirthDate2);
        fixDatePicker(dpBirthDate3);
    }

    public static void fixDatePicker(DatePicker dp) {

        dp.setConverter(new javafx.util.StringConverter<java.time.LocalDate>() {
            private final java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(java.time.LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public java.time.LocalDate fromString(String text) {
                if (text == null || text.trim().isEmpty()) return null;
                try {
                    return java.time.LocalDate.parse(text.trim(), formatter);
                } catch (Exception e) {
                    return null; // vrati null umjesto bacanja exceptiona
                }
            }
        });
    }

    @FXML
    private void handleSaveTeam() {

        String teamName = txtTeamName.getText();
        String association = txtAssociation.getText();

        if (teamName.isEmpty() || association.isEmpty() || txtFirstName1.getText().isEmpty() || txtLastName1.getText().isEmpty()) {
            showAlert("Greška", "Sva polja moraju biti popunjena!");
            return;
        }

        if (dpBirthDate1.getValue() == null || dpBirthDate2.getValue() == null || dpBirthDate3.getValue() == null) {
            showAlert("Greška", "Morate odabrati datum rođenja za svakog takmičara!");
            return;
        }

        if (txtFirstName2.getText().isEmpty() || txtLastName2.getText().isEmpty() ||
                txtFirstName3.getText().isEmpty() || txtLastName3.getText().isEmpty()) {

            showAlert("Greška", "Sva polja moraju biti popunjena!");
            return;
        }


        List<Competitor> members = new ArrayList<>();

        members.add(new Competitor(0, txtFirstName1.getText(), txtLastName1.getText(), dpBirthDate1.getValue().toString(), 0, teamName));
        members.add(new Competitor(0, txtFirstName2.getText(), txtLastName2.getText(), dpBirthDate2.getValue().toString(), 0, teamName));
        members.add(new Competitor(0, txtFirstName3.getText(), txtLastName3.getText(), dpBirthDate3.getValue().toString(), 0, teamName));

        boolean success = teamDAO.insertTeamWithMembers(teamName, association, members, idTakmicenja);

        if (success) {
            showAlert("Uspjeh", "Ekipa i takmičari su uspješno dodani!");
            clearFields();
        } else {
            showAlert("Greška", "Došlo je do greške prilikom spasavanja u bazu.");
        }
    }

    private void clearFields() {
        txtTeamName.clear();
        txtFirstName1.clear(); txtLastName1.clear();
        txtFirstName2.clear(); txtLastName2.clear();
        txtFirstName3.clear(); txtLastName3.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}