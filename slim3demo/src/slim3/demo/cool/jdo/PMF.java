package slim3.demo.cool.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {

    /**
     * The persistence manager factory.
     */
    private static PersistenceManagerFactory persistenceManagerFactory;

    static {
        initialize();
    }

    /**
     * Initializes this class.
     */
    private static void initialize() {
        persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory();
    }

    /**
     * Returns the persistence manager factory.
     * 
     * @return the persistence manager factory
     */
    public static PersistenceManagerFactory get() {
        return persistenceManagerFactory;
    }

    /**
     * Constructor.
     */
    private PMF() {
    }
}