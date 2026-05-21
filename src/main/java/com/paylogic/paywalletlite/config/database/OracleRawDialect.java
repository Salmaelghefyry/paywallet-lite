package com.paylogic.paywalletlite.config.database;

import org.hibernate.dialect.Oracle12cDialect;
import java.sql.Types;

public class OracleRawDialect extends Oracle12cDialect {

    public OracleRawDialect() {
        super();
        // Force RAW(16) pour tous les VARBINARY (UUID)
        // Le $l donnerait 255, donc on force 16 explicitement
        registerColumnType(Types.VARBINARY, 16, "RAW(16)");
        registerColumnType(Types.VARBINARY, "RAW(16)");
    }
}