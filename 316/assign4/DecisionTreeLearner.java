import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Uses a decision tree to learn information about a set of examples and make general decisions about what determines
 * their classifications.
 * 
 * @author gb21
 */
public class DecisionTreeLearner
{
    /**
     * 
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // Check for correct number of arguments.
        if (args.length != 2)
        {
            System.out.println("Two arguments required, usage:");
            System.out.println("DecisionTreeLearner <examples file name> <test size>");
            System.exit(0);
        }

        int testSize = 0;

        // Check test size is an integer.
        try
        {
            testSize = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Test size must be an integer, usage:");
            System.out.println("DecisionTreeLearner <examples file name> <test size>");
            System.exit(0);
        }

        ArrayList examples = readExamples(args[0]);

        // Remove test examples from examples to build tree from.
        ArrayList testExamples = new ArrayList();
        for (int tests = 0; tests < testSize; tests++)
        {
            testExamples.add(examples.remove(examples.size() - 1));
        }

        System.out.println("Building tree...");

        DecisionTree tree = createTree(examples, initAttributes(((String) examples.get(0)).length() - 1), false);

        System.out.println("Tree built.");
        System.out.println("Testing tree with " + testSize + " last examples...");

        Iterator testExampleIter = testExamples.iterator();
        int passed = 0;
        while (testExampleIter.hasNext())
        {
            if (tree.testExample((String) testExampleIter.next()))
            {
                passed++;
            }
        }
        float passRate = passed / testSize * 100;

        System.out.println("Tree tested with " + passRate + "% pass rate.");
    }

    /**
     * Determines if all of the attributes in <code>attributes</code> have been used by the tree already.
     * 
     * @param attributes The attributes to check.
     * 
     * @return True if they have all been used by the tree already, false otherwise.
     */
    private static boolean allAttributesUsed(boolean[] attributes)
    {
        for (int index = 0; index < attributes.length; index++)
        {
            if (attributes[index] == true)
            {
                return (false);
            }
        }

        return (true);
    }

    /**
     * Uses the information gain strategy to choose the most promising attribute to base the tree's next decision on.
     * 
     * @param examples The examples to check.
     * @param attributes The available attributes to choose from.
     * 
     * @return The index of the most promising attribute in <code>attributes</code>.
     */
    private static int chooseAttributeInformationGain(ArrayList examples, boolean[] attributes)
    {
        InformationGainCalculator gainCalc = new InformationGainCalculator();
        int chosenIndex = -1;
        double gain = 0.0f;
        double currentGain;

        for (int index = 0; index < attributes.length; index++)
        {
            if (attributes[index] == true)
            {
                System.out.print(".");

                currentGain = gainCalc.calculate(examples, index);

                if (gain <= currentGain)
                {
                    gain = currentGain;
                    chosenIndex = index;
                }
            }
        }

        System.out.print("\n");

        return (chosenIndex);
    }

    /**
     * Uses a ratio of the correlation between the value of the attribute and the classification of the examples to
     * choose the most promising attribute to base the tree's next decision on.
     * 
     * @param examples The examples to check.
     * @param attributes The available attributes to choose from.
     * 
     * @return The index of the most promising attribute in <code>attributes</code>.
     */
    private static int chooseAttributeMyAlgorithm(ArrayList examples, boolean[] attributes)
    {
        int chosenIndex = -1;
        float ratio = 0.0f;
        float currentRatio;
        int examplesFound = 0;
        Iterator exampleIter = examples.iterator();
        String example;

        for (int index = 0; index < attributes.length; index++)
        {
            if (attributes[index] == true)
            {
                // Find numbers of positive examples with attribute true and negative examples with attribute false.
                while (exampleIter.hasNext())
                {
                    example = (String) exampleIter.next();

                    // If false attribute -> negative example.
                    if (example.charAt(example.length() - 1) == '0' && example.charAt(index) == '0')
                    {
                        examplesFound++;
                    }
                    // If true attribute -> positive example.
                    else if (example.charAt(example.length() - 1) == '1' && example.charAt(index) == '1')
                    {
                        examplesFound++;
                    }
                }
                
                // Ratio represents strength of correlation between attribute and example.
                currentRatio = examplesFound / examples.size();

                // The strength of the ratio is tested in two situations:
                //
                // The first determines its strength when:
                // false attribute -> negative example and true attribute -> positive example.
                if (ratio <= currentRatio)
                {
                    ratio = currentRatio;
                    chosenIndex = index;
                }
                // The second determines its strength when:
                // false attribute -> positive example and true attribute -> negative example.
                else if (ratio <= 1 - currentRatio)
                {
                    ratio = 1 - currentRatio;
                    chosenIndex = index;
                }
            }
        }

        return (chosenIndex);
    }

