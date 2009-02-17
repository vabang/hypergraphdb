package org.hypergraphdb.peer;

import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.util.HGUtils;

/**
 * 
 * <p>
 * This is a simple data structure that represents a HyperGraphDB peer identity. The
 * class is intended for use only by the HGDB API which will attempt to ensure uniqueness.
 * To store info about other peer, use the derived <code>HGPeerIdentity</code> class which
 * is only different from this one by its type. 
 * </p>
 * 
 * @author Borislav Iordanov
 * 
 */
public class PrivatePeerIdentity
{
    private HGPersistentHandle id;
    private String hostname;
    private String ipAddress;
    private String graphLocation;
    private String name;

    public PrivatePeerIdentity()
    {
        
    }
    
    public HGPeerIdentity makePublicIdentity()
    {
        HGPeerIdentity pid = new HGPeerIdentity();
        pid.setId(id);
        pid.setHostname(hostname);
        pid.setIpAddress(ipAddress);
        pid.setGraphLocation(graphLocation);
        pid.setName(name);
        return pid;
    }
    
    public HGPersistentHandle getId()
    {
        return id;
    }

    public void setId(HGPersistentHandle id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getGraphLocation()
    {
        return graphLocation;
    }

    public void setGraphLocation(String graphLocation)
    {
        this.graphLocation = graphLocation;
    }
    
    public int hashCode()
    {
        return id == null ? 0 : id.hashCode();
    }
    
    public boolean equals(Object x)
    {
        if (! (x instanceof PrivatePeerIdentity))
            return false;
        else
            return HGUtils.eq(id, ((PrivatePeerIdentity)x).id);
    }
    
    public String toString()
    {
        return "HGPeerIdentity[" + id + "," + hostname + "," + 
            ipAddress + "," + graphLocation + "," + name + "]"; 
    }
}