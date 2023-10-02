package th.mfu.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity//TODO: add proper annotation
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//TODO: add proper annotation 
    private Long id;
    private String title;
    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToOne(cascade = CascadeType.PERSIST)//TODO: add proper annotation
    private Performer performer;
    

    public Concert() {
    }
    public Concert(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Performer getPerformer() {
        return performer;
    }
    public void setPerformer(Performer performer) {
        this.performer = performer;
    }
 

    
}
