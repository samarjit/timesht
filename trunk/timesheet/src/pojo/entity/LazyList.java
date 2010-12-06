package pojo.entity;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 
import javax.persistence.EntityManager;
import javax.persistence.Query;


 

import org.apache.openjpa.persistence.OpenJPAQuery;
 



/**
 * This is a list backed by a JPA Query, but only loading the results of the
 * query one page at a time. The same Query object is used for each page, but
 * starting at a different index. Results are cached and pages are loaded one 
 * page *ahead* for each fetch miss. If you are iterating through query results
 * backwards you will get poor performance and might prefer to change the
 * ORDER BY of your query instead.
 */
public class LazyList extends AbstractList {
  
    /** backing query */
    Query query;
    
    /** cache of loaded items */
    Map loaded;

    /** total number of results expected */
    long numResults;
    
    /** number of results to fetch on cache miss */
    int pageSize;
    
    /** default constructor */
    public LazyList() {
        loaded = new HashMap();
    }
    
    /**
     * Create a LazyList backed by the given query, using pageSize results
     * per page. The number of results expected is calculated by
     * reconstructing and executing an equivalent COUNT query when the list 
     * is created.
     */
    public LazyList(Query query, int pageSize, long numResults ) {
        this();
        this.query = query;
        this.pageSize = pageSize;
        this.numResults = numResults;
    }
    
    /**
     * Fetch an item, loading it from the query results if it hasn't already 
     * been.
     */
     public Object get(int i) {
         if (!loaded.containsKey(i)) {
             
             /* fetch the page required starting from this index */
             List results = query.setFirstResult(i).setMaxResults(pageSize)
                        .getResultList();
             for (int j = 0; j < results.size(); j++) {
                  loaded.put(i + j, results.get(j));
             }    
         }
         return loaded.get(i);
     }

     /** 
      * Return the total number of items in the list. This is calculated
      * using an equivalent COUNT query when the list is created.
      */
     public int size() {
         return (int) numResults;
     }
     
     /** update the number of results expected in this list */
     public void setNumResults(long numResults) {
         this.numResults = numResults;
     }
}