/* Copyright (C) 2006  Versant Inc.  http://www.db4o.com */

package com.db4o.db4ounit.common.assorted;

import com.db4o.*;
import com.db4o.query.*;

import db4ounit.*;
import db4ounit.extensions.*;


public class LongLinkedListTestCase extends AbstractDb4oTestCase {
	
	private static final int COUNT = 1000;
	
	public static class LinkedList{
		
		public LinkedList _next;
		
		public int _depth;
		
	}

	private static LinkedList newLongCircularList(){
		LinkedList head = new LinkedList();
		LinkedList tail = head;
		for (int i = 1; i < COUNT; i++) {
			tail._next = new LinkedList();
			tail = tail._next;
			tail._depth = i;
		}
		tail._next = head;
		return head;
	}
	
	public static void main(String[] args) throws Exception {
		new LongLinkedListTestCase().runSolo();
	}
	
	protected void store() throws Exception {
		store(newLongCircularList());
	}
	
	public void test(){
		Query q = newQuery(LinkedList.class);
		q.descend("_depth").constrain(new Integer(0));
		ObjectSet objectSet = q.execute();
		Assert.areEqual(1, objectSet.size());
		LinkedList head = (LinkedList) objectSet.next();
		db().activate(head, Integer.MAX_VALUE);
		assertListIsComplete(head);
		db().deactivate(head, Integer.MAX_VALUE);
		db().activate(head, Integer.MAX_VALUE);
		assertListIsComplete(head);
		db().deactivate(head, Integer.MAX_VALUE);
		db().refresh(head, Integer.MAX_VALUE);
		assertListIsComplete(head);
		
// TODO: The following produces a stack overflow. That's OK for now, peekPersisted is rarely
//		 used and users can control behaviour with the depth parameter. 
// 		 
//		LinkedList peeked = (LinkedList) db().ext().peekPersisted(head, Integer.MAX_VALUE, true);
//		assertListIsComplete(peeked);
		
	}
	
	private void assertListIsComplete(LinkedList head){
		int count = 1;
		LinkedList tail = head._next;
		while (tail != head){
			count++;
			tail = tail._next;
		}
		Assert.areEqual(COUNT, count);
	}

}
