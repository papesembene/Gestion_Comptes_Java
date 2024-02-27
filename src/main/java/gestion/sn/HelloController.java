package gestion.sn;

import gestion.sn.model.Bd;
import gestion.sn.model.Compte;
import gestion.sn.repository.CompteRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    Bd bd;
    Connection con = null;
    @FXML
    private TextField cmotant;




    @FXML
    private TableColumn<Compte, String> coltypem;
    @FXML
    private TableColumn<Compte, Integer> colid;
    @FXML
    private ComboBox<String> combo;
    @FXML
    private TextField ctaux;
    @FXML
    private TextField cnom;
    @FXML
    private TableColumn<Compte, String> colnom;
    @FXML
    private TableColumn<Compte, Integer> colamount;
    @FXML
    private TableView<Compte> table;
    @FXML
    private Button ajouter;
    @FXML
    private Button delete;
    @FXML
    private Button annuler;
    @FXML
    private TextField searchFild;
    @FXML
    private TableColumn<Compte, Integer> coltaux;

    @FXML
    void btnAjouter(ActionEvent event) {

        String sql = "insert into compte(fullname ,montant ,typecompte ,taux) values(?,?,?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, cnom.getText());
            pst.setInt(2, Integer.parseInt(cmotant.getText()));
            pst.setString(3, combo.getValue());
            pst.setInt(4, Integer.parseInt(ctaux.getText()));
            pst.execute();
            System.out.println("Ajouté");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
       afficher();
        annuler(event);
    }

    @FXML
    void btndelete(ActionEvent event) {
        int id=this.table.getSelectionModel().getSelectedItem().getId();
        try {
            String sql=" DELETE FROM compte WHERE id = ?";
            PreparedStatement statement=con.prepareStatement(sql);
            statement.setInt(1,id);
            statement.executeUpdate();
            afficher();
            annuler(event);

        }catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    @FXML
    void updatecompte(ActionEvent event) {

        int id= table.getSelectionModel().getSelectedItem().getId();
        String sql = "update compte  set fullname=?,montant=?,typecompte=? , taux=? where id=?";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, cnom.getText());
            pst.setInt(2, Integer.parseInt(cmotant.getText()));
            pst.setString(3, combo.getValue());
            pst.setInt(4,Integer.parseInt(ctaux.getText()));
            pst.setInt(5, id);
            pst.execute();
            System.out.println("modifier");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        afficher();
        annuler(event);
    }
    @FXML
    void editcompte(MouseEvent event) {
        if(event.getClickCount()== 2){
            Compte compte = table.getSelectionModel().getSelectedItem();
            cmotant.setText(Integer.toString(compte.getMontant()));
            cnom.setText(compte.getNom());
            combo.setValue(compte.getType());
            ctaux.setText(Integer.toString(compte.getTaux()));
    }
    }
    public void afficher()
    {
        CompteRepository compt=new CompteRepository();
        ObservableList<Compte> list = compt.getAllCompte();
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colamount.setCellValueFactory(new PropertyValueFactory<>("montant"));
        coltypem.setCellValueFactory(new PropertyValueFactory<>("type"));
        coltaux.setCellValueFactory(new PropertyValueFactory<>("taux"));
        table.setItems(list);
    }


    @FXML
    void annuler(ActionEvent event) {
         cnom.setText("");
         cmotant.setText("");
         ctaux.setText("");
         combo.setValue("");
         searchFild.setText("");
    }

    @FXML
    void btnSearch(ActionEvent event) {
        String sql = "SELECT * FROM compte WHERE fullname LIKE ? OR montant LIKE ? OR typecompte LIKE ? OR taux LIKE ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            String searchValue = "%" + searchFild.getText() + "%";
            statement.setString(1, searchValue);
            statement.setString(2, searchValue);
            statement.setString(3, searchValue);
            statement.setString(4, searchValue);
            ResultSet rs = statement.executeQuery();

            ObservableList<Compte> list = FXCollections.observableArrayList();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt(1));
                compte.setNom(rs.getString(2));
                compte.setMontant(rs.getInt(3));
                compte.setType(rs.getString(4));
                compte.setTaux(rs.getInt(5));
                list.add(compte);
            }

            colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colamount.setCellValueFactory(new PropertyValueFactory<>("montant"));
            coltypem.setCellValueFactory(new PropertyValueFactory<>("type"));
            coltaux.setCellValueFactory(new PropertyValueFactory<>("taux"));
            table.setItems(list);
        } catch (SQLException ex) {
            // Gérer l'exception de manière appropriée (afficher un message d'erreur, journalisation, etc.)
            ex.printStackTrace();
        }
    }

    @FXML
    private Label label;
    @FXML
    void comptetype(ActionEvent event) {
        String selectedType = combo.getSelectionModel().getSelectedItem();
        if ("Epargne".equals(selectedType)) {
            label.setText("Taux");
        } else if("Courant".equals(selectedType)) {
            label.setText("Decouverte");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bd = new Bd();
        con = bd.getConnection();
        afficher();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Epargne", "Courant", "Bloqué");
        combo.setItems(list);


    }

}