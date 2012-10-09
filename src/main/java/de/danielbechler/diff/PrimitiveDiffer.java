/*
 * Copyright 2012 Daniel Bechler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.danielbechler.diff;

import de.danielbechler.diff.node.*;

import static de.danielbechler.diff.Configuration.PrimitiveDefaultValueMode.*;

/** @author Daniel Bechler */
public class PrimitiveDiffer extends AbstractDiffer<Node>
{
	public PrimitiveDiffer(final DelegatingObjectDiffer delegatingObjectDiffer)
	{
		super(delegatingObjectDiffer);
	}

	@Override
	protected Node internalCompare(final Node parentNode, final Instances instances)
	{
		if (!instances.getType().isPrimitive())
		{
			throw new IllegalArgumentException("The primitive differ can only deal with primitive types.");
		}

		final Node node = newNode(parentNode, instances);
		if (getDelegate().isIgnored(node))
		{
			node.setState(Node.State.IGNORED);
		}
		else if (shouldTreatPrimitiveDefaultsAsUnassigned() && instances.hasBeenAdded())
		{
			node.setState(Node.State.ADDED);
		}
		else if (shouldTreatPrimitiveDefaultsAsUnassigned() && instances.hasBeenRemoved())
		{
			node.setState(Node.State.REMOVED);
		}
		else if (!instances.areEqual())
		{
			node.setState(Node.State.CHANGED);
		}
		
		return node;
	}

	private boolean shouldTreatPrimitiveDefaultsAsUnassigned()
	{
		return getDelegate().getConfiguration().getPrimitiveDefaultValueMode() == UNASSIGNED;
	}

	@Override
	protected Node newNode(final Node parentNode, final Instances instances)
	{
		return new DefaultNode(parentNode, instances.getSourceAccessor(), instances.getType());
	}
}