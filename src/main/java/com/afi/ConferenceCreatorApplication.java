package com.afi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@SpringBootApplication
@EnableScheduling
@Configuration
public class ConferenceCreatorApplication extends Application implements WebMvcConfigurer {
//	private static Logger logger = LoggerFactory.getLogger(ConferenceCreatorApplication.class);
	private ConfigurableApplicationContext springContext;
	private VBox rootElement;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(ConferenceCreatorApplication.class);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("JavaFX.fxml"));
		loader.setControllerFactory(springContext::getBean);
		rootElement = (VBox) loader.load();
	}
	
	@Override
    public void start(Stage stage) throws Exception {
    	try
		{
			Scene scene = new Scene(rootElement, 1000, 600);
			stage.setTitle("Kreator konferencji");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
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