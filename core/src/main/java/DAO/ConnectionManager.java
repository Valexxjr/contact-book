package DAO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionManager {

    static DataSource dataSource;

    static final String DB_JNDI = "java:comp/env/jdbc/ContactDB";

    static {
        dataSource = null;
        try {
            Context context = new InitialContext();
            Object res = context.lookup(DB_JNDI);
            if(res != null)
                dataSource = (DataSource) res;
        } catch (NamingException e) {
            // log error System.out.println("error");
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
