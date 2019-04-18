package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class InterestCalculator extends Application {

    // declare the visual components at class scope
    // Declare for the main user interface here - declare for the dialog further down

    Label lblCapital;
    Label lblInterestRate;
    Label lblInvestmentTerm;
    Label lblInterestType;

    TextField tfShowCapital;
    TextField tfShowInterestRate;
    TextField tfShowInvestmentTerm;

    Button btnInvestmentTerm;
    Button btnQuit;
    Button btnCalculate;

    CheckBox cbSimpleInterest;
    CheckBox cbCompoundInterest;

    TextArea mainTextArea;

    //Declare for the dialog here....

    Label lblInvstStartDate;
    Label lblInvstEndDate;

    DatePicker dpsd;
    DatePicker dped;

    Button btnCancel;
    Button btnOk;


    public InterestCalculator() {
        //TODO
    }

    @Override
    public void init() {

        // instantiate your objects/visual components here and any action events here

        lblCapital = new Label("Capital:");
        tfShowCapital = new TextField("");
        lblInterestRate = new Label("Interest Rate (%):");
        tfShowInterestRate = new TextField("");
        lblInvestmentTerm = new Label("Investment Term (years):");
        tfShowInvestmentTerm = new TextField("");
        btnInvestmentTerm = new Button("...");
        btnInvestmentTerm.setOnAction(actionEvent -> selectInvestmentTerm());
        lblInterestType = new Label("Interest Type:");
        cbSimpleInterest = new CheckBox("Simple");
        cbCompoundInterest = new CheckBox("Compound");
        btnQuit = new Button("Quit");
        btnQuit.setOnAction(actionEvent -> Platform.exit());
        btnCalculate = new Button("Calculate");
        btnCalculate.setOnAction(actionEvent -> calculate());
        mainTextArea = new TextArea();


        lblInvstStartDate = new Label("Investment start date:");
        lblInvstEndDate = new Label("Investment end date:");
        dpsd = new DatePicker();
        dped = new DatePicker();

        btnCancel = new Button("Cancel");
        btnOk = new Button("OK");
        btnOk.setId("btnOk");


    } // end of init

    // Probably a bit OTT but practice makes perfect - I'm creating a method to test if the input given by the user is correct
    //; If not, it will alert them to same.

    private boolean makeSureItsADouble(String userInput) {

        double catchMeIfYouCan = 0;

        try {
            catchMeIfYouCan = Double.parseDouble(userInput);
            return true;
        } catch (NumberFormatException ignore) {
            return false;

        }
    } // end of makeSureItsADouble

    private String rounding(double input){

        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(input);

    } // end of rounding


    // This is the method that is engaged when the user hits calculate
    private void calculate() {

        String capitalS = tfShowCapital.getText();
        String interestRateS = tfShowInterestRate.getText();
        String investmentTermS = tfShowInvestmentTerm.getText();

        if (makeSureItsADouble(capitalS) && makeSureItsADouble(interestRateS) && makeSureItsADouble(investmentTermS)) {
            double capital = Double.parseDouble(capitalS);
            double interestRate = Double.parseDouble(interestRateS);
            double investmentTerm = Double.parseDouble(investmentTermS);

            double totala = 0;
            double totalb = 0;
            double interesta = 0;
            double interestb = 0;

            if (cbSimpleInterest.isSelected() && !cbCompoundInterest.isSelected()) {
                // for when Simple interest is to be applied

                totala = capital * (1 + (interestRate / 100) * investmentTerm);
                interesta = totala - capital;

                mainTextArea.setText("Simple Interest: \n" + "Investment Term (Years): " + tfShowInvestmentTerm.getText() +
                        "\nInitial Capital: €" + rounding(capital) + "\nInterest earned: €" + rounding(interesta) + "\nFinal amount: €" + rounding(totala));


            } else if (!cbSimpleInterest.isSelected() && cbCompoundInterest.isSelected()) {
                // for when compound interest is to be applied
                // I'm assuming a monthly compound - so interest is compounded 12 times per year

                totalb = capital * (Math.pow((1 + ((interestRate / 100) / 12)), (12 * investmentTerm)));
                interestb = totalb - capital;

                mainTextArea.setText("Compound Interest: \n" + "Investment Term (Years): " + tfShowInvestmentTerm.getText() +
                        "\nInitial Capital: €" + rounding(capital) + "\nInterest earned: €" + rounding(interestb) + "\nFinal amount: €" + rounding(totalb));


            } else if (cbSimpleInterest.isSelected() && cbCompoundInterest.isSelected()) {
                // for when both are to be applied - just duplicate the code above

                totala = capital * (1 + (interestRate / 100) * investmentTerm);
                interesta = totala - capital;

                totalb = capital * (Math.pow((1 + ((interestRate / 100) / 12)), (12 * investmentTerm)));
                interestb = totalb - capital;

                mainTextArea.setText("Simple Interest: \n" + "Investment Term (Years): " + tfShowInvestmentTerm.getText() +
                        "\nInitial Capital: €" + rounding(capital) + "\nInterest earned: €" + rounding(interesta) + "\nFinal amount: €" + rounding(totala) + "\n\n" +
                        "Compound Interest: \n" + "Investment Term (Years): " + tfShowInvestmentTerm.getText() +
                        "\nInitial Capital: €" + rounding(capital) + "\nInterest earned: €" + rounding(interestb) + "\nFinal amount: €" + rounding(totalb));

            } else {
                Alert noInterest = new Alert(Alert.AlertType.ERROR);
                noInterest.setTitle("Is This Of Interest To You?");
                noInterest.setContentText("Interestingly, you appear to have little interest in the type of interest. Why save if not for the interest? Please select an interest type.");
                noInterest.showAndWait();
            }

        } else {
            Alert noValue = new Alert(Alert.AlertType.ERROR);
            noValue.setTitle("Invalid Entry");
            if (!makeSureItsADouble(capitalS)){
                noValue.setContentText("You have entered an incorrect initial capital value.");
                noValue.showAndWait();
            } else if (!makeSureItsADouble(interestRateS)){
                noValue.setContentText("You have entered an incorrect interest rate value.");
                noValue.showAndWait();
            } else if (!makeSureItsADouble(investmentTermS)){
                noValue.setContentText("You have entered an incorrect investment term value.");
                noValue.showAndWait();
            }


        }



    }


    // This is the dialogue that pops up when the investment term button is pressed.

    private void selectInvestmentTerm(){

        // Ok, so we're going to have to set up a whole new stage, scene etc here.

        Stage secondaryStage = new Stage();

        // Set the title:

        secondaryStage.setTitle("Select Investment term:");

        //Set the width and height:

        secondaryStage.setWidth(400);
        secondaryStage.setHeight(200);
        secondaryStage.setResizable(false);

        // Create a layout

        GridPane gp2 = new GridPane();
        gp2.setGridLinesVisible(false); // I want to be able to the gridlines whilst I get things in order
        gp2.setAlignment(Pos.TOP_LEFT);


        // Setting the current date for the date pickers

        dpsd.setValue(LocalDate.now());
        dped.setValue(LocalDate.now());

        // Set the margins around the nodes and add the labels, buttons nodes  etc to the gridpane;

        Insets insetA = new Insets(10, 5, 10, 5); // I'll use a defined inset on each of the components below

        gp2.setMargin(lblInvstStartDate, insetA);
        gp2.setPadding(insetA);
        gp2.add(lblInvstStartDate, 0, 0 );
        gp2.setMargin(dpsd, insetA);
        gp2.add(dpsd, 1, 0);
        gp2.setMargin(lblInvstEndDate, insetA);
        gp2.add(lblInvstEndDate, 0,1);
        gp2.setMargin(dped, insetA);
        gp2.add(dped, 1,1);

        HBox hb2 = new HBox();
        hb2.setSpacing(3);
        hb2.setAlignment(Pos.CENTER);
        btnOk.setPrefWidth(80);
        btnCancel.setPrefWidth(80);
        hb2.getChildren().setAll(btnCancel, btnOk);

        gp2.setMargin(hb2, new Insets(15,15,15,15));
        gp2.add(hb2, 1,2);

        btnOk.setOnAction(actionEvent -> {
                    fillTheInvestmentTerm();
                    secondaryStage.close();
        });
        btnCancel.setOnAction(actionEvent -> secondaryStage.close());

        // Create a scene

        Scene s2 = new Scene(gp2);


        // Add the scene to the stage - set the scene

        secondaryStage.setScene(s2);

        // Finally, showtime

        secondaryStage.show();




    } // end of selectInvestmentTerm


    private void fillTheInvestmentTerm(){

        LocalDate start = dpsd.getValue();
        LocalDate end = dped.getValue();

        if (end.isAfter(start)){
            String years = Long.toString(ChronoUnit.YEARS.between(start,end));
            tfShowInvestmentTerm.setText(years);
        } else {
            Alert timeIsLoopy = new Alert(Alert.AlertType.ERROR);
            timeIsLoopy.setTitle("Chronology Error!");
            timeIsLoopy.setContentText("Your end date is the same as, or pre-dates, your start date. We assume a linear time dimension in this universe. Please correct.");
            timeIsLoopy.showAndWait();
        }


    }


    @Override
    public void start(Stage primaryStage) throws Exception{


        //Set the title:
        primaryStage.setTitle("Interest Calculator v.1.0");

        //Set the width and height

        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.setResizable(false);

        // Create a layout

        VBox vb = new VBox();
        vb.setSpacing(10);
        vb.setPadding(new Insets(30, 10, 20, 10));


        // I want to create a number of HBox's to put into the VBox - this is probably overkill but I tried using a
        // gridpane and wasn't happy with the degree of control I had over specific nodes - this way I have a greater degree of control

        HBox Hb1 = new HBox();
        Hb1.setAlignment(Pos.CENTER_LEFT);
        Hb1.getChildren().addAll(lblCapital, tfShowCapital);
        Hb1.setMargin(tfShowCapital, new Insets(0,20, 0, 0 ));
        lblCapital.setPrefWidth(280);
        tfShowCapital.setPrefWidth(280);
        lblCapital.setPadding(new Insets(0, 0, 0, 100));


        HBox Hb2 = new HBox();
        Hb2.setAlignment(Pos.CENTER_LEFT);
        Hb2.getChildren().addAll(lblInterestRate, tfShowInterestRate);
        Hb2.setMargin(tfShowInterestRate, new Insets(0,20,0,0));
        lblInterestRate.setPrefWidth(280);
        tfShowInterestRate.setPrefWidth(280);
        lblInterestRate.setPadding(new Insets(0,0,0,100));


        HBox Hb3 = new HBox();
        Hb3.setAlignment(Pos.CENTER_LEFT);
        Hb3.getChildren().addAll(lblInvestmentTerm, tfShowInvestmentTerm, btnInvestmentTerm);
        Hb3.setMargin(btnInvestmentTerm, new Insets(0,20,0,0));
        Hb3.setMargin(tfShowInvestmentTerm, new Insets(0,20,0,0));
        lblInvestmentTerm.setPrefWidth(280);
        tfShowInvestmentTerm.setPrefWidth(220);
        btnInvestmentTerm.setPrefWidth(40);
        lblInvestmentTerm.setPadding(new Insets(0,0,0,100));


        HBox Hb4 = new HBox();
        Hb4.setAlignment(Pos.CENTER_LEFT);
        Hb4.getChildren().addAll(lblInterestType, cbSimpleInterest, cbCompoundInterest);
        lblInterestType.setPrefWidth(280);
        lblInterestType.setPadding(new Insets(0,0,0,100));
        Hb4.setMargin(cbSimpleInterest, new Insets(0,20,0,0));


        HBox Hb5 = new HBox();
        mainTextArea.setPrefWidth(580);
        mainTextArea.setPrefHeight(160);
        Hb5.getChildren().addAll(mainTextArea);


        HBox Hb6 = new HBox();
        Hb6.setAlignment(Pos.CENTER_RIGHT);
        Hb6.getChildren().addAll(btnQuit, btnCalculate);
        Hb6.setMargin(btnCalculate, new Insets(0, 15, 0, 15));
        btnQuit.setPrefWidth(90);
        btnCalculate.setPrefWidth(90);



        vb.getChildren().addAll(Hb1, Hb2, Hb3, Hb4, Hb5, Hb6);
        vb.setMargin(Hb4, new Insets(5,0,10,0)); // I just want to make the simple and compund interest more seperate from the main text area.


        // Create a scene and add the layout to the scene

        Scene s = new Scene(vb);

        // Add the scene to the stage

        primaryStage.setScene(s);

        // Show the stage

        primaryStage.show();


    } // end of start

    @Override
    public void stop(){

    } // end of stop


    public static void main(String[] args) {
        launch(args);
    } // end of main
}
