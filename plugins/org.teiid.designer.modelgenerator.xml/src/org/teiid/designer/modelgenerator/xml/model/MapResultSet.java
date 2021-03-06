/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.modelgenerator.xml.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.teiid.designer.modelgenerator.xml.XmlImporterUiPlugin;

/**
 * @since 8.0
 */
public class MapResultSet implements ResultSet {

    List values; // a list of lists
    Map names; // mapping names to indexes
    String[] reverseNames; // mapping indexes to names;
    int row;
    boolean wasNull;
    MetaData metaData;

    public MapResultSet( List values,
                         Map names ) {
        super();
        this.values = Collections.unmodifiableList(values);
        this.names = names;
        reverseNames = new String[names.size() + 1];
        for (Iterator iter = names.keySet().iterator(); iter.hasNext();) {
            Object o = iter.next();
            String name = (String)o;
            Number n = (Number)names.get(name);
            int column = n.intValue();
            reverseNames[column] = name;
        }
        row = -1;
        wasNull = false;
        metaData = new MetaData();
    }

    /**
     * @see java.sql.ResultSet#getConcurrency()
     */
    @Override
	public int getConcurrency() {
        return CONCUR_READ_ONLY;
    }

    /**
     * @see java.sql.ResultSet#getFetchDirection()
     */
    @Override
	public int getFetchDirection() {
        return FETCH_FORWARD;
    }

    /**
     * @see java.sql.ResultSet#getFetchSize()
     */
    @Override
	public int getFetchSize() {
        return 1;
    }

    /**
     * @see java.sql.ResultSet#getRow()
     */
    @Override
	public int getRow() {
        return row + 1;
    }

    /**
     * @see java.sql.ResultSet#getType()
     */
    @Override
	public int getType() {
        return TYPE_FORWARD_ONLY;
    }

    /**
     * @see java.sql.ResultSet#afterLast()
     */
    @Override
	public void afterLast() {
        row = values.size();
    }

    /**
     * @see java.sql.ResultSet#beforeFirst()
     */
    @Override
	public void beforeFirst() {
        row = -1;
    }

    /**
     * @see java.sql.ResultSet#cancelRowUpdates()
     */
    @Override
	public void cancelRowUpdates() {
    }

    /**
     * @see java.sql.ResultSet#clearWarnings()
     */
    @Override
	public void clearWarnings() {
    }

    /**
     * @see java.sql.ResultSet#close()
     */
    @Override
	public void close() {
    }

    /**
     * @see java.sql.ResultSet#deleteRow()
     */
    @Override
	public void deleteRow() throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#insertRow()
     */
    @Override
	public void insertRow() throws SQLException {
        throwReadOnly();
    }

    private void throwReadOnly() throws SQLException {
        throw new SQLException(XmlImporterUiPlugin.getDefault().getPluginUtil().getString("MapResultSet.readOnly")); //$NON-NLS-1$
    }

    private void throwNotImplemented() throws SQLException {
        throw new SQLException(XmlImporterUiPlugin.getDefault().getPluginUtil().getString("MapResultSet.notImplemented")); //$NON-NLS-1$
    }

    /**
     * @see java.sql.ResultSet#moveToCurrentRow()
     */
    @Override
	public void moveToCurrentRow() {
    }

    /**
     * @see java.sql.ResultSet#moveToInsertRow()
     */
    @Override
	public void moveToInsertRow() throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#refreshRow()
     */
    @Override
	public void refreshRow() throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateRow()
     */
    @Override
	public void updateRow() throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#first()
     */
    @Override
	public boolean first() {
        row = 0;
        return values.size() > 0;
    }

    /**
     * @see java.sql.ResultSet#isAfterLast()
     */
    @Override
	public boolean isAfterLast() {
        return row >= values.size();
    }

    /**
     * @see java.sql.ResultSet#isBeforeFirst()
     */
    @Override
	public boolean isBeforeFirst() {
        return row < 0;
    }

    /**
     * @see java.sql.ResultSet#isFirst()
     */
    @Override
	public boolean isFirst() {
        return row == 0;
    }

