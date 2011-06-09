package slim3.demo.cool.service;

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

    public List<Entity> getBarListUsingLL() {
        AsyncDatastoreService ds =
            DatastoreServiceFactory.getAsyncDatastoreService();
        Query q = new Query("Bar");
        PreparedQuery pq = ds.prepare(q);
        List<Entity> list =
            pq.asList(FetchOptions.Builder.withDefaults().limit(
                Integer.MAX_VALUE));
        list.size();
        return list;
    }

    public List<Bar> getBarListUsingSlim3() {
        List<Bar> list = Datastore.query(Bar.class).asList();
        list.size();
        return list;
    }

    public List<BarObjectify> getBarListUsingObjectify() {
        Objectify ofy = ObjectifyService.begin();
        List<BarObjectify> list = ofy.query(BarObjectify.class).list();
        list.size();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<BarJDO> getBarListUsingJDO() {
        List<BarJDO> list = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            javax.jdo.Query q = pm.newQuery(BarJDO.class);
            list = (List<BarJDO>) q.execute();
            list = (List<BarJDO>) pm.detachCopyAll(list);
        } finally {
            pm.close();
        }
        list.size();
        return list;
    }
}