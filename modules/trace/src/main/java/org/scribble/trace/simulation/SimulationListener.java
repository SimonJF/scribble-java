/*
 * Copyright 2009-14 www.scribble.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.scribble.trace.simulation;

import org.scribble.trace.model.Step;
import org.scribble.trace.model.Trace;

/**
 * This interface represents a simulation listener.
 *
 */
public interface SimulationListener {

	/**
	 * This method identifies when a trace simulation has started.
	 * 
	 * @param trace The trace
	 */
	public void start(Trace trace);
	
	/**
	 * This method identifies when the simulation of trace step has
	 * started.
	 * 
	 * @param trace The trace
	 * @param step The step
	 */
	public void start(Trace trace, Step step);
	
	/**
	 * This method identifies when the simulation of trace step has
	 * been successful.
	 * 
	 * @param trace The trace
	 * @param step The step
	 */
	public void successful(Trace trace, Step step);
	
	/**
	 * This method identifies when the simulation of trace step has
	 * been unsuccessful.
	 * 
	 * @param trace The trace
	 * @param step The step
	 */
	public void failed(Trace trace, Step step);
	
	/**
	 * This method identifies when a trace simulation has stopped.
	 * 
	 * @param trace The trace
	 */
	public void stop(Trace trace);
	
}
