package ar.edu.itba.ss.models.entity;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {

    private final static AtomicInteger atomicInteger = new AtomicInteger(1);

    private final Integer id = atomicInteger.getAndIncrement();

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Entity)) {
            return false;
        }
        Entity e = (Entity) obj;
        return this.id.equals(e.getId());
    }

}

