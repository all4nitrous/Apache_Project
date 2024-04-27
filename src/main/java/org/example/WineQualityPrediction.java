package org.example;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;

public class WineQualityPrediction {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("Wine Quality Prediction")
            .master("local[*]")  // Use local in development only
            .getOrCreate();

        // Load training data
        String pathToTrainingCsv = "src/main/resources/TrainingDataset.csv";
        Dataset<Row> trainingData = spark.read()
            .format("csv")
            .option("header", "true")
            .option("inferSchema", "true")
            .load(pathToTrainingCsv);

        // Load validation data
        String pathToValidationCsv = "src/main/resources/ValidationDataset.csv";
        Dataset<Row> validationData = spark.read()
            .format("csv")
            .option("header", "true")
            .option("inferSchema", "true")
            .load(pathToValidationCsv);

        // Assembling a feature vector
        String[] featureCols = new String[]{"fixed acidity", "volatile acidity", "citric acid",
            "residual sugar", "chlorides", "free sulfur dioxide", "total sulfur dioxide",
            "density", "pH", "sulphates", "alcohol"};
        VectorAssembler assembler = new VectorAssembler()
            .setInputCols(featureCols)
            .setOutputCol("features");
        Dataset<Row> trainingFeatures = assembler.transform(trainingData);
        Dataset<Row> validationFeatures = assembler.transform(validationData);

        // Initialize logistic regression
        LogisticRegression lr = new LogisticRegression()
            .setLabelCol("quality")  // Assuming the label column is named "quality"
            .setFeaturesCol("features");

        // Fit the model
        LogisticRegressionModel model = lr.fit(trainingFeatures);

        // Make predictions on validation data
        Dataset<Row> predictions = model.transform(validationFeatures);

        // Evaluate model using F1 score
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
            .setLabelCol("quality")
            .setPredictionCol("prediction")
            .setMetricName("f1"); // F1 score is the harmonic mean of precision and recall

        double f1Score = evaluator.evaluate(predictions);
        System.out.println("F1 Score = " + f1Score);

        // Stop Spark session
        spark.stop();
    }
}
