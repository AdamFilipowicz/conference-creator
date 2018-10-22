package com.afi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SpringBootApplication
@EnableScheduling
@Configuration
public class ConferenceCreatorApplication extends Application implements WebMvcConfigurer {
//	private static Logger logger = LoggerFactory.getLogger(ConferenceCreatorApplication.class);
	private ConfigurableApplicationContext springContext;
	private VBox rootElement;
	private JavaFXController controller;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(ConferenceCreatorApplication.class);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("JavaFX.fxml"));
		loader.setControllerFactory(springContext::getBean);
		rootElement = (VBox) loader.load();
		controller = (JavaFXController)loader.getController();
	}
	
	@Override
    public void start(Stage stage) throws Exception {
    	try
		{
			Scene scene = new Scene(rootElement, 1000, 600);
			stage.setTitle("Kreator konferencji");
			stage.setScene(scene);
			scene.getStylesheets().add("styles/java-fx-styles.css");
			stage.setResizable(false);
			stage.show();
			
			stage.setOnCloseRequest(event -> {
				if(!controller.checkTableSave()) {
					event.consume();
				}
				else {
					Platform.exit();
			        System.exit(0);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
}