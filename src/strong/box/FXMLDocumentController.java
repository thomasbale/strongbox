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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Popup;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

//  Responsible for the user interface and FXML tags; makes calls of model
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
    // user tries to upload a file in a session
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
            //  There is a restriction on the complexity of the cipher based on available libraries
               if(secure.restrictedCryptography()){
            this.showSecuritywarning("Encrytion level limited (128). If stronger algorithms are needed (for example, AES with 256-bit keys), the JCE Unlimited Strength Jurisdiction Policy Files must be obtained and installed in the JDK/JRE.\n" +
"\n" +
"It is the user's responsibility to verify that this action is permissible under local regulations.");
        }
    }
    //  user tries to download their key(once generated)
    @FXML
    private void handleKeydownload(ActionEvent event) {
        System.out.println("Key download requested...");
        //  refuse if not ready
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
                // Initiate the download
                this.saveKey();
            }
            else {
            System.out.println("KD requested and cancelled");
                status.setText("Key download selection cancelled.");
            }
    }

    private void saveKey(){
        if(savedkey == null){
          // Tell the user there has been an issue saving
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
        //  Always reset after download
        session.reset();
        // Check reset has worked on key (security measure)
        assert session.getKey() == null;
        this.initInstructions();
        this.showSecuritywarning("Key saved: " +savedkey.toPath().toString() +".txt"+ "\n All local keys now cleared and ciphers reset." + "\nKeep key safe as file lost without key!");
        return;
        }
    //  user wants to encrypt
    @FXML
    private void encryptFile(ActionEvent event) {
        // Container for a new key /secret
        Randompass keyr = new Randompass();
        //  If no file complain
        if(selectedFile == null){
            this.showInformation("Error", "Please select a file");
            return;
        }
        // Perform additional reset for security
        session.reset();
        //  Generate key
        session.setKey(keyr.generateToken(session.keylen()));
        //  Start
        startProcess(selectedFile,ENCRYPT_MODE);
        //  Wait for completion
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
        // Perform additional reset for security
        session.reset();
        session.setKey(this.getPassword());
        //  Complain if no key entered
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
    }
    // To launch an encryption event
    private void startProcess(File input, int mode) {
      //  Which method has a user chosen?
      this.extractMethod(method.getValue().toString());
      System.out.println(input.toPath().toString());
      session.setInput(mode, input);

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
    //  Button to clear the session at a user request
    @FXML
    private void Reset(ActionEvent event){
        session.reset();
        assert session.getKey() == null;
        this.initInstructions();
        this.showSecuritywarning("All keys cleared and ciphers reset");
        password.setText("");
        return;
    }
    //  Converts user selection into standardised API cipher method
    private void extractMethod(String method){
      switch(method){
      case  "Low: DES (56)" : session.method("DES");break;
      case  "Medium: AES (128)" : session.method("AES");break;
      case  "High: DESede (168)" : session.method("DESede");break;
      case  "Extreme: RSA (1024)" : session.method("RSA");break;
      default : session.method("AES");
    }
    }
    // Gets the password from the user interface
    private String getPassword(){
        return password.getText();
    }
    //This wil update the combobox when the combobox is changed
    public void comboBoxwasUpdated(){
      // session.method(method.getValue().toString());
    }
    //  Warns user if their system is limited
    private void showSecuritywarning(String message){
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Strongbox");
    alert.setHeaderText("Security Alert");
    alert.setContentText(message);
    alert.show();
    }
    // Helper
    private void showInformation(String title, String message){
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Strongbox");
    alert.setHeaderText(title);
    alert.setContentText(message);
    alert.show();
    }
    // Helper
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
      // With no green light the application quits
        System.out.print("Fatal: Security exception program aborted");
        System.exit(0);
    }
    }
}
