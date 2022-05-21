package me.conclure.eventful.hub.utility;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.function.DoubleUnaryOperator;

public class AssistRecord {
    private final LinkedList<DamageRecord> queue;

    public AssistRecord() {
        this.queue = new LinkedList<>();
    }

    public void recover(double damage) {
        double[] damageArr = new double[]{damage};
        ListIterator<DamageRecord> iterator = this.queue.listIterator();
        while (iterator.hasNext()) {
            DamageRecord damageRecord = iterator.next();
            if (damageArr[0] < damageRecord.damage) {
                iterator.set(damageRecord.redefine(theDamage -> theDamage-damageArr[0]));
                break;
            }
            iterator.remove();
            damageArr[0] -= damageRecord.damage;
        }
    }

    public void add(User user, double damage) {
        this.queue.addLast(new DamageRecord(user,damage));
    }

    record DamageRecord(User user, double damage){
        DamageRecord redefine(double damage) {
            return new DamageRecord(this.user,damage);
        }

        DamageRecord redefine(DoubleUnaryOperator operator) {
            return new DamageRecord(this.user,operator.applyAsDouble(this.damage));
        }
    }

    public void clear() {
        this.queue.clear();
    }
}