    /**
     * Creates a decision tree recursively using the examples, attributes and majority value given.
     * 
     * @param examples The examples the tree is to evaluate.
     * @param attributes The attributes the tree should evaluate the examples in respect to.
     * @param majority The majority classificaiton value of the parent to the root node of this tree is one exists.
     * 
     * @return A decision tree that uses the attributes given to classify the examples generally.
     */
    private static DecisionTree createTree(ArrayList examples, boolean[] attributes, boolean majority)
    {
        DecisionTree tree = new DecisionTree();

        // If no examples are left.
        if (examples.size() == 0)
        {
            tree.getRoot().setClassification(majority);
            return (tree);
        }

        // If all remaining examples have the same classification.
        if (sameClassification(examples))
        {
            String example = (String) examples.get(0);
            char classChar = example.charAt(example.length() - 1);

            if (classChar == '0')
            {
                tree.getRoot().setClassification(false);
            }
            else
            {
                tree.getRoot().setClassification(true);
            }

            return (tree);
        }

        // If all attributes have been used already.
        if (allAttributesUsed(attributes))
        {
            tree.getRoot().setClassification(majorityValue(examples));
            return (tree);
        }

        // COMMENTING OUT APPROPRIATE LINE DETERMINES ATTRIBUTE CHOOSING ALGORITHM \\
        int bestIndex = chooseAttributeInformationGain(examples, attributes);
        //int bestIndex = chooseAttributeMyAlgorithm(examples, attributes);

        attributes[bestIndex] = false;
        tree.getRoot().setAttribute(bestIndex);

        majority = majorityValue(examples);

        // For attribute value of false.
        DecisionTree subtree = createTree(getAllWithValue(bestIndex, false, examples), attributes, majority);
        subtree.getRoot().setValue(false);
        tree.getRoot().addChild(subtree.getRoot());

        // For attribute value of true.
        subtree = createTree(getAllWithValue(bestIndex, true, examples), attributes, majority);
        subtree.getRoot().setValue(true);
        tree.getRoot().addChild(subtree.getRoot());

        return (tree);
    }

    /**
     * Returns all examples in <code>examples</code> where the value of the attribute at index <code>index</code> has
     * a value equal to <code>value</code>.
     * 
     * @param index The index of the attribute to check.
     * @param value The value of the attribute to check.
     * @param examples The examples to check.
     * 
     * @return A list of all examples that have the correct value of the attribute.
     */
    private static ArrayList getAllWithValue(int index, boolean value, ArrayList examples)
    {
        ArrayList chosenExamples = new ArrayList();
        Iterator exampleIter = examples.iterator();
        String example;

        while (exampleIter.hasNext())
        {
            example = (String) exampleIter.next();

            if (example.charAt(index) == '0' && value == false)
            {
                chosenExamples.add(example);
            }
            else if (example.charAt(index) == '1' && value == true)
            {
                chosenExamples.add(example);
            }
        }

        return (chosenExamples);
    }

    /**
     * Initialises the attributes array to the correct length and sets all values to true.
     * 
     * @param length The number of attributes to initialise.
     * 
     * @return The initialised attributes array.
     */
    private static boolean[] initAttributes(int length)
    {
        boolean[] attributes = new boolean[length];

        for (int index = 0; index < length; index++)
        {
            attributes[index] = true;
        }

        return (attributes);
    }

    /**
     * Determines the majority classification value of the examples in <code>examples</code>.
     * 
     * @param examples The examples to check.
     * 
     * @return True if true is the majority classification value, false otherwise.
     */
    private static boolean majorityValue(ArrayList examples)
    {
        int falseValues = 0;
        int trueValues = 0;
        String example;
        Iterator exampleIter = examples.iterator();

        while (exampleIter.hasNext())
        {
            example = (String) exampleIter.next();

            if (example.charAt(example.length() - 1) == '0')
            {
                falseValues++;
            }
            else
            {
                trueValues++;
            }
        }

        return (falseValues >= trueValues ? false : true);
    }

    /**
     * Reads a list of examples from a text file.
     * 
     * Example of text file format is as follows:
     * 
     * 11001010
     * 10101001
     * 01010101
     * 
     * Where each line is the same length and contains only 1's and 0's. The last number represents the classification
     * and the rest are the attributes. 1 represents true and 0 represents false.
     * 
     * @param filename The name of the file containing the examples.
     * 
     * @return A list of examples where each example is represented by a String.
     */
    private static ArrayList readExamples(String filename)
    {
        BufferedReader inFromFile = null;
        String example;
        ArrayList examples = new ArrayList();

        try
        {
            inFromFile = new BufferedReader(new FileReader(filename));

            while ((example = inFromFile.readLine()) != null)
            {
                examples.add(example);
            }
        }
        catch (IOException e)
        {
            System.out.println("Invalid file name, usage:");
            System.out.println("FileClient <examples file name> <test size>");
            System.exit(0);
        }

        return (examples);
    }

    /**
     * Determines if all examples in <code>examples</code> have the same classification.
     * 
     * @param examples The examples to be checked.
     * 
     * @return True if all the examples have the same classification, false otherwise.
     */
    private static boolean sameClassification(ArrayList examples)
    {
        Iterator exampleIter = examples.iterator();
        String example = (String) exampleIter.next();
        boolean classification;

        if (example.charAt(example.length() - 1) == '0')
        {
            classification = false;
        }
        else
        {
            classification = true;
        }

        while (exampleIter.hasNext())
        {
            example = (String) exampleIter.next();

            if (example.charAt(example.length() - 1) == '0')
            {
                if (classification == true)
                {
                    return (false);
                }
            }
            else if (classification == false)
            {
                return (false);
            }
        }

        return (true);
    }
}
