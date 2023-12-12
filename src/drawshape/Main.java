package drawshape;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Main extends Application {
    
    private Pane pane;
    private ColorPicker colorPicker;
    private Slider sizeSlider = new Slider();
    private ComboBox<String> elementCombobox;
    private Paint savedColor;
    private Label coordinates;
    

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        HBox buttonsHBox = createButtonsHBox();
        pane = createPane();

        root.getChildren().addAll(pane, buttonsHBox);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Complex Shape Draw");
        primaryStage.show();
    }

    private HBox createButtonsHBox(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20, 20, 20, 20));
        container.setSpacing(10);

        Button exitButton = createExitButton();
        Label colorLabel = new Label("Color:");
        colorPicker = new ColorPicker(Color.RED);
        Label sizeLabel = new Label("Size:");
        sizeSlider = createSizeSlider();
        Label elementLabel = new Label("Shape:");
        elementCombobox = createElementComboBox();

        container.getChildren().addAll(exitButton, colorLabel, colorPicker, sizeLabel, sizeSlider, elementLabel, elementCombobox);
        return container;
    }

    private Button createExitButton() {
        Button exitButton = new Button("Exit");
        exitButton.setOnAction((actionEvent) -> System.exit(0));
        return exitButton;
    }

    private Slider createSizeSlider() {
        sizeSlider.setMin(10);
        sizeSlider.setMax(100);
        sizeSlider.setValue(50);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);
        return sizeSlider;
    }

    private ComboBox<String> createElementComboBox() {
        ComboBox<String> elementComboBox = new ComboBox<>(FXCollections.observableArrayList("Circle", "Rectangle", "Polygon"));
        elementComboBox.setValue("Circle");
        return elementComboBox;
    }

    private Pane createPane(){
        pane = new Pane();
        pane.setPrefWidth(400);
        pane.setPrefHeight(500);

        pane.setOnMouseClicked(this::handleMouseClickedOnPane);
        coordinates = createCoordinatesLabel();
        pane.getChildren().add(coordinates);
        pane.setOnMouseMoved(this::updateCoordinates);
        pane.setOnMouseDragged(this::updateCoordinates);

        return pane;
    }

    private void updateCoordinates(MouseEvent event) {
        coordinates.setText("X - " + event.getSceneX() + ", Y - " + event.getSceneY());
    }

    private Label createCoordinatesLabel(){
        return new Label("X - 0, Y - 0");
    }

    private void handleMouseClickedOnPane(MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY) {
            handleDrawing(event);
        }
    }

    private void handleDrawing(MouseEvent event){
        Shape resultShape = createShapeBasedOnComboBox(event.getX(), event.getY(), sizeSlider.getValue(), colorPicker.getValue());
        addEventHandlersToShape(resultShape);
    }

    private Shape createShapeBasedOnComboBox(double x, double y, double size, Color color){
        switch (elementCombobox.getValue()){
            case "Circle":    return drawCircle(x, y, size, color);
            case "Rectangle": return drawRectangle(x, y, size, color);
            case "Polygon":   return drawPolygon(x, y, size, color);
        }
        return null;
    }

    private void addEventHandlersToShape(Shape shape) {
        shape.setOnMouseClicked(event -> handleMouseClickedOnShape(event, shape));
        shape.setOnMouseEntered(event -> handleMouseEnteredOnShape(shape));
        shape.setOnMouseExited(event -> handleMouseExitedOnShape(shape));
        
    }

    private void handleMouseClickedOnShape(MouseEvent event, Shape shape){
        if (event.getButton().equals(MouseButton.SECONDARY)) {
            deleteShape(shape);
        } 
    }

    private void handleMouseEnteredOnShape(Shape shape){
        savedColor = shape.getFill();
        shape.setFill(Color.AQUA);
    }

    private void handleMouseExitedOnShape(Shape shape){
        shape.setFill(savedColor);
    }

    private Shape drawCircle(double x, double y, double size, Color color){
        Circle circle = new Circle(x, y, size / 2, color);
        pane.getChildren().add(circle);
        return circle;
    }

    private Shape drawRectangle(double x, double y, double size, Color color){
        Rectangle rectangle = new Rectangle(x - size / 2 , y - size / 2, size, size);
        rectangle.setFill(color);
        pane.getChildren().add(rectangle);
        return rectangle;
    }

    private Shape drawPolygon(double x, double y, double size, Color color){
        Polygon polygon = new Polygon(x - size / 2, y, x - size / 4, y - size / 2, x + size / 4, y - size / 2, x + size / 2, y, x + size / 4, y + size / 2, x - size / 4, y + size / 2);
        polygon.setFill(color);
        pane.getChildren().add(polygon);
        return polygon;
    }

    private void deleteShape(Shape shape){
        pane.getChildren().remove(shape);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
