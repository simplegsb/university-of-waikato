package net.sourceforge.waters.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.waters.model.base.DuplicateNameException;
import net.sourceforge.waters.model.des.AutomatonProxy;
import net.sourceforge.waters.model.des.EventProxy;
import net.sourceforge.waters.model.des.StateProxy;
import net.sourceforge.waters.model.des.TransitionProxy;
import net.sourceforge.waters.xsd.base.ComponentKind;

/**
 * 
 * 
 * @author gb21
 */
public class SynchronousProductAutomatonProxy extends AutomatonProxy
{
    /**
     * The initial state of this synchronous product.
     */
    private StateProxy initialState;

    /**
     * Creates an instance of SynchronousProductAutomatonProxy.
     * 
     * @param name The name of this automaton.
     * @param kind The kind of this automaton.
     * @param automata The automata to make this synchronous product from.
     */
    public SynchronousProductAutomatonProxy(String name, ComponentKind kind, Collection automata)
    {
        super(name, kind);

        createSynchronousProduct(kind, automata);
    }

    /**
     * Creates an initial state for a synchronous product automaton of one kind using <code>automata</code>.
     * 
     * The name of the initial state conforms to the naming convention <A1-init>#...#<An-init> where Ai is the ith
     * automaton to be put in the synchronous product and Ai-init is the name of the initial state of that automaton.
     * 
     * @param automata The automata to create the synchronous product for.
     * @param kind The kind of automata to include.
     */
    private void createInitialState(Collection automata, ComponentKind kind)
    {
        String stateName = new String();
        ArrayList propositions = new ArrayList();

        // For every automaton of the correct kind.
        Iterator automataIter = automata.iterator();
        AutomatonProxy currentAutomaton;
        StateProxy currentState;
        while (automataIter.hasNext())
        {
            currentAutomaton = (AutomatonProxy) automataIter.next();

            if (currentAutomaton.getKind() == kind)
            {
                currentState = AutomatonUtil.getInitialState(currentAutomaton);

                stateName = stateName + currentState.getName() + "#";

                propositions = AutomatonUtil.getDisjunction(propositions, currentState.getPropositions());
            }
        }

        initialState = new StateProxy(stateName, true, propositions);
    }

    /**
     * Creates a new synchronous product state using the states from the original <code>automata</code> of
     * <code>kind</code>.
     * 
     * Assumes the naming convention to get the appropriate states to create this state from.
     * 
     * @param automata The automata to make this state from.
     * @param kind The kind of automata to include.
     * @param name The name of the state to create.
     * 
     * @return The new synchronous product state.
     */
    private StateProxy createStateFromName(Collection automata, ComponentKind kind, String name)
    {
        String[] names = name.split("#");
        ArrayList propositions = new ArrayList();

        // For every automaton of the correct kind.
        Iterator automataIter = automata.iterator();
        AutomatonProxy currentAutomaton;
        int automatonIndex = 0;
        boolean initial = true;
        while (automataIter.hasNext())
        {
            currentAutomaton = (AutomatonProxy) automataIter.next();

            if (currentAutomaton.getKind() == kind)
            {
                // For every state, check to see if it is the state with the correct name, if it is add any
                // propositions from the state not already in the synchronous product state to the synchronous
                // product state.
                Iterator stateIter = currentAutomaton.getStates().iterator();
                StateProxy currentState;
                while (stateIter.hasNext())
                {
                    currentState = (StateProxy) stateIter.next();

                    if (currentState.getName().compareTo(names[automatonIndex]) == 0)
                    {
                        propositions = AutomatonUtil.getDisjunction(propositions, currentState.getPropositions());
                        if (!currentState.isInitial())
                        {
                            initial = false;
                        }
                    }
                }

                automatonIndex++;
            }
        }

        return (new StateProxy(name, initial, propositions));
    }

