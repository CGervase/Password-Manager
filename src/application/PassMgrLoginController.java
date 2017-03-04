package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PassMgrLoginController implements Initializable {
	@FXML private AnchorPane apLogin;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private Button btnLogin;
	@FXML private Button btnNewUser;

	private int count = 0;
	private Scanner input;
	private Map<String, User> userMap = new TreeMap<String, User>(String.CASE_INSENSITIVE_ORDER);
	private File usersFile = new File("src/Users.txt");
	private static User curUser;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			input = new Scanner(usersFile);
		} catch(Exception e) {
			e.printStackTrace();
		}
		while(input.hasNextLine()) {
			String username = input.nextLine();
			String password = input.nextLine();
			userMap.put(username, new User(username, password));
		}
		input.close();
		
		/*txtUsername.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER)
				login();
		});*/
		
		txtPassword.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER)
				login();
		});
		
		btnLogin.setOnAction(event -> {
			login();
		});

		btnNewUser.setOnAction(event -> {
			newUser();
		});
	}

	private void login() {
		if (validate()) {
			setCurUser(userMap.get(txtUsername.getText()));
			// set new scene w/curUser
			try {
				FXMLLoader fl = new FXMLLoader(getClass().getResource("PassManagerMain.fxml"));
				Parent root = fl.load();
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
				
				//hide login window
				apLogin.getScene().getWindow().hide();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			count++;
			if (count > 4) {
				lockout();
			} else {
				showAlert("Error", "Invalid credentials", "Username or password is incorrect.", true);
			}
			txtPassword.clear();
			txtUsername.requestFocus();
			txtUsername.selectAll();
		}
	}
	
	private boolean validate() {
		String user = txtUsername.getText();
		String pass = txtPassword.getText(); 
		if (!userMap.containsKey(user))
			return false;
		else if (!pass.equals(userMap.get(user).getPass().toString()))
			return false;
		
		return true;
	}

	private void lockout() {
		btnLogin.setDisable(true);
		showAlert("Login Locked", "Exceeded number of login attempts", "Login has been disabled for 5 minutes.", true);
		final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(3), 
				new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				btnLogin.setDisable(false);
			}
		}));
		animation.setCycleCount(1);
		animation.play();
	}
	
	private void newUser() {
		try {
			String user = txtUsername.getText();
			String pass = txtPassword.getText();
			if (user.isEmpty() || pass.isEmpty())
				throw new NullPointerException();
			else if (userMap.containsKey(user))
				throw new IOException();
			
			userMap.put(user, new User(user, pass));
			try {
				FileWriter fw = new FileWriter(usersFile, true);
				fw.write(System.getProperty("line.separator"));
				fw.write(user);
				fw.write(System.getProperty("line.separator"));
				fw.write(pass);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			showAlert("Account Created", "Account successfully created", "The user account has been successfully created.", false);
		} catch (NullPointerException e) {
			//Username & Password cannot be blank
			showAlert("Blank User/Pass", "Blank username/password", "Username and password cannot be blank.", true);
		} catch (IOException e) {
			//Account Exists
			showAlert("Username Exists", "Selected username exists", "The selected username already exists.", true);
		}
	}
	
	private void setCurUser(User curUser) {
		PassMgrLoginController.curUser = curUser;
	}
	
	public static User getCurUser() {
		return curUser;
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
}
