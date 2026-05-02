package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import project.dao.JudgeDAO;
import project.dao.LakeDAO;
import project.models.Judge;
import project.models.Sector;

import java.util.ArrayList;
import java.util.List;

public class AddJudgesToCompetitionController {

    @FXML private ComboBox<Judge> comboJudge1;
    @FXML private ComboBox<Judge> comboJudge2;
    @FXML private ComboBox<Judge> comboJudge3;

    private int idTakmicenja;
    private List<Sector> sectors = new ArrayList<>();
    private JudgeDAO judgeDAO = new JudgeDAO();

    public void setData(int idTakmicenja, int idJezera) {
        this.idTakmicenja = idTakmicenja;
        this.sectors = LakeDAO.getSectorsByLake(idJezera);
        loadJudges();
    }

    @FXML
    public void initialize() {
        setupJudgeConverter(comboJudge1);
        setupJudgeConverter(comboJudge2);
        setupJudgeConverter(comboJudge3);
    }

    private void loadJudges() {
        List<Judge> judges = judgeDAO.getAllJudges();
        comboJudge1.getItems().setAll(judges);
        comboJudge2.getItems().setAll(judges);
        comboJudge3.getItems().setAll(judges);
    }

    @FXML
    private void handleSave() {
        if (comboJudge1.getValue() == null ||
                comboJudge2.getValue() == null ||
                comboJudge3.getValue() == null) {
            showAlert("Greška", "Morate odabrati sudiju za svaki sektor!");
            return;
        }

        if (sectors.size() < 3) {
            showAlert("Greška", "Jezero nema dovoljno sektora!");
            return;
        }

        // Sektor 1 → comboJudge1, Sektor 2 → comboJudge2, Sektor 3 → comboJudge3
        List<int[]> dodjelaList = new ArrayList<>();
        dodjelaList.add(new int[]{comboJudge1.getValue().getId(), sectors.get(0).getSectorId()});
        dodjelaList.add(new int[]{comboJudge2.getValue().getId(), sectors.get(1).getSectorId()});
        dodjelaList.add(new int[]{comboJudge3.getValue().getId(), sectors.get(2).getSectorId()});

        boolean success = judgeDAO.assignJudgesToSectors(idTakmicenja, dodjelaList);

        if (success) {
            showAlert("Uspjeh", "Sudije su uspješno dodijeljene!");
            ((Stage) comboJudge1.getScene().getWindow()).close();
        } else {
            showAlert("Greška", "Greška pri upisu u bazu!");
        }
    }

    private void setupJudgeConverter(ComboBox<Judge> combo) {
        combo.setConverter(new StringConverter<Judge>() {
            @Override
            public String toString(Judge j) {
                return (j == null) ? "" : j.getFirstName() + " " + j.getLastName();
            }
            @Override
            public Judge fromString(String s) { return null; }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}