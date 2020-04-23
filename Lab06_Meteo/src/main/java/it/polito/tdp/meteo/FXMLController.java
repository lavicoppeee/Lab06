/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Model;
import it.polito.tdp.meteo.model.Rilevamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {

	private Model model;
	private ObservableList<Integer> mesi= FXCollections.observableArrayList();
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
       txtResult.setDisable(false);
    	
    	Integer meseI=boxMese.getValue();
    	
    			
    	if(meseI==null) {
    		txtResult.appendText("Devi scegliere un mese");
    		return;
    	}
    	
    	List<Rilevamento> sequenzaOttima=this.model.calcolaSottoinsiemeRilevamenti(meseI);
    	txtResult.appendText("Costo totale: "+this.model.sommaCosti(sequenzaOttima)+"\n");
    	
    	for(Rilevamento r : sequenzaOttima) {
    		txtResult.appendText(r.toString()+"\n");
    	}
    	
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	txtResult.setDisable(false);
    	
    	Integer meseI=boxMese.getValue();
    
    	if(meseI==null) {
    		txtResult.appendText("Devi scegliere un mese");
    		return;
    	}
    	
    	String rMedi="";
    	List<String> localita = this.model.getLocalita();
    	
    	for(String l: localita) {
    		rMedi+=this.model.getAllRilevamentiLocalitaMese(meseI, l);
    	}
    	
    	txtResult.appendText(rMedi.toString());
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		// TODO Auto-generated method stub
		txtResult.setDisable(true);
		
		 for (int monthCount = 1; monthCount < 13; monthCount++)
		      mesi.add(monthCount);
			 
		
		 boxMese.setItems(mesi);
		 boxMese.setValue(null);
		
		this.model=model;
	}
}

