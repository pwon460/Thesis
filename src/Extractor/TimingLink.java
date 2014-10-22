package Extractor;

public class TimingLink {
	
	//private int sequenceNo;
	private String fromStopId;
	private String toStopId;
	private String runTime;
	
	public TimingLink() {
		// TODO Auto-generated constructor stub
	}
	/*
	public int getSequenceNo() {
		return this.sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	*/
	public String getFromStopId() {
		return this.fromStopId;
	}

	public void setFromStopId(String fromStopId) {
		this.fromStopId = fromStopId;
	}

	public String getToStopId() {
		return this.toStopId;
	}

	public void setToStopId(String toStopId) {
		this.toStopId = toStopId;
	}

	public String getRunTime() {
		return this.runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	/*
	public boolean equals(TimingLink other) {
		if (this.sequenceNo != other.getSequenceNo()) {
			return false;
		}
		if (this.fromStopId.equals(other.getFromStopId())) {
			return false;
		}
		if (this.toStopId.equals(other.getToStopId())) {
			return false;
		}
		if (this.runTime == other.getRunTime()) {
			return false;
		}
		return true;
	}
	*/
}
