/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.crypto.Cipher;
import javafx.scene.control.ChoiceBox;

/**
 * This class controls the application layer (model) sitting behind the GUI
 * @author thomasbale
 */
public class FXMLDocumentController implements Initializable {
    //  Selected file needs to be visible to whole of controller
    File selectedFile;
    //  Holds session state data
    State session = new State();
            
    @FXML private Label status;
    @FXML private Label instruction;
    @FXML private Label key;
    @FXML private TextField password;
    @FXML private ChoiceBox method = new ChoiceBox();
    
    ObservableList<String> available_methods = 
    FXCollections.observableArrayList(
        "Low: DES (56)",
        "Medium: AES (128)",
        "High: DESede (168)",
        "Extreme: RSA (1024)"
     );
    
    public FXMLDocumentController() {
        
    }
    @FXML
    private void handleUploadedFile(ActionEvent event) {
         
        System.out.println("File requested...");
        
        FileChooser fileChooser = new FileChooser();
        //  Launches a dialogue window to the main scene
        selectedFile = fileChooser.showOpenDialog(null);
        //  Handle cases where the user aborts or the process fails
        if (selectedFile != null) {
            System.out.println("File requested and selected");

                status.setText("File selected: " + selectedFile.getName());
                
            }
            else {
            System.out.println("File requested and cancelled");
                status.setText("File selection cancelled.");
            }
    }
    
    @FXML
    private void encryptFile(ActionEvent event) {
        
        String crypt = "This is a secret";
        key.setText(crypt);
        startProcess(selectedFile, crypt,true);
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch(InterruptedException e){}
        status.setText("Encryption complete " );
    }
    
    @FXML
    private void decryptFile(ActionEvent event) {
        
        String key1 = "This is a secret";
        startProcess(selectedFile, key1,false);
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch(InterruptedException e){};
        status.setText("Decryption complete " );
        status.setText(key.getText());
    }
    
    private void startProcess(File input, String key, boolean mode) {
        
        System.out.println(input.toPath().toString());
        
        File encryptedFile = new File(input.toPath().toString() + ".enc") ;
        File decryptedFile = new File(input.toPath().toString() + ".dec");
        comboBoxwasUpdated();

        try {
            if(mode = true){
                Crypto.fileProcessor(Cipher.ENCRYPT_MODE,key,input,encryptedFile);
            }else{
                Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
            }
          System.out.println("Success");
        }

        catch (Exception ex) {
          System.out.println(ex.getMessage());
        }     
 }
    
    //This wil update the combobox when the combobox is changed
    public void comboBoxwasUpdated(){
       this.instruction.setText(method.getValue().toString());
       session.method(method.getValue().toString());
  
    }
    
    // Function gets called at GUI start
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    // Start a new state to hold the encryption task details
    session.init();
    method.setItems(available_methods);
    //  Set a defult method
    method.getSelectionModel().selectFirst();
         
    }    
    
}
