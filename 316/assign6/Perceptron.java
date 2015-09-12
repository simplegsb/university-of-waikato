import java.io.*;
import java.util.*;

/**
 * 
 * 
 * @author gb21
 */
public class Perceptron
{
    /**
     * Random number generator for generating data.
     * 
     * Useful procedures = nextGaussian() {0..1}
     * nextBoolean() {set false=0, true=1} for the data
     */
    private Random random = new Random();
    
    /**
     * The weights that constitute the neural network state.
     */
    private double[] weights = new double[6];

    /**
     * Creates an instance of Perceptron.
     * 
     * Initialize the weight vector.
     */
    public Perceptron()
    {
        // Set weights to random values (distributed according to
        // Gaussian with zero mean and unit variance)
        for (int i = 0; i < weights.length; i++)
        {
            weights[i] = random.nextGaussian();
        }
    }

    /**
     * Adds a batch of data to the given vector.
     * 
     * @param numExamples The number of examples to add.
     * @param examples The vector to add the examples to.
     */
    public void addData(int numExamples, Vector examples)
    {
        // Add numExamples examples
        for (int j = 0; j < numExamples; j++)
        {
            // An example has bias + 6 inputs + the classification
            double[] example = new double[8];

            // The input for the bias is always -1
            example[0] = -1;

            // Get other input values
            for (int i = 1; i < example.length - 1; i++)
            {
                example[i] = random.nextBoolean() ? 1 : 0;
            }

            // Add classification
            example[example.length - 1] = (example[1] == 1.0 && example[3] == 1.0 && example[4] == 1.0 && example[6] == 1.0) ? 1
                    : 0;

            // Add example to vector
            examples.addElement(example);
        }
    }

    /**
     * Gets a classification for the given example.
     * 
     * @param example
     */
    public double getClassification(double[] example)
    {
        // Compute weighted input to output neuron

        // Compute and return predicted classification
        return 0.0; // always false!
    }

    /**
     * Train perceptron on data.
     * 
     * @param examples The examples to train the perceptron on.
     */
    public void trainPerceptron(Vector examples)
    {        
        Iterator exampleIter;
        double[] example;
        int errors;
        
        do
        {
            errors = 0;
            
            exampleIter = examples.iterator();
            while (exampleIter.hasNext())
            {
                example = (double[]) exampleIter.next();
            
                if (getClassification(example) <= 0)
                {
                    //weight[errors + 1] = weight[errors] + ;
                    //bias[errors + 1] = bias[errors] + ;
                    
                    errors++;
                }
            }
        }
        while (errors > 0);
    }

    /**
     * Computes error rate on given data.
     * 
     * @param examples The examples to compute the error rate of.
     */
    public double getErrorRate(Vector examples)
    {        
        Iterator exampleIter = examples.iterator();
        double[] example;
        double errors = 0.0;
        
        while (exampleIter.hasNext())
        {
            example = (double[]) exampleIter.next();
            
            // If classification should be true.
            if (example[1] == 1.0 && example[3] == 1.0 && example[4] == 1.0 && example[6] == 1.0)
            {
                if (example[example.length - 1] == 0.0)
                {
                    errors++;
                }
            }
            else if (example[example.length - 1] == 1.0)
            {
                errors++;
            }
        }

        return (errors / examples.size());  
    }

    /**
     * The main control function.
     */
    public static void main(String[] options)
    {
        Vector forTraining = new Vector();
        Vector forTesting = new Vector();
        Perceptron perc = new Perceptron();

        // Get test data
        perc.addData(500, forTesting);

        // Print the first ten test examples
        System.out.println("\n=======================================");
        System.out.println("10 test examples");
        System.out.println("=======================================");
        for (int i = 0; i < 10; i++)
        {
            double[] example = (double[]) forTesting.elementAt(i);
            for (int j = 1; j < example.length; j++)
            {
                System.out.print(example[j] + " ");
            }
            System.out.println();
        }
        System.out.println("=======================================\n");

        // Generate data for learning curve;
        System.out.println("=======================================");
        System.out.println("#Training\tPercent correct");
        System.out.println("=======================================");
        while (forTraining.size() < 500)
        {
            perc.addData(50, forTraining);
            perc.trainPerceptron(forTraining);
            System.out.println(forTraining.size() + "\t\t" + (100 * (1 - perc.getErrorRate(forTesting))));
        }
        System.out.println("=======================================\n");
    }
}