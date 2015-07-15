/**
 * org.darwin.genericDao.dao.impl.JdbcTemplateWrapper.java
 * created by Tianxin(tianjige@163.com) on 2015年7月6日 上午10:48:40
 */
package org.darwin.genericDao.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * 
 * <br/>created by Tianxin on 2015年7月6日 上午10:48:40
 */
public class JdbcTemplateWrapper implements JdbcOperations {
  
  /**
   * 构造函数
   * @param jdbcTemplate
   */
  public JdbcTemplateWrapper(JdbcTemplate jdbcTemplate) {
    core = jdbcTemplate;
  }

  private JdbcTemplate core = null;
  
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(JdbcTemplateWrapper.class);

  public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.execute(action);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(null, null, cost);
    }
  }

  public <T> T execute(StatementCallback<T> action) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.execute(action);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(null, null, cost);
    }
  }

  public void execute(String sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      core.execute(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.query(sql, rse);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      core.query(sql, rch);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.query(sql, rowMapper);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForObject(sql, rowMapper);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForObject(sql, requiredType);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public Map<String, Object> queryForMap(String sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForMap(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public long queryForLong(String sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForLong(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public int queryForInt(String sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForInt(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForList(sql, elementType);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForList(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.queryForRowSet(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public int update(String sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.update(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public int[] batchUpdate(String[] sql) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.batchUpdate(sql);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(null, null, cost);
    }
  }

  public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.execute(psc, action);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(null, null, cost);
    }
  }

  public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.execute(sql, action);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.query(psc, rse);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(null, null, cost);
    }
  }

  public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.query(sql, pss, rse);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, null, cost);
    }
  }

  public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.query(sql, args, argTypes, rse);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, args, cost);
    }
  }

  public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.query(sql, args, rse);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, args, cost);
    }
  }

  public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
    long start = System.currentTimeMillis();
    try{
      return core.query(sql, args, rse);
    }finally{
      long cost = System.currentTimeMillis() - start;
      recordLog(sql, args, cost);
    }
  }

  public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
    // TODO Auto-generated method stub

  }

  public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
    // TODO Auto-generated method stub

  }

  public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
    // TODO Auto-generated method stub

  }

  public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
    // TODO Auto-generated method stub

  }

  public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub

  }

  public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public long queryForLong(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public long queryForLong(String sql, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int queryForInt(String sql, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public int update(PreparedStatementCreator psc) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int update(String sql, Object... args) throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
    // TODO Auto-generated method stub
    return null;
  }

  public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs, int batchSize, ParameterizedPreparedStatementSetter<T> pss) {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }
  
  private void recordLog(String sql, Object[] args, long costMillis){
    
  }
}
