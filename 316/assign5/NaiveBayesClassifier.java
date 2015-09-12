import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A basic naive bayes classifier.
 * 
 * @author gb21
 */
public class NaiveBayesClassifier
{
    /**
     * 
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // Check for correct number of arguments.
        if (args.length != 1)
        {
            System.out.println("One argument required, usage:");
            System.out.println("NaiveBayesClassifier <file name>");
            System.exit(0);
        }

        BufferedReader inFromFile = null;

        // Attempt to open file.
        try
        {
            inFromFile = new BufferedReader(new FileReader(args[0]));
        }
        catch (IOException e)
        {
            System.out.println("Invalid file name, usage:");
            System.out.println("NaiveBayesClassifier <file name>");
            System.exit(0);
        }

        System.out.println("Generating joint probability table...");
        JointProbabilityTable table = generateProbabilityTable(inFromFile);
        System.out.println("Table for relation: " + table.getName() + " generated.\n");

        System.out.println("Welcome to the naive bayes classifier by gb21...\n");
        
        printHelp();
        
        try
        {
            boolean quit = false;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            String query;
            while (!quit)
            {
                System.out.print("> ");
                
                query = inFromUser.readLine();
                
                if (query.compareTo("q") == 0)
                {
                    quit = true;
                }
                else if (query.compareTo("h") == 0)
                {
                    System.out.println();
                    printHelp();
                }
                else
                {
                    float[] distribution = table.getDistribution(query.split(","));
                    
                    System.out.println();
                    System.out.println("play = yes: " + distribution[0]);
                    System.out.println("play = no: " + distribution[1] + "\n");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Generates a probability table using the attributes and evidence from the 
     * 
     * @param inFromFile The stream to get input from for generating the table.
     * 
     * @return A completed probability table.
     */
    private static JointProbabilityTable generateProbabilityTable(BufferedReader inFromFile)
    {
        String relation = null;
        List variables = new ArrayList();
        JointProbabilityTable table = null;

        try
        {
            String line;
            String[] tokens;
            while ((line = inFromFile.readLine()) != null)
            {
                tokens = line.split(" ");
                if (tokens[0].compareTo("@relation") == 0)
                {                    
                    relation = tokens[1];
                }
                else if (tokens[0].compareTo("@attribute") == 0)
                {                    
                    List variable = new ArrayList();
                    for (int index = 2; index < tokens.length; index++)
                    {
                        if (index == 2)
                        {
                            variable.add(tokens[index].substring(1, tokens[index].length() - 1));
                        }
                        else
                        {
                            variable.add(tokens[index].substring(0, tokens[index].length() - 1));
                        }
                    }

                    variables.add(variable);
                }
                else if (tokens[0].compareTo("@data") == 0)
                {
                    table = new JointProbabilityTable(variables, relation);

                    while ((line = inFromFile.readLine()) != null)
                    {
                        tokens = line.split(",");

                        if (tokens.length == variables.size())
                        {
                            table.includeEvidence(tokens);
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return (table);
    }
    
    /**
     * Prints text about the usage of the classifier.
     */
    private static void printHelp()
    {
        System.out.println("Options available:\n");
        
        System.out.println("\tEnter a query fomatted as follows:\n");
        
        System.out.println("\t<value of v0>,...,<value of vn>");
        System.out.println("\tWhere vi is the ith variable and there are n variables.\n");
        
        System.out.println("\tRe-print this help text: h\n");
        
        System.out.println("\tQuit: q\n");
    }
}
