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
            
    @FXML private Label status;
    @FXML private Label instruction;
    @FXML private TextField key;
    @FXML private ChoiceBox method = new ChoiceBox();
    
    ObservableList<String> options = 
    FXCollections.observableArrayList(
        "Option 1",
        "Option 2",
        "Option 3"
    );
    
    public FXMLDocumentController() {
        
    }
    @FXML
    private void handleUploadedFile(ActionEvent event) {
         
        System.out.println("File submitted");
        
        FileChooser fileChooser = new FileChooser();
        //  Launches a dialogue window to the main scene
        selectedFile = fileChooser.showOpenDialog(null);
        //  Handle cases where the user aborts or the process fails
        if (selectedFile != null) {

                status.setText("File selected: " + selectedFile.getName());
                
            }
            else {
                status.setText("File selection cancelled.");
            }
    }
    
    @FXML
    private void encryptFile(ActionEvent event) {
        
        String key = "This is a secret";
        startProcess(selectedFile, key,true);
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
        
        File encryptedFile = new File("text.encrypted");
        File decryptedFile = new File("decrypted-text.txt");
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
    }
    
    // Function gets called at GUI start
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    method.setItems(options);
         
    }    
    
}
