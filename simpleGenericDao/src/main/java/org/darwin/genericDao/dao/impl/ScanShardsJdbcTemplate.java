/**
 * org.darwin.genericDao.dao.impl.ShardsJdbcTemplate.java
 * created by Tianxin(tianjige@163.com) on 2015年6月15日 下午2:47:05
 */
package org.darwin.genericDao.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.darwin.common.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
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
 * created by Tianxin on 2015年6月15日 下午2:47:05
 */
public class ScanShardsJdbcTemplate extends JdbcTemplate {
  
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(ScanShardsJdbcTemplate.class);

  protected JdbcTemplate core;
  private List<Integer> shardingKeys = null;
  private String shardingKeyName = null;

  public void setShardingKeyName(String shardingKeyName) {
    this.shardingKeyName = shardingKeyName;
  }

  public void setJdbcTemplate(JdbcTemplate core) {
    this.core = core;
  }

  public void setShardingKeys(String keys) {
    String [] shardingKeyArray = keys.trim().split(",");
    List<Integer> iKeys = new ArrayList<Integer>(shardingKeyArray.length);
    for(String key : shardingKeyArray){
      iKeys.add(Integer.valueOf(key));
    }
    this.shardingKeys = iKeys;
  }

  private Integer getShardingKey() {
    ThreadContext.ensuerInited();
    if (shardingKeyName == null) {
      return ThreadContext.getShardingKey();
    } else {
      return ThreadContext.get(shardingKeyName);
    }
  }

  private void putShardingKey(Object key) {
    ThreadContext.ensuerInited();
    if (shardingKeyName == null) {
      ThreadContext.putShardingKey(key);
    } else {
      ThreadContext.put(shardingKeyName, key);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public int queryForInt(String sql, Object... args) throws DataAccessException {
    Object oldKey = getShardingKey();
    try {
      int totalCount = 0;
      for (Integer key : shardingKeys) {
        putShardingKey(key);
        totalCount += core.queryForInt(sql, args);
      }
      return totalCount;
    } finally {
      putShardingKey(oldKey);
    }
  }

  @Override
  public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
    
    Object oldKey = getShardingKey();
    try {
      List<T> list = new ArrayList<T>();
      for (Integer key : shardingKeys) {
        putShardingKey(key);
        List<T> shardList = core.query(sql, args, rowMapper);
        if(shardList != null){
          list.addAll(shardList);
        }
      }
      return list;
    } finally {
      putShardingKey(oldKey);
    }
  }

  public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T execute(StatementCallback<T> action) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public void execute(String sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
    throw new RuntimeException("not implements");

  }

  public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public Map<String, Object> queryForMap(String sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public long queryForLong(String sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int queryForInt(String sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int update(String sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int[] batchUpdate(String[] sql) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
    throw new RuntimeException("not implements");

  }

  public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
    throw new RuntimeException("not implements");

  }

  public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
    throw new RuntimeException("not implements");

  }

  public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
    throw new RuntimeException("not implements");

  }

  public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");

  }

  public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public long queryForLong(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public long queryForLong(String sql, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int update(PreparedStatementCreator psc) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int update(String sql, Object... args) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
    throw new RuntimeException("not implements");
  }

  public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
    throw new RuntimeException("not implements");
  }

  public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs, int batchSize, ParameterizedPreparedStatementSetter<T> pss) {
    throw new RuntimeException("not implements");
  }

  public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException {
    throw new RuntimeException("not implements");
  }

  public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters) throws DataAccessException {
    throw new RuntimeException("not implements");
  }
}
