/**
 * org.darwin.common.utils.FileUtils.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 下午5:32:42
 */
package org.darwin.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 文件类的方法集合
 * created by Tianxin on 2015年6月8日 下午5:32:42
 */
public class IOUtils {

  /**
   * 安全关闭流的方法
   * @param is
   * created by Tianxin on 2015年6月8日 下午5:33:22
   */
  public final static void closeInputStream(InputStream is) {
    if (is != null) {
      try {
        is.close();
      } catch (IOException e) {
        //ignore
      }
    }
  }

  /**
   * 安全关闭流的方法
   * @param os
   * created by Tianxin on 2015年6月8日 下午5:33:22
   */
  public final static void closeOutputStream(OutputStream os) {
    if (os != null) {
      try {
        os.flush();
        os.close();
      } catch (IOException e) {
        //ignore
      }
    }
  }

  /**
   * 安全关闭流的方法
   * @param r
   * created by Tianxin on 2015年6月8日 下午5:33:22
   */
  public final static void closeReader(Reader r) {
    if (r != null) {
      try {
        r.close();
      } catch (IOException e) {
        //ignore
      }
    }
  }

  /**
   * 安全关闭流的方法
   * @param w
   * created by Tianxin on 2015年6月8日 下午5:33:22
   */
  public final static void closeWriter(Writer w) {
    if (w != null) {
      try {
        w.flush();
        w.close();
      } catch (IOException e) {
        //ignore
      }
    }
  }

  /**
   * 安全关闭SQL句柄的方法
   * @param conn
   * @param stmt
   * @param rs
   * created by Tianxin on 2015年6月8日 下午5:36:58
   */
  public final static void closeRDBResource(Connection conn, Statement stmt, ResultSet rs) {

    //关闭结果集
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
      }
    }

    //关闭执行句柄
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
      }
    }

    //关闭连接
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
      }
    }
  }
}
