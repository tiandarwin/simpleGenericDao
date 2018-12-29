package org.darwin.simpleGenericDao.shard;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import org.darwin.genericDao.shard.ShardTableRule;

/**
 * @author hexiufeng
 * @date 2018/12/29下午12:11
 */
public class FarmerShardTable implements ShardTableRule {

  @Override
  public String shardTable(Object shardKey, int count, String tableNamePrefix) {
    long hc = Hashing.farmHashFingerprint64()
        .hashString(shardKey.toString(), StandardCharsets.UTF_8).asLong();
    long index = hc % (long) count;
    if (index < 0L) {
      index = -index;
    }

    return tableNamePrefix + "_" + index;
  }
}
