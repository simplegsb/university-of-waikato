import java.util.ArrayList;
import java.util.Iterator;

/**
 * Calculates the information gain for choosing a given attribute with a given set of examples.
 * 
 * @author gb21
 */
public class InformationGainCalculator
{
    /**
     * Calculates the information gain of choosing tha ettribute at <code>attributeIndex</code> as the next decision
     * on <code>examples</code>.
     * 
     * @param examples The examples to evaluate.
     * @param attributeIndex The index of the attribute to evaluate the examples against.
     * 
     * @return The information gain of choosing the attribute given.
     */
    public double calculate(ArrayList examples, int attributeIndex)
    {
        int negativeExamples = 0;
        int negativeAttrFalseExamples = 0;
        int negativeAttrTrueExamples = 0;
        int positiveExamples = 0;
        int positiveAttrFalseExamples = 0;
        int positiveAttrTrueExamples = 0;
        Iterator exampleIter = examples.iterator();
        String example;

        // Find numbers of positive and negative examples.
        while (exampleIter.hasNext())
        {
            example = (String) exampleIter.next();

            if (example.charAt(example.length() - 1) == '0')
            {
                negativeExamples++;

                if (example.charAt(attributeIndex) == '0')
                {
                    negativeAttrFalseExamples++;
                }
                else
                {
                    negativeAttrTrueExamples++;
                }
            }
            else
            {
                positiveExamples++;

                if (example.charAt(attributeIndex) == '0')
                {
                    positiveAttrFalseExamples++;
                }
                else
                {
                    positiveAttrTrueExamples++;
                }
            }
        }

        // For the examples where the value of the attribute in question is false.
        double remainderFalse = (negativeAttrFalseExamples + positiveAttrFalseExamples) / (negativeExamples + positiveExamples);
        remainderFalse *= calculateInformationContent(negativeAttrFalseExamples, positiveAttrFalseExamples);
        
        // For the examples where the value of the attribute in question is true.
        double remainderTrue = (negativeAttrTrueExamples + positiveAttrTrueExamples) / (negativeExamples + positiveExamples);
        remainderTrue *= calculateInformationContent(negativeAttrTrueExamples, positiveAttrTrueExamples);
        
        // For all examples.
        double remainder = remainderFalse + remainderTrue;
        
        double gain = calculateInformationContent(negativeExamples, positiveExamples) - remainder;
        
        return (gain);
    }
    
    /**
     * Calculates the information content of a value of the attribute with the given amount of examples with positive
     * and negative classifications.
     * 
     * @param negativeExamples The amount of examples with negative classifications.
     * @param positiveExamples The amount of examples with positive classifications.
     * 
     * @return The information content of a value of the attribute.
     */
    private double calculateInformationContent(double negativeExamples, double positiveExamples)
    {
        double positiveProportion = positiveExamples / (positiveExamples + negativeExamples);
        double negativeProportion = negativeExamples / (positiveExamples + negativeExamples);
        
        double positiveComponent = -1 * positiveProportion * log2(positiveProportion);
        double negativeComponent = negativeProportion * log2(negativeProportion);
        
        return (positiveComponent - negativeComponent);
    }
    
    /**
     * Computes the logarithm of base 2 of <code>x</code>.
     * 
     * If computation results in a NaN value it is just set to 0 and if it results in -infinity it is set to -1.0.
     * 
     * @param x The variable to compute the logarithm of base 2 of.
     * 
     * @return The logarithm of base 2 of <code>x</code>.
     */
    private double log2(double x)
    {        
        double log2 = Math.log(x) / Math.log(2);
        
        // Hacked for invalid results.
        if (log2 == Double.NaN || log2 == Double.NEGATIVE_INFINITY)
        {
            return (0.0);
        }
        else if (log2 == Double.POSITIVE_INFINITY)
        {
            return (1.0);
        }
        
        return (log2);
    }
}
