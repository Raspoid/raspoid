/*******************************************************************************
 * Copyright (c) 2016 Julien Louette & Gaël Wittorski
 * 
 * This file is part of Raspoid.
 * 
 * Raspoid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Raspoid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Raspoid.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.raspoid.behavioral;

import java.io.Serializable;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.raspoid.exceptions.RaspoidBehavioralException;

/**
 * Implementation of a simple arbitrator scheme between behaviors.
 * The behaviors are checked regularly and the behavior with higher priority is executed.
 * The monitor delay can be set with a custom value to better fit the needs. A behavior
 * with a reference to the arbitrator can also trigger an immediate priority check.
 *
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SimpleArbitrator implements Arbitrator {

    /**
     * Contains the default monitor delay
     */
    private static final int DEFAULT_MONITOR_DELAY = 10;

    /**
     * Contains a comparator to compare the behaviors by their priorities
     */
    private BehaviorComparator comparator = new BehaviorComparator();

    /**
     * Contains a sorted set of behaviors sorted by their priorities
     */
    private SortedSet<Behavior> behaviors = new TreeSet<>(comparator);

    /**
     * Contains the priority monitor thread executing regularly
     */
    private ScheduledExecutorService priorityMonitor = Executors.newSingleThreadScheduledExecutor();

    /**
     * Contains the behavior execution thread for the one in control
     */
    private ExecutorService behaviorExecutor = Executors.newSingleThreadExecutor();

    /**
     * Contains a future object for retrieving a value when
     * a behavior has finished executing
     */
    private Future<?> behaviorResult;

    /**
     * Contains the current behavior in control
     */
    private Behavior currentBehavior = null;

    /**
     * Contains the monitor delay used for checking
     * the priorities regularly
     */
    private final int monitorDelay;

    /**
     * Creates a simple arbitrator with
     * the default monitor delay
     */
    public SimpleArbitrator() {
        this(DEFAULT_MONITOR_DELAY);
    }

    /**
     * Creates a simple arbitrator with
     * a custom monitor delay
     * @param monitorDelay the monitor delay to use in milliseconds
     */
    public SimpleArbitrator(int monitorDelay) {
        if (monitorDelay <= 0)
            throw new RaspoidBehavioralException("The monitor delay should be greater than 0");
        this.monitorDelay = monitorDelay;
    }

    @Override
    public void start() {
        priorityMonitor.scheduleAtFixedRate(() -> checkPriority(), 0, monitorDelay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        priorityMonitor.shutdown();
    }

    /**
     * Traverse through the behaviors while checking their priorities
     * and ensures the behavior with higher priority executes
     */
    private void checkPriority() {
        for (Behavior behavior : behaviors) {
            boolean claimsControl = behavior.claimsControl();
            // Current behavior reached, it's running do nothing
            if (claimsControl && behavior == currentBehavior && !behaviorResult.isDone()) {
                return;
            } else if (claimsControl) {
                // Current behavior not reached yet or not claiming,
                // and higher priority behavior is claiming control
                if (currentBehavior != null) {
                    // Ask current behavior to yield control
                    currentBehavior.yieldControl();
                    try {
                        behaviorResult.get(); //Ensure previous behavior finished
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RaspoidBehavioralException("The behavior execution ended unexpectedly", e);
                    }
                }
                //Reset the behavior so that shouldYield is false
                behavior.reset();
                //Set the current behavior and enqueue it in execution thread
                currentBehavior = behavior;
                behaviorResult = behaviorExecutor.submit(() -> behavior.gainControl());
                return;
            }
        }
    }

    @Override
    public void immediateClaim() {
        priorityMonitor.submit(() -> checkPriority());
    }

    /**
     * Add a behavior to the arbitration process
     * @param behavior the behavior to be added
     */
    public void addBehavior(Behavior behavior){
        behaviors.add(behavior);
    }

    /**
     * Add a behavior with a defined priority to the arbitrator process
     * @param behavior the behavior to be added
     * @param priority the priority of the behavior
     * (overrides the priority defined by the behavior implementation)
     */
    public void addBehavior(Behavior behavior, int priority){
        behaviors.add(new BehaviorProxy(behavior, priority));
    }

    /**
     * Removes a behavior from the arbitrator process
     * @param behavior the behavior to be removed.
     */
    public void removeBehavior(Behavior behavior){
        behaviors.remove(behavior);
    }

    /**
     * Behavior comparator using their priority to compare them
     * 
     * @author Julien Louette &amp; Ga&euml;l Wittorski
     * @version 1.0
     */
    private static class BehaviorComparator implements Comparator<Behavior>, Serializable {

        /**
         * The serialization key used to serialize these Pin instances.
         */
        private static final long serialVersionUID = 42918029371924L;

        @Override
        public int compare(Behavior behavior1, Behavior behavior2) {
            return behavior2.getPriority() - behavior1.getPriority();
        }

    }

    /**
     * Behavior proxy class used to override the priority of a behavior
     * 
     * @author Julien Louette &amp; Ga&euml;l Wittorski
     * @version 1.0
     */
    private static class BehaviorProxy implements Behavior {

        /**
         * Contains the behavior underneath
         */
        private Behavior behavior;

        /**
         * Contains the overriding priority
         */
        private int priority;


        /**
         * Creates a new behavior proxy with the underneath behavior
         * along with the overriding priority
         * @param behavior the behavior underneath
         * @param priority the overriding priority
         */
        public BehaviorProxy(Behavior behavior, int priority) {
            this.behavior = behavior;
            this.priority = priority;
        }

        @Override
        public boolean claimsControl() {
            return behavior.claimsControl();
        }

        @Override
        public void gainControl() {
            behavior.gainControl();
        }

        @Override
        public void yieldControl() {
            behavior.yieldControl();
        }

        @Override
        public int getPriority() {
            return priority; //Overriding priority
        }

        @Override
        public void reset() {
            behavior.reset();
        }

    }

}
