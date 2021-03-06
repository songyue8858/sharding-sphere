/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.shardingjdbc.jdbc.core.connection;

import io.shardingsphere.shardingjdbc.jdbc.adapter.AbstractConnectionAdapter;
import io.shardingsphere.shardingjdbc.jdbc.core.ShardingContext;
import io.shardingsphere.shardingjdbc.jdbc.core.statement.ShardingPreparedStatement;
import io.shardingsphere.shardingjdbc.jdbc.core.statement.ShardingStatement;
import io.shardingsphere.transaction.api.TransactionType;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Connection that support sharding.
 *
 * @author zhangliang
 * @author caohao
 * @author gaohongtao
 * @author zhaojun
 */
@Getter
public final class ShardingConnection extends AbstractConnectionAdapter {
    
    private final Map<String, DataSource> dataSourceMap;
    
    private final ShardingContext shardingContext;
    
    public ShardingConnection(final Map<String, DataSource> dataSourceMap, final ShardingContext shardingContext, final TransactionType transactionType) {
        super(transactionType);
        this.dataSourceMap = dataSourceMap;
        this.shardingContext = shardingContext;
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getCachedConnections().isEmpty() ? shardingContext.getCachedDatabaseMetaData() : getCachedConnections().values().iterator().next().getMetaData();
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) {
        return new ShardingPreparedStatement(this, sql);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) {
        return new ShardingPreparedStatement(this, sql, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
        return new ShardingPreparedStatement(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) {
        return new ShardingPreparedStatement(this, sql, autoGeneratedKeys);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) {
        return new ShardingPreparedStatement(this, sql, Statement.RETURN_GENERATED_KEYS);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) {
        return new ShardingPreparedStatement(this, sql, Statement.RETURN_GENERATED_KEYS);
    }
    
    @Override
    public Statement createStatement() {
        return new ShardingStatement(this);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) {
        return new ShardingStatement(this, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
        return new ShardingStatement(this, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
}