    /**
     * Creates the synchronous product using <code>automata</code> of <code>kind</code>.
     * 
     * @param kind The name of this automaton.
     * @param automata The automata to make this synchronous product from.
     */
    private void createSynchronousProduct(ComponentKind kind, Collection automata)
    {
        createInitialState(automata, kind);

        // Add initial state.
        ArrayList unvisitedStates = new ArrayList();
        unvisitedStates.add(initialState);
        try
        {
            addState(initialState);
        }
        catch (DuplicateNameException e)
        {
            e.printStackTrace();
        }

        // Add valid successor states for the current state to the synchronous product automaton.
        ArrayList currentTransitions;
        StateProxy currentState;
        int stateNum = 0;
        while (!unvisitedStates.isEmpty())
        {
            currentState = (StateProxy) unvisitedStates.remove(0);

            currentTransitions = getValidTransitions(automata, kind, currentState);

            // For all valid transitions.
            Iterator transitionIter = currentTransitions.iterator();
            TransitionProxy currentTransition;
            while (transitionIter.hasNext())
            {
                currentTransition = (TransitionProxy) transitionIter.next();

                // THE FOLLOWING CODE IS INCLUDED TO SOLVE PROBLEMS I HAVE HAD WITH EQUALITY OF STATES \\
                // If the target state from the transition is one that already exists in this synchronous product
                // exchange it for the already existing state.
                StateProxy existingState = AutomatonUtil.getStateWithName(this, currentTransition.getTarget()
                        .getName());
                if (existingState != null)
                {
                    currentTransition = new TransitionProxy(currentTransition.getSource(), existingState,
                            currentTransition.getEvent());
                }

                try
                {
                    // If automaton does not already contain target state.
                    if (!getStates().contains(currentTransition.getTarget()))
                    {
                        addState(currentTransition.getTarget());
                        unvisitedStates.add(currentTransition.getTarget());
                    }
                    addTransition(currentTransition);
                }
                catch (DuplicateNameException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the initial state of this synchronous product.
     * 
     * @return The initial state of this synchronous product.
     */
    public StateProxy getInitialState()
    {
        return (initialState);
    }

    /**
     * Finds a state in an automaton using the index of the automaton and the state of the synchronous product that
     * includes this automaton.
     * 
     * Assumes naming convention of the source state that determines the name of the state in question.
     * 
     * @param currentAutomaton The automaton to search for the state in.
     * @param sourceState The state of the synchronous product that includes <code>currentAutomaton</code>.
     * @param automatonIndex The index of the automaton (also dependant on naming convention).
     * 
     * @return The state being searched for or null if it does not exist.
     */
    public StateProxy getState(AutomatonProxy currentAutomaton, StateProxy sourceState, int automatonIndex)
    {
        String[] stateNames = sourceState.getName().split("#");

        Iterator stateIter = currentAutomaton.getStates().iterator();
        StateProxy currentState;
        while (stateIter.hasNext())
        {
            currentState = (StateProxy) stateIter.next();

            if (currentState.getName().compareTo(stateNames[automatonIndex]) == 0)
            {
                return (currentState);
            }
        }

        return (null);
    }

    /**
     * Returns the events in automata of <code>kind</code> from <code>state</code> that are valid in the synchronous
     * product.
     * 
     * @param automata The automata to use.
     * @param kind The kind of automata to include.
     * @param sourceState The source state of the transitions.
     * 
     * @return The events in automata of <code>kind</code> from <code>state</code> that are valid in the synchronous
     * product.
     */
    private ArrayList getValidEvents(Collection automata, ComponentKind kind, StateProxy sourceState)
    {
        ArrayList allEvents = new ArrayList();
        ArrayList validEvents = new ArrayList();

        // Get list of all events supported by any automaton of the correct kind.
        Iterator automataIter = automata.iterator();
        AutomatonProxy currentAutomaton;
        while (automataIter.hasNext())
        {
            currentAutomaton = (AutomatonProxy) automataIter.next();

            if (currentAutomaton.getKind() == kind)
            {
                allEvents = AutomatonUtil.getDisjunction(allEvents, AutomatonUtil.getEnabledEvents(currentAutomaton));
            }
        }

        // For all events.
        Iterator eventIter = allEvents.iterator();
        EventProxy currentEvent;
        boolean eventValid;
        while (eventIter.hasNext())
        {
            currentEvent = (EventProxy) eventIter.next();

            // For every automaton of the correct kind, check if the event is valid. If it is valid in all automaton
            // add it to the list of valid events.
            automataIter = automata.iterator();
            int automatonIndex = 0;
            eventValid = true;
            while (automataIter.hasNext())
            {
                currentAutomaton = (AutomatonProxy) automataIter.next();

                if (currentAutomaton.getKind() == kind)
                {
                    if (!AutomatonUtil.acceptedInState(currentAutomaton, getState(currentAutomaton, sourceState,
                            automatonIndex), currentEvent))
                    {
                        eventValid = false;
                    }

                    automatonIndex++;
                }
            }

            if (eventValid)
            {
                validEvents.add(currentEvent);
            }
        }

        return (validEvents);
    }

    /**
     * Returns the transitions taken and states reached in automata of <code>kind</code> from <code>state</code> that
     * are valid in the synchronous product.
     * 
     * @param automata The automata to use.
     * @param kind The kind of automata to include.
     * @param state The state the action will be taken at.
     * 
     * @return The valid transitions taken (and states reached implicitly) of the current state.
     */
    private ArrayList getValidTransitions(Collection automata, ComponentKind kind, StateProxy sourceState)
    {
        ArrayList transitions = new ArrayList();
        ArrayList validEvents = getValidEvents(automata, kind, sourceState);

        // For all valid events.
        Iterator eventIter = validEvents.iterator();
        EventProxy currentEvent;
        boolean eventValid;
        while (eventIter.hasNext())
        {
            currentEvent = (EventProxy) eventIter.next();

            // Create target state and transition using event and add it to list of transitions.
            Iterator automataIter = automata.iterator();
            AutomatonProxy currentAutomaton;
            String[] names = sourceState.getName().split("#");
            String targetName = new String();
            int automatonIndex = 0;
            while (automataIter.hasNext())
            {
                currentAutomaton = (AutomatonProxy) automataIter.next();

                if (currentAutomaton.getKind() == kind)
                {
                    // If the current event is not enabled by the current automaton it will have an implicit loop
                    // transition back to the source state.
                    if (!AutomatonUtil.getEnabledEvents(currentAutomaton).contains(currentEvent))
                    {
                        targetName = targetName + names[automatonIndex] + "#";
                    }
                    else
                    {
                        // Find the correct transition.
                        Iterator transitionIter = currentAutomaton.getTransitions().iterator();
                        TransitionProxy currentTransition;
                        while (transitionIter.hasNext())
                        {
                            currentTransition = (TransitionProxy) transitionIter.next();

                            if ((currentTransition.getSource().getName().compareTo(names[automatonIndex]) == 0)
                                    && currentTransition.getEvent() == currentEvent)
                            {
                                targetName = targetName + currentTransition.getTarget().getName() + "#";
                            }
                        }
                    }

                    automatonIndex++;
                }
            }

            // Create the target state and transition.
            StateProxy targetState = createStateFromName(automata, kind, targetName);
            transitions.add(new TransitionProxy(sourceState, targetState, currentEvent));
        }

        return (transitions);
    }
}
