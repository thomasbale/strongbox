/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author thomasbale
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    private Label label2;

    @FXML
    private void handleUploadedFile(ActionEvent event) {
        
        System.out.println("File submitted");
        
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        
        if (selectedFile != null) {

                label.setText("File selected: " + selectedFile.getName());
                startProcess(selectedFile);
            }
            else {
                label.setText("File selection cancelled.");
            }
    }
    
    @FXML
    private void encryptFile(ActionEvent event) {
        
        label.setText("Encrypting file... " );
    }
    
    @FXML
    private void decryptFile(ActionEvent event) {
        
        label.setText("Decrypting file..." );
      
    }
    
    private void startProcess(File input) {
        
  
        String key = "This is a secret";
  
        File encryptedFile = new File("text.encrypted");
        File decryptedFile = new File("decrypted-text.txt");

  try {
    Crypto.fileProcessor(Cipher.ENCRYPT_MODE,key,input,encryptedFile);
    Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
    System.out.println("Success");
  }

  catch (Exception ex) {
    System.out.println(ex.getMessage());
          ex.printStackTrace();
  
}
        
      
 }
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
