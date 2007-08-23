package org.hypergraphdb.query;

import java.util.HashMap;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.type.HGAtomType;
import org.hypergraphdb.type.HGProjection;
import org.hypergraphdb.type.TypeUtils;
import org.hypergraphdb.util.HGUtils;

/**
 * <p>
 * An <code>AtomProjectionCondition</code> will yield all atoms that are
 * projections along a certain dimension of a given base atom set. The
 * base atom set is specified as a <code>HGQueryCondition</code>. 
 * </p>
 *  
 * <p>
 * The <code>satisfies</code> will work, but it is not intended to be
 * used frequently (e.g. in a loop) since it will execute the condition
 * defined for the base atom set each
 * </p>
 * 
 * @author Borislav Iordanov
 */
public class AtomProjectionCondition implements HGQueryCondition, HGAtomPredicate 
{
	private String [] dimensionPath;
	private HGQueryCondition baseSetCondition;
	private HashMap<HGHandle, HGHandle> baseSet = null;
	
	public AtomProjectionCondition(String dimensionPath, HGQueryCondition baseSetCondition)
	{
		this.dimensionPath = TypeUtils.parseDimensionPath(dimensionPath);
		this.baseSetCondition = baseSetCondition;
		 
	}
	
	public AtomProjectionCondition(String [] dimensionPath, HGQueryCondition baseSetCondition)
	{
		this.dimensionPath = dimensionPath;
		this.baseSetCondition = baseSetCondition;
		 
	}
	
	public String [] getDimensionPath()
	{
		return this.dimensionPath;
	}
	
	public HGQueryCondition getBaseSetCondition()
	{
		return this.baseSetCondition;
	}
	
	public boolean satisfies(HyperGraph graph, HGHandle handle) 
	{
		if (baseSet == null)
		{
			baseSet = new HashMap<HGHandle, HGHandle>();
			HGSearchResult<HGHandle> rs = null;
			try
			{
				rs = graph.find(baseSetCondition);
				while (rs.hasNext())
				{
					HGHandle h = rs.next();
					HGAtomType ot = graph.getTypeSystem().getAtomType(h);
					HGProjection proj = TypeUtils.getProjection(graph, ot, dimensionPath);
					if (proj != null)					
					{
						Object part = proj.project(graph.get(h));
						if (part != null)
							baseSet.put(graph.getHandle(part), h);
					}
				}
			}
			finally
			{
				HGUtils.closeNoException(rs);
			}
		}		
		return baseSet.containsKey(handle);
	}
}