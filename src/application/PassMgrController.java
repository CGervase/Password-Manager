package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class PassMgrController implements Initializable {
	@FXML private Button btnAdd;
	@FXML private Button btnRemove;
	@FXML private Button btnEdit;
	@FXML private Button btnCopy;
	@FXML private Button btnReveal;
	@FXML private Button btnRevealClear;
	@FXML private Button btnPassGen;
	@FXML private Button btnPassGenClear;
	@FXML private ListView<String> lstPasswordDisplay;
	@FXML private MenuItem mnuFileAdd;
	@FXML private MenuItem mnuFileGen;
	@FXML private MenuItem mnuFileClose;
	@FXML private MenuItem mnuEditRemove;
	@FXML private MenuItem mnuEditEdit;
	@FXML private MenuItem mnuEditCopy;
	@FXML private MenuItem mnuEditReveal;
	@FXML private MenuItem mnuHelpAbout;
	@FXML private TextField txtPassReveal;
	@FXML private TextField txtPassGen;
	
	User user;
	private Scanner input;
	private File passFile = new File("src/Passwords.txt");
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		user = PassMgrLoginController.getCurUser();
		
		//read passwords
		try {
			input = new Scanner(passFile);
		} catch(Exception e) {
			e.printStackTrace();
		}
		boolean cont = true;
		boolean read = false;
		while(input.hasNextLine() && cont) {
			String line = input.nextLine();
			if (read)
				user.addPass(line, input.nextLine());
			
			if (line.equals(user.getID()))
				read = true;
			else if (line.isEmpty() && read == true)
				cont = false;
		}
		input.close();
		
		//display keys
		ObservableList<String> keys = FXCollections.observableList(user.keyList());
		lstPasswordDisplay.setItems(keys);
		
		lstPasswordDisplay.setOnMouseClicked(event -> {
			if (lstPasswordDisplay.getSelectionModel().getSelectedItem() != null) {
				btnRemove.setDisable(false);
				btnEdit.setDisable(false);
				btnCopy.setDisable(false);
				btnReveal.setDisable(false);
				mnuEditRemove.setDisable(false);
				mnuEditEdit.setDisable(false);
				mnuEditCopy.setDisable(false);
				mnuEditReveal.setDisable(false);
			}
		});
		
		//menuItems
		mnuFileAdd.setOnAction(event -> {
			btnAdd.fire();
		});
		
		mnuFileGen.setOnAction(event -> {
			btnPassGen.fire();
		});
		
		mnuFileClose.setOnAction(event -> {
			Platform.exit();
		});
		
		mnuEditRemove.setOnAction(event -> {
			btnRemove.fire();
		});
		
		mnuEditEdit.setOnAction(event -> {
			btnEdit.fire();
		});
		
		mnuEditCopy.setOnAction(event -> {
			btnCopy.fire();
		});
		
		mnuEditReveal.setOnAction(event -> {
			btnReveal.fire();
		});
		
		mnuHelpAbout.setOnAction(event -> {
			//set info dialog window
			showAlert("About", "About Password Manager", "Password Manager is a standalone desktop application "
					+ "that stores and retrieves your passwords. With the boundless growth of the Internet and "
					+ "the digital migration of various services, login information begins to pile up and sometimes "
					+ "we can't quite remember our credentials for a website we only use once a year. Rather than "
					+ "going through the tedious \"Forgot my Username/Password\" process, Password Manager allows "
					+ "hassle-free storage of passwords.\n\nThe source code for this project can be found at "
					+ "http://github.com/CGervase/Password-Manager", false);
		});
		
		//buttons
		btnAdd.setOnAction(event -> {
			String key = inputDialog("Add Password", "Enter password description.", "Please enter a description "
					+ "for the password. (Email, Twitter, etc.)", "");
			if (!key.isEmpty()) {
				String pass = inputDialog("Add Password", "Enter password.", "Please enter the password.", "");
				if (!pass.isEmpty()) {
					user.addPass(key, pass);
					keys.add(key);
				} else {
					showAlert("Add Error", "Blank password.", "The password cannot be blank.", true);
				}
			} else {
				showAlert("Add Error", "Blank description.", "The description cannot be blank.", true);
			}
		});
		
		btnRemove.setOnAction(event -> {
			String key = lstPasswordDisplay.getSelectionModel().getSelectedItem(); 
			if (confirmAlert("Remove Password", "Confirm password removal.", "Are your sure you want to "
					+ "delete the selected password?")) {
				user.deletePass(key);
				keys.remove(key);
			}
		});
		
		btnEdit.setOnAction(event -> {
			String oldKey = lstPasswordDisplay.getSelectionModel().getSelectedItem();
			String oldPass = user.getPass(oldKey).toString();
			String newKey = inputDialog("Edit Password", "Enter new password description.", 
					"Please update the description for the password. (Email, Twitter, etc.)", oldKey);
			if (!newKey.isEmpty()) {
				String newPass = inputDialog("Edit Password", "Enter new password.", "Please enter the new password.", oldPass);
				if (!newPass.isEmpty()) {
					user.deletePass(oldKey);
					keys.remove(oldKey);
					user.addPass(newKey, newPass);
					keys.add(newKey);
				} else {
					showAlert("Edit Error", "Blank password.", "The password cannot be blank.", true);
				}
			} else {
				showAlert("Edit Error", "Blank description.", "The description cannot be blank.", true);
			}
		});
		
		btnCopy.setOnAction(event -> {
			String key = lstPasswordDisplay.getSelectionModel().getSelectedItem();
			String pass = user.getPass(key).toString();
			copyToClipboard(pass);
		});
		
		btnReveal.setOnAction(event -> {
			String key = lstPasswordDisplay.getSelectionModel().getSelectedItem();
			String pass = user.getPass(key).toString();
			txtPassReveal.setText(pass);
		});
		
		btnRevealClear.setOnAction(event -> {
			txtPassReveal.clear();
		});
		
		btnPassGen.setOnAction(event -> {
			txtPassGen.setText(PasswordGenerator.genPassword(true, true, true, true));
		});
		
		btnPassGenClear.setOnAction(event -> {
			txtPassGen.clear();
		});
	}
	
	private static void showAlert(String title, String header, String content, boolean error) {
		Alert alert = new Alert(AlertType.NONE);
		if (error)
			alert.setAlertType(AlertType.ERROR);
		else
			alert.setAlertType(AlertType.INFORMATION);
		
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private static String inputDialog(String title, String header, String content, String value) {
		TextInputDialog dialog = new TextInputDialog(value);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
			return result.get();
		return value;
	}
	
	private boolean confirmAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		}
		//else
		return false;
	}
	
	/*private static void noPassSelected() {
		showAlert("No Password Selected", "No password selected.", "No password is selected."
				+ " Please select a password.", true);
	}*/
	
	private static void copyToClipboard(String pass) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Clipboard cb = tk.getSystemClipboard();
		StringSelection sel = new StringSelection(pass);
		cb.setContents(sel, null);
	}
}