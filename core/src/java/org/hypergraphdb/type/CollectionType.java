/*
 * This file is part of the HyperGraphDB source distribution. This is copyrighted
 * software. For permitted uses, licensing options and redistribution, please see 
 * the LicensingInformation file at the root level of the distribution. 
 *
 * Copyright (c) 2005-2006
 *  Kobrix Software, Inc.  All rights reserved.
 */
package org.hypergraphdb.type;

import java.util.Collection;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGHandleFactory;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.LazyRef;

/**
 * <p>
 * A <code>CollectionType</code> instance is capable for managing collections
 * of objects in the HyperGraph store. Nothing is assumed about the elements in the
 * collection. In particular, the latter may be heterogenous. 
 * </p>
 *  
 * @author Borislav Iordanov
 */
public class CollectionType implements HGAtomType 
{
	private HyperGraph hg = null;
	private ObjectFactory<Collection<Object>> factory = null;
	
	public CollectionType(ObjectFactory<Collection<Object>> factory)
	{
		this.factory = factory;
	}
	
	public void setHyperGraph(HyperGraph hg) 
	{
		this.hg = hg;
	}

	public ObjectFactory getFactory()
	{
		return factory;
	}
	
	public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet, LazyRef<HGHandle[]> incidenceSet) 
	{
		Collection<Object> result;
		if (targetSet == null || targetSet.deref().length == 0)
			result = factory.make();
		else
			result = factory.make(targetSet.deref());
		TypeUtils.setValueFor(hg, handle, result);
		HGPersistentHandle [] layout = hg.getStore().getLink(handle);
		for (int i = 0; i < layout.length; i += 2)
		{
			Object current = null;			
			HGPersistentHandle typeHandle = layout[i];
			HGPersistentHandle valueHandle = layout[i+1];
			if (!typeHandle.equals(HGHandleFactory.nullHandle()))
			{
				HGAtomType type = hg.getTypeSystem().getType(typeHandle); 
				current = TypeUtils.makeValue(hg, valueHandle, type);
			}
			result.add(current);
		}
		return result;
	}

	public HGPersistentHandle store(Object instance) 
	{
		HGPersistentHandle result = TypeUtils.getNewHandleFor(hg, instance);
		Collection collection = (Collection)instance;
		HGPersistentHandle [] layout = new HGPersistentHandle[collection.size()*2];
		int pos = 0;
		for (Object curr : collection)
		{
			if (curr == null)
			{
				layout[pos++] = HGHandleFactory.nullHandle();
				layout[pos++] = HGHandleFactory.nullHandle();
			}
			else
			{
				HGHandle typeHandle = hg.getTypeSystem().getTypeHandle(curr.getClass());
				layout[pos++] = hg.getPersistentHandle(typeHandle);
				layout[pos++] = TypeUtils.storeValue(hg, 
													 curr, 
													 hg.getTypeSystem().getType(typeHandle));
			}
		}
		hg.getStore().store(result, layout);
		return result;
	}

	public void release(HGPersistentHandle handle) 
	{
//		TypeUtils.releaseValue(hg, handle);
		HGPersistentHandle [] layout = hg.getStore().getLink(handle);
		for (int i = 0; i < layout.length; i += 2)
		{		
			HGPersistentHandle typeHandle = layout[i];
			HGPersistentHandle valueHandle = layout[i+1];
			if (!TypeUtils.isValueReleased(hg, valueHandle) &&
				!typeHandle.equals(HGHandleFactory.nullHandle()))
			{
				TypeUtils.releaseValue(hg, valueHandle);
				hg.getTypeSystem().getType(typeHandle).release(valueHandle);
			}
		}
		hg.getStore().remove(handle);
	}

	public boolean subsumes(Object general, Object specific) 
	{
		return false;
	}
}