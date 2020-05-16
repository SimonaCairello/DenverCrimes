/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.OffenseTypePairs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<OffenseTypePairs> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	List<String> percorso = new ArrayList<>();
    	String v1 = null;
    	String v2 = null;
    	OffenseTypePairs otp = boxArco.getValue();
    	
    	if(otp==null) {
    		txtResult.appendText("Scegliere un arco!\n");
    		return;
    	}
    	
    	v1 = otp.getOffenseType1();
    	v2 = otp.getOffenseType2();
    	
    	percorso = model.getCamminoMax(v1, v2);
    	
    	for(String s : percorso)
    		txtResult.appendText(s+"\n");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String offenseCategory = boxCategoria.getValue();
    	Integer month = boxMese.getValue();
    	Set<OffenseTypePairs> edges = new HashSet<>();
    	
    	if(offenseCategory==null || month==null) {
    		txtResult.appendText("Seleziona una voce da entrambe le tendine!\n");
    		return;
    	}
    	
    	model.generateGraph(month, offenseCategory);
    	
    	edges.addAll(model.getEdgeForMeanWeight());
    	for(OffenseTypePairs otp : edges)
    		txtResult.appendText(otp.toString());
    	
    	boxArco.getItems().setAll(edges);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	Set<String> categories = model.getAllCategories();
    	Set<Integer> months = model.getAllMonths();
    	
    	boxCategoria.getItems().setAll(categories);
    	boxMese.getItems().setAll(months);
    }
}
