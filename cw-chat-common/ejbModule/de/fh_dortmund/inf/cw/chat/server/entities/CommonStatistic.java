package de.fh_dortmund.inf.cw.chat.server.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries({
	@NamedQuery(
			name="CommonStatistic.findAll",
			query="SELECT cs FROM CommonStatistic cs WHERE cs.endDate != NULL"),
	@NamedQuery(
			name="CommonStatistic.findMostRecent",
			query="SELECT cs FROM CommonStatistic cs WHERE cs.endDate = NULL"),
	@NamedQuery(
			name="CommonStatistic.findAllSorted",
			query="SELECT cs FROM CommonStatistic cs ORDER BY cs.startingDate DESC")
})
@Entity
public class CommonStatistic extends Statistic {
	@Id
	@GeneratedValue
	@Column(nullable=false)
	private long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	@Basic(optional=false)
	private Date startingDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((startingDate == null) ? 0 : startingDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommonStatistic other = (CommonStatistic) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id != other.id)
			return false;
		if (startingDate == null) {
			if (other.startingDate != null)
				return false;
		} else if (!startingDate.equals(other.startingDate))
			return false;
		return true;
	}
	
	
}
