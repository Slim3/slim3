package slim3.demo.cool.service;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.slim3.datastore.Datastore;

import slim3.demo.cool.jdo.PMF;
import slim3.demo.cool.model.Bar;
import slim3.demo.cool.model.BarJDO;
import slim3.demo.cool.model.BarObjectify;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class PerformanceService {

    static {
        ObjectifyService.register(BarObjectify.class);
    }

    public Iterator<Entity> getBarsUsingLL() {
        AsyncDatastoreService ds =
            DatastoreServiceFactory.getAsyncDatastoreService();
        Query q = new Query("Bar");
        PreparedQuery pq = ds.prepare(q);
        return pq.asIterator(FetchOptions.Builder.withDefaults().limit(
            Integer.MAX_VALUE));
    }

    public Iterator<Bar> getBarsUsingSlim3() {
        return Datastore.query(Bar.class).asIterator();
    }

    public Iterator<BarObjectify> getBarsUsingObjectify() {
        Objectify ofy = ObjectifyService.begin();
        return ofy.query(BarObjectify.class).iterator();
    }

    @SuppressWarnings("unchecked")
    public List<BarJDO> getBarsUsingJDO() {
        List<BarJDO> list = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            javax.jdo.Query q = pm.newQuery(BarJDO.class);
            list = (List<BarJDO>) q.execute();
            list = (List<BarJDO>) pm.detachCopyAll(list);
        } finally {
            pm.close();
        }
        return list;
    }
}