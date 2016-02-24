package vstu.lab.controllers;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import vstu.lab.MainApp;
import vstu.lab.utility.BigMath;
import vstu.lab.utility.FXValidator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * TVLab_1 created by Dmitry on 21.02.2016.
 */
public class MathCombinatoricsController {

    @FXML
    private TextArea resultField;

    @FXML
    private Label calcTimeLabel;

    @FXML
    private TextField nField;
    private FXValidator nFieldValidator;

    @FXML
    private TextField mField;
    private FXValidator mFieldValidator;

    @FXML
    private TextArea permutationRepeatListField;
    private FXValidator permutationRepeatListFieldValidator;

    @FXML
    private TextArea taskField;
    private FXValidator taskFieldValidator;

    @FXML
    private RadioButton permutationRepeatButton;

    @FXML
    private RadioButton combinationsButton;

    @FXML
    private Tab calculatorTab;

    @FXML
    private Tab taskTab;

    @FXML
    private Button calculateButton;

    private Service taskService;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MathCombinatoricsController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        // Валидатор для целых чисел
        FXValidator.Validator intValidator = (input) -> {
            if(input.length() == 0)
                return FXValidator.INTERMEDIATE;
            try {
                int n = Integer.parseUnsignedInt(input);
                if(n == 0)
                    return FXValidator.INVALID;
            } catch (NumberFormatException e) {
                return FXValidator.INVALID;
            }
            return FXValidator.ACCEPTABLE;
        };
        nFieldValidator = new FXValidator(nField, intValidator);
        mFieldValidator = new FXValidator(mField, intValidator);

        // Валидатор для групп чисел
        FXValidator.Validator numberListValidator = (input) -> {
            if(input.length() == 0)
                return FXValidator.INTERMEDIATE;
            if(Pattern.compile("^(\\d+,)*$").matcher(input).find())
                return FXValidator.INTERMEDIATE;
            if(Pattern.compile("^(\\d+,)*\\d+$").matcher(input).find())
                return FXValidator.ACCEPTABLE;
            return FXValidator.INVALID;
        };
        permutationRepeatListFieldValidator = new FXValidator(permutationRepeatListField, numberListValidator);
        taskFieldValidator = new FXValidator(taskField, numberListValidator);

        // Связываю радиокнопки с полями воода
        nField.disableProperty().bind(permutationRepeatButton.selectedProperty());
        mField.disableProperty().bind(permutationRepeatButton.selectedProperty());
        permutationRepeatListField.disableProperty().bind(combinationsButton.selectedProperty());

        // Создаем службу для расчетов
        taskService = new TaskService();
    }

    /**
     * Вычисляем при нажатии кнопки - "Вычислить"
     */
    @FXML
    private void handleCalculate() {
        if (calculatorTab.isSelected()) {
            if(permutationRepeatButton.isSelected()) {
                if(!permutationRepeatListFieldValidator.isValid()) {
                    showErrorInputDialog("Для перестановки с повторениями введены некорректные данные.");
                    return;
                }
            } else {
                if(!nFieldValidator.isValid() | !mFieldValidator.isValid()) {
                    showErrorInputDialog("Для сочетаний без повторений введены некорректные данные.");
                    return;
                }
            }
        } else if(taskTab.isSelected()) {
            if(!taskFieldValidator.isValid()) {
                showErrorInputDialog("Введено некорректное кол-во писем для каждого канала.");
                return;
            }
        }

        taskService.restart();
    }

    private void showErrorInputDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!");
        alert.setHeaderText("Неккоректные входные данные!");
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Таск расчетов
     */
    private class CalculationTask extends Task<String> {

        @Override
        protected String call() throws Exception {
            Date startDate = new Date();

            String result = "";
            if(calculatorTab.isSelected())
                result = doCalculator();
            else if(taskTab.isSelected())
                result = doTask();

            Date endDate = new Date();
            //String calcTime = String.valueOf(endDate.getTime() - startDate.getTime()) + " ms";
            updateMessage(String.valueOf(endDate.getTime() - startDate.getTime()) + " ms");
            return result;
        }

        private String doCalculator() {
            System.out.println("Debug: Calculator");
            BigInteger result = BigInteger.ONE;
            if(permutationRepeatButton.isSelected()) {

                System.out.println("Debug: permutation repeat");
                ArrayList<Integer> permutationRepeatList = new ArrayList<>();
                for(String num : permutationRepeatListField.getText().split(","))
                    permutationRepeatList.add(Integer.valueOf(num));
                result = BigMath.Combinatorics.permutationsRepeat(permutationRepeatList);
            } else if(combinationsButton.isSelected()) {

                System.out.println("Debug: combinations");
                result = BigMath.Combinatorics.combinations(
                        Integer.valueOf(nField.getText()),
                        Integer.valueOf(mField.getText())
                );
            }
            return result.toString();
        }

        private String doTask() {
            System.out.println("Debug: Task");
            ArrayList<Integer> mailCountList = new ArrayList<>();
            int m = 0;
            for(String num : taskField.getText().split(",")) {
                int value = Integer.valueOf(num);
                mailCountList.add(value);
                m += value;
            }
            int n = mailCountList.size();
            BigInteger part1 = BigMath.Combinatorics.permutationsRepeat(mailCountList);
            BigInteger part2 = BigMath.Combinatorics.placementRepeat(n, m);
            return new BigDecimal(part1).divide(new BigDecimal(part2), 2000, BigDecimal.ROUND_CEILING).toString();
        }
    }

    /**
     * Служба-обработчик таска расчетов
     */
    private class TaskService extends Service<String> {

        @Override
        protected Task<String> createTask() {
            Task<String> task = new CalculationTask();
            calcTimeLabel.textProperty().bind(task.messageProperty());
            calculateButton.disableProperty().bind(task.runningProperty());
            resultField.textProperty().bind(task.valueProperty());
            task.stateProperty().addListener((observable, oldState, newState) -> {
                if(newState == Worker.State.SUCCEEDED)
                    taskService.cancel();
            });
            return task;
        }
    }
}