/* Copyright (C) 2004 - 2005  db4objects Inc.  http://www.db4o.com */

package com.db4o.inside.replication;

import com.db4o.ext.*;

/**
 * @exclude
 */
public interface Db4oReplicationReference {
    
    public Db4oDatabase signaturePart();
    
    public long longPart();
    
    public long version();

}
