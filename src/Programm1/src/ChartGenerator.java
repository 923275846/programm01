package Programm1.src;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class ChartGenerator {
	
	    File filePath;
	
	

	
	    public ChartGenerator(File file) {
	        // Path to the data file
	        filePath = file;
	    }
	        
	        
	    public void GenChart() {
	    // Create dataset
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        
	        // Read data from file
	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            // Skip the first line (header)
	            br.readLine();
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split(",");
	                if (values.length == 2) {
	                    String xValue = values[0];
	                    double yValue = Double.parseDouble(values[1]);
	                    dataset.addValue(yValue, "Series1", xValue);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // Create chart
	        JFreeChart lineChart = ChartFactory.createLineChart(
	                "Line Chart Example",
	                "X-Axis",
	                "Y-Axis",
	                dataset,
	                PlotOrientation.VERTICAL,
	                true, true, false);
	        
	        
	        
	     

	        // Create and display chart panel
	        ChartPanel chartPanel = new ChartPanel(lineChart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
	        JFrame frame = new JFrame();
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        frame.setTitle("Line Chart Example");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);
	    }
	


}
