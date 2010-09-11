package org.hypergraphdb.handle;

import org.hypergraphdb.HGHandleFactory;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.storage.BAUtils;

public class IntHandleFactory implements HGHandleFactory
{

    private static final IntPersistentHandle any = new IntPersistentHandle(1);
    private static final IntPersistentHandle nil = new IntPersistentHandle(0);
    
    private int next = 1000;
     
    public int getNext()
    {
        return next;
    }

    public void setNext(int next)
    {
        this.next = next;
    }

    public HGPersistentHandle anyHandle()
    {
        return any;
    }

    public HGPersistentHandle makeHandle()
    {
        return new IntPersistentHandle(next++);
    }

    public HGPersistentHandle makeHandle(String handleAsString)
    {
        return new IntPersistentHandle(Integer.parseInt(handleAsString));
    }

    public HGPersistentHandle makeHandle(byte[] buffer)
    {
        return new IntPersistentHandle(BAUtils.readInt(buffer, 0));
    }

    public HGPersistentHandle makeHandle(byte[] buffer, int offset)
    {
        return new IntPersistentHandle(BAUtils.readInt(buffer, offset));
    }

    public HGPersistentHandle nullHandle()
    {
        return nil;
    }
}