package jfxtras.samples.menu;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.samples.layout.CircularPaneSample1;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.menu.CornerMenu;

public class CornerMenuSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public CornerMenuSample1() {
        borderPane = new BorderPane();
        // corner menu will be created when the control panel is setup 
	}
    final private BorderPane borderPane;
	final private MenuItem facebookMenuItem = registerAction(new MenuItem("Facebook", new ImageView(new Image(this.getClass().getResourceAsStream("social_facebook_button_blue.png")))));
	final private MenuItem googleMenuItem = registerAction(new MenuItem("Google", new ImageView(new Image(this.getClass().getResourceAsStream("social_google_button_blue.png")))));
	final private MenuItem skypeMenuItem = registerAction(new MenuItem("Skype", new ImageView(new Image(this.getClass().getResourceAsStream("social_skype_button_blue.png")))));
	final private MenuItem twitterMenuItem = registerAction(new MenuItem("Twitter", new ImageView(new Image(this.getClass().getResourceAsStream("social_twitter_button_blue.png")))));
	final private MenuItem windowsMenuItem = registerAction(new MenuItem("Windows", new ImageView(new Image(this.getClass().getResourceAsStream("social_windows_button.png")))));
    private CornerMenu cornerMenu;
	
    private MenuItem registerAction(MenuItem menuItem) {
    	menuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	showPopup(borderPane, "You clicked the " + menuItem.getText() + " icon");
            }
        });
    	return menuItem;
    }
    
	/**
     *
     * @return
     */
    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    /**
     *
     * @return
     */
    @Override
    public String getSampleDescription() {
        return "Basic CornerMenu usage; CornerMenu is a quarter circle menu that can be placed in the corner of a pane.";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
    	//stackPane.getChildren().add(new Label("Some stuff that is visible on the pane"));
    	return borderPane;
    }

    @Override
    public Node getControlPanel() {
        // the result
        GridPane lGridPane = new GridPane();
        lGridPane.setVgap(2.0);
        lGridPane.setHgap(2.0);

        // setup the grid so all the labels will not grow, but the rest will
        ColumnConstraints lColumnConstraintsAlwaysGrow = new ColumnConstraints();
        lColumnConstraintsAlwaysGrow.setHgrow(Priority.ALWAYS);
        ColumnConstraints lColumnConstraintsNeverGrow = new ColumnConstraints();
        lColumnConstraintsNeverGrow.setHgrow(Priority.NEVER);
        lGridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow, lColumnConstraintsAlwaysGrow);
        int lRowIdx = 0;

        // Location
        {
            lGridPane.add(new Label("Location"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            locationChoiceBox.getSelectionModel().select(CornerMenu.Location.TOP_LEFT);
            lGridPane.add(locationChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            locationChoiceBox.valueProperty().addListener( (observable) -> {
            	createCornerMenu();
            });
        	createCornerMenu();
        }
        lRowIdx++;

        // Animation
        {
            lGridPane.add(new Label("Animation"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            lGridPane.add(animationChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            
            animationChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable) -> {
            	cornerMenu.setAnimationInterpolation( CircularPaneSample1.convertAnimationInterPolation(animationChoiceBox) );
            });
            animationChoiceBox.getSelectionModel().select(CircularPaneSample1.Animations.FromOriginWithFadeRotate.toString()); 
            cornerMenu.setAnimationInterpolation( CircularPaneSample1.convertAnimationInterPolation(animationChoiceBox) );
        }
        lRowIdx++;

        // show and hide buttons
        {
            Label lLabel = new Label("Manual actions");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));

            HBox lHBox = new HBox();
            lGridPane.add(lHBox, new GridPane.C().row(lRowIdx).col(1));
            // show
            Button lShowButton = new Button("show");
            lHBox.add(lShowButton);
            lShowButton.setOnAction((actionEvent) -> {
				cornerMenu.show();
            });
            // show
            Button lHideButton = new Button("hide");
            lHBox.add(lHideButton);
            lHideButton.setOnAction((actionEvent) -> {
				cornerMenu.hide();
            });
        }
        lRowIdx++;
        

        // autoShowAndHide
        {
            Label lLabel = new Label("Auto show and hide");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            autoShowAndHideCheckBox.setTooltip(new Tooltip("Automatically show and hide"));
            lGridPane.add(autoShowAndHideCheckBox, new GridPane.C().row(lRowIdx).col(1));
            
            // when activating auto show hide, and the menu is visible, hide it (because the mouse is not inside the pane)
            autoShowAndHideCheckBox.selectedProperty().addListener( (observable) -> {
            	if (autoShowAndHideCheckBox.selectedProperty().get() && cornerMenu.isShown()) {
            		cornerMenu.hide();
            	}
            });
        }
        lRowIdx++;
        // done
        return lGridPane;
    }
    private ChoiceBox<CornerMenu.Location> locationChoiceBox =  new ChoiceBox<CornerMenu.Location>(FXCollections.observableArrayList(CornerMenu.Location.values()));;
    private CheckBox autoShowAndHideCheckBox = new CheckBox();
    private ChoiceBox<String> animationChoiceBox = CircularPaneSample1.animationChoiceBox();
    
    private void createCornerMenu() {
    	// uninstall the current cornerMenu
    	if (cornerMenu != null) {
    		cornerMenu.autoShowAndHideProperty().unbind();
        	cornerMenu.removeFromPane();
        	cornerMenu = null;
    	}
    	
    	// create a new one
    	cornerMenu = new CornerMenu(locationChoiceBox.getValue(), borderPane, !autoShowAndHideCheckBox.selectedProperty().get());
		if (CornerMenu.Location.TOP_LEFT.equals(cornerMenu.getLocation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		}
		else if (CornerMenu.Location.TOP_RIGHT.equals(cornerMenu.getLocation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem);
		}
		else if (CornerMenu.Location.BOTTOM_RIGHT.equals(cornerMenu.getLocation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem);
		}
		else if (CornerMenu.Location.BOTTOM_LEFT.equals(cornerMenu.getLocation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem);
		}
    	cornerMenu.autoShowAndHideProperty().bind(autoShowAndHideCheckBox.selectedProperty());
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + CornerMenu.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}