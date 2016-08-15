package mmo.db.role.entity;

import mmo.db.DbEntity;
import mmo.db.MapperMgr;
import mmo.db.role.mapper.RoleMapper;
import mmo.db.trans.TransLog;
import mmo.db.trans.Transaction;

public class Role extends DbEntity {
    private Integer id;

    private String nickname;

    private Integer level;

    public Role(Integer id, String nickname, Integer level) {
        this.id = id;
        this.nickname = nickname;
        this.level = level;
    }

    public Role() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        if (this.id != null && this.id.equals(id)) {
            return;
        }
        if (isMirror) {
            this.id = id;
            return;
        }
        Transaction transaction = Transaction.current();
        if (transaction != null) {
            final Integer curr = this.id;
            transaction.addTRecord(new TransLog() {
                @Override
                public void commit() {
                    Role role = getMirror();
                    role.setId(id);
                    role.onUpdate();
                }

                @Override
                public void rollback() {
                    Role.this.id = curr;
                }
            });
        } else {
            Role role = getMirror();
            role.setId(id);
            role.onUpdate();
        }
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        if (this.nickname != null && this.nickname.equals(nickname)) {
            return;
        }
        if (isMirror) {
            this.nickname = nickname == null ? null : nickname.trim();
            return;
        }
        Transaction transaction = Transaction.current();
        if (transaction != null) {
            final String curr = this.nickname;
            transaction.addTRecord(new TransLog() {
                @Override
                public void commit() {
                    Role role = getMirror();
                    role.setNickname(nickname);
                    role.onUpdate();
                }

                @Override
                public void rollback() {
                    Role.this.nickname = curr;
                }
            });
        } else {
            Role role = getMirror();
            role.setNickname(nickname);
            role.onUpdate();
        }
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        if (this.level != null && this.level.equals(level)) {
            return;
        }
        if (isMirror) {
            this.level = level;
            return;
        }
        Transaction transaction = Transaction.current();
        if (transaction != null) {
            final Integer curr = this.level;
            transaction.addTRecord(new TransLog() {
                @Override
                public void commit() {
                    Role role = getMirror();
                    role.setLevel(level);
                    role.onUpdate();
                }

                @Override
                public void rollback() {
                    Role.this.level = curr;
                }
            });
        } else {
            Role role = getMirror();
            role.setLevel(level);
            role.onUpdate();
        }
        this.level = level;
    }

    @Override
    public void update() {
        Role mirror = getMirror();
        if (mirror.getOp().equals(Mark.UPDATE)) {
            MapperMgr.getMapper(RoleMapper.class).updateByPrimaryKeySelective(mirror);
            nickname = null;
            level = null;
            mirror.resetMark();
        }
    }

    @Override
    public void initMirror() {
        Role role = new Role();
        role.isMirror = true;
        role.id = this.id;
        mirror = role;
    }
}