package com.sh0rtcut.data;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.*;

public class Connector
{

	private static final String CON_URL = "";
	
    public Connector()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("Problem finding driver com.mysql.jdbc.Driver");
        }
    }

    private PrintWriter getBufferedPrintWriterForFile(String fileName)
    {
        PrintWriter writer = null;
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter buffer = new BufferedWriter(fileWriter);
            writer = new PrintWriter(buffer);
        }
        catch(Exception ex)
        {
            System.err.println((new StringBuilder("Exception during creation of buffered print writer for file ")).append(fileName).toString());
        }
        return writer;
    }

    private void writeExceptionChainToWriter(SQLException ex, PrintWriter writer)
    {
        try
        {
            for(; ex != null; ex = ex.getNextException())
            {
                writer.println("SQL Exception");
                writer.println((new StringBuilder("Error Code: ")).append(ex.getErrorCode()).toString());
                writer.println((new StringBuilder("Message: ")).append(ex.getMessage()).toString());
                ex.printStackTrace(writer);
            }

        }
        catch(Exception e)
        {
            System.err.println("Exception during write of Exception chain in DatabaseManager.java");
        }
    }

    public void handleSQLException(SQLException ex)
    {
        Date now = new Date();
        String fileName = (new StringBuilder("/data/tomcat/webapps/examTestErrors/sqlException-")).append(now.getTime()).append(".txt").toString();
        try
        {
            PrintWriter writer = getBufferedPrintWriterForFile(fileName);
            writeExceptionChainToWriter(ex, writer);
            writer.close();
        }
        catch(Exception e)
        {
            System.err.println("exception during write to smileErrors directory sqlException");
        }
    }

    public void handleException(Exception ex)
    {
        Date now = new Date();
        String fileName = (new StringBuilder("/data/tomcat/webapps/examTestErrors/Exception-")).append(now.getTime()).append(".txt").toString();
        try
        {
            PrintWriter writer = getBufferedPrintWriterForFile(fileName);
            ex.printStackTrace(writer);
            writer.close();
        }
        catch(Exception e)
        {
            System.err.println("exception during write to smileErrors directory sqlException");
        }
    }

    public static Connector getInstance()
    {
        if(connector == null)
            connector = new Connector();
        return connector;
    }

    public boolean fillStringListsFromQuery(ArrayList<ArrayList<String>> pop, String query, int cols)
    {
        boolean flag = true;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            statement = connection.createStatement();
            for(rs = statement.executeQuery(query); rs.next(); )
            {
                buffer = new ArrayList<String>();
                for(int i = 0; i < cols; i++)
                {
                    buffer.add(rs.getString(i + 1));
                }

                pop.add(buffer);
            }

            rs.close();
            statement.close();
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println((new StringBuilder("Error in fillListArrayForQuery: ")).append(ex).toString());
            handleSQLException(ex);
            flag = false;
        }
        return flag;
    }

    public int updateTable(String statement) 
    {
        Connection connection = null;
        int numRows = 0;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            Statement s = connection.createStatement();
            numRows = s.executeUpdate(statement);
            s.close();
            connection.close();
        }
        catch(SQLException e)
        {
            System.out.println((new StringBuilder("Error in updateTable: ")).append(e).toString());
            handleSQLException(e);
        }
        return numRows;
    }

    public List<String> getResultList(String query, int num)
    {
        List<String> resultList = new ArrayList<String>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while(rs.next()) 
            {
                String temp = null;
                for(int i = 1; i <= num; i++)
                {
                    temp = rs.getString(i);
                    resultList.add(temp);
                }

            }
            rs.close();
            statement.close();
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println((new StringBuilder("Error in getResultList for query  ")).append(query).append(" ").append(ex).toString());
        }
        return resultList;
    }

    public List<String> getResultColumnVector(String query)
    {
        List<String> resultList = new ArrayList<String>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            String temp;
            for(; rs.next(); resultList.add(temp))
            {
                temp = new String();
                temp = rs.getString(1);
            }

            rs.close();
            statement.close();
            connection.close();
        }
        catch(SQLException sqlexception) { }
        return resultList;
    }

    public int fillStringArrayForQuery(String stringArray[], String query)
    {
        int index = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        String temp = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            statement = connection.createStatement();
            for(rs = statement.executeQuery(query); rs.next();)
            {
                temp = rs.getString(1);
                if(temp != null)
                    stringArray[index++] = temp;
            }

            rs.close();
            statement.close();
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println((new StringBuilder("Error in fillStringArrayForQuery: ")).append(ex).toString());
        }
        return index;
    }

    public String retrieveItemFromTable(String query)
    {
        String q = null;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            Statement s = connection.createStatement();
            s.execute(query);
            ResultSet rs = s.getResultSet();
            if(rs.next())
                q = rs.getString(1);
            else
                System.out.println("Error: ResultSet = null");
            rs.close();
            s.close();
            connection.close();
        }
        catch(SQLException e)
        {
            System.out.println((new StringBuilder("Error in RetrieveItemFromTable: ")).append(e).toString());
            handleSQLException(e);
        }
        return q;
    }

    public int getRandomID(String table_name)
    {
        int q = 0;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            Statement s = connection.createStatement();
            s.execute((new StringBuilder("select count(*) from ")).append(table_name).toString());
            ResultSet rs = s.getResultSet();
            if(rs.next())
                q = rs.getInt(1);
            else
                System.out.println("Error: ResultSet = null");
            Random generator = new Random();
            q = generator.nextInt(q);
            rs.close();
            s.close();
            connection.close();
        }
        catch(SQLException e)
        {
            System.out.println((new StringBuilder("Error in GetRandomID: ")).append(e).toString());
            handleSQLException(e);
        }
        return q;
    }

    public int getMaxID(String tableName)
    {
        int q = 0;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            Statement s = connection.createStatement();
            s.execute((new StringBuilder("select MAX(")).append(tableName).append("ID").append(") from ").append(tableName).toString());
            ResultSet rs = s.getResultSet();
            if(rs.next())
                q = rs.getInt(1);
            else
                System.out.println("Error: ResultSet = null");
            rs.close();
            s.close();
            connection.close();
        }
        catch(SQLException e)
        {
            System.out.println((new StringBuilder("Error in GetRandomID: ")).append(e).toString());
            handleSQLException(e);
        }
        return q;
    }

    public int getNextID(String tableName)
    {
        int maxID = getMaxID(tableName);
        return maxID + 1;
    }

    public List<String> getIDList(String query)
    {
        List<String> idList = new ArrayList<String>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            statement = connection.createStatement();
            for(rs = statement.executeQuery(query); rs.next(); idList.add(rs.getString(1)));
            rs.close();
            statement.close();
            connection.close();
        }
        catch(SQLException ex)
        {
            System.out.println("Error during get id list");
            handleSQLException(ex);
        }
        return idList;
    }

    public List<String> getIDList(String tableName, String colName, int value)
    {
        String query = (new StringBuilder("SELECT ")).append(tableName).append("ID FROM ").append(tableName).append(" WHERE ").append(colName).append("=").append(value).toString();
        List<String> idList = getIDList(query);
        return idList;
    }

    public List<String> getIDListForLessThanOrEqual(String tableName, String colName, int value)
    {
        String query = (new StringBuilder("SELECT ")).append(tableName).append("ID FROM ").append(tableName).append(" WHERE ").append(colName).append("<=").append(value).toString();
        List<String> idList = getIDList(query);
        return idList;
    }

    public List<String> getIDListForGreaterThanOrEqual(String tableName, String colName, int value)
    {
        List<String> idList = new ArrayList<String>();
        String query = (new StringBuilder("SELECT ")).append(tableName).append("ID FROM ").append(tableName).append(" WHERE ").append(colName).append(">=").append(value).toString();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            statement = connection.createStatement();
            for(rs = statement.executeQuery(query); rs.next(); idList.add(rs.getString(1)));
            rs.close();
            statement.close();
            connection.close();
        }
        catch(Exception ex)
        {
            System.out.println("Error during get id list");
        }
        return idList;
    }

    public int[] retrieveIDnumber(String table_name, String qual, int value)
    {
        int q[] = new int[5];
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(CON_URL);
            Statement s = connection.createStatement();
            s.execute((new StringBuilder("select ")).append(table_name).append("ID from ").append(table_name).append(" where ").append(qual).append(" = ").append(value).toString());
            ResultSet rs = s.getResultSet();
            for(int i = 0; i < 5; i++)
            {
                rs.next();
                q[i] = rs.getInt(1);
            }

            rs.close();
            s.close();
            connection.close();
        }
        catch(SQLException e)
        {
            System.out.println((new StringBuilder("Error in RetrieveIDnumber: ")).append(e).toString());
            handleSQLException(e);
        }
        return q;
    }



    private static Connector connector = null;
    private ArrayList<String> buffer;

}


