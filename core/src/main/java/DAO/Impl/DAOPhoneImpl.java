package DAO.Impl;

import DAO.ConnectionManager;
import DAO.DAOPhone;
import model.NumberType;
import model.Phone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPhoneImpl implements DAOPhone {
    private static Logger logger = LogManager.getLogger(DAOPhoneImpl.class);
    DataSource dataSource;

    private static final String GET_PHONES_SQL = "select id, country_code, operator_code," +
            " phone_number, type, note from phone";

    private static final String GET_PHONES_BY_CONTACT_SQL = "select id, country_code, operator_code, " +
            "phone_number, type, note from phone where contact_id = ?";

    private static final String INSERT_PHONE_SQL = "insert into phone (contact_id, phone_number, type, note, " +
            "country_code, operator_code) values (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_PHONE_SQL = "update phone set phone_number = ?, type = ?, note = ?, " +
            "country_code = ?, operator_code = ? where id = ?";

    private static final String DELETE_PHONE_SQL = "delete from phone where id = ?";


    public DAOPhoneImpl() {
        dataSource = new ConnectionManager().getDataSource();
    }

    public List<Phone> getAll() {
        Statement statement = null;
        Connection connection = null;
        List<Phone> phones = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_PHONES_SQL);
            phones = new ArrayList<Phone>();
            while (rs.next()) {
                Phone phone = new Phone();
                phone.setId(rs.getInt(1));
                phone.setCountryCode(rs.getString(2));
                phone.setOperatorCode(rs.getString(3));
                phone.setNumber(rs.getString(4));
                phone.setNumberType(NumberType.valueOf(rs.getString(5).toUpperCase()));
                phone.setNote(rs.getString(6));
                phones.add(phone);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
            return phones;
        }
    }

    public List<Phone> getAllByContact(Integer contactId) {
        PreparedStatement statement = null;
        Connection connection = null;
        List<Phone> phones = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(GET_PHONES_BY_CONTACT_SQL);
            statement.setInt(1, contactId);
            ResultSet rs = statement.executeQuery();
            phones = new ArrayList<Phone>();
            while (rs.next()) {
                Phone phone = new Phone();
                phone.setId(rs.getInt(1));
                phone.setCountryCode(rs.getString(2));
                phone.setOperatorCode(rs.getString(3));
                phone.setNumber(rs.getString(4));
                phone.setNumberType(NumberType.valueOf(rs.getString(5).toUpperCase()));
                phone.setNote(rs.getString(6));
                phones.add(phone);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
            return phones;
        }
    }

    public boolean insertPhones(Integer contactId, Phone[] phones) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for(Phone phone: phones) {
                statement = connection.prepareStatement(INSERT_PHONE_SQL);
                statement.setInt(1, contactId);
                statement.setString(2, phone.getNumber());
                statement.setString(3, phone.getNumberType().toString().toLowerCase());
                statement.setString(4, phone.getNote());
                statement.setString(5, phone.getCountryCode());
                statement.setString(6, phone.getOperatorCode());

                statement.executeUpdate();
            }
            connection.commit();
            logger.info("Phones inserted");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while inserting phones - rollback");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }

        }
        return isSuccessful;
    }

    public boolean updatePhones(Phone[] phones) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for(Phone phone: phones) {
                statement = connection.prepareStatement(UPDATE_PHONE_SQL);
                statement.setString(1, phone.getNumber());
                statement.setString(2, phone.getNumberType().toString().toLowerCase());
                statement.setString(3, phone.getNote());
                statement.setString(4, phone.getCountryCode());
                statement.setString(5, phone.getOperatorCode());
                statement.setInt(6, phone.getId());

                statement.executeUpdate();
            }

            connection.commit();
            logger.info("Phones updated");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while updating phones - rollback");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }

        }
        return isSuccessful;
    }

    public boolean deletePhones(Phone[] phones) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for(Phone phone: phones) {
                statement = connection.prepareStatement(DELETE_PHONE_SQL);
                statement.setInt(1, phone.getId());
                statement.executeUpdate();
            }

            connection.commit();
            logger.info("Phones deleted");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while deleting phones - rollback");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }

        }
        return isSuccessful;
    }

}
