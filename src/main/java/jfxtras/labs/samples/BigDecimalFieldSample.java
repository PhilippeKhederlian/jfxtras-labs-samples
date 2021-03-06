package jfxtras.labs.samples;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBoxBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.labs.scene.control.BigDecimalFieldBuilder;
import jfxtras.scene.control.CalendarTextField;

/**
 * Demo for the {@link BigDecimalField} control.
 *
 * @author Thomas Bolz
 */
public class BigDecimalFieldSample extends JFXtrasLabsSampleBase {

    /**
     *
     */
    public BigDecimalFieldSample() {
        bigDecimalField = new BigDecimalField();
    }

    final BigDecimalField bigDecimalField;

    /**
     * @return
     */
    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    /**
     * @return
     */
    @Override
    public String getSampleDescription() {
        return "Basic BigDecimalField usage";
    }

    /**
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
        javafx.scene.layout.GridPane root = new javafx.scene.layout.GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        final BigDecimalField defaultSpinner = new BigDecimalField();
        final BigDecimalField decimalFormat = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.05"), new DecimalFormat("#,##0.00"));
        final BigDecimalField percent = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getPercentInstance());
        final BigDecimalField localizedCurrency = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getCurrencyInstance(Locale.UK));
        final BigDecimalField promptText = new BigDecimalField();
        promptText.setNumber(null);
        promptText.setPromptText("Enter something");
        Label label1;
        root.addRow(1, new Label("default"), defaultSpinner, label1 = LabelBuilder.create().build());
//        label1.textProperty().bind(Bindings.convert(defaultSpinner.numberProperty()));
        root.addRow(2, new Label("custom decimal format"), decimalFormat);
        root.addRow(3, new Label("percent"), percent);
        root.addRow(4, new Label("localized currency"), localizedCurrency);
        final BigDecimalField disabledField = new BigDecimalField();
        disabledField.setDisable(true);
        root.addRow(5, new Label("disabled field"), disabledField);
        root.addRow(6, new Label("regular TextField"), new TextField("1.000,1234"));
        root.addRow(7, new Label("with promptText"), promptText);
        root.addRow(8, new Label("CalendarTextField"), new CalendarTextField());
        root.addRow(9, new Label("ComboBox"), ComboBoxBuilder
                .create()
                .editable(true)
                .build()
        );

        root.addRow(10, new Label("Field with boundaries (0,100%)"),
                BigDecimalFieldBuilder.create()
                        .number(new BigDecimal("0.1"))
                        .minValue(BigDecimal.ZERO)
                        .maxValue(BigDecimal.ONE)
                        .stepwidth(new BigDecimal("0.01"))
                        .format(DecimalFormat.getPercentInstance())
                        .build()
        );

        promptText.numberProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observableValue, BigDecimal o, BigDecimal o1) {
                System.out.println(o1);
            }
        });

        Button button = new Button("Reset fields");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                defaultSpinner.setNumber(new BigDecimal(Math.random() * 1000));
                decimalFormat.setNumber(new BigDecimal(Math.random() * 1000));
                percent.setNumber(new BigDecimal(Math.random()));
                localizedCurrency.setNumber(new BigDecimal(Math.random() * 1000));
                disabledField.setNumber(new BigDecimal(Math.random() * 1000));
                promptText.setNumber(null);
            }
        });
        root.addRow(11, new Label(), button);
        return root;
    }

    @Override
    public String getJavaDocURL() {
        return "http://jfxtras.org/doc/8.0labs/" + BigDecimalField.class.getName().replace(".", "/") + ".html";
    }


    public static void main(String[] args) {
        launch(args);
    }
}