    /**
     * @see java.sql.ResultSet#isLast()
     */
    @Override
	public boolean isLast() {
        return row == values.size() - 1;
    }

    /**
     * @see java.sql.ResultSet#last()
     */
    @Override
	public boolean last() {
        row = values.size() - 1;
        return values.size() > 0;
    }

    /**
     * @see java.sql.ResultSet#next()
     */
    @Override
	public boolean next() {
        ++row;
        return !isAfterLast();
    }

    /**
     * @see java.sql.ResultSet#previous()
     */
    @Override
	public boolean previous() {
        ++row;
        return !isBeforeFirst();
    }

    /**
     * @see java.sql.ResultSet#rowDeleted()
     */
    @Override
	public boolean rowDeleted() {
        return false;
    }

    /**
     * @see java.sql.ResultSet#rowInserted()
     */
    @Override
	public boolean rowInserted() {
        return false;
    }

    /**
     * @see java.sql.ResultSet#rowUpdated()
     */
    @Override
	public boolean rowUpdated() {
        return false;
    }

    /**
     * @see java.sql.ResultSet#wasNull()
     */
    @Override
	public boolean wasNull() {
        return wasNull;
    }

    /**
     * @see java.sql.ResultSet#getByte(int)
     */
    @Override
	public byte getByte( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return 0;
        }
        return new Byte(str).byteValue();
    }

    /**
     * @see java.sql.ResultSet#getDouble(int)
     */
    @Override
	public double getDouble( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return 0;
        }
        return new Double(str).doubleValue();
    }

    /**
     * @see java.sql.ResultSet#getFloat(int)
     */
    @Override
	public float getFloat( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return 0;
        }
        return new Float(str).floatValue();
    }

    /**
     * @see java.sql.ResultSet#getInt(int)
     */
    @Override
	public int getInt( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return 0;
        }
        return new Integer(str).intValue();
    }

    /**
     * @see java.sql.ResultSet#getLong(int)
     */
    @Override
	public long getLong( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return 0;
        }
        return new Long(str).longValue();
    }

    /**
     * @see java.sql.ResultSet#getShort(int)
     */
    @Override
	public short getShort( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return 0;
        }
        return new Short(str).shortValue();
    }

    /**
     * @see java.sql.ResultSet#setFetchDirection(int)
     */
    @Override
	public void setFetchDirection( int direction ) {
    }

    /**
     * @see java.sql.ResultSet#setFetchSize(int)
     */
    @Override
	public void setFetchSize( int rows ) {
    }

    /**
     * @see java.sql.ResultSet#updateNull(int)
     */
    @Override
	public void updateNull( int columnIndex ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#absolute(int)
     */
    @Override
	public boolean absolute( int row ) {
        if (row > 0) {
            this.row = row - 1;
        } else {
            this.row = values.size() + row;
        }
        return !isBeforeFirst() && !isAfterLast();
    }

    /**
     * @see java.sql.ResultSet#getBoolean(int)
     */
    @Override
	public boolean getBoolean( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return false;
        }
        return new Boolean(str).booleanValue();
    }

    /**
     * @see java.sql.ResultSet#relative(int)
     */
    @Override
	public boolean relative( int rows ) {
        this.row += rows;
        return !isBeforeFirst() && !isAfterLast();
    }

    /**
     * @see java.sql.ResultSet#getBytes(int)
     */
    @Override
	public byte[] getBytes( int columnIndex ) {

        Object o = getObject(columnIndex);
        wasNull = (o == null);
        if (o instanceof byte[]) {
            return (byte[])o;
        }
        if (wasNull) {
            return new byte[0];
        }
        return o.toString().getBytes();
    }

    /**
     * @see java.sql.ResultSet#updateByte(int, byte)
     */
    @Override
	public void updateByte( int columnIndex,
                            byte x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateDouble(int, double)
     */
    @Override
	public void updateDouble( int columnIndex,
                              double x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateFloat(int, float)
     */
    @Override
	public void updateFloat( int columnIndex,
                             float x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateInt(int, int)
     */
    @Override
	public void updateInt( int columnIndex,
                           int x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateLong(int, long)
     */
    @Override
	public void updateLong( int columnIndex,
                            long x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateShort(int, short)
     */
    @Override
	public void updateShort( int columnIndex,
                             short x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateBoolean(int, boolean)
     */
    @Override
	public void updateBoolean( int columnIndex,
                               boolean x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateBytes(int, byte[])
     */
    @Override
	public void updateBytes( int columnIndex,
                             byte[] x ) throws SQLException {
        throwReadOnly();
    }

    static class ReaderInputStream extends InputStream {
        // Why on earth is there not a built in class to do this?
        private Reader reader;
        private static final String ASCII = "US-ASCII"; //$NON-NLS-1$
        private static final String UNICODE = "UTF16"; //$NON-NLS-1$
        private String charset;
        private byte[] buffer;

        public ReaderInputStream( Reader reader,
                                  String charset ) {
            super();
            this.reader = reader;
            this.charset = charset;
            buffer = new byte[0];
        }

        // implement the read() method to make this all work
        @Override
        public int read() throws IOException {
            if (buffer.length > 0) {
                int retval = buffer[0];
                byte[] newBytes = new byte[buffer.length - 1];
                System.arraycopy(buffer, 1, newBytes, 0, newBytes.length);
                buffer = newBytes;
                return retval;
            }

            int t = reader.read();
            if (t <= 0) {
                return t;
            }

            buffer = new Character((char)t).toString().getBytes(charset);
            if (buffer.length == 0) {
                return -1;
            }

            int retval = read(); // now we have something in the buffer we call call recursively
            return retval;
        }
    }

    /**
     * @see java.sql.ResultSet#getAsciiStream(int)
     */
    @Override
	public InputStream getAsciiStream( int columnIndex ) throws SQLException {
        Reader reader = getCharacterStream(columnIndex);
        wasNull = (reader == null);
        if (wasNull) {
            return null;
        }

        return new ReaderInputStream(reader, ReaderInputStream.ASCII);
    }

    /**
     * @see java.sql.ResultSet#getBinaryStream(int)
     */
    @Override
	public InputStream getBinaryStream( int columnIndex ) {
        Object o = getObject(columnIndex);
        wasNull = (o == null);
        if (wasNull) {
            return null;
        }
        if (o instanceof InputStream) {
            return (InputStream)o;
        }
        if (o instanceof Reader) {
            return new ReaderInputStream((Reader)o, ReaderInputStream.UNICODE);
        }

        byte[] bytes = getBytes(columnIndex);
        return new ByteArrayInputStream(bytes);
    }

    /**
     * @see java.sql.ResultSet#getUnicodeStream(int)
     */
    @Override
	public InputStream getUnicodeStream( int columnIndex ) throws SQLException {
        Reader reader = getCharacterStream(columnIndex);
        wasNull = (reader == null);
        if (wasNull) {
            return null;
        }

        return new ReaderInputStream(reader, ReaderInputStream.UNICODE);
    }

    /**
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
     */
    @Override
	public void updateAsciiStream( int columnIndex,
                                   InputStream x,
                                   int length ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
     */
    @Override
	public void updateBinaryStream( int columnIndex,
                                    InputStream x,
                                    int length ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getCharacterStream(int)
     */
    @Override
	public Reader getCharacterStream( int columnIndex ) throws SQLException {
        Object o = getObject(columnIndex);
        wasNull = (o == null);
        if (wasNull) {
            return null;
        }
        if (o instanceof Reader) {
            return (Reader)o;
        }
        if (o instanceof InputStream) {
            try {
                return new InputStreamReader((InputStream)o, ReaderInputStream.UNICODE);
            } catch (UnsupportedEncodingException e) {
                throw wrapException(e);
            }
        }

        String str = getString(columnIndex);
        return new StringReader(str);
    }

    private SQLException wrapException( Exception e ) {
        return new SQLException(e.toString());
    }

    /**
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
     */
    @Override
	public void updateCharacterStream( int columnIndex,
                                       Reader x,
                                       int length ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getObject(int)
     */
    @Override
	public Object getObject( int columnIndex ) {
        return getObjectFromRow(columnIndex, row);
    }

    Object getObjectFromRow( int columnIndex,
                             int rowIndex ) {
        List rowvalues = (List)values.get(rowIndex);
        return rowvalues.get(columnIndex - 1);
    }

    /**
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
     */
    @Override
	public void updateObject( int columnIndex,
                              Object x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
     */
    @Override
	public void updateObject( int columnIndex,
                              Object x,
                              int scale ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getCursorName()
     */
    @Override
	public String getCursorName() {
        return ""; //$NON-NLS-1$
    }

    /**
     * @see java.sql.ResultSet#getString(int)
     */
    @Override
	public String getString( int columnIndex ) {
        Object o = getObject(columnIndex);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * @see java.sql.ResultSet#updateString(int, java.lang.String)
     */
    @Override
	public void updateString( int columnIndex,
                              String x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getByte(java.lang.String)
     */
    @Override
	public byte getByte( String columnName ) {
        int index = findColumn(columnName);
        return getByte(index);
    }

    /**
     * @see java.sql.ResultSet#getDouble(java.lang.String)
     */
    @Override
	public double getDouble( String columnName ) {
        int index = findColumn(columnName);
        return getDouble(index);
    }

    /**
     * @see java.sql.ResultSet#getFloat(java.lang.String)
     */
    @Override
	public float getFloat( String columnName ) {
        int index = findColumn(columnName);
        return getFloat(index);
    }

    /**
     * @see java.sql.ResultSet#findColumn(java.lang.String)
     */
    @Override
	public int findColumn( String columnName ) {
        Object o = names.get(columnName);
        Number n = (Number)o;
        int index = n.intValue();
        return index;
    }

    public String reverseFindColumn( int column ) {
        return reverseNames[column];
    }

    /**
     * @see java.sql.ResultSet#getInt(java.lang.String)
     */
    @Override
	public int getInt( String columnName ) {
        int index = findColumn(columnName);
        return getInt(index);
    }

    /**
     * @see java.sql.ResultSet#getLong(java.lang.String)
     */
    @Override
	public long getLong( String columnName ) {
        int index = findColumn(columnName);
        return getLong(index);
    }

    /**
     * @see java.sql.ResultSet#getShort(java.lang.String)
     */
    @Override
	public short getShort( String columnName ) {
        int index = findColumn(columnName);
        return getShort(index);
    }

    /**
     * @see java.sql.ResultSet#updateNull(java.lang.String)
     */
    @Override
	public void updateNull( String columnName ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getBoolean(java.lang.String)
     */
    @Override
	public boolean getBoolean( String columnName ) {
        int index = findColumn(columnName);
        return getBoolean(index);
    }

    /**
     * @see java.sql.ResultSet#getBytes(java.lang.String)
     */
    @Override
	public byte[] getBytes( String columnName ) {
        int index = findColumn(columnName);
        return getBytes(index);
    }

    /**
     * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
     */
    @Override
	public void updateByte( String columnName,
                            byte x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
     */
    @Override
	public void updateDouble( String columnName,
                              double x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
     */
    @Override
	public void updateFloat( String columnName,
                             float x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateInt(java.lang.String, int)
     */
    @Override
	public void updateInt( String columnName,
                           int x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateLong(java.lang.String, long)
     */
    @Override
	public void updateLong( String columnName,
                            long x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateShort(java.lang.String, short)
     */
    @Override
	public void updateShort( String columnName,
                             short x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
     */
    @Override
	public void updateBoolean( String columnName,
                               boolean x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
     */
    @Override
	public void updateBytes( String columnName,
                             byte[] x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(int)
     */
    @Override
	public BigDecimal getBigDecimal( int columnIndex ) {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return null;
        }
        return new BigDecimal(str);
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(int, int)
     */
    @Override
	public BigDecimal getBigDecimal( int columnIndex,
                                     int scale ) {
        return getBigDecimal(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
     */
    @Override
	public void updateBigDecimal( int columnIndex,
                                  BigDecimal x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getURL(int)
     */
    @Override
	public URL getURL( int columnIndex ) throws SQLException {
        String str = getString(columnIndex);
        wasNull = (str == null);
        if (wasNull) {
            return null;
        }
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            throw wrapException(e);
        }
    }

    /**
     * @see java.sql.ResultSet#getArray(int)
     */
    @Override
	public Array getArray( int i ) throws SQLException {
        throwNotImplemented();
        return null;
    }

    /**
     * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
     */
    @Override
	public void updateArray( int columnIndex,
                             Array x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getBlob(int)
     */
    @Override
	public Blob getBlob( int i ) throws SQLException {
        throwNotImplemented();
        return null;
    }

    /**
     * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
     */
    @Override
	public void updateBlob( int columnIndex,
                            Blob x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getClob(int)
     */
    @Override
	public Clob getClob( int i ) throws SQLException {
        throwNotImplemented();
        return null;
    }

    /**
     * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
     */
    @Override
	public void updateClob( int columnIndex,
                            Clob x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getDate(int)
     */
    @Override
	public Date getDate( int columnIndex ) {
        String str = getString(columnIndex);
        if (str == null) return null;

        try {
            return new Date(DateFormat.getDateInstance().parse(str).getTime());
        } catch (ParseException error) {
            throw new IllegalArgumentException(error);
        }
    }

    /**
     * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
     */
    @Override
	public void updateDate( int columnIndex,
                            Date x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getRef(int)
     */
    @Override
	public Ref getRef( int i ) throws SQLException {
        throwNotImplemented();
        return null;
    }

    /**
     * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
     */
    @Override
	public void updateRef( int columnIndex,
                           Ref x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getMetaData()
     */
    @Override
	public ResultSetMetaData getMetaData() {
        return metaData;
    }

    /**
     * @see java.sql.ResultSet#getWarnings()
     */
    @Override
	public SQLWarning getWarnings() {
        return null;
    }

    /**
     * @see java.sql.ResultSet#getStatement()
     */
    @Override
	public Statement getStatement() {

        return null;
    }

    /**
     * @see java.sql.ResultSet#getTime(int)
     */
    @Override
	public Time getTime( int columnIndex ) {
        String str = getString(columnIndex);
        if (str == null) return null;
        try {
            return new Time(DateFormat.getTimeInstance().parse(str).getTime());
        } catch (ParseException error) {
            throw new IllegalArgumentException(error);
        }
    }

    /**
     * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
     */
    @Override
	public void updateTime( int columnIndex,
                            Time x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(int)
     */
    @Override
	public Timestamp getTimestamp( int columnIndex ) {
        String str = getString(columnIndex);
        if (str == null) return null;
        try {
            return new Timestamp(DateFormat.getInstance().parse(str).getTime());
        } catch (ParseException error) {
            throw new IllegalArgumentException(error);
        }
    }

    /**
     * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
     */
    @Override
	public void updateTimestamp( int columnIndex,
                                 Timestamp x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
     */
    @Override
	public InputStream getAsciiStream( String columnName ) throws SQLException {
        int index = findColumn(columnName);
        return getAsciiStream(index);
    }

    /**
     * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
     */
    @Override
	public InputStream getBinaryStream( String columnName ) {
        int index = findColumn(columnName);
        return getBinaryStream(index);
    }

    /**
     * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
     */
    @Override
	public InputStream getUnicodeStream( String columnName ) throws SQLException {
        int index = findColumn(columnName);
        return getUnicodeStream(index);
    }

    /**
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
     */
    @Override
	public void updateAsciiStream( String columnName,
                                   InputStream x,
                                   int length ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
     */
    @Override
	public void updateBinaryStream( String columnName,
                                    InputStream x,
                                    int length ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
     */
    @Override
	public Reader getCharacterStream( String columnName ) throws SQLException {
        int index = findColumn(columnName);
        return getCharacterStream(index);
    }

    /**
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
     */
    @Override
	public void updateCharacterStream( String columnName,
                                       Reader reader,
                                       int length ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getObject(java.lang.String)
     */
    @Override
	public Object getObject( String columnName ) {
        int index = findColumn(columnName);
        return getObject(index);
    }

    /**
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
     */
    @Override
	public void updateObject( String columnName,
                              Object x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object, int)
     */
    @Override
	public void updateObject( String columnName,
                              Object x,
                              int scale ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getString(java.lang.String)
     */
    @Override
	public String getString( String columnName ) {
        int index = findColumn(columnName);
        return getString(index);
    }

    /**
     * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
     */
    @Override
	public void updateString( String columnName,
                              String x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
     */
    @Override
	public BigDecimal getBigDecimal( String columnName ) {
        int index = findColumn(columnName);
        return getBigDecimal(index);
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
     */
    @Override
	public BigDecimal getBigDecimal( String columnName,
                                     int scale ) {
        return getBigDecimal(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
     */
    @Override
	public void updateBigDecimal( String columnName,
                                  BigDecimal x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getURL(java.lang.String)
     */
    @Override
	public URL getURL( String columnName ) throws SQLException {
        int index = findColumn(columnName);
        return getURL(index);
    }

    /**
     * @see java.sql.ResultSet#getArray(java.lang.String)
     */
    @Override
	public Array getArray( String colName ) throws SQLException {
        int index = findColumn(colName);
        return getArray(index);
    }

    /**
     * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
     */
    @Override
	public void updateArray( String columnName,
                             Array x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getBlob(java.lang.String)
     */
    @Override
	public Blob getBlob( String colName ) throws SQLException {
        int index = findColumn(colName);
        return getBlob(index);
    }

    /**
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
     */
    @Override
	public void updateBlob( String columnName,
                            Blob x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getClob(java.lang.String)
     */
    @Override
	public Clob getClob( String colName ) throws SQLException {
        int index = findColumn(colName);
        return getClob(index);
    }

    /**
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
     */
    @Override
	public void updateClob( String columnName,
                            Clob x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getDate(java.lang.String)
     */
    @Override
	public Date getDate( String columnName ) {
        int index = findColumn(columnName);
        return getDate(index);
    }

    /**
     * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
     */
    @Override
	public void updateDate( String columnName,
                            Date x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
     */
    @Override
	public Date getDate( int columnIndex,
                         Calendar cal ) {
        return getDate(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getRef(java.lang.String)
     */
    @Override
	public Ref getRef( String colName ) throws SQLException {
        int index = findColumn(colName);
        return getRef(index);
    }

    /**
     * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
     */
    @Override
	public void updateRef( String columnName,
                           Ref x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getTime(java.lang.String)
     */
    @Override
	public Time getTime( String columnName ) {
        int index = findColumn(columnName);
        return getTime(index);
    }

    /**
     * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
     */
    @Override
	public void updateTime( String columnName,
                            Time x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
     */
    @Override
	public Time getTime( int columnIndex,
                         Calendar cal ) {
        return getTime(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(java.lang.String)
     */
    @Override
	public Timestamp getTimestamp( String columnName ) {
        int index = findColumn(columnName);
        return getTimestamp(index);
    }

    /**
     * @see java.sql.ResultSet#updateTimestamp(java.lang.String, java.sql.Timestamp)
     */
    @Override
	public void updateTimestamp( String columnName,
                                 Timestamp x ) throws SQLException {
        throwReadOnly();
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
     */
    @Override
	public Timestamp getTimestamp( int columnIndex,
                                   Calendar cal ) {
        return getTimestamp(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
     */
    @Override
	public Date getDate( String columnName,
                         Calendar cal ) {
        int index = findColumn(columnName);
        return getDate(index);
    }

    /**
     * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
     */
    @Override
	public Time getTime( String columnName,
                         Calendar cal ) {
        int index = findColumn(columnName);
        return getTime(index);
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
     */
    @Override
	public Timestamp getTimestamp( String columnName,
                                   Calendar cal ) {
        int index = findColumn(columnName);
        return getTimestamp(index);
    }

    class MetaData implements ResultSetMetaData {
        @Override
		public String getCatalogName( int column ) {
            return ""; //$NON-NLS-1$
        }

        @Override
		public String getColumnClassName( int column ) {
            Class cls = getColumnClass(column);
            return cls.getName();
        }

        private Class getColumnClass( int column ) {
            Class retval = null;
            for (int i = 0; i < values.size(); ++i) {
                Object value = getObjectFromRow(column, i);
                if (value != null) {
                    retval = value.getClass();
                    break;
                }
            }
            if (retval == null) {
                retval = Object.class;
            }
            return retval;
        }

        @Override
		public int getColumnCount() {
            return names.size();
        }

        @Override
		public int getColumnDisplaySize( int column ) {
            return 32; // arbitrary number
        }

        @Override
		public String getColumnLabel( int column ) {
            return reverseFindColumn(column);
        }

        @Override
		public String getColumnName( int column ) {
            return reverseFindColumn(column);
        }

        @Override
		public int getColumnType( int column ) {
            Class cls = getColumnClass(column);
            if (cls == String.class) return Types.VARCHAR;
            if (cls == String.class) return Types.VARCHAR;
            if (cls == BigDecimal.class) return Types.NUMERIC;
            if (cls == Boolean.class) return Types.BIT;
            if (cls == Integer.class) return Types.INTEGER;
            if (cls == Long.class) return Types.BIGINT;
            if (cls == Float.class) return Types.REAL;
            if (cls == Double.class) return Types.DOUBLE;
            if (cls == byte[].class) return Types.VARBINARY;
            if (cls == Date.class) return Types.DATE;
            if (cls == Time.class) return Types.TIME;
            if (cls == Timestamp.class) return Types.TIMESTAMP;
            if (cls == Timestamp.class) return Types.TIMESTAMP;
            if (cls == Clob.class) return Types.CLOB;
            if (cls == Blob.class) return Types.BLOB;
            if (cls == Array.class) return Types.ARRAY;
            if (cls == Struct.class) return Types.STRUCT;
            if (cls == Ref.class) return Types.REF;
            return Types.JAVA_OBJECT;

        }

        @Override
		public String getColumnTypeName( int column ) {
            Class cls = getColumnClass(column);
            return cls.getName();
        }

        @Override
		public int getPrecision( int column ) {
            return 10; // arbitrary number
        }

        @Override
		public int getScale( int column ) {
            return 10; // arbitrary number
        }

        @Override
		public String getSchemaName( int column ) {
            return ""; //$NON-NLS-1$
        }

        @Override
		public String getTableName( int column ) {
            return ""; //$NON-NLS-1$
        }

        @Override
		public boolean isAutoIncrement( int column ) {
            return false;
        }

        @Override
		public boolean isCaseSensitive( int column ) {
            return true;
        }

        @Override
		public boolean isCurrency( int column ) {
            return false;
        }

        @Override
		public boolean isDefinitelyWritable( int column ) {
            return false;
        }

        @Override
		public int isNullable( int column ) {
            return 0;
        }

        @Override
		public boolean isReadOnly( int column ) {
            return true;
        }

        @Override
		public boolean isSearchable( int column ) {
            return false;
        }

        @Override
		public boolean isSigned( int column ) {
            return false;
        }

        @Override
		public boolean isWritable( int column ) {
            return false;
        }

        @Override
        public boolean isWrapperFor( Class<?> iface ) {

            return false;
        }

        @Override
        public <T> T unwrap( Class<T> iface ) {

            return null;
        }
    }

    @Override
    public int getHoldability() {

        return 0;
    }

    @Override
    public Reader getNCharacterStream( int columnIndex ) {

        return null;
    }

    @Override
    public Reader getNCharacterStream( String columnLabel ) {

        return null;
    }

    @Override
    public NClob getNClob( int columnIndex ) {

        return null;
    }

    @Override
    public NClob getNClob( String columnLabel ) {

        return null;
    }

    @Override
    public String getNString( int columnIndex ) {

        return null;
    }

    @Override
    public String getNString( String columnLabel ) {

        return null;
    }

    @Override
    public Object getObject( int columnIndex,
                             Map<String, Class<?>> map ) {
        return getObject(columnIndex);
    }

    @Override
    public Object getObject( String columnLabel,
                             Map<String, Class<?>> map ) {
        int index = findColumn(columnLabel);
        return getObject(index);
    }

    @Override
    public RowId getRowId( int columnIndex ) {

        return null;
    }

    @Override
    public RowId getRowId( String columnLabel ) {

        return null;
    }

    @Override
    public SQLXML getSQLXML( int columnIndex ) {

        return null;
    }

    @Override
    public SQLXML getSQLXML( String columnLabel ) {

        return null;
    }

    @Override
    public boolean isClosed() {

        return false;
    }

    @Override
    public void updateAsciiStream( int columnIndex,
                                   InputStream x ) {

    }

    @Override
    public void updateAsciiStream( String columnLabel,
                                   InputStream x ) {

    }

    @Override
    public void updateAsciiStream( int columnIndex,
                                   InputStream x,
                                   long length ) {

    }

    @Override
    public void updateAsciiStream( String columnLabel,
                                   InputStream x,
                                   long length ) {

    }

    @Override
    public void updateBinaryStream( int columnIndex,
                                    InputStream x ) {

    }

    @Override
    public void updateBinaryStream( String columnLabel,
                                    InputStream x ) {

    }

    @Override
    public void updateBinaryStream( int columnIndex,
                                    InputStream x,
                                    long length ) {

    }

    @Override
    public void updateBinaryStream( String columnLabel,
                                    InputStream x,
                                    long length ) {

    }

    @Override
    public void updateBlob( int columnIndex,
                            InputStream inputStream ) {

    }

    @Override
    public void updateBlob( String columnLabel,
                            InputStream inputStream ) {

    }

    @Override
    public void updateBlob( int columnIndex,
                            InputStream inputStream,
                            long length ) {

    }

    @Override
    public void updateBlob( String columnLabel,
                            InputStream inputStream,
                            long length ) {

    }

    @Override
    public void updateCharacterStream( int columnIndex,
                                       Reader x ) {

    }

    @Override
    public void updateCharacterStream( String columnLabel,
                                       Reader reader ) {

    }

    @Override
    public void updateCharacterStream( int columnIndex,
                                       Reader x,
                                       long length ) {

    }

    @Override
    public void updateCharacterStream( String columnLabel,
                                       Reader reader,
                                       long length ) {

    }

    @Override
    public void updateClob( int columnIndex,
                            Reader reader ) {

    }

    @Override
    public void updateClob( String columnLabel,
                            Reader reader ) {

    }

    @Override
    public void updateClob( int columnIndex,
                            Reader reader,
                            long length ) {

    }

    @Override
    public void updateClob( String columnLabel,
                            Reader reader,
                            long length ) {

    }

    @Override
    public void updateNCharacterStream( int columnIndex,
                                        Reader x ) {

    }

    @Override
    public void updateNCharacterStream( String columnLabel,
                                        Reader reader ) {

    }

    @Override
    public void updateNCharacterStream( int columnIndex,
                                        Reader x,
                                        long length ) {

    }

    @Override
    public void updateNCharacterStream( String columnLabel,
                                        Reader reader,
                                        long length ) {

    }

    @Override
    public void updateNClob( int columnIndex,
                             NClob clob ) {

    }

    @Override
    public void updateNClob( String columnLabel,
                             NClob clob ) {

    }

    @Override
    public void updateNClob( int columnIndex,
                             Reader reader ) {

    }

    @Override
    public void updateNClob( String columnLabel,
                             Reader reader ) {

    }

    @Override
    public void updateNClob( int columnIndex,
                             Reader reader,
                             long length ) {

    }

    @Override
    public void updateNClob( String columnLabel,
                             Reader reader,
                             long length ) {

    }

    @Override
    public void updateNString( int columnIndex,
                               String string ) {

    }

    @Override
    public void updateNString( String columnLabel,
                               String string ) {

    }

    @Override
    public void updateRowId( int columnIndex,
                             RowId x ) {

    }

    @Override
    public void updateRowId( String columnLabel,
                             RowId x ) {

    }

    @Override
    public void updateSQLXML( int columnIndex,
                              SQLXML xmlObject ) {

    }

    @Override
    public void updateSQLXML( String columnLabel,
                              SQLXML xmlObject ) {

    }

    @Override
    public boolean isWrapperFor( Class<?> iface ) {

        return false;
    }

    @Override
    public <T> T unwrap( Class<T> iface ) {

        return null;
    }
}
