package mmo.db.role.entity;

import mmo.db.*;
import mmo.db.role.mapper.RoleMapper;

public class Role extends DbEntity {
    private Integer id;

    private String nickname;

    private Integer level;

    private java.util.HashMap<String, String> testJson;

    public Role(Integer id, String nickname, Integer level, java.util.HashMap<String, String> testJson) {
        this.id = id;
        this.nickname = nickname;
        this.level = level;
        this.testJson = testJson;
    }

    public Role() {
        super();
    }

    public Integer getId() {
        return id;
    }

    @Override
    public void save() {
        if (!dirtyProps.isEmpty()) {
            try {
                while (!isLock.compareAndSet(UN_LOCK, LOCK)) { } //spin
                if(!dirtyProps.isEmpty()){ //double check
                    Role update = new Role();
                    update.id = this.id;
                    dirtyProps.forEach(prop -> {
                        switch (prop){
                            case "nickname": update.nickname = this.nickname; break;
                            case "level": update.level = this.level; break;
                            case "testJson": update.testJson = this.testJson; break;
                            default: throw new RuntimeException();
                        }
                    });
                    MapperMgr.getRoleMapper(RoleMapper.class).updateByPrimaryKeySelective(update);
                    dirtyProps.clear();
                }
            } finally {
                isLock.set(UN_LOCK);
            }
        }
    }

    public void setId(final Integer id) {
        if (this.id != null && this.id.equals(id)) {
            return;
        }
        final Integer curr = this.id;
        Transaction transaction = Transaction.current();
        boolean addLogSucc = transaction != null && transaction.addLog(new TransLog() {
            @Override
            public void commit() {
                markPropDirty("id");
            }

            @Override
            public void rollback() {
                Role.this.id = curr;
            }
        });
        if (!addLogSucc) {
            this.id = id; //后面没有事务再修改，最终数据
            markPropDirty("id");
        } else {
            removePropDirty("id");
            this.id = id; //中间数据，事务可能再做修改
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        if (this.nickname != null && this.nickname.equals(nickname)) {
            return;
        }
        final String curr = this.nickname;
        Transaction transaction = Transaction.current();
        boolean addLogSucc = transaction != null && transaction.addLog(new TransLog() {
            @Override
            public void commit() {
                markPropDirty("nickname");
            }

            @Override
            public void rollback() {
                Role.this.nickname = curr;
            }
        });
        if (!addLogSucc) {
            this.nickname = nickname == null ? null : nickname.trim();
            markPropDirty("nickname");
        } else {
            removePropDirty("nickname");
            this.nickname = nickname == null ? null : nickname.trim();
        }
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        if (this.level != null && this.level.equals(level)) {
            return;
        }
        final Integer curr = this.level;
        Transaction transaction = Transaction.current();
        boolean addLogSucc = transaction != null && transaction.addLog(new TransLog() {
            @Override
            public void commit() {
                markPropDirty("level");
            }

            @Override
            public void rollback() {
                Role.this.level = curr;
            }
        });
        if (!addLogSucc) {
            this.level = level;
            markPropDirty("level");
        } else {
            removePropDirty("level");
            this.level = level;
        }
    }


    public java.util.Map<String, String> getTestJson() {
        return new ProxyMap<>(this, testJson, "testJson");
    }
}