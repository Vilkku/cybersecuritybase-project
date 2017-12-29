package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String name;
    private String address;
    private boolean vip;

    public Signup() {
        super();
    }

    public Signup(String name, String address, boolean vip) {
        this();
        this.name = name;
        this.address = address;
        this.vip = vip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getVip() { return vip; }

    public void setVip(boolean vip) { this.vip = vip; }

}
