package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestInsert {

	 public ParentTbl create(ParentTbl newParentTbl) {
	    	
	    	EntityManagerFactory emf =
	    		Persistence.createEntityManagerFactory("ParentChildTest");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	
	    /*	ChildTbl child = new ChildTbl();
	    	child.setChilddesc("child of parent 2");
	    	child.setParentTbl(newParentTbl);
	    	
	    	ChildTbl child2 = new ChildTbl();
	    	child2.setChilddesc("child of parent 2");
	    	child2.setParentTbl(newParentTbl);
	    	
	    	List<ChildTbl> lstChild = new ArrayList();
	    	lstChild.add(child);
	    	lstChild.add(child2);
	    	newParentTbl.setChildTbls(lstChild);*/
	    	
	    	Address addr = new Address();
	    	addr.setAddressdesc("addr 1");
	    	
	    	Person per = new Person();
	    	per.setName("sam");
	    	per.setAddress(addr);
	    	
	    	Person per2 = new Person();
	    	per2.setName("samar");
	    	per2.setAddress(addr);
	    	
	    	List<Person> persons = new ArrayList();
	    	persons.add(per);
	    	persons.add(per2);
	    	
			addr.setPersons(persons );
	    	
			em.getTransaction().begin();
	    	em.persist(newParentTbl);
//	    	em.persist(child);
//	    	em.persist(child2);
	    	em.persist(addr);
	    	em.persist(per2);
	    	em.persist(per);
	    	
	    	em.getTransaction().commit();
	    	return newParentTbl;
	    }

	    public static void main(String[] args){
	    	//setUpDatasource() ;
	    	ParentTbl parent = new ParentTbl();
	    	parent.setDescription("parent 1");
	    	System.out.println("ParentTbl id before creation:"
	    			+ parent.getParentid());
	    	
	    	
	    	
	    	//parent.setChildTbls(lstChild);
	    	
	    	TestInsert testInsert = new TestInsert();
	    	ParentTbl persistedParentTbl = testInsert.create(parent);
	    	System.out.println("ParentTbl id after creation:"
	    			+ persistedParentTbl.getParentid());
	    }

}
