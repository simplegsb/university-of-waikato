package net.sourceforge.waters.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.waters.model.des.AutomatonProxy;
import net.sourceforge.waters.model.des.EventProxy;
import net.sourceforge.waters.model.des.StateProxy;
import net.sourceforge.waters.model.des.TransitionProxy;

public class AutomatonUtil
{
    /**
     * Determines if <code>event</code> is accepted in <code>state</code> of <code>automaton</code>.
     * 
     * @param automaton The automaton to check.
     * @param state The state to check.
     * @param event The event to check for.
     * 
     * @return True if the event is accepted, false otherwise.
     */
    public static boolean acceptedInState(AutomatonProxy automaton, StateProxy state, EventProxy event)
    {
        ArrayList eventsSupportedInState = new ArrayList();

        Iterator transitionIter = automaton.getTransitions().iterator();
        TransitionProxy currentTransition;
        while (transitionIter.hasNext())
        {
            currentTransition = (TransitionProxy) transitionIter.next();

            if (currentTransition.getSource() == state)
            {
                eventsSupportedInState.add(currentTransition.getEvent());
            }
        }

        if (automaton.getEvents().contains(event) && !eventsSupportedInState.contains(event))
        {
            return (false);
        }

        return (true);
    }
    
    /**
     * Returns the disjunction of two collections.
     * 
     * @param a The first collection.
     * @param b The second collection.
     * 
     * @return The disjunction of two collections.
     */
    public static ArrayList getDisjunction(Collection a, Collection b)
    {
        ArrayList disjunction = new ArrayList();
        Iterator iter;
        Object current;
        
        iter = a.iterator();
        while (iter.hasNext())
        {
            current = iter.next();

            if (!disjunction.contains(current))
            {
                disjunction.add(current);
            }
        }

        iter = b.iterator();
        while (iter.hasNext())
        {
            current = iter.next();

            if (!disjunction.contains(current))
            {
                disjunction.add(current);
            }
        }

        return (disjunction);
    }
    
    /**
     * Returns a list of all events enabled by <code>automaton</code>.
     * 
     * @param automaton The automaton to check.
     * 
     * @return A list of all events enabled by <code>automaton</code>.
     */
    public static ArrayList getEnabledEvents(AutomatonProxy automaton)
    {
        ArrayList enabledEvents = new ArrayList();

        Iterator transitionIter = automaton.getTransitions().iterator();
        while (transitionIter.hasNext())
        {
            enabledEvents.add(((TransitionProxy) transitionIter.next()).getEvent());
        }

        // Remove duplicates.
        enabledEvents = getDisjunction(new ArrayList(), enabledEvents);

        return (enabledEvents);
    }
    
    /**
     * Returns the first initial state of this automaton found.
     * 
     * @param automaton The automaton to search.
     * 
     * @return The first initial state of this automaton found or null if <code>automaton</code> does not contain any
     * initial states.
     */
    public static StateProxy getInitialState(AutomatonProxy automaton)
    {
        Iterator stateIter = automaton.getStates().iterator();
        StateProxy currentState;
        while (stateIter.hasNext())
        {
            currentState = (StateProxy) stateIter.next();

            if (currentState.isInitial())
            {
                return (currentState);
            }
        }

        return (null);
    }
    
    /**
     * Returns the state in <code>automaton</code> with the name <code>name</code> if one exists.
     * 
     * @param automaton The automaton to check.
     * @param name The name of the state to find.
     * 
     * @return The state in <code>automaton</code> with the name <code>name</code> if one exists, null otherwise.
     */
    public static StateProxy getStateWithName(AutomatonProxy automaton, String name)
    {
        Iterator stateIter = automaton.getStates().iterator();
        StateProxy currentState;
        while (stateIter.hasNext())
        {
            currentState = (StateProxy) stateIter.next();

            if (currentState.getName().compareTo(name) == 0)
            {
                return (currentState);
            }
        }

        return (null);
    }
}
