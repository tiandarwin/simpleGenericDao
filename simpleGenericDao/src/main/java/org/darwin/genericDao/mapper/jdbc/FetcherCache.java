/**
 * org.darwin.genericDao.mapper.jdbc.IntFetcher.java
 * created by Tianxin(tianjige@163.com) on 2015年6月5日 上午11:33:58
 */
package org.darwin.genericDao.mapper.jdbc;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

import org.darwin.common.utils.Utils;

/**
 * created by Tianxin on 2015年6月5日 上午11:33:58
 */
public class FetcherCache {

	/**
	 * 获取某个类型的fetcher
	 * @param typeClass
	 * @return
	 * created by Tianxin on 2015年6月5日 上午11:36:10
	 */
	public final static TypeFetcher getFetcher(Class<?> typeClass){
		TypeFetcher fetcher = cache.get(typeClass);
		return fetcher == null ? objectFetcher : fetcher;
	}
	
	private static TypeFetcher objectFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getObject(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getObject(index);
		}
	};
	private static TypeFetcher integerFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getInt(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getInt(index);
		}
	};
	private static TypeFetcher longFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getLong(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getLong(index);
		}
	};
	private static TypeFetcher shortFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getShort(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getShort(index);
		}
	};
	private static TypeFetcher floatFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getFloat(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getFloat(index);
		}
	};
	private static TypeFetcher doubleFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getDouble(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getDouble(index);
		}
	};
	private static TypeFetcher byteFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getByte(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getByte(index);
		}
	};
	private static TypeFetcher booleanFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getBoolean(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getBoolean(index);
		}
	};
	private static TypeFetcher stringFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getString(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getString(index);
		}
	};
	private static TypeFetcher dateFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getDate(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getDate(index);
		}
	};
	private static TypeFetcher timestampFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getTimestamp(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getTimestamp(index);
		}
	};
	private static TypeFetcher timeFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getTime(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getTime(index);
		}
	};
	private static TypeFetcher bigDecimalFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getBigDecimal(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getBigDecimal(index);
		}
	};
	private static TypeFetcher urlFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getURL(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getURL(index);
		}
	};
	private static TypeFetcher clobFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getClob(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getClob(index);
		}
	};
	private static TypeFetcher blobFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getBlob(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getBlob(index);
		}
	};
	private static TypeFetcher arrayFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getArray(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getArray(index);
		}
	};
	protected static TypeFetcher asciiStreamFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getAsciiStream(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getAsciiStream(index);
		}
	};
	protected static TypeFetcher binaryStreamFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getBinaryStream(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getBinaryStream(index);
		}
	};
	protected static TypeFetcher characterStreamFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getCharacterStream(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getCharacterStream(index);
		}
	};
	protected static TypeFetcher nCharacterStreamFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getNCharacterStream(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getNCharacterStream(index);
		}
	};
	protected static TypeFetcher nClobFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getNClob(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getNClob(index);
		}
	};
	protected static TypeFetcher nStringFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getNString(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getNString(index);
		}
	};
	protected static TypeFetcher refFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getRef(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getRef(index);
		}
	};
	protected static TypeFetcher rowIdFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getRowId(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getRowId(index);
		}
	};
	protected static TypeFetcher sqlXMLFetcher = new TypeFetcher() {
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getSQLXML(label);
		}
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getSQLXML(index);
		}
	};
	protected static TypeFetcher unicodeStreamFetcher = new TypeFetcher() {
		@SuppressWarnings("deprecation")
		public Object getFromResultSet(ResultSet rs, String label) throws SQLException{
			return rs.getUnicodeStream(label);
		}
		@SuppressWarnings("deprecation")
		public Object getFromResultSet(ResultSet rs, int index) throws SQLException{
			return rs.getUnicodeStream(index);
		}
	};
	
	private final static Map<Class<?> , TypeFetcher> cache = Utils.newMap();
	static{
		cache.put(Integer.TYPE, integerFetcher);
		cache.put(Integer.class, integerFetcher);
		cache.put(Long.TYPE, longFetcher);
		cache.put(Long.class, longFetcher);
		cache.put(Double.TYPE, doubleFetcher);
		cache.put(Double.class, doubleFetcher);
		cache.put(Float.TYPE, floatFetcher);
		cache.put(Float.class, floatFetcher);
		cache.put(Short.TYPE, shortFetcher);
		cache.put(Short.class, shortFetcher);
		cache.put(Boolean.TYPE, booleanFetcher);
		cache.put(Boolean.class, booleanFetcher);
		cache.put(Byte.TYPE, byteFetcher);
		cache.put(Byte.class, byteFetcher);
		cache.put(String.class, stringFetcher);
		cache.put(java.util.Date.class, timestampFetcher);
		cache.put(Timestamp.class, timestampFetcher);
		cache.put(Time.class, timeFetcher);
		cache.put(Date.class, dateFetcher);
		cache.put(BigDecimal.class, bigDecimalFetcher);
		cache.put(URL.class, urlFetcher);
		cache.put(Clob.class, clobFetcher);
		cache.put(Blob.class, blobFetcher);
		cache.put(Array.class, arrayFetcher);
	}

}
