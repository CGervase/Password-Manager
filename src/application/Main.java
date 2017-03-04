package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PassManagerLogin.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.setRoot(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void stop() throws IOException {
		User user = PassMgrLoginController.getCurUser();
		if (user == null)
			return;
		
		File passFile = new File("src/Passwords.txt");
		File temp = new File("src/temp.txt");
		temp.createNewFile();
		Scanner input = new Scanner(passFile);
		FileWriter fw = new FileWriter(temp, false);
		
		boolean write = true;
		
		while (input.hasNextLine()) {
			String line = input.nextLine();
			if (line.equals(user.getID())) {
				write = false;
				fw.write(user.getID());
				fw.write(System.getProperty("line.separator"));
				for (String key : user.keyList()) {
					fw.write(key);
					fw.write(System.getProperty("line.separator"));
					fw.write(user.getPass(key).toString());
					fw.write(System.getProperty("line.separator"));
				}
			} else if (line.isEmpty()) {
				write = true;
			}
			
			if (write) {
				fw.write(line);
				fw.write(System.getProperty("line.separator"));
			}
		}
		
		input.close();
		fw.close();
		boolean success = temp.renameTo(passFile);
		System.out.println(success);
	}
}
