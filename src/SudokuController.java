import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class SudokuController {

    @FXML
    private GridPane grid;
    private final int SIZE = 9 ;
    private TextField txf [][] ;



    public void initialize() {
        txf = new TextField[SIZE][SIZE];
        for (int i =0 ; i< SIZE ; i++) {
            for (int j =0 ; j< SIZE ; j++) {
                txf[j][i] = new TextField ();
                txf[j][i].setPrefSize(grid.getPrefWidth()/SIZE, grid.getPrefHeight()/SIZE);

                if (((i / 3) + (j / 3)) % 2 == 1) {
                    txf[j][i].setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
                } else {
                    txf[j][i].setStyle("-fx-background-color: white; -fx-border-color: black;");
                }
                grid.add(txf[j][i], j, i);

                final int row =j , column =i;

                txf[j][i].setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ENTER) {
                            if (checkText(row, column) == -1) {
                                showError("Invalid input", "Please enter number between 1-9");
                                txf[row][column].setText("");
                            } else if (checkLocation(row, column) == -1) {
                                showError("Invalid input", "Value in invalid location");
                                txf[row][column].setText("");
                            }
                        }
                    }


                });
                txf[j][i].focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) {
                        handleValidation(row, column);
                    }
                });

            }
        }
    }
    private void handleValidation(int row, int column) {
        if (txf[row][column].getText().trim().isEmpty()) {
            return;}
        else if (checkText(row, column) == -1) {
            showError("Invalid input", "Please enter a number between 1-9");
            txf[row][column].setText("");
        } else if (checkLocation(row, column) == -1) {
            showError("Invalid input", "Value in invalid location");
            txf[row][column].setText("");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean eventShouldTriggerCheck(String input) {
        return input.matches("(1-9)");
    }


    private String getCellStyle(int row, int column, String textStyle) {
        String bgColor = (((column / 3) + (row / 3)) % 2 == 1) ? "lightgray" : "white";
        String textColor = "black";
        String fontWeight = "normal";

        switch (textStyle) {
            case "error":
                textColor = "#FF0000";
                break;
            case "set":
                textColor = "green";
                fontWeight = "bold";
                break;
            case "normal":
            default:
                textColor = "black";
                fontWeight = "normal";
                break;
        }

        return String.format("-fx-background-color: %s; -fx-border-color: black; -fx-text-inner-color: %s; -fx-font-weight: %s;",
                bgColor, textColor, fontWeight);
    }


    private int checkText (int row, int column) {
        try {
            if (Integer.parseInt(txf[row][column].getText()) > 0 &&
                    Integer.parseInt(txf[row][column].getText()) <= SIZE)
                return 0;
        }catch(NumberFormatException e) {
            return -1;
        }
        return -1;
    }

    private int checkLocation(int row, int column) {
        String value = txf[row][column].getText();
        for (int i =0 ; i < SIZE ; i++) {
            if (i == column) continue;
            if (txf[row][i].getText().equals(value))
                return -1;
        }
        for (int i =0 ; i < SIZE ; i++) {
            if (i == row) continue;
            if (txf[i][column].getText().equals(value))
                return -1;
        }
        int blockRowStart = (row / 3) * 3;
        int blockColStart = (column / 3) * 3;
        for (int i = blockRowStart; i < blockRowStart + 3; i++) {
            for (int j = blockColStart; j < blockColStart + 3; j++) {
                if (i == row && j == column) continue;
                if (txf[i][j].getText().equals(value))
                    return -1;
            }
        }
        return 0;
    }

    @FXML
    void clearPressed(ActionEvent event) {
        for (int i = 0; i < SIZE ; i++) {
            for(int j =0 ; j < SIZE ; j++) {
                txf[i][j].setText("");
                txf[i][j].setDisable(false);
                txf[i][j].setStyle("-fx-text-inner-color: black;");
                txf[i][j].setStyle(getCellStyle(i, j, "normal"));
            }
        }
    }


    @FXML
    void setPressed(ActionEvent event) {
        for (int i = 0; i < SIZE ; i++) {
            for(int j =0 ; j < SIZE ; j++) {
                if (!txf[i][j].getText().isEmpty()) {
                    txf[i][j].setDisable(true);
                    txf[i][j].setStyle(getCellStyle(i, j, "set"));


                }
                else txf[i][j].setStyle(getCellStyle(i, j, "normal"));
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Data Saved");
        alert.setHeaderText(null);
        alert.setContentText("The data has been saved. You can continue playing!");
        alert.showAndWait();


    }

}
