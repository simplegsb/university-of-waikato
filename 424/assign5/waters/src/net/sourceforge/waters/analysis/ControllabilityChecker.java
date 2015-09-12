//###########################################################################
//# PROJECT: Waters
//# PACKAGE: net.sourceforge.waters.analysis
//# CLASS:   ControllabilityChecker
//###########################################################################
//# $Id: ControllabilityChecker.java,v 1.1 2005/05/08 00:24:31 robi Exp $
//###########################################################################

package net.sourceforge.waters.analysis;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.waters.model.des.AutomatonProxy;
import net.sourceforge.waters.model.des.EventProxy;
import net.sourceforge.waters.model.des.ProductDESProxy;
import net.sourceforge.waters.model.des.StateProxy;
import net.sourceforge.waters.model.des.TransitionProxy;
import net.sourceforge.waters.xsd.base.ComponentKind;
import net.sourceforge.waters.xsd.base.EventKind;

/**
 * Checks the controllability of a model.
 *
 * @see ModelChecker
 *
 * @author Robi Malik & gb21
 */

public class ControllabilityChecker extends ModelChecker
{
    //#########################################################################
    //# Fields
    /**
     * True if the model has been checked and is controllable, false otherwise.
     */
    private boolean controllable = false;

    /**
     * A counter example to the controllablity of the model if the model was found to be uncontrollable.
     */
    private List counterExample = null;

    /**
     * The model to be checked by this controllability checker.
     */
    private ProductDESProxy model;

    //#########################################################################
    //# Constructors
    /**
     * Creates a new controllability checker to check a particular model.
     * @param  model   The model to be checked by this controllability checker.
     */
    public ControllabilityChecker(final ProductDESProxy model)
    {
        super(model);

        this.model = model;
    }

    //#########################################################################
    //# Methods    
    /**
     * Gets a counterexample if the model was found to be not controllable.
     * @return A list of events of type
     *         {@link net.sourceforge.waters.model.des.EventProxy EventProxy}
     *         representing a controllability error trace.
     *         A controllability error trace is a nonempty sequence of events
     *         such that all except the last event in the list can be
     *         executed by the model. The last event in list is an
     *         uncontrollable event that is possible in all plant
     *         automata, but not in all specification automata present
     *         in the model. Thus, the last step demonstrates why the
     *         model is not controllable.
     */
    public List getCounterExample()
    {
        return (counterExample);
    }

    /**
     * Gets the result of controllability checking.
     * @return <CODE>true</CODE> if the model was found to be controllable,
     *         <CODE>false</CODE> otherwise.
     */
    public boolean getResult()
    {
        return (controllable);
    }

    /**
     * Returns the state reached when taking the action <code>proposition</code> from <code>sourceState</code> in
     * <code>automaton</code>.
     * 
     * @param automaton The automaton to search.
     * @param sourceState The state to take the action from.
     * @param proposition The action to take.
     * 
     * @return The state reached if the action is valid and the source state otherwise (assumes implicit loop).
     */
    private StateProxy getSuccessor(AutomatonProxy automaton, StateProxy sourceState, EventProxy proposition)
    {
        Iterator transitionIter = automaton.getTransitions().iterator();
        TransitionProxy currentTransition;
        while (transitionIter.hasNext())
        {
            currentTransition = (TransitionProxy) transitionIter.next();

            if (currentTransition.getSource() == sourceState && currentTransition.getEvent() == proposition)
            {
                return (currentTransition.getTarget());
            }
        }

        return (sourceState);
    }

    /**
     * Determines if the model is controllable or not.
     * 
     * @return True if the model is controllable, false otherwise.
     */
    public boolean run()
    {
        long startTime = System.currentTimeMillis();

        // Create synchronous products.
        SynchronousProductAutomatonProxy plantProduct = new SynchronousProductAutomatonProxy("Plant",
                ComponentKind.PLANT, model.getAutomata());
        SynchronousProductAutomatonProxy specProduct = new SynchronousProductAutomatonProxy("Spec",
                ComponentKind.SPEC, model.getAutomata());

        System.out.print("products created (" + plantProduct.getStates().size() + ", "
                + specProduct.getStates().size() + ") ... ");

        ArrayList enabledPlantProductEvents = AutomatonUtil.getEnabledEvents(plantProduct);
        ArrayList enabledSpecProductEvents = AutomatonUtil.getEnabledEvents(specProduct);

        // Initiate data structures.
        Tree tree = new Tree(new TreeNode(plantProduct.getInitialState(), specProduct.getInitialState(), null));
        ArrayList unvisitedNodes = new ArrayList();
        unvisitedNodes.add(tree.getRoot());
        Hashtable visitedNodes = new Hashtable();

        // For all nodes, create successor states (avoid duplicates), putting them into the tree and the list of
        // unvisited nodes. If controllability fails, return false and build state list of counter example.
        TreeNode currentNode;
        while (!unvisitedNodes.isEmpty())
        {
            currentNode = (TreeNode) unvisitedNodes.remove(0);
            visitedNodes.put(currentNode.hashCode(), currentNode);

            // For all events enabled in plant synchronous product.
            Iterator eventIter = enabledPlantProductEvents.iterator();
            EventProxy currentEvent;
            while (eventIter.hasNext())
            {
                currentEvent = (EventProxy) eventIter.next();

                // If event is enabled by the plant synchronous product in its current state.
                if (AutomatonUtil.acceptedInState(plantProduct, currentNode.getPlantState(), currentEvent))
                {
                    // If the spec synchronous product can execute this event in its current state.
                    if (AutomatonUtil.acceptedInState(specProduct, currentNode.getSpecState(), currentEvent))
                    {
                        TreeNode newNode = new TreeNode(getSuccessor(plantProduct, currentNode.getPlantState(),
                                currentEvent), getSuccessor(specProduct, currentNode.getSpecState(), currentEvent),
                                currentEvent);

                        if (!visitedNodes.contains(newNode))
                        {
                            currentNode.addChild(newNode);
                            unvisitedNodes.add(newNode);
                        }
                    }
                    // Event is uncontrollable & spec's enabled events contains event -> System is uncontrollable.
                    else if (currentEvent.getKind() == EventKind.UNCONTROLLABLE
                            && enabledSpecProductEvents.contains(currentEvent))
                    {
                        counterExample = new ArrayList();

                        while (currentNode.getParent() != null)
                        {
                            counterExample.add(0, currentNode.getEvent());
                            currentNode = currentNode.getParent();
                        }

                        System.out.print("<time taken: " + (System.currentTimeMillis() - startTime) + "ms> ");

                        return (false);
                    }
                }
            }
        }

        System.out.print("<time taken: " + (System.currentTimeMillis() - startTime) + "ms> ");

        return (true);
    }
}
