package com.softserveinc.ita.entity;

import com.softserveinc.ita.entity.exceptions.DateException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Appointments")
public class Appointment {

    private static final long DEFAULT_DURATION = 30 * 60 * 1000;
    public static final int TOMORROW = 24 * 60 * 60 * 1000;


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", unique = true)
    private String ID;

    @ElementCollection
    @CollectionTable(name = "Users", joinColumns = @JoinColumn(name = "UserId"))
	@Column(name = "UserIdList")
	private List <String> userIdList = new ArrayList<>();

    @Column(name = "ApplicantId")
	private String applicantId;

    @Column(name = "StartTime")
	private long startTime = System.currentTimeMillis() + TOMORROW;

    @Column(name = "Duration")
	private long duration = DEFAULT_DURATION;

	public Appointment() {}

	public Appointment(List<String> userIdList, String applicantId, long startTime, long duration) {
		this.userIdList = userIdList;
		this.applicantId = applicantId;
		this.startTime = startTime;
		this.duration = duration;
	}

	public Appointment(List<String> userIdList, String applicantId, long startTime) {
		this.userIdList = userIdList;
		this.applicantId = applicantId;
		this.startTime = startTime;
	}

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

	@Override
	public String toString() {
		return "Appointment{" +
				"userIdList=" + userIdList +
				", applicantId=" + applicantId +
				", startTime=" + startTime +
				", duration=" + duration +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Appointment that = (Appointment) o;

		if (duration != that.duration) return false;
		if (startTime != that.startTime) return false;
		if (!applicantId.equals(that.applicantId)) return false;
		if (!userIdList.equals(that.userIdList)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = userIdList.hashCode();
		result = 31 * result + applicantId.hashCode();
		result = 31 * result + (int) (startTime ^ (startTime >>> 32));
		result = 31 * result + (int) (duration ^ (duration >>> 32));
		return result;
	}

    public void dateValidation(long startTime, long durationTime) throws DateException {

        if (durationTime < 0) throw new DateException("Wrong duration time");

        long currentDate=new Date().getTime();
        if (startTime < currentDate) throw new DateException("Start time has already passed");

        long bigDurationTime = 1000 * 60 * 60 * 12; //interview for the whole day, bad idea
        if (durationTime > bigDurationTime) throw new DateException("Too long duration time");
    }
}
