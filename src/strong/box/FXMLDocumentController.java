/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;

import java.io.BufferedWriter;
import java.net.URL;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.out;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Popup;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 * This class controls the application layer (model) sitting behind the GUI
 * @author thomasbale
 */
public class FXMLDocumentController implements Initializable {
    //  Selected file needs to be visible to whole of controller
    File selectedFile;
    //  Location where user wants to save key once encrypted
    File savedkey;
    //  Holds session state data
    State session = new State();
    unitIntegrityTest secure = new unitIntegrityTest();
            
    @FXML private Label status;
    @FXML private Label instruction;
    @FXML private Label key;
    @FXML private TextField password;
    @FXML private ChoiceBox method = new ChoiceBox();
    @FXML private Popup securitywarn = new Popup();
    
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
                // Assign file to current session
                status.setText("File selected: " + selectedFile.getName());
            }
            else {
            System.out.println("File requested and cancelled");
                status.setText("File selection cancelled.");
            }
               if(secure.restrictedCryptography()){
            this.showSecuritywarning("Encrytion level limited (128). If stronger algorithms are needed (for example, AES with 256-bit keys), the JCE Unlimited Strength Jurisdiction Policy Files must be obtained and installed in the JDK/JRE.\n" +
"\n" +
"It is the user's responsibility to verify that this action is permissible under local regulations.");
        }
    }
    
    @FXML
    private void handleKeydownload(ActionEvent event) {
         
        System.out.println("Key download requested...");
        
        if(!session.isReady()){
            this.showInformation("Error", "You must encrypt something to generate a key");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        //  Launches a dialogue window to the main scene
        savedkey = fileChooser.showSaveDialog(null);
        //  Handle cases where the user aborts or the process fails
        if (savedkey != null) {
            System.out.println("KD requested and selected");
                // Assign file to current session
                //status.setText("File selected: " + selectedFile.getName());
                this.saveKey();
            }
            else {
            System.out.println("KD requested and cancelled");
                status.setText("Key download selection cancelled.");
            }
    }
    
    private void saveKey(){
        if(savedkey == null){
            this.showInformation("Error", "Cannot save to this location");
            return;
        }
        
        try{
            FileWriter target = new FileWriter(savedkey.toPath().toString() + ".txt");
            BufferedWriter output = new BufferedWriter(target);
            output.write("Encryption key:\n" + session.getKey());      
            output.close();
            
        }catch(IOException e){
            System.out.println("Exception saving key!");
            this.showSecuritywarning("Error saving cipher key! Try re-encrypting.");
        }
        
        session.reset();
        assert session.getKey() == null;
        this.initInstructions();
        this.showSecuritywarning("Key saved: " +savedkey.toPath().toString() +".txt"+ "\n All local keys now cleared and ciphers reset." + "\nKeep key safe as file lost without key!");
        return;
        }
        
    
    
    @FXML
    private void encryptFile(ActionEvent event) {
        Randompass keyr = new Randompass();
        
        if(selectedFile == null){
            this.showInformation("Error", "Please select a file");
            return;
        }
        session.reset();
        session.setKey(keyr.generateToken(session.keylen()));
        
        startProcess(selectedFile,ENCRYPT_MODE);
        try{
            TimeUnit.SECONDS.sleep(1);
            key.setText(session.getKey());
            password.setText(session.getKey());
        } catch(InterruptedException e){}
        status.setText("Encryption complete " );
    }
    
    @FXML
    private void decryptFile(ActionEvent event) {
        if(selectedFile == null){
            this.showInformation("Error", "Please select a file");
            return;
        }
        //  Always clear ciphers
        session.reset();
        session.setKey(this.getPassword());
        if(session.getKey() == null || session.getKey().equals("")){
            this.showInformation("Error", "Please enter valid decryption key");
            return;
        }
        startProcess(selectedFile,DECRYPT_MODE);
        
        try{
            TimeUnit.SECONDS.sleep(1);
            key.setText(session.getKey());
            
        } catch(InterruptedException e){};
        status.setText("Decryption complete " );
        //status.setText(key.getText());
    }
    
    private void startProcess(File input, int mode) {
        
        this.extractMethod(method.getValue().toString());
        
        System.out.println(input.toPath().toString());
        session.setInput(mode, input);
        comboBoxwasUpdated();

        try {
            if(Crypto.fileProcessor(session)){
                System.out.println("Success");
                this.showInformation(session.direction() + " complete\n", "Output: " + selectedFile.getName()+ " " + session.direction() + "\nLevel: " + method.getValue().toString()+ " \nKey: " + session.getKey());
                instruction.setText("Completed " + session.direction() +" of " + selectedFile.getName());
                password.setText("");
            }
            
            else{
                System.out.println("Failure");
                this.showInformation(session.direction() + " complete", "failure");
                instruction.setText("Unable to complete " + session.direction() +"of " + selectedFile.getName());
                password.setText("");
            }
            }
         
        catch (Exception ex) {
          System.out.println(ex.getMessage());
        }     
 }
    @FXML
    private void Reset(ActionEvent event){
        session.reset();
        assert session.getKey() == null;
        this.initInstructions();
        this.showSecuritywarning("All keys cleared and ciphers reset");
        password.setText("");
        return;
    }

    
    private void extractMethod(String method){
      switch(method){
      case  "Low: DES (56)" : session.method("DES");break;
      case  "Medium: AES (128)" : session.method("AES");break;
      case  "High: DESede (168)" : session.method("DESede");break;
      case  "Extreme: RSA (1024)" : session.method("RSA");break;
      default : session.method("AES");
    }
    }
    
    private String getPassword(){
        return password.getText();
    }
    
    //This wil update the combobox when the combobox is changed
    public void comboBoxwasUpdated(){
      // session.method(method.getValue().toString());
  
    }
    
    private void showSecuritywarning(String message){
    //  This warns users that their JRE doesn't support full RSA encryption above 128 and that algorithms will be truncated 
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Strongbox");
    alert.setHeaderText("Security Alert");
    alert.setContentText(message);
    alert.show();
    
    }
    
    private void showInformation(String title, String message){
    //  This warns users that their JRE doesn't support full RSA encryption above 128 and that algorithms will be truncated 
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Strongbox");
    alert.setHeaderText(title);
    alert.setContentText(message);
    alert.show();
    
    }
    
    private void initInstructions(){
        status.setText("Select a file: " );
        instruction.setText("Select file and method" );
        key.setText("");
        password.setText("");
    }
    
    // Function gets called at GUI start
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    // Start a new state to hold the encryption task details
    // Perform security checks prior to launching
    if (secure.greenLight()){
        session.init();
        this.initInstructions();
        method.setItems(available_methods);
        //  Set a defult method
        method.getSelectionModel().selectFirst();
        // Warn if restricted crypto present
        
    }else{
        System.out.print("Fatal: Security exception program aborted");
        System.exit(0);
    }    
    }     
}
