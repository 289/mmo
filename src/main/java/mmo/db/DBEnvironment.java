package mmo.db;


import mmo.db.role.mapper.IRoleMapper;

/**
 * @author Jin Shuai
 */
public enum DBEnvironment {
    ROLE("db-role.xml", IRoleMapper.class),
//    LOG,
    ;

    public final String dbConfigFile;

    public final Class<? extends MybatisMapper> mapperClazz;

    private DBEnvironment(String dbConfigFile, Class<? extends MybatisMapper> mapperClazz) {
        this.dbConfigFile = dbConfigFile;
        this.mapperClazz = mapperClazz;
    }

    /**
     * 根据 mapper.class 对象，获取此mapper对应的DB
     *
     * @param clazz
     * @return
     */
    public static DBEnvironment getDBByMapperClazz(Class<? extends MybatisMapper> clazz) {
        if (clazz == null) {
            return null;
        }
        for (DBEnvironment dbEnvironment : values()) {
            Class<? extends MybatisMapper> dbClazz = dbEnvironment.mapperClazz;
            if (clazz != dbClazz && dbClazz.isAssignableFrom(clazz)) {
                return dbEnvironment;
            }
        }
        return null;
    }
}
